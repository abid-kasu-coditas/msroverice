package com.eps.clientonboarding.mapper;

import com.eps.clientonboarding.dto.ClientOnboardingRequestDTO;
import com.eps.clientonboarding.dto.ClientOnboardingResponseDTO;
import com.eps.clientonboarding.model.ClientCompany;
import com.eps.clientonboarding.model.OnboardingStatus;
import java.time.LocalDateTime;

public class ClientOnboardingMapper {

  public static ClientCompany toModel(ClientOnboardingRequestDTO dto) {
    ClientCompany company = new ClientCompany();
    company.setSalesPocId(dto.getSalesPocId());
    company.setCompanyName(dto.getCompanyName());
    company.setRegistrationNumber(dto.getRegistrationNumber());
    company.setEmail(dto.getEmail());
    company.setPhone(dto.getPhone());
    company.setAddress(dto.getAddress());
    company.setCity(dto.getCity());
    company.setState(dto.getState());
    company.setCountry(dto.getCountry());
    company.setAdminUsername(dto.getAdminUsername());
    company.setAdminEmail(dto.getAdminEmail());
    company.setStatus(OnboardingStatus.SUBMITTED);
    company.setOnboardedAt(LocalDateTime.now());

    String schemaName = generateSchemaName(dto.getCompanyName());
    company.setSchemaName(schemaName);
    company.setDatabaseUrl("jdbc:postgresql://localhost:5432/electricity_distribution?currentSchema=" + schemaName);

    return company;
  }

  public static ClientOnboardingResponseDTO toDTO(ClientCompany company) {
    ClientOnboardingResponseDTO dto = new ClientOnboardingResponseDTO();
    dto.setId(company.getId());
    dto.setSalesPocId(company.getSalesPocId());
    dto.setCompanyName(company.getCompanyName());
    dto.setRegistrationNumber(company.getRegistrationNumber());
    dto.setEmail(company.getEmail());
    dto.setPhone(company.getPhone());
    dto.setAddress(company.getAddress());
    dto.setCity(company.getCity());
    dto.setState(company.getState());
    dto.setCountry(company.getCountry());
    dto.setStatus(company.getStatus());
    dto.setSchemaName(company.getSchemaName());
    dto.setDatabaseUrl(company.getDatabaseUrl());
    dto.setOnboardedAt(company.getOnboardedAt());
    dto.setActivatedAt(company.getActivatedAt());
    return dto;
  }

  private static String generateSchemaName(String companyName) {
    return companyName.toLowerCase().replaceAll("\\s+", "_").replaceAll("[^a-z0-9_]", "")
        + "_" + System.currentTimeMillis();
  }
}
