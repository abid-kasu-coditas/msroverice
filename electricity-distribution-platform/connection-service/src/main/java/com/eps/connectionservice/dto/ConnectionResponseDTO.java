package com.eps.connectionservice.dto;

import com.eps.connectionservice.model.ConnectionStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class ConnectionResponseDTO {

    private UUID id;
    private UUID customerId;
    private String connectionNumber;
    private String serviceAddress;
    private String tariffPlan;
    private Double loadCapacity;
    private ConnectionStatus status;
    private LocalDate connectionDate;
    private LocalDate terminationDate;
    private LocalDateTime createdAt;

    public ConnectionResponseDTO() {}

    public ConnectionResponseDTO(UUID id, UUID customerId, String connectionNumber,
                                 String serviceAddress, String tariffPlan, Double loadCapacity,
                                 ConnectionStatus status, LocalDate connectionDate,
                                 LocalDate terminationDate, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.connectionNumber = connectionNumber;
        this.serviceAddress = serviceAddress;
        this.tariffPlan = tariffPlan;
        this.loadCapacity = loadCapacity;
        this.status = status;
        this.connectionDate = connectionDate;
        this.terminationDate = terminationDate;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public String getConnectionNumber() { return connectionNumber; }
    public void setConnectionNumber(String connectionNumber) { this.connectionNumber = connectionNumber; }

    public String getServiceAddress() { return serviceAddress; }
    public void setServiceAddress(String serviceAddress) { this.serviceAddress = serviceAddress; }

    public String getTariffPlan() { return tariffPlan; }
    public void setTariffPlan(String tariffPlan) { this.tariffPlan = tariffPlan; }

    public Double getLoadCapacity() { return loadCapacity; }
    public void setLoadCapacity(Double loadCapacity) { this.loadCapacity = loadCapacity; }

    public ConnectionStatus getStatus() { return status; }
    public void setStatus(ConnectionStatus status) { this.status = status; }

    public LocalDate getConnectionDate() { return connectionDate; }
    public void setConnectionDate(LocalDate connectionDate) { this.connectionDate = connectionDate; }

    public LocalDate getTerminationDate() { return terminationDate; }
    public void setTerminationDate(LocalDate terminationDate) { this.terminationDate = terminationDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
