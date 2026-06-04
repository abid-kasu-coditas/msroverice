package com.eps.paymentservice.mapper;

import com.eps.paymentservice.dto.PaymentRequestDTO;
import com.eps.paymentservice.dto.PaymentResponseDTO;
import com.eps.paymentservice.model.Payment;
import com.eps.paymentservice.model.PaymentStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentMapper {

    public static Payment toModel(PaymentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Payment(
            dto.getBillId(),
            dto.getCustomerId(),
            dto.getAmount(),
            dto.getMethod(),
            PaymentStatus.PENDING,
            LocalDateTime.now(),
            resolveTransactionId(dto.getTransactionId())
        );
    }

    public static PaymentResponseDTO toDTO(Payment model) {
        if (model == null) {
            return null;
        }
        return new PaymentResponseDTO(
            model.getId(),
            model.getBillId(),
            model.getCustomerId(),
            model.getAmount(),
            model.getMethod(),
            model.getStatus(),
            model.getPaymentDate(),
            model.getTransactionId()
        );
    }

    private static String resolveTransactionId(String transactionId) {
        if (transactionId == null || transactionId.isBlank()) {
            return "TXN-" + UUID.randomUUID();
        }
        return transactionId;
    }
}
