package com.eps.clientonboarding.service;

import com.eps.clientonboarding.dto.ClientOnboardingRequestDTO;
import com.eps.clientonboarding.dto.ClientOnboardingResponseDTO;
import com.eps.clientonboarding.exception.ClientCompanyException;
import com.eps.clientonboarding.mapper.ClientOnboardingMapper;
import com.eps.clientonboarding.model.ClientCompany;
import com.eps.clientonboarding.model.OnboardingStatus;
import com.eps.clientonboarding.repository.ClientCompanyRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Service
public class ClientOnboardingService {

  private final ClientCompanyRepository clientCompanyRepository;
  private final JdbcTemplate jdbcTemplate;
  private final RestClient employeeServiceClient;

  public ClientOnboardingService(ClientCompanyRepository clientCompanyRepository,
      JdbcTemplate jdbcTemplate,
      @Value("${employee.service.url:http://localhost:8091}") String employeeServiceUrl) {
    this.clientCompanyRepository = clientCompanyRepository;
    this.jdbcTemplate = jdbcTemplate;
    this.employeeServiceClient = RestClient.builder()
        .baseUrl(employeeServiceUrl)
        .build();
  }

  @Transactional
  public ClientOnboardingResponseDTO onboardClient(ClientOnboardingRequestDTO dto) {
    if (dto.getSalesPocId() == null) {
      throw new ClientCompanyException("Sales POC ID is required for client onboarding");
    }
    validateSalesPoc(dto.getSalesPocId());
    if (clientCompanyRepository.existsByRegistrationNumber(dto.getRegistrationNumber())) {
      throw new ClientCompanyException(
          "Client company with registration number already exists: " + dto.getRegistrationNumber());
    }

    ClientCompany company = ClientOnboardingMapper.toModel(dto);
    ClientCompany savedCompany = clientCompanyRepository.save(company);

    try {
      createClientSchema(savedCompany.getSchemaName());
      initializeClientSchema(savedCompany.getSchemaName());

      savedCompany.setStatus(OnboardingStatus.SCHEMA_CREATED);
      savedCompany.setActivatedAt(LocalDateTime.now());
      savedCompany = clientCompanyRepository.save(savedCompany);

    } catch (Exception e) {
      clientCompanyRepository.delete(savedCompany);
      throw new ClientCompanyException("Failed to onboard client: " + e.getMessage(), e);
    }

    return ClientOnboardingMapper.toDTO(savedCompany);
  }

  private void createClientSchema(String schemaName) {
    String sql = String.format("CREATE SCHEMA IF NOT EXISTS %s", schemaName);
    jdbcTemplate.execute(sql);
  }

  private void validateSalesPoc(UUID salesPocId) {
    try {
      employeeServiceClient.get()
          .uri("/api/employees/{id}/role/SALES_POC/active", salesPocId)
          .retrieve()
          .toBodilessEntity();
    } catch (RestClientResponseException e) {
      throw new ClientCompanyException(
          "Sales POC ID must reference an active SALES_POC employee: " + salesPocId);
    } catch (RestClientException e) {
      throw new ClientCompanyException(
          "Unable to validate Sales POC with employee-service: " + e.getMessage(), e);
    }
  }

  private void initializeClientSchema(String schemaName) {
    String[] initScripts = {
        String.format("CREATE TABLE IF NOT EXISTS %s.customers ("
            + "id UUID PRIMARY KEY DEFAULT gen_random_uuid(), "
            + "name VARCHAR(255) NOT NULL, "
            + "email VARCHAR(255) UNIQUE, "
            + "phone VARCHAR(20), "
            + "address TEXT, "
            + "city VARCHAR(100), "
            + "state VARCHAR(100), "
            + "status VARCHAR(50), "
            + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ")", schemaName),
        String.format("CREATE TABLE IF NOT EXISTS %s.connections ("
            + "id UUID PRIMARY KEY DEFAULT gen_random_uuid(), "
            + "customer_id UUID NOT NULL, "
            + "connection_number VARCHAR(255) UNIQUE, "
            + "service_address TEXT, "
            + "tariff_plan VARCHAR(100), "
            + "load_capacity DECIMAL(10,2), "
            + "status VARCHAR(50), "
            + "connection_date DATE, "
            + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ")", schemaName),
        String.format("CREATE TABLE IF NOT EXISTS %s.meters ("
            + "id UUID PRIMARY KEY DEFAULT gen_random_uuid(), "
            + "connection_id UUID NOT NULL, "
            + "meter_number VARCHAR(255) UNIQUE, "
            + "status VARCHAR(50), "
            + "installation_date DATE, "
            + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ")", schemaName)
    };

    for (String script : initScripts) {
      jdbcTemplate.execute(script);
    }
  }

  @Transactional(readOnly = true)
  public List<ClientOnboardingResponseDTO> getAllClients() {
    return clientCompanyRepository.findAll().stream()
        .map(ClientOnboardingMapper::toDTO).toList();
  }

  @Transactional(readOnly = true)
  public ClientOnboardingResponseDTO getClientById(UUID id) {
    ClientCompany company = clientCompanyRepository.findById(id)
        .orElseThrow(() -> new ClientCompanyException("Client not found with ID: " + id));
    return ClientOnboardingMapper.toDTO(company);
  }

  @Transactional(readOnly = true)
  public List<ClientOnboardingResponseDTO> getClientsBySalesPoc(UUID salesPocId) {
    return clientCompanyRepository.findBySalesPocId(salesPocId).stream()
        .map(ClientOnboardingMapper::toDTO).toList();
  }

  @Transactional
  public void deleteClient(UUID id) {
    ClientCompany company = clientCompanyRepository.findById(id)
        .orElseThrow(() -> new ClientCompanyException("Client not found with ID: " + id));

    try {
      dropClientSchema(company.getSchemaName());
      clientCompanyRepository.delete(company);
    } catch (Exception e) {
      throw new ClientCompanyException("Failed to delete client: " + e.getMessage(), e);
    }
  }

  private void dropClientSchema(String schemaName) {
    String sql = String.format("DROP SCHEMA IF EXISTS %s CASCADE", schemaName);
    jdbcTemplate.execute(sql);
  }
}
