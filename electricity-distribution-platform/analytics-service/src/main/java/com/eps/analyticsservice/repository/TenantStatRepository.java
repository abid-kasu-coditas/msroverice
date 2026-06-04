package com.eps.analyticsservice.repository;

import com.eps.analyticsservice.model.TenantStat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantStatRepository extends JpaRepository<TenantStat, Long> {

  List<TenantStat> findByTenantCode(String tenantCode);
}
