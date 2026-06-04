package com.eps.shared.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

  @Override
  public String resolveCurrentTenantIdentifier() {
    String tenant = TenantContext.getCurrentTenant();
    return tenant == null || tenant.isBlank() ? "public" : tenant;
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }
}
