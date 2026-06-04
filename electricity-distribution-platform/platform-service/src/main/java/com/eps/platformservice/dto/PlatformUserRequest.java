package com.eps.platformservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PlatformUserRequest(
    @NotNull UUID authUserId,
    @NotBlank String name,
    @NotBlank String role,
    Boolean active) {
}
