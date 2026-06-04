package com.eps.employeeservice.mapper;

import com.eps.employeeservice.dto.EmployeeRequestDTO;
import com.eps.employeeservice.dto.EmployeeResponseDTO;
import com.eps.employeeservice.model.Employee;
import com.eps.employeeservice.model.EmployeeStatus;

public class EmployeeMapper {

  public static Employee toModel(EmployeeRequestDTO dto) {
    Employee employee = new Employee();
    employee.setAuthUserId(dto.getAuthUserId());
    employee.setFirstName(dto.getFirstName());
    employee.setLastName(dto.getLastName());
    employee.setEmail(dto.getEmail());
    employee.setPhone(dto.getPhone());
    employee.setRole(dto.getRole());
    employee.setParentId(dto.getParentId());
    employee.setAssignedState(dto.getAssignedState());
    employee.setAssignedDistrict(dto.getAssignedDistrict());
    employee.setAssignedCity(dto.getAssignedCity());
    employee.setJoinedDate(dto.getJoinedDate());
    employee.setStatus(EmployeeStatus.ACTIVE);
    return employee;
  }

  public static EmployeeResponseDTO toDTO(Employee employee) {
    EmployeeResponseDTO dto = new EmployeeResponseDTO();
    dto.setId(employee.getId());
    dto.setAuthUserId(employee.getAuthUserId());
    dto.setFirstName(employee.getFirstName());
    dto.setLastName(employee.getLastName());
    dto.setEmail(employee.getEmail());
    dto.setPhone(employee.getPhone());
    dto.setRole(employee.getRole());
    dto.setParentId(employee.getParentId());
    dto.setAssignedState(employee.getAssignedState());
    dto.setAssignedDistrict(employee.getAssignedDistrict());
    dto.setAssignedCity(employee.getAssignedCity());
    dto.setStatus(employee.getStatus());
    dto.setJoinedDate(employee.getJoinedDate());
    return dto;
  }
}
