package com.eps.platformservice.service;

import com.eps.platformservice.dto.PlatformUserRequest;
import com.eps.platformservice.dto.PocTenantAssignmentRequest;
import com.eps.platformservice.model.PlatformUser;
import com.eps.platformservice.model.PocTenantAssignment;
import com.eps.platformservice.repository.PlatformUserRepository;
import com.eps.platformservice.repository.PocTenantAssignmentRepository;
import com.eps.platformservice.repository.TenantRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlatformUserService {

  private final PlatformUserRepository platformUserRepository;
  private final PocTenantAssignmentRepository pocTenantAssignmentRepository;
  private final TenantRepository tenantRepository;

  public PlatformUserService(PlatformUserRepository platformUserRepository,
      PocTenantAssignmentRepository pocTenantAssignmentRepository,
      TenantRepository tenantRepository) {
    this.platformUserRepository = platformUserRepository;
    this.pocTenantAssignmentRepository = pocTenantAssignmentRepository;
    this.tenantRepository = tenantRepository;
  }

  public List<PlatformUser> findAllUsers() {
    return platformUserRepository.findAll();
  }

  @Transactional
  public PlatformUser createUser(PlatformUserRequest request) {
    if (platformUserRepository.existsByAuthUserId(request.authUserId())) {
      throw new IllegalArgumentException("Platform user already exists for auth user: " + request.authUserId());
    }
    PlatformUser user = new PlatformUser();
    user.setAuthUserId(request.authUserId());
    user.setName(request.name());
    user.setRole(request.role());
    user.setActive(request.active() == null ? Boolean.TRUE : request.active());
    return platformUserRepository.save(user);
  }

  public List<PocTenantAssignment> findAllAssignments() {
    return pocTenantAssignmentRepository.findAll();
  }

  @Transactional
  public PocTenantAssignment assignPoc(PocTenantAssignmentRequest request) {
    if (!platformUserRepository.existsById(request.pocUserId())) {
      throw new IllegalArgumentException("Platform POC user not found: " + request.pocUserId());
    }
    if (!tenantRepository.existsById(request.tenantId())) {
      throw new IllegalArgumentException("Tenant not found: " + request.tenantId());
    }
    if (pocTenantAssignmentRepository.existsByPocUserIdAndTenantId(
        request.pocUserId(), request.tenantId())) {
      throw new IllegalArgumentException("POC is already assigned to tenant");
    }
    PocTenantAssignment assignment = new PocTenantAssignment();
    assignment.setPocUserId(request.pocUserId());
    assignment.setTenantId(request.tenantId());
    return pocTenantAssignmentRepository.save(assignment);
  }
}
