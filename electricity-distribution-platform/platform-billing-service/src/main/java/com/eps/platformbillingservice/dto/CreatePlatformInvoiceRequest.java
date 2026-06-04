package com.eps.platformbillingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePlatformInvoiceRequest(
    @NotBlank String tenantCode,
    @NotBlank String invoiceMonth,
    @NotNull BigDecimal amount,
    @NotNull LocalDate dueDate) {
}
