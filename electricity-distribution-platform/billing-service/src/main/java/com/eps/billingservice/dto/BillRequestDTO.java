package com.eps.billingservice.dto;

import com.eps.billingservice.model.BillStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public class BillRequestDTO {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Meter ID is required")
    private Long meterId;

    private String billNumber;
    private LocalDate billDate;
    private LocalDate dueDate;

    @NotNull(message = "Units consumed is required")
    @PositiveOrZero(message = "Units consumed cannot be negative")
    private Double unitsConsumed;

    @PositiveOrZero(message = "Rate per unit cannot be negative")
    private Double ratePerUnit;

    @PositiveOrZero(message = "Penalties cannot be negative")
    private Double penalties;

    @PositiveOrZero(message = "Discounts cannot be negative")
    private Double discounts;

    private BillStatus status;

    public BillRequestDTO() {}

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getMeterId() { return meterId; }
    public void setMeterId(Long meterId) { this.meterId = meterId; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Double getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(Double unitsConsumed) { this.unitsConsumed = unitsConsumed; }

    public Double getRatePerUnit() { return ratePerUnit; }
    public void setRatePerUnit(Double ratePerUnit) { this.ratePerUnit = ratePerUnit; }

    public Double getPenalties() { return penalties; }
    public void setPenalties(Double penalties) { this.penalties = penalties; }

    public Double getDiscounts() { return discounts; }
    public void setDiscounts(Double discounts) { this.discounts = discounts; }

    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }
}
