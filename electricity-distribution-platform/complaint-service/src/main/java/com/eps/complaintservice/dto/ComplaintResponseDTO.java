package com.eps.complaintservice.dto;

import com.eps.complaintservice.model.ComplaintCategory;
import com.eps.complaintservice.model.ComplaintStatus;
import java.time.LocalDateTime;

public class ComplaintResponseDTO {

    private Long id;
    private Long customerId;
    private ComplaintCategory category;
    private String description;
    private Long cityId;
    private Long areaId;
    private Long bpoEmployeeId;
    private ComplaintStatus status;
    private LocalDateTime createdAt;
    private Long technicianId;
    private Long assignedByUserId;
    private LocalDateTime assignedAt;
    private LocalDateTime resolvedAt;
    private String resolution;

    public ComplaintResponseDTO() {}

    public ComplaintResponseDTO(Long id, Long customerId, ComplaintCategory category,
                                String description, Long cityId, Long areaId, Long bpoEmployeeId,
                                ComplaintStatus status, LocalDateTime createdAt,
                                Long technicianId, Long assignedByUserId,
                                LocalDateTime assignedAt, LocalDateTime resolvedAt,
                                String resolution) {
        this.id = id;
        this.customerId = customerId;
        this.category = category;
        this.description = description;
        this.cityId = cityId;
        this.areaId = areaId;
        this.bpoEmployeeId = bpoEmployeeId;
        this.status = status;
        this.createdAt = createdAt;
        this.technicianId = technicianId;
        this.assignedByUserId = assignedByUserId;
        this.assignedAt = assignedAt;
        this.resolvedAt = resolvedAt;
        this.resolution = resolution;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public ComplaintCategory getCategory() { return category; }
    public void setCategory(ComplaintCategory category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }

    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }

    public Long getBpoEmployeeId() { return bpoEmployeeId; }
    public void setBpoEmployeeId(Long bpoEmployeeId) { this.bpoEmployeeId = bpoEmployeeId; }

    public ComplaintStatus getStatus() { return status; }
    public void setStatus(ComplaintStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getTechnicianId() { return technicianId; }
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }

    public Long getAssignedByUserId() { return assignedByUserId; }
    public void setAssignedByUserId(Long assignedByUserId) { this.assignedByUserId = assignedByUserId; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
}
