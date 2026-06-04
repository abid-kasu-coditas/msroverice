package com.eps.tenantuserservice.repository;

import com.eps.tenantuserservice.model.TenantUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantUserRepository extends JpaRepository<TenantUser, Long> {

  List<TenantUser> findByRole(String role);

  Optional<TenantUser> findFirstByRoleAndAreaIdAndActiveTrue(String role, Long areaId);

  Optional<TenantUser> findFirstByRoleAndCityIdAndActiveTrue(String role, Long cityId);
}
