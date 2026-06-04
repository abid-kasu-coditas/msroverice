package com.eps.meterservice.dto;

import com.eps.meterservice.model.MeterStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MeterAccountResponseDTO {

    private Long id;
    private Long connectionId;
    private String meterSerialNumber;
    private String meterType;
    private LocalDate installationDate;
    private MeterStatus status;
    private LocalDateTime createdAt;

    public MeterAccountResponseDTO() {}

    public MeterAccountResponseDTO(Long id, Long connectionId, String meterSerialNumber,
                                   String meterType, LocalDate installationDate,
                                   MeterStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.connectionId = connectionId;
        this.meterSerialNumber = meterSerialNumber;
        this.meterType = meterType;
        this.installationDate = installationDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getConnectionId() { return connectionId; }
    public void setConnectionId(Long connectionId) { this.connectionId = connectionId; }

    public String getMeterSerialNumber() { return meterSerialNumber; }
    public void setMeterSerialNumber(String meterSerialNumber) { this.meterSerialNumber = meterSerialNumber; }

    public String getMeterType() { return meterType; }
    public void setMeterType(String meterType) { this.meterType = meterType; }

    public LocalDate getInstallationDate() { return installationDate; }
    public void setInstallationDate(LocalDate installationDate) { this.installationDate = installationDate; }

    public MeterStatus getStatus() { return status; }
    public void setStatus(MeterStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
