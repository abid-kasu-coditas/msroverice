package com.eps.platformservice.dto;

import jakarta.validation.constraints.NotNull;

public record PocTenantAssignmentRequest(
    @NotNull Long pocUserId,
    @NotNull Long tenantId) {
}
