package com.eps.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JwtValidationGatewayFilterFactory
    extends AbstractGatewayFilterFactory<JwtValidationGatewayFilterFactory.Config> {

  private final SecretKey signingKey;

  public JwtValidationGatewayFilterFactory(
      @Value("${jwt.secret:mySecretKeyForElectricityDistributionPlatformThatIsLongEnoughForHS256Algorithm}")
      String jwtSecret) {
    super(Config.class);
    this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      String authorizationHeader =
          exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

      if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }

      String token = authorizationHeader.substring("Bearer ".length());
      try {
        Claims claims = Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        String role = claims.get("role", String.class);
        if (!config.allowedRoles.isEmpty() && !config.allowedRoles.contains(role)) {
          exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
          return exchange.getResponse().setComplete();
        }

        String userId = claims.get("userId", String.class);
        String userType = claims.get("userType", String.class);
        String tenantId = claims.get("tenantId", String.class);

        var requestBuilder = exchange.getRequest().mutate()
            .header("X-Auth-Username", claims.getSubject())
            .header("X-Auth-Role", role);
        if (userId != null) {
          requestBuilder.header("X-Auth-User-Id", userId);
        }
        if (userType != null) {
          requestBuilder.header("X-Auth-User-Type", userType);
        }
        if (tenantId != null && !tenantId.isBlank()) {
          requestBuilder.header("X-Tenant-ID", tenantId);
        }

        return chain.filter(exchange.mutate()
            .request(requestBuilder.build())
            .build());
      } catch (JwtException | IllegalArgumentException e) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }
    };
  }

  public static Config withRoles(String... roles) {
    return new Config(new HashSet<>(Arrays.asList(roles)));
  }

  public static class Config {
    private Set<String> allowedRoles = new HashSet<>();

    public Config() {
    }

    public Config(Set<String> allowedRoles) {
      this.allowedRoles = allowedRoles;
    }

    public Set<String> getAllowedRoles() {
      return allowedRoles;
    }

    public void setAllowedRoles(Set<String> allowedRoles) {
      this.allowedRoles = allowedRoles;
    }
  }
}
