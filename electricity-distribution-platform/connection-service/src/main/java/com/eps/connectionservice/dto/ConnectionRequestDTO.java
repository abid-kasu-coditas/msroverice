package com.eps.connectionservice.dto;

import com.eps.connectionservice.model.ConnectionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.UUID;

public class ConnectionRequestDTO {

    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @NotBlank(message = "Connection number is required")
    private String connectionNumber;

    @NotBlank(message = "Service address is required")
    private String serviceAddress;

    @NotBlank(message = "Tariff plan is required")
    private String tariffPlan;

    @NotNull(message = "Load capacity is required")
    @Positive(message = "Load capacity must be greater than zero")
    private Double loadCapacity;

    private ConnectionStatus status;
    private LocalDate connectionDate;
    private LocalDate terminationDate;

    public ConnectionRequestDTO() {}

    public ConnectionRequestDTO(UUID customerId, String connectionNumber, String serviceAddress,
                                String tariffPlan, Double loadCapacity, ConnectionStatus status,
                                LocalDate connectionDate, LocalDate terminationDate) {
        this.customerId = customerId;
        this.connectionNumber = connectionNumber;
        this.serviceAddress = serviceAddress;
        this.tariffPlan = tariffPlan;
        this.loadCapacity = loadCapacity;
        this.status = status;
        this.connectionDate = connectionDate;
        this.terminationDate = terminationDate;
    }

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
}
