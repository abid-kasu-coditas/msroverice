package com.eps.paymentservice.dto;

import com.eps.paymentservice.model.PaymentMethod;
import com.eps.paymentservice.model.PaymentStatus;
import java.time.LocalDateTime;

public class PaymentResponseDTO {

    private Long id;
    private Long billId;
    private Long customerId;
    private Double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private String transactionId;

    public PaymentResponseDTO() {}

    public PaymentResponseDTO(Long id, Long billId, Long customerId, Double amount,
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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

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
