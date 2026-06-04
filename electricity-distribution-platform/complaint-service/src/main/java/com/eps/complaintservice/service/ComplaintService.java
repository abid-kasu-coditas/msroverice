package com.eps.complaintservice.service;

import com.eps.complaintservice.dto.ComplaintRequestDTO;
import com.eps.complaintservice.dto.ComplaintResponseDTO;
import com.eps.complaintservice.event.ComplaintEventPublisher;
import com.eps.complaintservice.exception.ComplaintAssignmentException;
import com.eps.complaintservice.exception.ComplaintNotFoundException;
import com.eps.complaintservice.mapper.ComplaintMapper;
import com.eps.complaintservice.model.Complaint;
import com.eps.complaintservice.model.ComplaintCategory;
import com.eps.complaintservice.model.ComplaintEscalation;
import com.eps.complaintservice.model.ComplaintStatus;
import com.eps.complaintservice.repository.ComplaintEscalationRepository;
import com.eps.complaintservice.repository.ComplaintRepository;
import com.eps.grpc.common.IdRequest;
import com.eps.grpc.tenantuser.BPOEmployeeResponse;
import com.eps.grpc.tenantuser.ManagerRequest;
import com.eps.grpc.tenantuser.ManagerResponse;
import com.eps.grpc.tenantuser.TechnicianResponse;
import com.eps.grpc.tenantuser.TenantUserServiceGrpc;
import com.eps.shared.tenant.TenantContext;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintEscalationRepository complaintEscalationRepository;
    private final ComplaintEventPublisher complaintEventPublisher;
    private final String tenantUserServiceAddress;
    private final int tenantUserServiceGrpcPort;

    public ComplaintService(ComplaintRepository complaintRepository,
                            ComplaintEscalationRepository complaintEscalationRepository,
                            ComplaintEventPublisher complaintEventPublisher,
                            @Value("${tenant-user-service.address:localhost}") String tenantUserServiceAddress,
                            @Value("${tenant-user-service.grpc-port:9088}") int tenantUserServiceGrpcPort) {
        this.complaintRepository = complaintRepository;
        this.complaintEscalationRepository = complaintEscalationRepository;
        this.complaintEventPublisher = complaintEventPublisher;
        this.tenantUserServiceAddress = tenantUserServiceAddress;
        this.tenantUserServiceGrpcPort = tenantUserServiceGrpcPort;
    }

    public List<ComplaintResponseDTO> getComplaints() {
        return complaintRepository.findAll().stream().map(ComplaintMapper::toDTO).toList();
    }

    public List<ComplaintResponseDTO> getComplaintsByCustomerId(Long customerId) {
        return complaintRepository.findByCustomerId(customerId).stream().map(ComplaintMapper::toDTO).toList();
    }

    public List<ComplaintResponseDTO> getComplaintsByStatus(ComplaintStatus status) {
        return complaintRepository.findByStatus(status).stream().map(ComplaintMapper::toDTO).toList();
    }

    public List<ComplaintResponseDTO> getComplaintsByCategory(ComplaintCategory category) {
        return complaintRepository.findByCategory(category).stream().map(ComplaintMapper::toDTO).toList();
    }

    @Transactional
    public ComplaintResponseDTO createComplaint(ComplaintRequestDTO request) {
        Complaint complaint = ComplaintMapper.toModel(request);
        BPOEmployeeResponse bpoEmployee = getBpoEmployeeByCity(complaint.getCityId());
        complaint.setBpoEmployeeId(bpoEmployee.getId());
        complaint.setStatus(ComplaintStatus.ASSIGNED);
        Complaint saved = complaintRepository.save(complaint);
        complaintEventPublisher.publishComplaintCreated(saved);
        return ComplaintMapper.toDTO(saved);
    }

    public ComplaintResponseDTO getComplaintById(Long id) {
        return ComplaintMapper.toDTO(findComplaint(id));
    }

    @Transactional
    public ComplaintResponseDTO updateComplaint(Long id, ComplaintRequestDTO request) {
        Complaint complaint = findComplaint(id);
        complaint.setCustomerId(request.getCustomerId());
        complaint.setCategory(request.getCategory());
        complaint.setDescription(request.getDescription());
        complaint.setCityId(request.getCityId());
        complaint.setAreaId(request.getAreaId());
        complaint.setStatus(request.getStatus() == null ? complaint.getStatus() : request.getStatus());
        return ComplaintMapper.toDTO(complaintRepository.save(complaint));
    }

    @Transactional
    public ComplaintResponseDTO assignTechnician(Long id, Long requestedTechnicianId, Long assignedByUserId) {
        Complaint complaint = findComplaint(id);
        if (complaint.getStatus() == ComplaintStatus.RESOLVED
            || complaint.getStatus() == ComplaintStatus.CLOSED) {
            throw new ComplaintAssignmentException("Resolved or closed complaints cannot be assigned");
        }
        if (complaint.getTechnicianId() != null
            && complaint.getStatus() == ComplaintStatus.IN_PROGRESS) {
            throw new ComplaintAssignmentException("Complaint already has an assigned technician");
        }

        TechnicianResponse technician = getTechnicianByArea(complaint.getAreaId());
        if (requestedTechnicianId != null && !requestedTechnicianId.equals(technician.getId())) {
            throw new ComplaintAssignmentException(
                "Requested technician is not the active technician for complaint area " + complaint.getAreaId());
        }

        complaint.setTechnicianId(technician.getId());
        complaint.setAssignedByUserId(assignedByUserId);
        complaint.setAssignedAt(LocalDateTime.now());
        complaint.setStatus(ComplaintStatus.IN_PROGRESS);
        Complaint assignedComplaint = complaintRepository.save(complaint);
        return ComplaintMapper.toDTO(assignedComplaint);
    }

    @Transactional
    public ComplaintResponseDTO resolveComplaint(Long id, String resolution) {
        Complaint complaint = findComplaint(id);
        complaint.setStatus(ComplaintStatus.RESOLVED);
        complaint.setResolution(resolution);
        complaint.setResolvedAt(LocalDateTime.now());
        Complaint resolvedComplaint = complaintRepository.save(complaint);
        complaintEventPublisher.publishComplaintResolved(resolvedComplaint);
        return ComplaintMapper.toDTO(resolvedComplaint);
    }

    @Transactional
    public ComplaintResponseDTO escalateComplaint(Long id, String reason) {
        Complaint complaint = findComplaint(id);
        if (complaint.getStatus() == ComplaintStatus.RESOLVED
            || complaint.getStatus() == ComplaintStatus.CLOSED) {
            throw new ComplaintAssignmentException("Resolved or closed complaints cannot be escalated");
        }
        if (complaint.getStatus() == ComplaintStatus.ESCALATED_L2) {
            throw new ComplaintAssignmentException("Complaint is already escalated to L2");
        }

        String level = complaint.getStatus() == ComplaintStatus.ESCALATED_L1 ? "L2" : "L1";
        ManagerResponse manager = getManagerByLevel(level, complaint.getCityId());

        ComplaintEscalation escalation = new ComplaintEscalation();
        escalation.setComplaintId(complaint.getId());
        escalation.setLevel(level);
        escalation.setManagerId(manager.getId());
        escalation.setReason(reason);
        ComplaintEscalation savedEscalation = complaintEscalationRepository.save(escalation);

        complaint.setStatus("L2".equals(level) ? ComplaintStatus.ESCALATED_L2 : ComplaintStatus.ESCALATED_L1);
        Complaint escalatedComplaint = complaintRepository.save(complaint);
        complaintEventPublisher.publishComplaintEscalated(escalatedComplaint, savedEscalation);
        return ComplaintMapper.toDTO(escalatedComplaint);
    }

    public void deleteComplaint(Long id) {
        if (!complaintRepository.existsById(id)) {
            throw new ComplaintNotFoundException("Complaint not found with ID: " + id);
        }
        complaintRepository.deleteById(id);
    }

    private Complaint findComplaint(Long id) {
        return complaintRepository.findById(id)
            .orElseThrow(() -> new ComplaintNotFoundException("Complaint not found with ID: " + id));
    }

    private BPOEmployeeResponse getBpoEmployeeByCity(Long cityId) {
        return callTenantUserService(stub -> stub.getBPOEmployeeByCity(IdRequest.newBuilder()
            .setId(cityId)
            .setTenantId(tenantId())
            .build()), "Unable to assign BPO employee for city " + cityId);
    }

    private TechnicianResponse getTechnicianByArea(Long areaId) {
        return callTenantUserService(stub -> stub.getTechnicianByArea(IdRequest.newBuilder()
            .setId(areaId)
            .setTenantId(tenantId())
            .build()), "Unable to assign technician for area " + areaId);
    }

    private ManagerResponse getManagerByLevel(String level, Long cityId) {
        return callTenantUserService(stub -> stub.getManagerByLevel(ManagerRequest.newBuilder()
            .setLevel(level)
            .setCityId(cityId)
            .setTenantId(tenantId())
            .build()), "Unable to find BPO manager " + level + " for city " + cityId);
    }

    private <T> T callTenantUserService(
        Function<TenantUserServiceGrpc.TenantUserServiceBlockingStub, T> call,
        String failureMessage) {
        ManagedChannel channel = ManagedChannelBuilder
            .forAddress(tenantUserServiceAddress, tenantUserServiceGrpcPort)
            .usePlaintext()
            .build();
        try {
            return call.apply(TenantUserServiceGrpc.newBlockingStub(channel));
        } catch (StatusRuntimeException | IllegalArgumentException ex) {
            throw new ComplaintAssignmentException(failureMessage + ": " + ex.getMessage(), ex);
        } finally {
            channel.shutdown();
        }
    }

    private String tenantId() {
        String tenantId = TenantContext.getCurrentTenant();
        return tenantId == null ? "" : tenantId;
    }
}
