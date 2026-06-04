package com.eps.shared.tenant;

import java.util.UUID;

/**
 * TenantContext holds the current tenant information for the executing thread.
 * This class is thread-safe and uses ThreadLocal to store tenant context.
 */
public class TenantContext {

  private static final ThreadLocal<UUID> tenantId = new ThreadLocal<>();
  private static final ThreadLocal<String> tenantName = new ThreadLocal<>();

  public static void setTenantId(UUID id) {
    tenantId.set(id);
  }

  public static UUID getTenantId() {
    return tenantId.get();
  }

  public static void setTenantName(String name) {
    tenantName.set(name);
  }

  public static String getTenantName() {
    return tenantName.get();
  }

  public static void clear() {
    tenantId.remove();
    tenantName.remove();
  }

  public static boolean isSet() {
    return tenantId.get() != null;
  }
}
