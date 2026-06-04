package com.eps.shared.tenant;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

@Component
public class SchemaBasedMultiTenantConnectionProvider
    implements MultiTenantConnectionProvider<String> {

  private static final Pattern TENANT_CODE = Pattern.compile("[a-z0-9_]+");

  private final DataSource dataSource;

  public SchemaBasedMultiTenantConnectionProvider(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Connection getAnyConnection() throws SQLException {
    return dataSource.getConnection();
  }

  @Override
  public void releaseAnyConnection(Connection connection) throws SQLException {
    connection.close();
  }

  @Override
  public Connection getConnection(String tenantIdentifier) throws SQLException {
    Connection connection = getAnyConnection();
    setSearchPath(connection, tenantIdentifier);
    return connection;
  }

  @Override
  public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
    setPublicSearchPath(connection);
    releaseAnyConnection(connection);
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return false;
  }

  @Override
  public boolean isUnwrappableAs(Class<?> unwrapType) {
    return unwrapType.isAssignableFrom(getClass());
  }

  @Override
  public <T> T unwrap(Class<T> unwrapType) {
    if (isUnwrappableAs(unwrapType)) {
      return unwrapType.cast(this);
    }
    throw new IllegalArgumentException("Unknown unwrap type: " + unwrapType);
  }

  private void setSearchPath(Connection connection, String tenantIdentifier) throws SQLException {
    String tenantCode = normalizeTenantCode(tenantIdentifier);
    try (Statement statement = connection.createStatement()) {
      if ("public".equals(tenantCode)) {
        statement.execute("SET search_path TO public");
      } else {
        statement.execute("SET search_path TO tenant_" + tenantCode + ", public");
      }
    }
  }

  private void setPublicSearchPath(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.execute("SET search_path TO public");
    }
  }

  private String normalizeTenantCode(String tenantIdentifier) {
    if (tenantIdentifier == null || tenantIdentifier.isBlank() || "public".equals(tenantIdentifier)) {
      return "public";
    }
    String tenantCode = tenantIdentifier.toLowerCase();
    if (!TENANT_CODE.matcher(tenantCode).matches()) {
      throw new IllegalArgumentException("Invalid tenant code: " + tenantIdentifier);
    }
    return tenantCode;
  }
}
