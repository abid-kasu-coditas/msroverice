package com.eps.billingservice.dto;

import com.eps.billingservice.model.BillStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class BillResponseDTO {

    private UUID id;
    private UUID customerId;
    private UUID meterId;
    private String billNumber;
    private LocalDate billDate;
    private LocalDate dueDate;
    private Double unitsConsumed;
    private Double baseAmount;
    private Double taxes;
    private Double penalties;
    private Double discounts;
    private Double totalAmount;
    private BillStatus status;
    private LocalDateTime createdAt;

    public BillResponseDTO() {}

    public BillResponseDTO(UUID id, UUID customerId, UUID meterId, String billNumber,
                           LocalDate billDate, LocalDate dueDate, Double unitsConsumed,
                           Double baseAmount, Double taxes, Double penalties, Double discounts,
                           Double totalAmount, BillStatus status, LocalDateTime createdAt) {
        this.id = id;
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
        this.status = status;
        this.createdAt = createdAt;
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
