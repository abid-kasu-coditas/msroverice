package com.eps.employeeservice.service;

import com.eps.employeeservice.dto.EmployeeRequestDTO;
import com.eps.employeeservice.dto.EmployeeResponseDTO;
import com.eps.employeeservice.exception.EmployeeException;
import com.eps.employeeservice.mapper.EmployeeMapper;
import com.eps.employeeservice.model.Employee;
import com.eps.employeeservice.model.EmployeeRole;
import com.eps.employeeservice.model.EmployeeStatus;
import com.eps.employeeservice.repository.EmployeeRepository;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Service
public class EmployeeService {

  private static final Map<EmployeeRole, Set<EmployeeRole>> ALLOWED_PARENT_ROLES =
      new EnumMap<>(EmployeeRole.class);

  static {
    ALLOWED_PARENT_ROLES.put(EmployeeRole.MANAGEMENT,
        EnumSet.of(EmployeeRole.SUPER_ADMIN));
    ALLOWED_PARENT_ROLES.put(EmployeeRole.SALES_POC,
        EnumSet.of(EmployeeRole.SUPER_ADMIN, EmployeeRole.MANAGEMENT));
    ALLOWED_PARENT_ROLES.put(EmployeeRole.STATE_HEAD,
        EnumSet.of(EmployeeRole.SUPER_ADMIN, EmployeeRole.MANAGEMENT));
    ALLOWED_PARENT_ROLES.put(EmployeeRole.DISTRICT_HEAD,
        EnumSet.of(EmployeeRole.SUPER_ADMIN, EmployeeRole.MANAGEMENT, EmployeeRole.STATE_HEAD));
    ALLOWED_PARENT_ROLES.put(EmployeeRole.CITY_HEAD,
        EnumSet.of(EmployeeRole.SUPER_ADMIN, EmployeeRole.MANAGEMENT, EmployeeRole.DISTRICT_HEAD));
    ALLOWED_PARENT_ROLES.put(EmployeeRole.CRM,
        EnumSet.of(EmployeeRole.SUPER_ADMIN, EmployeeRole.MANAGEMENT, EmployeeRole.CITY_HEAD));
    ALLOWED_PARENT_ROLES.put(EmployeeRole.TECHNICIAN,
        EnumSet.of(EmployeeRole.SUPER_ADMIN, EmployeeRole.MANAGEMENT, EmployeeRole.CITY_HEAD));
    ALLOWED_PARENT_ROLES.put(EmployeeRole.BILLER,
        EnumSet.of(EmployeeRole.SUPER_ADMIN, EmployeeRole.MANAGEMENT, EmployeeRole.CITY_HEAD));
  }

  private final EmployeeRepository employeeRepository;
  private final RestClient authServiceClient;
  private final String internalServiceToken;

  public EmployeeService(EmployeeRepository employeeRepository,
      @Value("${auth.service.url:http://localhost:8081}") String authServiceUrl,
      @Value("${internal.service.token:internal-service-token}") String internalServiceToken) {
    this.employeeRepository = employeeRepository;
    this.authServiceClient = RestClient.builder()
        .baseUrl(authServiceUrl)
        .build();
    this.internalServiceToken = internalServiceToken;
  }

  @Transactional(readOnly = true)
  public List<EmployeeResponseDTO> getAllEmployees() {
    return employeeRepository.findAll().stream()
        .map(EmployeeMapper::toDTO).toList();
  }

  @Transactional(readOnly = true)
  public EmployeeResponseDTO getEmployeeById(UUID id) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeException("Employee not found with ID: " + id));
    return EmployeeMapper.toDTO(employee);
  }

  @Transactional
  public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
    if (employeeRepository.existsByEmail(dto.getEmail())) {
      throw new EmployeeException("Employee with email already exists: " + dto.getEmail());
    }
    Employee employee = EmployeeMapper.toModel(dto);
    validateEmployee(employee, null);
    Employee saved = employeeRepository.save(employee);
    return EmployeeMapper.toDTO(saved);
  }

  @Transactional
  public EmployeeResponseDTO updateEmployee(UUID id, EmployeeRequestDTO dto) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeException("Employee not found with ID: " + id));
    if (employeeRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
      throw new EmployeeException("Employee with email already exists: " + dto.getEmail());
    }
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
    validateEmployee(employee, id);
    Employee updated = employeeRepository.save(employee);
    return EmployeeMapper.toDTO(updated);
  }

  @Transactional
  public void deleteEmployee(UUID id) {
    if (!employeeRepository.existsById(id)) {
      throw new EmployeeException("Employee not found with ID: " + id);
    }
    if (employeeRepository.existsByParentId(id)) {
      throw new EmployeeException(
          "Employee has direct reports and must be reassigned before deletion: " + id);
    }
    employeeRepository.deleteById(id);
  }

  @Transactional
  public void terminateEmployee(UUID id) {
    updateEmployeeStatus(id, EmployeeStatus.TERMINATED);
  }

  @Transactional
  public EmployeeResponseDTO updateEmployeeStatus(UUID id, EmployeeStatus status) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeException("Employee not found with ID: " + id));
    employee.setStatus(status);
    return EmployeeMapper.toDTO(employeeRepository.save(employee));
  }

  @Transactional(readOnly = true)
  public EmployeeResponseDTO validateActiveEmployeeRole(UUID id, EmployeeRole role) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeException("Employee not found with ID: " + id));
    if (employee.getRole() != role) {
      throw new EmployeeException("Employee " + id + " is not assigned role " + role);
    }
    if (employee.getStatus() != EmployeeStatus.ACTIVE) {
      throw new EmployeeException("Employee " + id + " is not active");
    }
    return EmployeeMapper.toDTO(employee);
  }

  @Transactional
  public EmployeeResponseDTO assignTechnician(UUID id) {
    Employee employee = getAssignableTechnician(id);
    employee.setStatus(EmployeeStatus.ASSIGNED);
    return EmployeeMapper.toDTO(employeeRepository.save(employee));
  }

  @Transactional
  public EmployeeResponseDTO releaseTechnician(UUID id) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeException("Employee not found with ID: " + id));
    if (employee.getRole() != EmployeeRole.TECHNICIAN) {
      throw new EmployeeException("Employee " + id + " is not a technician");
    }
    if (employee.getStatus() == EmployeeStatus.ASSIGNED) {
      employee.setStatus(EmployeeStatus.ACTIVE);
    }
    return EmployeeMapper.toDTO(employeeRepository.save(employee));
  }

  @Transactional(readOnly = true)
  public List<EmployeeResponseDTO> getEmployeesByRole(EmployeeRole role) {
    return employeeRepository.findByRole(role).stream()
        .map(EmployeeMapper::toDTO).toList();
  }

  @Transactional(readOnly = true)
  public List<EmployeeResponseDTO> getEmployeesByState(String state) {
    return employeeRepository.findByAssignedState(state).stream()
        .map(EmployeeMapper::toDTO).toList();
  }

  @Transactional(readOnly = true)
  public List<EmployeeResponseDTO> getEmployeesByTerritory(String state, String district,
      String city) {
    if (!hasText(state)) {
      return getAllEmployees();
    }
    if (!hasText(district)) {
      return getEmployeesByState(state);
    }
    if (!hasText(city)) {
      return employeeRepository.findByAssignedStateAndAssignedDistrict(state, district).stream()
          .map(EmployeeMapper::toDTO).toList();
    }
    return employeeRepository
        .findByAssignedStateAndAssignedDistrictAndAssignedCity(state, district, city).stream()
        .map(EmployeeMapper::toDTO).toList();
  }

  @Transactional(readOnly = true)
  public List<EmployeeResponseDTO> getSubordinates(UUID managerId) {
    if (!employeeRepository.existsById(managerId)) {
      throw new EmployeeException("Employee not found with ID: " + managerId);
    }
    return employeeRepository.findByParentId(managerId).stream()
        .map(EmployeeMapper::toDTO).toList();
  }

  @Transactional
  public EmployeeResponseDTO assignManager(UUID employeeId, UUID managerId) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new EmployeeException("Employee not found with ID: " + employeeId));
    employee.setParentId(managerId);
    validateEmployee(employee, employeeId);
    return EmployeeMapper.toDTO(employeeRepository.save(employee));
  }

  private void validateEmployee(Employee employee, UUID currentEmployeeId) {
    validateAuthUserRole(employee);
    validateTerritory(employee);
    validateReportingLine(employee, currentEmployeeId);
  }

  private void validateAuthUserRole(Employee employee) {
    if (employee.getAuthUserId() == null) {
      throw new EmployeeException("Auth user ID is required for workforce employees");
    }

    AuthUserSnapshot authUser = getAuthUser(employee.getAuthUserId());
    if (Boolean.FALSE.equals(authUser.active())) {
      throw new EmployeeException("Auth user is inactive: " + employee.getAuthUserId());
    }
    if (authUser.role() != employee.getRole()) {
      throw new EmployeeException("Auth user role " + authUser.role()
          + " does not match workforce role " + employee.getRole());
    }
  }

  private AuthUserSnapshot getAuthUser(UUID authUserId) {
    try {
      AuthUserSnapshot authUser = authServiceClient.get()
          .uri("/auth/internal/users/{id}", authUserId)
          .header("X-Internal-Service-Token", internalServiceToken)
          .retrieve()
          .body(AuthUserSnapshot.class);
      if (authUser == null) {
        throw new EmployeeException("Auth-service returned no user data");
      }
      return authUser;
    } catch (RestClientResponseException e) {
      throw new EmployeeException("Auth user not found or not accessible: " + authUserId);
    } catch (RestClientException e) {
      throw new EmployeeException(
          "Unable to validate auth user with auth-service: " + e.getMessage(), e);
    }
  }

  private Employee getAssignableTechnician(UUID id) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeException("Employee not found with ID: " + id));
    if (employee.getRole() != EmployeeRole.TECHNICIAN) {
      throw new EmployeeException("Employee " + id + " is not a technician");
    }
    if (employee.getStatus() != EmployeeStatus.ACTIVE) {
      throw new EmployeeException("Technician " + id + " is not available for assignment");
    }
    return employee;
  }

  private void validateReportingLine(Employee employee, UUID currentEmployeeId) {
    EmployeeRole role = employee.getRole();
    if (role == EmployeeRole.SUPER_ADMIN) {
      if (employee.getParentId() != null) {
        throw new EmployeeException("SUPER_ADMIN cannot report to another employee");
      }
      return;
    }

    if (employee.getParentId() == null) {
      throw new EmployeeException(role + " must have a reporting manager");
    }

    if (employee.getParentId().equals(currentEmployeeId)) {
      throw new EmployeeException("Employee cannot report to itself");
    }

    Employee manager = employeeRepository.findById(employee.getParentId())
        .orElseThrow(() -> new EmployeeException(
            "Reporting manager not found with ID: " + employee.getParentId()));

    if (manager.getStatus() == EmployeeStatus.TERMINATED) {
      throw new EmployeeException("Employee cannot report to a terminated manager");
    }

    Set<EmployeeRole> allowedParents = ALLOWED_PARENT_ROLES.get(role);
    if (allowedParents == null || !allowedParents.contains(manager.getRole())) {
      throw new EmployeeException(
          role + " can report only to one of: " + allowedParents);
    }

    validateManagerTerritory(employee, manager);
  }

  private void validateTerritory(Employee employee) {
    switch (employee.getRole()) {
      case STATE_HEAD -> requireTerritory(employee, true, false, false);
      case DISTRICT_HEAD -> requireTerritory(employee, true, true, false);
      case CITY_HEAD, CRM, TECHNICIAN, BILLER -> requireTerritory(employee, true, true, true);
      default -> {
      }
    }
  }

  private void requireTerritory(Employee employee, boolean stateRequired, boolean districtRequired,
      boolean cityRequired) {
    if (stateRequired && !hasText(employee.getAssignedState())) {
      throw new EmployeeException(employee.getRole() + " requires assigned state");
    }
    if (districtRequired && !hasText(employee.getAssignedDistrict())) {
      throw new EmployeeException(employee.getRole() + " requires assigned district");
    }
    if (cityRequired && !hasText(employee.getAssignedCity())) {
      throw new EmployeeException(employee.getRole() + " requires assigned city");
    }
  }

  private void validateManagerTerritory(Employee employee, Employee manager) {
    if (manager.getRole() == EmployeeRole.STATE_HEAD
        && !same(employee.getAssignedState(), manager.getAssignedState())) {
      throw new EmployeeException("DISTRICT_HEAD must belong to the STATE_HEAD assigned state");
    }
    if (manager.getRole() == EmployeeRole.DISTRICT_HEAD
        && (!same(employee.getAssignedState(), manager.getAssignedState())
        || !same(employee.getAssignedDistrict(), manager.getAssignedDistrict()))) {
      throw new EmployeeException("CITY_HEAD must belong to the DISTRICT_HEAD assigned district");
    }
    if (manager.getRole() == EmployeeRole.CITY_HEAD
        && (!same(employee.getAssignedState(), manager.getAssignedState())
        || !same(employee.getAssignedDistrict(), manager.getAssignedDistrict())
        || !same(employee.getAssignedCity(), manager.getAssignedCity()))) {
      throw new EmployeeException(
          "CRM, TECHNICIAN, and BILLER users must belong to the CITY_HEAD assigned city");
    }
  }

  private boolean same(String left, String right) {
    if (left == null || right == null) {
      return false;
    }
    return left.trim().equalsIgnoreCase(right.trim());
  }

  private boolean hasText(String value) {
    return value != null && !value.trim().isEmpty();
  }

  private record AuthUserSnapshot(UUID id, String username, String email, EmployeeRole role,
                                  Boolean active) {
  }
}
