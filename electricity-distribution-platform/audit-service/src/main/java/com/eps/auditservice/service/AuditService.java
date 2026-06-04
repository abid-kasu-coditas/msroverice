package com.eps.auditservice.service;

import com.eps.auditservice.dto.AuditLogDTO;
import com.eps.auditservice.mapper.AuditMapper;
import com.eps.auditservice.model.AuditLog;
import com.eps.auditservice.repository.AuditLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

  private final AuditLogRepository auditLogRepository;

  public AuditService(AuditLogRepository auditLogRepository) {
    this.auditLogRepository = auditLogRepository;
  }

  public AuditLogDTO logAction(String entityType, UUID entityId, String action, String changes,
      String performedBy) {
    AuditLog auditLog = new AuditLog();
    auditLog.setEntityType(entityType);
    auditLog.setEntityId(entityId);
    auditLog.setAction(action);
    auditLog.setChanges(changes);
    auditLog.setPerformedBy(performedBy);
    auditLog.setTimestamp(LocalDateTime.now());

    AuditLog saved = auditLogRepository.save(auditLog);
    return AuditMapper.toDTO(saved);
  }

  public List<AuditLogDTO> getAuditLogs() {
    return auditLogRepository.findAll().stream()
        .map(AuditMapper::toDTO).toList();
  }

  public List<AuditLogDTO> getAuditLogsByEntity(UUID entityId) {
    return auditLogRepository.findByEntityId(entityId).stream()
        .map(AuditMapper::toDTO).toList();
  }

  public List<AuditLogDTO> getAuditLogsByEntityType(String entityType) {
    return auditLogRepository.findByEntityType(entityType).stream()
        .map(AuditMapper::toDTO).toList();
  }
}
