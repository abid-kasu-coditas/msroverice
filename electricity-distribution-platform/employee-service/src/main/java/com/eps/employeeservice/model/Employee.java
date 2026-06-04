package com.eps.employeeservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "employees", indexes = {
    @Index(name = "idx_employee_role", columnList = "role"),
    @Index(name = "idx_employee_parent", columnList = "parent_id"),
    @Index(name = "idx_employee_territory",
        columnList = "assigned_state,assigned_district,assigned_city")
})
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "auth_user_id")
  private UUID authUserId;

  @NotNull
  @Column(nullable = false)
  private String firstName;

  @NotNull
  @Column(nullable = false)
  private String lastName;

  @NotNull
  @Column(nullable = false, unique = true)
  private String email;

  @NotNull
  @Column(nullable = false)
  private String phone;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EmployeeRole role;

  @Column(name = "parent_id")
  private UUID parentId;

  @Column(name = "assigned_state")
  private String assignedState;

  @Column(name = "assigned_district")
  private String assignedDistrict;

  @Column(name = "assigned_city")
  private String assignedCity;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EmployeeStatus status;

  @NotNull
  @Column(nullable = false)
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
