package com.eps.platformbillingservice.service;

import com.eps.platformbillingservice.dto.CreatePlatformInvoiceRequest;
import com.eps.platformbillingservice.event.PlatformBillingEventPublisher;
import com.eps.platformbillingservice.integration.RedisSuspensionCache;
import com.eps.platformbillingservice.model.PlatformInvoice;
import com.eps.platformbillingservice.model.PlatformInvoiceStatus;
import com.eps.platformbillingservice.repository.PlatformInvoiceRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlatformBillingService {

  private final PlatformInvoiceRepository platformInvoiceRepository;
  private final RedisSuspensionCache redisSuspensionCache;
  private final PlatformBillingEventPublisher eventPublisher;

  public PlatformBillingService(PlatformInvoiceRepository platformInvoiceRepository,
      RedisSuspensionCache redisSuspensionCache, PlatformBillingEventPublisher eventPublisher) {
    this.platformInvoiceRepository = platformInvoiceRepository;
    this.redisSuspensionCache = redisSuspensionCache;
    this.eventPublisher = eventPublisher;
  }

  @Transactional
  public PlatformInvoice create(CreatePlatformInvoiceRequest request) {
    return platformInvoiceRepository.findByTenantCodeAndInvoiceMonth(
            request.tenantCode(), request.invoiceMonth())
        .orElseGet(() -> {
          PlatformInvoice invoice = new PlatformInvoice();
          invoice.setTenantCode(request.tenantCode());
          invoice.setInvoiceMonth(request.invoiceMonth());
          invoice.setAmount(request.amount());
          invoice.setDueDate(request.dueDate());
          return platformInvoiceRepository.save(invoice);
        });
  }

  @Transactional
  public PlatformInvoice markPaid(Long id) {
    PlatformInvoice invoice = findById(id);
    invoice.setStatus(PlatformInvoiceStatus.PAID);
    invoice.setPaidAt(LocalDateTime.now());
    redisSuspensionCache.reinstate(invoice.getTenantCode());
    return platformInvoiceRepository.save(invoice);
  }

  public List<PlatformInvoice> findAll() {
    return platformInvoiceRepository.findAll();
  }

  @Scheduled(cron = "0 0 2 * * *")
  @Transactional
  public void suspendOverdueTenants() {
    List<PlatformInvoice> overdue = platformInvoiceRepository.findByStatusAndDueDateBefore(
        PlatformInvoiceStatus.UNPAID, LocalDate.now());
    overdue.forEach(invoice -> {
      invoice.setStatus(PlatformInvoiceStatus.OVERDUE);
      platformInvoiceRepository.save(invoice);
      redisSuspensionCache.suspend(invoice.getTenantCode());
      eventPublisher.tenantSuspended(invoice.getTenantCode(), "Platform invoice overdue");
    });
  }

  private PlatformInvoice findById(Long id) {
    return platformInvoiceRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Platform invoice not found: " + id));
  }
}
