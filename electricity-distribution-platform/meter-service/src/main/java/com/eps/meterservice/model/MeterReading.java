package com.eps.meterservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "meter_readings")
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID meterAccountId;

    @Column(nullable = false)
    private Double currentReading;

    @Column(nullable = false)
    private Double previousReading;

    @Column(nullable = false)
    private LocalDate readingDate;

    @Column(nullable = false)
    private Double unitsConsumed;

    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    public MeterReading() {}

    public MeterReading(UUID meterAccountId, Double currentReading, Double previousReading,
                        LocalDate readingDate) {
        this.meterAccountId = meterAccountId;
        this.currentReading = currentReading;
        this.previousReading = previousReading;
        this.readingDate = readingDate == null ? LocalDate.now() : readingDate;
        this.unitsConsumed = currentReading - previousReading;
        this.recordedAt = LocalDateTime.now();
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
