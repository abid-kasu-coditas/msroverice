package com.eps.shared.tenant;

/**
 * Stores the current tenant code for schema-per-tenant routing.
 */
public final class TenantContext {

  private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

  private TenantContext() {
  }

  public static void setCurrentTenant(String tenantId) {
    CURRENT_TENANT.set(tenantId);
  }

  public static String getCurrentTenant() {
    return CURRENT_TENANT.get();
  }

  public static void clear() {
    CURRENT_TENANT.remove();
  }

  public static boolean isSet() {
    return CURRENT_TENANT.get() != null;
  }
}
