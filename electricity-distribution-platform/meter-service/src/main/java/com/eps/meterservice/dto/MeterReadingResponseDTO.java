package com.eps.meterservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class MeterReadingResponseDTO {

    private UUID id;
    private UUID meterAccountId;
    private Double currentReading;
    private Double previousReading;
    private LocalDate readingDate;
    private Double unitsConsumed;
    private LocalDateTime recordedAt;

    public MeterReadingResponseDTO() {}

    public MeterReadingResponseDTO(UUID id, UUID meterAccountId, Double currentReading,
                                   Double previousReading, LocalDate readingDate,
                                   Double unitsConsumed, LocalDateTime recordedAt) {
        this.id = id;
        this.meterAccountId = meterAccountId;
        this.currentReading = currentReading;
        this.previousReading = previousReading;
        this.readingDate = readingDate;
        this.unitsConsumed = unitsConsumed;
        this.recordedAt = recordedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getMeterAccountId() { return meterAccountId; }
    public void setMeterAccountId(UUID meterAccountId) { this.meterAccountId = meterAccountId; }

    public Double getCurrentReading() { return currentReading; }
    public void setCurrentReading(Double currentReading) { this.currentReading = currentReading; }

    public Double getPreviousReading() { return previousReading; }
    public void setPreviousReading(Double previousReading) { this.previousReading = previousReading; }

    public LocalDate getReadingDate() { return readingDate; }
    public void setReadingDate(LocalDate readingDate) { this.readingDate = readingDate; }

    public Double getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(Double unitsConsumed) { this.unitsConsumed = unitsConsumed; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}
