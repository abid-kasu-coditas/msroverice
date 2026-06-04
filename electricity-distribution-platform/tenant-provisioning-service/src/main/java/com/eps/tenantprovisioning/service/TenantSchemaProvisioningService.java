package com.eps.tenantprovisioning.service;

import com.eps.grpc.events.tenant.TenantProvisionedEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TenantSchemaProvisioningService {

  private final DataSource dataSource;
  private final JdbcTemplate jdbcTemplate;
  private final KafkaTemplate<String, byte[]> kafkaTemplate;

  public TenantSchemaProvisioningService(DataSource dataSource, JdbcTemplate jdbcTemplate,
      KafkaTemplate<String, byte[]> kafkaTemplate) {
    this.dataSource = dataSource;
    this.jdbcTemplate = jdbcTemplate;
    this.kafkaTemplate = kafkaTemplate;
  }

  public Map<String, Object> provisionTenant(String tenantCode) {
    String normalizedCode = normalizeTenantCode(tenantCode);
    String schemaName = "tenant_" + normalizedCode;

    jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
    Flyway.configure()
        .dataSource(dataSource)
        .schemas(schemaName)
        .defaultSchema(schemaName)
        .locations("classpath:db/tenant-migration")
        .baselineOnMigrate(true)
        .load()
        .migrate();

    publishProvisioned(normalizedCode, schemaName);
    return Map.of(
        "tenantCode", normalizedCode,
        "schemaName", schemaName,
        "tables", listTables(schemaName));
  }

  public List<String> listTables(String schemaName) {
    return jdbcTemplate.queryForList("""
        SELECT table_name
        FROM information_schema.tables
        WHERE table_schema = ?
        ORDER BY table_name
        """, String.class, schemaName);
  }

  private void publishProvisioned(String tenantCode, String schemaName) {
    TenantProvisionedEvent event = TenantProvisionedEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setTenantCode(tenantCode)
        .setSchemaName(schemaName)
        .setProvisionedAt(LocalDateTime.now().toString())
        .build();
    kafkaTemplate.send("tenant-provisioned", tenantCode, event.toByteArray());
  }

  private String normalizeTenantCode(String tenantCode) {
    if (tenantCode == null || tenantCode.isBlank()) {
      throw new IllegalArgumentException("Tenant code is required");
    }
    String normalized = tenantCode.trim().toLowerCase().replace('-', '_');
    if (!normalized.matches("[a-z0-9_]+")) {
      throw new IllegalArgumentException("Invalid tenant code: " + tenantCode);
    }
    return normalized;
  }
}
