package com.eps.auditservice.controller;

import com.eps.auditservice.dto.AuditLogDTO;
import com.eps.auditservice.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit")
@Tag(name = "Audit", description = "API for Audit Logs")
public class AuditController {

  private final AuditService auditService;

  public AuditController(AuditService auditService) {
    this.auditService = auditService;
  }

  @GetMapping
  @Operation(summary = "Get all Audit Logs")
  public ResponseEntity<List<AuditLogDTO>> getAuditLogs() {
    return ResponseEntity.ok(auditService.getAuditLogs());
  }

  @GetMapping("/entity/{entityId}")
  @Operation(summary = "Get Audit Logs by Entity ID")
  public ResponseEntity<List<AuditLogDTO>> getAuditLogsByEntity(@PathVariable UUID entityId) {
    return ResponseEntity.ok(auditService.getAuditLogsByEntity(entityId));
  }

  @GetMapping("/type/{entityType}")
  @Operation(summary = "Get Audit Logs by Entity Type")
  public ResponseEntity<List<AuditLogDTO>> getAuditLogsByEntityType(@PathVariable String entityType) {
    return ResponseEntity.ok(auditService.getAuditLogsByEntityType(entityType));
  }
}
