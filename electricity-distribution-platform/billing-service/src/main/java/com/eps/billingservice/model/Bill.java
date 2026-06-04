package com.eps.billingservice.model;

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
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private UUID meterId;

    @Column(nullable = false, unique = true)
    private String billNumber;

    @Column(nullable = false)
    private LocalDate billDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private Double unitsConsumed;

    @Column(nullable = false)
    private Double baseAmount;

    @Column(nullable = false)
    private Double taxes;

    @Column(nullable = false)
    private Double penalties;

    @Column(nullable = false)
    private Double discounts;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BillStatus status = BillStatus.GENERATED;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Bill() {}

    public Bill(UUID customerId, UUID meterId, String billNumber, LocalDate billDate,
                LocalDate dueDate, Double unitsConsumed, Double baseAmount, Double taxes,
                Double penalties, Double discounts, Double totalAmount, BillStatus status) {
        this.customerId = customerId;
        this.meterId = meterId;
        this.billNumber = billNumber;
        this.billDate = billDate;
        this.dueDate = dueDate;
        this.unitsConsumed = unitsConsumed;
        this.baseAmount = baseAmount;
        this.taxes = taxes;
        this.penalties = penalties;
        this.discounts = discounts;
        this.totalAmount = totalAmount;
        this.status = status == null ? BillStatus.GENERATED : status;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public UUID getMeterId() { return meterId; }
    public void setMeterId(UUID meterId) { this.meterId = meterId; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Double getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(Double unitsConsumed) { this.unitsConsumed = unitsConsumed; }

    public Double getBaseAmount() { return baseAmount; }
    public void setBaseAmount(Double baseAmount) { this.baseAmount = baseAmount; }

    public Double getTaxes() { return taxes; }
    public void setTaxes(Double taxes) { this.taxes = taxes; }

    public Double getPenalties() { return penalties; }
    public void setPenalties(Double penalties) { this.penalties = penalties; }

    public Double getDiscounts() { return discounts; }
    public void setDiscounts(Double discounts) { this.discounts = discounts; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
