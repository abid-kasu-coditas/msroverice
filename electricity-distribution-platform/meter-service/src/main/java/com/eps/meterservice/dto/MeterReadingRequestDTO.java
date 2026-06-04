package com.eps.meterservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public class MeterReadingRequestDTO {

    @NotNull(message = "Current reading is required")
    @PositiveOrZero(message = "Current reading cannot be negative")
    private Double currentReading;

    @PositiveOrZero(message = "Previous reading cannot be negative")
    private Double previousReading;

    private LocalDate readingDate;

    public MeterReadingRequestDTO() {}

    public MeterReadingRequestDTO(Double currentReading, Double previousReading, LocalDate readingDate) {
        this.currentReading = currentReading;
        this.previousReading = previousReading;
        this.readingDate = readingDate;
    }

    public Double getCurrentReading() { return currentReading; }
    public void setCurrentReading(Double currentReading) { this.currentReading = currentReading; }

    public Double getPreviousReading() { return previousReading; }
    public void setPreviousReading(Double previousReading) { this.previousReading = previousReading; }

    public LocalDate getReadingDate() { return readingDate; }
    public void setReadingDate(LocalDate readingDate) { this.readingDate = readingDate; }
}
