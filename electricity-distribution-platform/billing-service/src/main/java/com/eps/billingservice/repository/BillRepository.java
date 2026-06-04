package com.eps.billingservice.repository;

import com.eps.billingservice.model.Bill;
import com.eps.billingservice.model.BillStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {

    boolean existsByBillNumber(String billNumber);

    boolean existsByBillNumberAndIdNot(String billNumber, Long id);

    List<Bill> findByCustomerId(Long customerId);

    List<Bill> findByStatus(BillStatus status);
}
