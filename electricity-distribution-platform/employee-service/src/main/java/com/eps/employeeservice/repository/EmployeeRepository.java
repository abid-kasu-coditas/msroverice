package com.eps.employeeservice.repository;

import com.eps.employeeservice.model.Employee;
import com.eps.employeeservice.model.EmployeeRole;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
  Optional<Employee> findByEmail(String email);

  List<Employee> findByRole(EmployeeRole role);

  List<Employee> findByAssignedState(String state);

  List<Employee> findByAssignedStateAndAssignedDistrict(String state, String district);

  List<Employee> findByAssignedStateAndAssignedDistrictAndAssignedCity(String state,
      String district, String city);

  List<Employee> findByParentId(UUID parentId);

  boolean existsByEmail(String email);

  boolean existsByEmailAndIdNot(String email, UUID id);

  boolean existsByParentId(UUID parentId);
}
