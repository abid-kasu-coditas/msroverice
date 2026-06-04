package com.eps.paymentservice.service;

import com.eps.paymentservice.dto.PaymentRequestDTO;
import com.eps.paymentservice.dto.PaymentResponseDTO;
import com.eps.paymentservice.client.BillingServiceClient;
import com.eps.paymentservice.event.PaymentEventPublisher;
import com.eps.paymentservice.exception.PaymentNotFoundException;
import com.eps.paymentservice.exception.TransactionAlreadyExistsException;
import com.eps.paymentservice.mapper.PaymentMapper;
import com.eps.paymentservice.model.Payment;
import com.eps.paymentservice.model.PaymentStatus;
import com.eps.paymentservice.repository.PaymentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;
    private final BillingServiceClient billingServiceClient;

    public PaymentService(PaymentRepository paymentRepository, PaymentEventPublisher paymentEventPublisher,
                          BillingServiceClient billingServiceClient) {
        this.paymentRepository = paymentRepository;
        this.paymentEventPublisher = paymentEventPublisher;
        this.billingServiceClient = billingServiceClient;
    }

    public List<PaymentResponseDTO> getPayments() {
        return paymentRepository.findAll().stream().map(PaymentMapper::toDTO).toList();
    }

    public List<PaymentResponseDTO> getPaymentsByBillId(UUID billId) {
        return paymentRepository.findByBillId(billId).stream().map(PaymentMapper::toDTO).toList();
    }

    public List<PaymentResponseDTO> getPaymentsByCustomerId(UUID customerId) {
        return paymentRepository.findByCustomerId(customerId).stream().map(PaymentMapper::toDTO).toList();
    }

    public List<PaymentResponseDTO> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream().map(PaymentMapper::toDTO).toList();
    }

    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {
        Payment payment = PaymentMapper.toModel(request);
        if (paymentRepository.existsByTransactionId(payment.getTransactionId())) {
            throw new TransactionAlreadyExistsException(
                "A payment with transaction ID " + payment.getTransactionId() + " already exists");
        }
        payment.setStatus(PaymentStatus.COMPLETED);
        Payment savedPayment = paymentRepository.save(payment);
        updateBillAfterPayment(savedPayment);
        paymentEventPublisher.publishPaymentProcessed(savedPayment);
        return PaymentMapper.toDTO(savedPayment);
    }

    public PaymentResponseDTO getPaymentById(UUID id) {
        return PaymentMapper.toDTO(findPayment(id));
    }

    public PaymentResponseDTO updatePayment(UUID id, PaymentRequestDTO request) {
        Payment payment = findPayment(id);
        String transactionId = request.getTransactionId() == null || request.getTransactionId().isBlank()
            ? payment.getTransactionId() : request.getTransactionId();

        if (paymentRepository.existsByTransactionIdAndIdNot(transactionId, id)) {
            throw new TransactionAlreadyExistsException(
                "A payment with transaction ID " + transactionId + " already exists");
        }

        payment.setBillId(request.getBillId());
        payment.setCustomerId(request.getCustomerId());
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId(transactionId);

        Payment savedPayment = paymentRepository.save(payment);
        updateBillAfterPayment(savedPayment);
        paymentEventPublisher.publishPaymentProcessed(savedPayment);
        return PaymentMapper.toDTO(savedPayment);
    }

    public void deletePayment(UUID id) {
        if (!paymentRepository.existsById(id)) {
            throw new PaymentNotFoundException("Payment not found with ID: " + id);
        }
        paymentRepository.deleteById(id);
    }

    private Payment findPayment(UUID id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + id));
    }

    private void updateBillAfterPayment(Payment payment) {
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            return;
        }
        try {
            billingServiceClient.updateBillStatusToPaid(payment.getBillId(), payment.getAmount());
            logger.info("Updated bill {} after payment {}", payment.getBillId(), payment.getId());
        } catch (RuntimeException ex) {
            logger.warn("Billing status update failed for bill {} after payment {}: {}",
                payment.getBillId(), payment.getId(), ex.getMessage());
        }
    }
}
