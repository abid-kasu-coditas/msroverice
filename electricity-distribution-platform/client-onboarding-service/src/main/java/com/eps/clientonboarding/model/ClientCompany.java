package com.eps.clientonboarding.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "client_companies", indexes = {
    @Index(name = "idx_client_company_sales_poc", columnList = "sales_poc_id")
})
public class ClientCompany {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotNull
  @Column(name = "sales_poc_id", nullable = false)
  private UUID salesPocId;

  @NotNull
  private String companyName;

  @NotNull
  private String registrationNumber;

  @NotNull
  private String email;

  @NotNull
  private String phone;

  @NotNull
  private String address;

  @NotNull
  private String city;

  @NotNull
  private String state;

  @NotNull
  private String country;

  @NotNull
  @Enumerated(EnumType.STRING)
  private OnboardingStatus status;

  @NotNull
  private String schemaName;

  @NotNull
  private String databaseUrl;

  private String adminUsername;

  private String adminEmail;

  @NotNull
  private LocalDateTime onboardedAt;

  private LocalDateTime activatedAt;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getSalesPocId() {
    return salesPocId;
  }

  public void setSalesPocId(UUID salesPocId) {
    this.salesPocId = salesPocId;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public OnboardingStatus getStatus() {
    return status;
  }

  public void setStatus(OnboardingStatus status) {
    this.status = status;
  }

  public String getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  public String getDatabaseUrl() {
    return databaseUrl;
  }

  public void setDatabaseUrl(String databaseUrl) {
    this.databaseUrl = databaseUrl;
  }

  public String getAdminUsername() {
    return adminUsername;
  }

  public void setAdminUsername(String adminUsername) {
    this.adminUsername = adminUsername;
  }

  public String getAdminEmail() {
    return adminEmail;
  }

  public void setAdminEmail(String adminEmail) {
    this.adminEmail = adminEmail;
  }

  public LocalDateTime getOnboardedAt() {
    return onboardedAt;
  }

  public void setOnboardedAt(LocalDateTime onboardedAt) {
    this.onboardedAt = onboardedAt;
  }

  public LocalDateTime getActivatedAt() {
    return activatedAt;
  }

  public void setActivatedAt(LocalDateTime activatedAt) {
    this.activatedAt = activatedAt;
  }
}
