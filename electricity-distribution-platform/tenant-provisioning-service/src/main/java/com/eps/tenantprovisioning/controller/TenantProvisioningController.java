package com.eps.tenantprovisioning.controller;

import com.eps.tenantprovisioning.service.TenantSchemaProvisioningService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provisioning")
public class TenantProvisioningController {

  private final TenantSchemaProvisioningService provisioningService;

  public TenantProvisioningController(TenantSchemaProvisioningService provisioningService) {
    this.provisioningService = provisioningService;
  }

  @PostMapping("/tenants/{tenantCode}")
  public ResponseEntity<Map<String, Object>> provision(@PathVariable String tenantCode) {
    return ResponseEntity.ok(provisioningService.provisionTenant(tenantCode));
  }

  @GetMapping("/tenants/{tenantCode}/tables")
  public ResponseEntity<Map<String, Object>> tables(@PathVariable String tenantCode) {
    String schemaName = "tenant_" + tenantCode.toLowerCase().replace('-', '_');
    return ResponseEntity.ok(Map.of(
        "schemaName", schemaName,
        "tables", provisioningService.listTables(schemaName)));
  }
}
