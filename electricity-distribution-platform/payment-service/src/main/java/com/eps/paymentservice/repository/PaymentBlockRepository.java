package com.eps.paymentservice.repository;

import com.eps.paymentservice.model.PaymentBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentBlockRepository extends JpaRepository<PaymentBlock, Long> {

    Optional<PaymentBlock> findByCustomerIdAndIsBlockedTrue(Long customerId);

    List<PaymentBlock> findByCustomerId(Long customerId);

    Optional<PaymentBlock> findByBillId(Long billId);

    long countByIsBlockedTrue();
}
