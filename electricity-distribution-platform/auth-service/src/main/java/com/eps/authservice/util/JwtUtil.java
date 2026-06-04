package com.eps.authservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import com.eps.authservice.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

  @Value("${jwt.secret:mySecretKeyForElectricityDistributionPlatformThatIsLongEnoughForHS256Algorithm}")
  private String jwtSecret;

  @Value("${jwt.expiration:86400000}")
  private long jwtExpirationMs;

  @Value("${jwt.refresh.expiration:604800000}")
  private long jwtRefreshExpirationMs;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", extractRole(userDetails));
    String userId = extractUserId(userDetails);
    if (userId != null) {
      claims.put("userId", userId);
    }
    if (userDetails instanceof User user) {
      claims.put("userType", user.getUserType());
      if (user.getTenantId() != null && !user.getTenantId().isBlank()) {
        claims.put("tenantId", user.getTenantId());
      }
    }
    return generateToken(userDetails.getUsername(),
        claims,
        new Date(System.currentTimeMillis() + jwtExpirationMs));
  }

  public String generateRefreshToken(String username) {
    return generateToken(username,
        new Date(System.currentTimeMillis() + jwtRefreshExpirationMs));
  }

  private String generateToken(String username, Map<String, Object> claims, Date expirationDate) {
    JwtBuilder builder = Jwts.builder()
        .subject(username)
        .claims(claims)
        .issuedAt(new Date())
        .expiration(expirationDate)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256);

    return builder.compact();
  }

  private String generateToken(String username, Date expirationDate) {
    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(expirationDate)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private String extractRole(UserDetails userDetails) {
    return userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .findFirst()
        .map(authority -> authority.startsWith("ROLE_")
            ? authority.substring("ROLE_".length())
            : authority)
        .orElse("UNKNOWN");
  }

  private String extractUserId(UserDetails userDetails) {
    if (userDetails instanceof User user && user.getId() != null) {
      return user.getId().toString();
    }
    return null;
  }

  public String extractUsername(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public String extractRole(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("role", String.class);
  }

  public String extractTenantId(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("tenantId", String.class);
  }

  public String extractUserType(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("userType", String.class);
  }

  public boolean isTokenExpired(String token) {
    try {
      return Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload()
          .getExpiration()
          .before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    }
  }
}
