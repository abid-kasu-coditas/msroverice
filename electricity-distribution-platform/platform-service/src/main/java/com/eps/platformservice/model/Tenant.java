package com.eps.platformservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenants")
public class Tenant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String companyName;

  @Column(nullable = false)
  private String contactEmail;

  private String contactPhone;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TenantStatus status = TenantStatus.REGISTERED;

  @Column(nullable = false, unique = true)
  private String schemaName;

  @Column(nullable = false, updatable = false)
  private LocalDateTime registeredAt;

  private LocalDateTime provisionedAt;
  private LocalDateTime suspendedAt;
  private String suspensionReason;

  @PrePersist
  void prePersist() {
    if (registeredAt == null) {
      registeredAt = LocalDateTime.now();
    }
    if (schemaName == null && code != null) {
      schemaName = "tenant_" + code;
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public String getContactPhone() {
    return contactPhone;
  }

  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }

  public TenantStatus getStatus() {
    return status;
  }

  public void setStatus(TenantStatus status) {
    this.status = status;
  }

  public String getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  public LocalDateTime getRegisteredAt() {
    return registeredAt;
  }

  public void setRegisteredAt(LocalDateTime registeredAt) {
    this.registeredAt = registeredAt;
  }

  public LocalDateTime getProvisionedAt() {
    return provisionedAt;
  }

  public void setProvisionedAt(LocalDateTime provisionedAt) {
    this.provisionedAt = provisionedAt;
  }

  public LocalDateTime getSuspendedAt() {
    return suspendedAt;
  }

  public void setSuspendedAt(LocalDateTime suspendedAt) {
    this.suspendedAt = suspendedAt;
  }

  public String getSuspensionReason() {
    return suspensionReason;
  }

  public void setSuspensionReason(String suspensionReason) {
    this.suspensionReason = suspensionReason;
  }
}
