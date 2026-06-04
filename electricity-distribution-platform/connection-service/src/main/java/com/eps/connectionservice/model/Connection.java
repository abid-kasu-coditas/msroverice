package com.eps.connectionservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "connections")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false, unique = true)
    private String connectionNumber;

    @Column(nullable = false)
    private String serviceAddress;

    @Column(nullable = false)
    private String tariffPlan;

    @Column(nullable = false)
    private Double loadCapacity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConnectionStatus status = ConnectionStatus.REQUESTED;

    @Column(nullable = false)
    private LocalDate connectionDate;

    private LocalDate terminationDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Connection() {}

    public Connection(UUID customerId, String connectionNumber, String serviceAddress,
                      String tariffPlan, Double loadCapacity, ConnectionStatus status,
                      LocalDate connectionDate, LocalDate terminationDate) {
        this.customerId = customerId;
        this.connectionNumber = connectionNumber;
        this.serviceAddress = serviceAddress;
        this.tariffPlan = tariffPlan;
        this.loadCapacity = loadCapacity;
        this.status = status == null ? ConnectionStatus.REQUESTED : status;
        this.connectionDate = connectionDate == null ? LocalDate.now() : connectionDate;
        this.terminationDate = terminationDate;
        this.createdAt = LocalDateTime.now();
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
