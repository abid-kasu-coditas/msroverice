package com.eps.paymentservice.repository;

import com.eps.paymentservice.model.Payment;
import com.eps.paymentservice.model.PaymentStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByBillId(UUID billId);

    List<Payment> findByCustomerId(UUID customerId);

    List<Payment> findByStatus(PaymentStatus status);

    boolean existsByTransactionId(String transactionId);

    boolean existsByTransactionIdAndIdNot(String transactionId, UUID id);
}
