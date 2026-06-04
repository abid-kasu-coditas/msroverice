package com.eps.clientonboarding.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class ClientOnboardingRequestDTO {
  @NotNull
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

  private String adminUsername;

  private String adminEmail;

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
}
