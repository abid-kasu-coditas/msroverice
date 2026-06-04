package com.eps.complaintservice.dto;

import com.eps.complaintservice.model.ComplaintCategory;
import com.eps.complaintservice.model.ComplaintStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class ComplaintResponseDTO {

    private UUID id;
    private UUID customerId;
    private ComplaintCategory category;
    private String description;
    private String state;
    private String district;
    private String city;
    private ComplaintStatus status;
    private LocalDateTime createdAt;
    private UUID assignedTechnicianId;
    private UUID assignedByUserId;
    private LocalDateTime assignedAt;
    private LocalDateTime resolvedAt;
    private String resolution;

    public ComplaintResponseDTO() {}

    public ComplaintResponseDTO(UUID id, UUID customerId, ComplaintCategory category,
                                String description, String state, String district, String city,
                                ComplaintStatus status, LocalDateTime createdAt,
                                UUID assignedTechnicianId, UUID assignedByUserId,
                                LocalDateTime assignedAt, LocalDateTime resolvedAt,
                                String resolution) {
        this.id = id;
        this.customerId = customerId;
        this.category = category;
        this.description = description;
        this.state = state;
        this.district = district;
        this.city = city;
        this.status = status;
        this.createdAt = createdAt;
        this.assignedTechnicianId = assignedTechnicianId;
        this.assignedByUserId = assignedByUserId;
        this.assignedAt = assignedAt;
        this.resolvedAt = resolvedAt;
        this.resolution = resolution;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public ComplaintCategory getCategory() { return category; }
    public void setCategory(ComplaintCategory category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public ComplaintStatus getStatus() { return status; }
    public void setStatus(ComplaintStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public UUID getAssignedTechnicianId() { return assignedTechnicianId; }
    public void setAssignedTechnicianId(UUID assignedTechnicianId) { this.assignedTechnicianId = assignedTechnicianId; }

    public UUID getAssignedByUserId() { return assignedByUserId; }
    public void setAssignedByUserId(UUID assignedByUserId) { this.assignedByUserId = assignedByUserId; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
}
