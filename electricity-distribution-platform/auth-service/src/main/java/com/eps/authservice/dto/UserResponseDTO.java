package com.eps.authservice.dto;

import java.util.UUID;

public class UserResponseDTO {

  private UUID id;
  private String username;
  private String email;
  private String role;
  private String userType;
  private String tenantId;
  private Boolean active;

  public UserResponseDTO() {
  }

  public UserResponseDTO(UUID id, String username, String email, String role, Boolean active) {
    this(id, username, email, role, "PLATFORM", null, active);
  }

  public UserResponseDTO(UUID id, String username, String email, String role,
      String userType, String tenantId, Boolean active) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.role = role;
    this.userType = userType;
    this.tenantId = tenantId;
    this.active = active;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}
