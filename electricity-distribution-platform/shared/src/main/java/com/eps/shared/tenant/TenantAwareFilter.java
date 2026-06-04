package com.eps.shared.tenant;

import java.io.IOException;
import java.util.UUID;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TenantAwareFilter extracts tenant information from HTTP request headers
 * and sets it in the TenantContext for the current thread.
 */
public class TenantAwareFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      String tenantId = request.getHeader("X-Tenant-Id");
      String tenantName = request.getHeader("X-Tenant-Name");

      if (tenantId != null && !tenantId.isEmpty()) {
        TenantContext.setTenantId(UUID.fromString(tenantId));
      }

      if (tenantName != null && !tenantName.isEmpty()) {
        TenantContext.setTenantName(tenantName);
      }

      filterChain.doFilter(request, response);
    } finally {
      TenantContext.clear();
    }
  }
}
