package com.eps.meterservice.dto;

import com.eps.meterservice.model.MeterStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public class MeterAccountRequestDTO {

    @NotNull(message = "Connection ID is required")
    private UUID connectionId;

    @NotBlank(message = "Meter serial number is required")
    private String meterSerialNumber;

    @NotBlank(message = "Meter type is required")
    private String meterType;

    private LocalDate installationDate;
    private MeterStatus status;

    public MeterAccountRequestDTO() {}

    public MeterAccountRequestDTO(UUID connectionId, String meterSerialNumber, String meterType,
                                  LocalDate installationDate, MeterStatus status) {
        this.connectionId = connectionId;
        this.meterSerialNumber = meterSerialNumber;
        this.meterType = meterType;
        this.installationDate = installationDate;
        this.status = status;
    }

    public UUID getConnectionId() { return connectionId; }
    public void setConnectionId(UUID connectionId) { this.connectionId = connectionId; }

    public String getMeterSerialNumber() { return meterSerialNumber; }
    public void setMeterSerialNumber(String meterSerialNumber) { this.meterSerialNumber = meterSerialNumber; }

    public String getMeterType() { return meterType; }
    public void setMeterType(String meterType) { this.meterType = meterType; }

    public LocalDate getInstallationDate() { return installationDate; }
    public void setInstallationDate(LocalDate installationDate) { this.installationDate = installationDate; }

    public MeterStatus getStatus() { return status; }
    public void setStatus(MeterStatus status) { this.status = status; }
}
