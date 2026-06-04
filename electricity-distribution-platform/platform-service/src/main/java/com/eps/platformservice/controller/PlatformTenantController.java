package com.eps.platformservice.controller;

import com.eps.platformservice.dto.TenantRegistrationRequest;
import com.eps.platformservice.dto.TenantStatusRequest;
import com.eps.platformservice.model.Tenant;
import com.eps.platformservice.service.PlatformTenantService;
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
@RequestMapping("/api/platform/tenants")
public class PlatformTenantController {

  private final PlatformTenantService platformTenantService;

  public PlatformTenantController(PlatformTenantService platformTenantService) {
    this.platformTenantService = platformTenantService;
  }

  @PostMapping
  public ResponseEntity<Tenant> register(@Valid @RequestBody TenantRegistrationRequest request) {
    return ResponseEntity.ok(platformTenantService.register(request));
  }

  @GetMapping
  public ResponseEntity<List<Tenant>> findAll() {
    return ResponseEntity.ok(platformTenantService.findAll());
  }

  @GetMapping("/{code}")
  public ResponseEntity<Tenant> findByCode(@PathVariable String code) {
    return ResponseEntity.ok(platformTenantService.findByCode(code));
  }

  @PutMapping("/{code}/provisioned")
  public ResponseEntity<Tenant> markProvisioned(@PathVariable String code) {
    return ResponseEntity.ok(platformTenantService.markProvisioned(code));
  }

  @PutMapping("/{code}/suspend")
  public ResponseEntity<Tenant> suspend(@PathVariable String code,
      @RequestBody(required = false) TenantStatusRequest request) {
    String reason = request == null ? null : request.reason();
    return ResponseEntity.ok(platformTenantService.suspend(code, reason));
  }

  @PutMapping("/{code}/reinstate")
  public ResponseEntity<Tenant> reinstate(@PathVariable String code) {
    return ResponseEntity.ok(platformTenantService.reinstate(code));
  }
}
