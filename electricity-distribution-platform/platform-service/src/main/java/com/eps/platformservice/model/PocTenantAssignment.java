package com.eps.platformservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "poc_tenant_assignments")
public class PocTenantAssignment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long pocUserId;

  @Column(nullable = false)
  private Long tenantId;

  @Column(nullable = false, updatable = false)
  private LocalDateTime assignedAt;

  @PrePersist
  void prePersist() {
    if (assignedAt == null) {
      assignedAt = LocalDateTime.now();
    }
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Long getPocUserId() { return pocUserId; }
  public void setPocUserId(Long pocUserId) { this.pocUserId = pocUserId; }

  public Long getTenantId() { return tenantId; }
  public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

  public LocalDateTime getAssignedAt() { return assignedAt; }
  public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}
