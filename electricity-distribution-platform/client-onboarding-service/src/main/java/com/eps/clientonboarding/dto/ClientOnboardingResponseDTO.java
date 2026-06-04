package com.eps.clientonboarding.dto;

import com.eps.clientonboarding.model.OnboardingStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class ClientOnboardingResponseDTO {
  private UUID id;
  private UUID salesPocId;
  private String companyName;
  private String registrationNumber;
  private String email;
  private String phone;
  private String address;
  private String city;
  private String state;
  private String country;
  private OnboardingStatus status;
  private String schemaName;
  private String databaseUrl;
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
