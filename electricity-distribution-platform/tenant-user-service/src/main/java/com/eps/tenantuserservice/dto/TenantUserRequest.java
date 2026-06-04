package com.eps.tenantuserservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record TenantUserRequest(
    UUID authUserId,
    @NotBlank String name,
    @Email @NotBlank String email,
    String phone,
    @NotBlank String role,
    Long cityId,
    Long areaId,
    Long managerId,
    Boolean active) {
}
