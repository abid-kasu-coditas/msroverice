package com.eps.auditservice.mapper;

import com.eps.auditservice.dto.AuditLogDTO;
import com.eps.auditservice.model.AuditLog;

public class AuditMapper {

  public static AuditLogDTO toDTO(AuditLog log) {
    AuditLogDTO dto = new AuditLogDTO();
    dto.setId(log.getId());
    dto.setEntityType(log.getEntityType());
    dto.setEntityId(log.getEntityId());
    dto.setAction(log.getAction());
    dto.setChanges(log.getChanges());
    dto.setPerformedBy(log.getPerformedBy());
    dto.setTimestamp(log.getTimestamp());
    return dto;
  }

  public static AuditLog toModel(AuditLogDTO dto) {
    AuditLog log = new AuditLog();
    log.setEntityType(dto.getEntityType());
    log.setEntityId(dto.getEntityId());
    log.setAction(dto.getAction());
    log.setChanges(dto.getChanges());
    log.setPerformedBy(dto.getPerformedBy());
    log.setTimestamp(dto.getTimestamp());
    return log;
  }
}
