package com.eps.billingservice.repository;

import com.eps.billingservice.model.Bill;
import com.eps.billingservice.model.BillStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, UUID> {

    boolean existsByBillNumber(String billNumber);

    boolean existsByBillNumberAndIdNot(String billNumber, UUID id);

    List<Bill> findByCustomerId(UUID customerId);

    List<Bill> findByStatus(BillStatus status);
}
