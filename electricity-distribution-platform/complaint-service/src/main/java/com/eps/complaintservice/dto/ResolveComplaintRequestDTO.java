package com.eps.complaintservice.dto;

import jakarta.validation.constraints.NotBlank;

public class ResolveComplaintRequestDTO {

    @NotBlank(message = "Resolution is required")
    private String resolution;

    public ResolveComplaintRequestDTO() {}

    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
}
