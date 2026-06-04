package com.eps.auditservice.repository;

import com.eps.auditservice.model.AuditLog;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
  List<AuditLog> findByEntityType(String entityType);

  List<AuditLog> findByEntityId(UUID entityId);

  List<AuditLog> findByAction(String action);
}
