package com.eps.clientonboarding.repository;

import com.eps.clientonboarding.model.ClientCompany;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientCompanyRepository extends JpaRepository<ClientCompany, UUID> {
  Optional<ClientCompany> findByRegistrationNumber(String registrationNumber);

  Optional<ClientCompany> findByEmail(String email);

  Optional<ClientCompany> findBySchemaName(String schemaName);

  List<ClientCompany> findBySalesPocId(UUID salesPocId);

  boolean existsByRegistrationNumber(String registrationNumber);
}
