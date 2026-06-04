package com.eps.tenantuserservice.controller;

import com.eps.tenantuserservice.dto.TenantUserRequest;
import com.eps.tenantuserservice.model.TenantUser;
import com.eps.tenantuserservice.service.TenantUserManagementService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenant-users")
public class TenantUserController {

  private final TenantUserManagementService tenantUserManagementService;

  public TenantUserController(TenantUserManagementService tenantUserManagementService) {
    this.tenantUserManagementService = tenantUserManagementService;
  }

  @GetMapping
  public ResponseEntity<List<TenantUser>> findAll(@RequestParam(required = false) String role) {
    return ResponseEntity.ok(tenantUserManagementService.findAll(role));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TenantUser> findById(@PathVariable Long id) {
    return ResponseEntity.ok(tenantUserManagementService.findById(id));
  }

  @PostMapping
  public ResponseEntity<TenantUser> create(@Valid @RequestBody TenantUserRequest request) {
    return ResponseEntity.ok(tenantUserManagementService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TenantUser> update(@PathVariable Long id,
      @Valid @RequestBody TenantUserRequest request) {
    return ResponseEntity.ok(tenantUserManagementService.update(id, request));
  }
}
