package com.eps.paymentservice.dto;

import com.eps.paymentservice.model.PaymentMethod;
import com.eps.paymentservice.model.PaymentStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentResponseDTO {

    private UUID id;
    private UUID billId;
    private UUID customerId;
    private Double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private String transactionId;

    public PaymentResponseDTO() {}

    public PaymentResponseDTO(UUID id, UUID billId, UUID customerId, Double amount,
                              PaymentMethod method, PaymentStatus status,
                              LocalDateTime paymentDate, String transactionId) {
        this.id = id;
        this.billId = billId;
        this.customerId = customerId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.paymentDate = paymentDate;
        this.transactionId = transactionId;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getBillId() { return billId; }
    public void setBillId(UUID billId) { this.billId = billId; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
