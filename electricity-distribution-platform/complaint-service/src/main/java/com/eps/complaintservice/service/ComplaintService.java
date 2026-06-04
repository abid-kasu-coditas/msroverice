package com.eps.complaintservice.service;

import com.eps.complaintservice.dto.ComplaintRequestDTO;
import com.eps.complaintservice.dto.ComplaintResponseDTO;
import com.eps.complaintservice.event.ComplaintEventPublisher;
import com.eps.complaintservice.exception.ComplaintAssignmentException;
import com.eps.complaintservice.exception.ComplaintNotFoundException;
import com.eps.complaintservice.mapper.ComplaintMapper;
import com.eps.complaintservice.model.Complaint;
import com.eps.complaintservice.model.ComplaintCategory;
import com.eps.complaintservice.model.ComplaintStatus;
import com.eps.complaintservice.repository.ComplaintRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintEventPublisher complaintEventPublisher;
    private final RestClient employeeServiceClient;

    public ComplaintService(ComplaintRepository complaintRepository,
                            ComplaintEventPublisher complaintEventPublisher,
                            @Value("${employee.service.url:http://localhost:8091}") String employeeServiceUrl) {
        this.complaintRepository = complaintRepository;
        this.complaintEventPublisher = complaintEventPublisher;
        this.employeeServiceClient = RestClient.builder()
            .baseUrl(employeeServiceUrl)
            .build();
    }

    public List<ComplaintResponseDTO> getComplaints() {
        return complaintRepository.findAll().stream().map(ComplaintMapper::toDTO).toList();
    }

    public List<ComplaintResponseDTO> getComplaintsByCustomerId(UUID customerId) {
        return complaintRepository.findByCustomerId(customerId).stream().map(ComplaintMapper::toDTO).toList();
    }

    public List<ComplaintResponseDTO> getComplaintsByStatus(ComplaintStatus status) {
        return complaintRepository.findByStatus(status).stream().map(ComplaintMapper::toDTO).toList();
    }

    public List<ComplaintResponseDTO> getComplaintsByCategory(ComplaintCategory category) {
        return complaintRepository.findByCategory(category).stream().map(ComplaintMapper::toDTO).toList();
    }

    public ComplaintResponseDTO createComplaint(ComplaintRequestDTO request) {
        Complaint complaint = complaintRepository.save(ComplaintMapper.toModel(request));
        complaintEventPublisher.publishComplaintCreated(complaint);
        return ComplaintMapper.toDTO(complaint);
    }

    public ComplaintResponseDTO getComplaintById(UUID id) {
        return ComplaintMapper.toDTO(findComplaint(id));
    }

    public ComplaintResponseDTO updateComplaint(UUID id, ComplaintRequestDTO request) {
        Complaint complaint = findComplaint(id);
        complaint.setCustomerId(request.getCustomerId());
        complaint.setCategory(request.getCategory());
        complaint.setDescription(request.getDescription());
        complaint.setState(request.getState());
        complaint.setDistrict(request.getDistrict());
        complaint.setCity(request.getCity());
        complaint.setStatus(request.getStatus() == null ? complaint.getStatus() : request.getStatus());
        return ComplaintMapper.toDTO(complaintRepository.save(complaint));
    }

    public ComplaintResponseDTO assignTechnician(UUID id, UUID technicianId, UUID assignedByUserId) {
        Complaint complaint = findComplaint(id);
        if (complaint.getStatus() == ComplaintStatus.RESOLVED
            || complaint.getStatus() == ComplaintStatus.CLOSED) {
            throw new ComplaintAssignmentException("Resolved or closed complaints cannot be assigned");
        }
        if (complaint.getAssignedTechnicianId() != null
            && complaint.getStatus() == ComplaintStatus.IN_PROGRESS) {
            throw new ComplaintAssignmentException("Complaint already has an assigned technician");
        }

        EmployeeSnapshot technician = getActiveTechnician(technicianId);
        validateTechnicianTerritory(complaint, technician);
        markTechnicianAssigned(technicianId);

        complaint.setAssignedTechnicianId(technicianId);
        complaint.setAssignedByUserId(assignedByUserId);
        complaint.setAssignedAt(LocalDateTime.now());
        complaint.setStatus(ComplaintStatus.IN_PROGRESS);
        Complaint assignedComplaint = complaintRepository.save(complaint);
        complaintEventPublisher.publishComplaintAssigned(assignedComplaint);
        return ComplaintMapper.toDTO(assignedComplaint);
    }

    public ComplaintResponseDTO resolveComplaint(UUID id, String resolution) {
        Complaint complaint = findComplaint(id);
        UUID assignedTechnicianId = complaint.getAssignedTechnicianId();
        complaint.setStatus(ComplaintStatus.RESOLVED);
        complaint.setResolution(resolution);
        complaint.setResolvedAt(LocalDateTime.now());
        Complaint resolvedComplaint = complaintRepository.save(complaint);
        if (assignedTechnicianId != null) {
            releaseTechnician(assignedTechnicianId);
        }
        complaintEventPublisher.publishComplaintResolved(resolvedComplaint);
        return ComplaintMapper.toDTO(resolvedComplaint);
    }

    public void deleteComplaint(UUID id) {
        if (!complaintRepository.existsById(id)) {
            throw new ComplaintNotFoundException("Complaint not found with ID: " + id);
        }
        complaintRepository.deleteById(id);
    }

    private Complaint findComplaint(UUID id) {
        return complaintRepository.findById(id)
            .orElseThrow(() -> new ComplaintNotFoundException("Complaint not found with ID: " + id));
    }

    private EmployeeSnapshot getActiveTechnician(UUID technicianId) {
        try {
            EmployeeSnapshot technician = employeeServiceClient.get()
                .uri("/api/employees/{id}/role/TECHNICIAN/active", technicianId)
                .retrieve()
                .body(EmployeeSnapshot.class);
            if (technician == null) {
                throw new ComplaintAssignmentException("Employee-service returned no technician data");
            }
            return technician;
        } catch (RestClientResponseException e) {
            throw new ComplaintAssignmentException(
                "Technician ID must reference an active TECHNICIAN employee: " + technicianId);
        } catch (RestClientException e) {
            throw new ComplaintAssignmentException(
                "Unable to validate technician with employee-service: " + e.getMessage(), e);
        }
    }

    private void markTechnicianAssigned(UUID technicianId) {
        try {
            employeeServiceClient.put()
                .uri("/api/employees/{id}/technician-assignment/assign", technicianId)
                .retrieve()
                .toBodilessEntity();
        } catch (RestClientException e) {
            throw new ComplaintAssignmentException(
                "Unable to mark technician assigned in employee-service: " + e.getMessage(), e);
        }
    }

    private void releaseTechnician(UUID technicianId) {
        try {
            employeeServiceClient.put()
                .uri("/api/employees/{id}/technician-assignment/release", technicianId)
                .retrieve()
                .toBodilessEntity();
        } catch (RestClientException e) {
            throw new ComplaintAssignmentException(
                "Unable to release technician in employee-service: " + e.getMessage(), e);
        }
    }

    private void validateTechnicianTerritory(Complaint complaint, EmployeeSnapshot technician) {
        if (hasText(complaint.getState()) && !same(complaint.getState(), technician.assignedState())) {
            throw new ComplaintAssignmentException("Technician is outside complaint state");
        }
        if (hasText(complaint.getDistrict())
            && !same(complaint.getDistrict(), technician.assignedDistrict())) {
            throw new ComplaintAssignmentException("Technician is outside complaint district");
        }
        if (hasText(complaint.getCity()) && !same(complaint.getCity(), technician.assignedCity())) {
            throw new ComplaintAssignmentException("Technician is outside complaint city");
        }
    }

    private boolean same(String left, String right) {
        if (left == null || right == null) {
            return false;
        }
        return left.trim().equalsIgnoreCase(right.trim());
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private record EmployeeSnapshot(UUID id, String role, String status, String assignedState,
                                    String assignedDistrict, String assignedCity) {
    }
}
