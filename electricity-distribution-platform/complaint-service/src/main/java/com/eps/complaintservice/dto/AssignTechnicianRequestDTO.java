package com.eps.complaintservice.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class AssignTechnicianRequestDTO {

    @NotNull(message = "Technician ID is required")
    private UUID technicianId;

    public AssignTechnicianRequestDTO() {}

    public UUID getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(UUID technicianId) {
        this.technicianId = technicianId;
    }
}
