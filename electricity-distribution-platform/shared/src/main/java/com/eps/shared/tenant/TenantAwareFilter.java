package com.eps.shared.tenant;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Extracts the tenant code injected by the API gateway.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantAwareFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String tenantId = request.getHeader("X-Tenant-ID");

    if (tenantId == null || tenantId.isBlank()) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing X-Tenant-ID header");
      return;
    }

    TenantContext.setCurrentTenant(tenantId);
    try {
      filterChain.doFilter(request, response);
    } finally {
      TenantContext.clear();
    }
  }
}
