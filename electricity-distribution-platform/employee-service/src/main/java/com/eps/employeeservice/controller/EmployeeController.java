package com.eps.employeeservice.controller;

import com.eps.employeeservice.dto.EmployeeRequestDTO;
import com.eps.employeeservice.dto.EmployeeResponseDTO;
import com.eps.employeeservice.model.EmployeeRole;
import com.eps.employeeservice.model.EmployeeStatus;
import com.eps.employeeservice.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee", description = "API for managing employees and internal staff")
public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping
  @Operation(summary = "Get all employees")
  public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
    return ResponseEntity.ok(employeeService.getAllEmployees());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get employee by ID")
  public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable UUID id) {
    return ResponseEntity.ok(employeeService.getEmployeeById(id));
  }

  @GetMapping("/{id}/role/{role}/active")
  @Operation(summary = "Validate that an employee has the expected active role")
  public ResponseEntity<EmployeeResponseDTO> validateActiveEmployeeRole(@PathVariable UUID id,
      @PathVariable EmployeeRole role) {
    return ResponseEntity.ok(employeeService.validateActiveEmployeeRole(id, role));
  }

  @PostMapping
  @Operation(summary = "Create a new employee")
  public ResponseEntity<EmployeeResponseDTO> createEmployee(
      @Valid @RequestBody EmployeeRequestDTO dto) {
    return ResponseEntity.ok(employeeService.createEmployee(dto));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an employee")
  public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable UUID id,
      @Valid @RequestBody EmployeeRequestDTO dto) {
    return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an employee")
  public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/terminate")
  @Operation(summary = "Terminate an employee")
  public ResponseEntity<Void> terminateEmployee(@PathVariable UUID id) {
    employeeService.terminateEmployee(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/role/{role}")
  @Operation(summary = "Get employees by role")
  public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByRole(
      @PathVariable EmployeeRole role) {
    return ResponseEntity.ok(employeeService.getEmployeesByRole(role));
  }

  @GetMapping("/state/{state}")
  @Operation(summary = "Get employees by assigned state")
  public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByState(@PathVariable String state) {
    return ResponseEntity.ok(employeeService.getEmployeesByState(state));
  }

  @GetMapping("/territory")
  @Operation(summary = "Get employees by assigned territory")
  public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByTerritory(
      @RequestParam(required = false) String state,
      @RequestParam(required = false) String district,
      @RequestParam(required = false) String city) {
    return ResponseEntity.ok(employeeService.getEmployeesByTerritory(state, district, city));
  }

  @GetMapping("/{id}/subordinates")
  @Operation(summary = "Get direct subordinates for an employee")
  public ResponseEntity<List<EmployeeResponseDTO>> getSubordinates(@PathVariable UUID id) {
    return ResponseEntity.ok(employeeService.getSubordinates(id));
  }

  @PutMapping("/{id}/manager/{managerId}")
  @Operation(summary = "Assign or reassign an employee manager")
  public ResponseEntity<EmployeeResponseDTO> assignManager(@PathVariable UUID id,
      @PathVariable UUID managerId) {
    return ResponseEntity.ok(employeeService.assignManager(id, managerId));
  }

  @PutMapping("/{id}/status")
  @Operation(summary = "Update employee status")
  public ResponseEntity<EmployeeResponseDTO> updateEmployeeStatus(@PathVariable UUID id,
      @RequestParam EmployeeStatus status) {
    return ResponseEntity.ok(employeeService.updateEmployeeStatus(id, status));
  }

  @PutMapping("/{id}/technician-assignment/assign")
  @Operation(summary = "Mark a technician as assigned")
  public ResponseEntity<EmployeeResponseDTO> assignTechnician(@PathVariable UUID id) {
    return ResponseEntity.ok(employeeService.assignTechnician(id));
  }

  @PutMapping("/{id}/technician-assignment/release")
  @Operation(summary = "Mark a technician as available")
  public ResponseEntity<EmployeeResponseDTO> releaseTechnician(@PathVariable UUID id) {
    return ResponseEntity.ok(employeeService.releaseTechnician(id));
  }
}
