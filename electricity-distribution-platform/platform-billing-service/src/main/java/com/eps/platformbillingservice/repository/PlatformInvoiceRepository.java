package com.eps.platformbillingservice.repository;

import com.eps.platformbillingservice.model.PlatformInvoice;
import com.eps.platformbillingservice.model.PlatformInvoiceStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformInvoiceRepository extends JpaRepository<PlatformInvoice, Long> {

  Optional<PlatformInvoice> findByTenantCodeAndInvoiceMonth(String tenantCode, String invoiceMonth);

  List<PlatformInvoice> findByStatusAndDueDateBefore(PlatformInvoiceStatus status, LocalDate dueDate);
}
