package com.eps.platformservice.service;

import com.eps.platformservice.dto.TenantRegistrationRequest;
import com.eps.platformservice.event.TenantEventPublisher;
import com.eps.platformservice.model.Tenant;
import com.eps.platformservice.model.TenantStatus;
import com.eps.platformservice.repository.TenantRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlatformTenantService {

  private final TenantRepository tenantRepository;
  private final TenantEventPublisher tenantEventPublisher;

  public PlatformTenantService(TenantRepository tenantRepository,
      TenantEventPublisher tenantEventPublisher) {
    this.tenantRepository = tenantRepository;
    this.tenantEventPublisher = tenantEventPublisher;
  }

  @Transactional
  public Tenant register(TenantRegistrationRequest request) {
    String tenantCode = normalizeTenantCode(request.code());
    if (tenantRepository.existsByCode(tenantCode)) {
      throw new IllegalArgumentException("Tenant already exists: " + tenantCode);
    }

    Tenant tenant = new Tenant();
    tenant.setCode(tenantCode);
    tenant.setCompanyName(request.companyName());
    tenant.setContactEmail(request.contactEmail());
    tenant.setContactPhone(request.contactPhone());
    tenant.setSchemaName("tenant_" + tenantCode);
    Tenant saved = tenantRepository.save(tenant);
    tenantEventPublisher.publishRegistered(saved);
    return saved;
  }

  public List<Tenant> findAll() {
    return tenantRepository.findAll();
  }

  public Tenant findByCode(String code) {
    return tenantRepository.findByCode(normalizeTenantCode(code))
        .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + code));
  }

  @Transactional
  public Tenant markProvisioned(String code) {
    Tenant tenant = findByCode(code);
    tenant.setStatus(TenantStatus.PROVISIONED);
    tenant.setProvisionedAt(LocalDateTime.now());
    return tenantRepository.save(tenant);
  }

  @Transactional
  public Tenant suspend(String code, String reason) {
    Tenant tenant = findByCode(code);
    tenant.setStatus(TenantStatus.SUSPENDED);
    tenant.setSuspendedAt(LocalDateTime.now());
    tenant.setSuspensionReason(reason);
    Tenant saved = tenantRepository.save(tenant);
    tenantEventPublisher.publishSuspended(saved);
    return saved;
  }

  @Transactional
  public Tenant reinstate(String code) {
    Tenant tenant = findByCode(code);
    tenant.setStatus(TenantStatus.ACTIVE);
    tenant.setSuspendedAt(null);
    tenant.setSuspensionReason(null);
    return tenantRepository.save(tenant);
  }

  private String normalizeTenantCode(String code) {
    if (code == null || code.isBlank()) {
      throw new IllegalArgumentException("Tenant code is required");
    }
    String normalized = code.trim().toLowerCase().replace('-', '_');
    if (!normalized.matches("[a-z0-9_]+")) {
      throw new IllegalArgumentException("Tenant code must contain only lowercase letters, numbers, or underscores");
    }
    return normalized;
  }
}
