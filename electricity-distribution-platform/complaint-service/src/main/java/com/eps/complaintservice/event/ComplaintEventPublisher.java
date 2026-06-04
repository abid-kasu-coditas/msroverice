package com.eps.complaintservice.event;

import com.eps.complaintservice.model.Complaint;
import com.eps.complaintservice.model.ComplaintEscalation;
import com.eps.grpc.events.complaint.ComplaintEscalatedEvent;
import com.eps.grpc.events.complaint.ComplaintRaisedEvent;
import com.eps.grpc.events.complaint.ComplaintResolvedEvent;
import com.eps.shared.tenant.TenantContext;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ComplaintEventPublisher {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public ComplaintEventPublisher(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishComplaintCreated(Complaint complaint) {
        ComplaintRaisedEvent event = ComplaintRaisedEvent.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setTenantCode(tenantCode())
            .setComplaintId(complaint.getId())
            .setCustomerId(complaint.getCustomerId())
            .setBpoEmployeeId(complaint.getBpoEmployeeId() == null ? 0 : complaint.getBpoEmployeeId())
            .setRaisedAt(complaint.getCreatedAt() == null
                ? LocalDateTime.now().toString()
                : complaint.getCreatedAt().toString())
            .build();
        kafkaTemplate.send("complaint-raised", tenantCode(), event.toByteArray());
    }

    public void publishComplaintResolved(Complaint complaint) {
        ComplaintResolvedEvent event = ComplaintResolvedEvent.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setTenantCode(tenantCode())
            .setComplaintId(complaint.getId())
            .setTechnicianId(complaint.getTechnicianId() == null ? 0 : complaint.getTechnicianId())
            .setResolvedAt(complaint.getResolvedAt() == null
                ? LocalDateTime.now().toString()
                : complaint.getResolvedAt().toString())
            .build();
        kafkaTemplate.send("complaint-resolved", tenantCode(), event.toByteArray());
    }

    public void publishComplaintEscalated(Complaint complaint, ComplaintEscalation escalation) {
        ComplaintEscalatedEvent event = ComplaintEscalatedEvent.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setTenantCode(tenantCode())
            .setComplaintId(complaint.getId())
            .setLevel(escalation.getLevel())
            .setManagerId(escalation.getManagerId() == null ? 0 : escalation.getManagerId())
            .setEscalatedAt(escalation.getEscalatedAt() == null
                ? LocalDateTime.now().toString()
                : escalation.getEscalatedAt().toString())
            .build();
        kafkaTemplate.send("complaint-escalated", tenantCode(), event.toByteArray());
    }

    private String tenantCode() {
        String tenant = TenantContext.getCurrentTenant();
        return tenant == null || tenant.isBlank() ? "unknown" : tenant;
    }
}
