package com.eps.tenantuserservice.service;

import com.eps.tenantuserservice.dto.TenantUserRequest;
import com.eps.tenantuserservice.model.TenantUser;
import com.eps.tenantuserservice.repository.TenantUserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantUserManagementService {

  private final TenantUserRepository tenantUserRepository;

  public TenantUserManagementService(TenantUserRepository tenantUserRepository) {
    this.tenantUserRepository = tenantUserRepository;
  }

  public List<TenantUser> findAll(String role) {
    return role == null || role.isBlank()
        ? tenantUserRepository.findAll()
        : tenantUserRepository.findByRole(role);
  }

  public TenantUser findById(Long id) {
    return tenantUserRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Tenant user not found: " + id));
  }

  @Transactional
  public TenantUser create(TenantUserRequest request) {
    TenantUser user = new TenantUser();
    apply(request, user);
    return tenantUserRepository.save(user);
  }

  @Transactional
  public TenantUser update(Long id, TenantUserRequest request) {
    TenantUser user = findById(id);
    apply(request, user);
    return tenantUserRepository.save(user);
  }

  public TenantUser technicianByArea(Long areaId) {
    return tenantUserRepository.findFirstByRoleAndAreaIdAndActiveTrue("TECHNICIAN", areaId)
        .orElseThrow(() -> new IllegalArgumentException("No active technician for area: " + areaId));
  }

  public TenantUser bpoByCity(Long cityId) {
    return tenantUserRepository.findFirstByRoleAndCityIdAndActiveTrue("BPO_EMPLOYEE", cityId)
        .orElseThrow(() -> new IllegalArgumentException("No active BPO employee for city: " + cityId));
  }

  public TenantUser managerByLevel(String level, Long cityId) {
    String role = "BPO_MANAGER_" + level.toUpperCase();
    return tenantUserRepository.findFirstByRoleAndCityIdAndActiveTrue(role, cityId)
        .orElseThrow(() -> new IllegalArgumentException("No active manager for level: " + level));
  }

  private void apply(TenantUserRequest request, TenantUser user) {
    user.setAuthUserId(request.authUserId());
    user.setName(request.name());
    user.setEmail(request.email());
    user.setPhone(request.phone());
    user.setRole(request.role());
    user.setCityId(request.cityId());
    user.setAreaId(request.areaId());
    user.setManagerId(request.managerId());
    user.setActive(request.active() == null ? Boolean.TRUE : request.active());
  }
}
