package com.eps.platformservice.repository;

import com.eps.platformservice.model.PocTenantAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PocTenantAssignmentRepository extends JpaRepository<PocTenantAssignment, Long> {

  boolean existsByPocUserIdAndTenantId(Long pocUserId, Long tenantId);
}
