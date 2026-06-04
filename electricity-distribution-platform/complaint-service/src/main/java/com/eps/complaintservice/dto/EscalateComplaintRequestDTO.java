package com.eps.complaintservice.dto;

import jakarta.validation.constraints.Size;

public class EscalateComplaintRequestDTO {

    @Size(max = 1000, message = "Reason cannot exceed 1000 characters")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
