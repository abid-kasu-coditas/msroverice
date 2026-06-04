package com.eps.employeeservice.dto;

import com.eps.employeeservice.model.EmployeeRole;
import com.eps.employeeservice.model.EmployeeStatus;
import java.time.LocalDate;
import java.util.UUID;

public class EmployeeResponseDTO {
  private UUID id;
  private UUID authUserId;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private EmployeeRole role;
  private UUID parentId;
  private String assignedState;
  private String assignedDistrict;
  private String assignedCity;
  private EmployeeStatus status;
  private LocalDate joinedDate;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getAuthUserId() {
    return authUserId;
  }

  public void setAuthUserId(UUID authUserId) {
    this.authUserId = authUserId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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

  public EmployeeRole getRole() {
    return role;
  }

  public void setRole(EmployeeRole role) {
    this.role = role;
  }

  public UUID getParentId() {
    return parentId;
  }

  public void setParentId(UUID parentId) {
    this.parentId = parentId;
  }

  public String getAssignedState() {
    return assignedState;
  }

  public void setAssignedState(String assignedState) {
    this.assignedState = assignedState;
  }

  public String getAssignedDistrict() {
    return assignedDistrict;
  }

  public void setAssignedDistrict(String assignedDistrict) {
    this.assignedDistrict = assignedDistrict;
  }

  public String getAssignedCity() {
    return assignedCity;
  }

  public void setAssignedCity(String assignedCity) {
    this.assignedCity = assignedCity;
  }

  public EmployeeStatus getStatus() {
    return status;
  }

  public void setStatus(EmployeeStatus status) {
    this.status = status;
  }

  public LocalDate getJoinedDate() {
    return joinedDate;
  }

  public void setJoinedDate(LocalDate joinedDate) {
    this.joinedDate = joinedDate;
  }
}
