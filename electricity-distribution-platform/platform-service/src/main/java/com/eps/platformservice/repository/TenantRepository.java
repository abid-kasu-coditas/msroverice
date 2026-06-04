package com.eps.platformservice.repository;

import com.eps.platformservice.model.Tenant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

  Optional<Tenant> findByCode(String code);

  boolean existsByCode(String code);
}
