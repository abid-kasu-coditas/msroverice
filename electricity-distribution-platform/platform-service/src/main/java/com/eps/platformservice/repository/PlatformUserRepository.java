package com.eps.platformservice.repository;

import com.eps.platformservice.model.PlatformUser;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformUserRepository extends JpaRepository<PlatformUser, Long> {

  boolean existsByAuthUserId(UUID authUserId);
}
