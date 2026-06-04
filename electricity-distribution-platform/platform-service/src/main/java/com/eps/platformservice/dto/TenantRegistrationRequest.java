package com.eps.platformservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TenantRegistrationRequest(
    @NotBlank String code,
    @NotBlank String companyName,
    @Email @NotBlank String contactEmail,
    String contactPhone) {
}
