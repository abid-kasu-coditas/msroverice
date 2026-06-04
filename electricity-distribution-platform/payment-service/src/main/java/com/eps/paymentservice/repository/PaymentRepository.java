package com.eps.paymentservice.repository;

import com.eps.paymentservice.model.Payment;
import com.eps.paymentservice.model.PaymentStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByBillId(Long billId);

    List<Payment> findByCustomerId(Long customerId);

    List<Payment> findByStatus(PaymentStatus status);

    boolean existsByTransactionId(String transactionId);

    boolean existsByTransactionIdAndIdNot(String transactionId, Long id);
}
