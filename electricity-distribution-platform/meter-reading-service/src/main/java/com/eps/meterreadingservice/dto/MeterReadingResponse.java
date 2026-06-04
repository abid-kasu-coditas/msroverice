package com.eps.meterreadingservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MeterReadingResponse(
    Long id,
    Long connectionId,
    BigDecimal readingValue,
    LocalDateTime readAt,
    Long generatedBillId,
    String billNumber) {
}
