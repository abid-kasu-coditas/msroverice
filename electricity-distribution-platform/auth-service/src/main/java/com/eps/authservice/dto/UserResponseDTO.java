package com.eps.authservice.dto;

import com.eps.authservice.model.UserRole;
import java.util.UUID;

public class UserResponseDTO {

  private UUID id;
  private String username;
  private String email;
  private UserRole role;
  private Boolean active;

  public UserResponseDTO() {
  }

  public UserResponseDTO(UUID id, String username, String email, UserRole role, Boolean active) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.role = role;
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

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}
