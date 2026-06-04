package com.eps.paymentservice.repository;

import com.eps.paymentservice.model.PaymentBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentBlockRepository extends JpaRepository<PaymentBlock, UUID> {

    Optional<PaymentBlock> findByCustomerIdAndIsBlockedTrue(UUID customerId);

    List<PaymentBlock> findByCustomerId(UUID customerId);

    Optional<PaymentBlock> findByBillId(UUID billId);

    long countByIsBlockedTrue();
}
