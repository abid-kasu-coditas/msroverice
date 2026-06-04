package com.eps.meterreadingservice.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubmitMeterReadingRequest(
    @NotNull Long connectionId,
    Long billerId,
    @NotNull BigDecimal readingValue,
    BigDecimal previousReadingValue,
    LocalDateTime readAt) {
}
