package com.eps.platformbillingservice.controller;

import com.eps.platformbillingservice.dto.CreatePlatformInvoiceRequest;
import com.eps.platformbillingservice.model.PlatformInvoice;
import com.eps.platformbillingservice.service.PlatformBillingService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/platform-billing")
public class PlatformBillingController {

  private final PlatformBillingService platformBillingService;

  public PlatformBillingController(PlatformBillingService platformBillingService) {
    this.platformBillingService = platformBillingService;
  }

  @PostMapping("/invoices")
  public ResponseEntity<PlatformInvoice> create(
      @Valid @RequestBody CreatePlatformInvoiceRequest request) {
    return ResponseEntity.ok(platformBillingService.create(request));
  }

  @GetMapping("/invoices")
  public ResponseEntity<List<PlatformInvoice>> findAll() {
    return ResponseEntity.ok(platformBillingService.findAll());
  }

  @PutMapping("/invoices/{id}/paid")
  public ResponseEntity<PlatformInvoice> markPaid(@PathVariable Long id) {
    return ResponseEntity.ok(platformBillingService.markPaid(id));
  }

  @PostMapping("/suspend-overdue")
  public ResponseEntity<Void> suspendOverdue() {
    platformBillingService.suspendOverdueTenants();
    return ResponseEntity.accepted().build();
  }
}
