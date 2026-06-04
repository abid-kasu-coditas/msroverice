package com.eps.complaintservice.dto;

import jakarta.validation.constraints.NotNull;

public class AssignTechnicianRequestDTO {

    @NotNull(message = "Technician ID is required")
    private Long technicianId;

    public AssignTechnicianRequestDTO() {}

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }
}
