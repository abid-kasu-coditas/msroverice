package com.eps.analyticsservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenant_stats")
public class TenantStat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String tenantCode;

  @Column(nullable = false)
  private String eventType;

  @Column(nullable = false)
  private Integer eventCount = 1;

  @Column(nullable = false, updatable = false)
  private LocalDateTime recordedAt = LocalDateTime.now();

  public TenantStat() {
  }

  public TenantStat(String tenantCode, String eventType) {
    this.tenantCode = tenantCode;
    this.eventType = eventType;
  }

  public Long getId() {
    return id;
  }

  public String getTenantCode() {
    return tenantCode;
  }

  public void setTenantCode(String tenantCode) {
    this.tenantCode = tenantCode;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public Integer getEventCount() {
    return eventCount;
  }

  public void setEventCount(Integer eventCount) {
    this.eventCount = eventCount;
  }

  public LocalDateTime getRecordedAt() {
    return recordedAt;
  }
}
