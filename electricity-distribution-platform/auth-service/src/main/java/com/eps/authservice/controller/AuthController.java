package com.eps.authservice.controller;

import com.eps.authservice.dto.LoginRequestDTO;
import com.eps.authservice.dto.LoginResponseDTO;
import com.eps.authservice.dto.UserRequestDTO;
import com.eps.authservice.dto.UserResponseDTO;
import com.eps.authservice.service.UserService;
import com.eps.authservice.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User authentication and registration APIs")
public class AuthController {

  private final UserService userService;
  private final JwtUtil jwtUtil;
  private final String internalServiceToken;

  public AuthController(UserService userService, JwtUtil jwtUtil,
      @Value("${internal.service.token:internal-service-token}") String internalServiceToken) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
    this.internalServiceToken = internalServiceToken;
  }

  @PostMapping("/register")
  @PreAuthorize("hasRole('SUPER_ADMIN') or "
      + "(hasRole('MANAGEMENT') and "
      + "#userRequestDTO.role != 'SUPER_ADMIN' and "
      + "#userRequestDTO.role != 'MANAGEMENT')")
  @Operation(summary = "Register a new user")
  public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequestDTO) {
    UserResponseDTO userResponseDTO = userService.registerUser(userRequestDTO);
    return ResponseEntity.ok(userResponseDTO);
  }

  @PostMapping("/login")
  @Operation(summary = "Login user and get JWT token")
  public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
    LoginResponseDTO loginResponseDTO = userService.login(loginRequestDTO);
    return ResponseEntity.ok(loginResponseDTO);
  }

  @GetMapping("/validate")
  @Operation(summary = "Validate JWT token")
  public ResponseEntity<Void> validateToken(
      @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String token = authorizationHeader.substring("Bearer ".length());
    return jwtUtil.validateToken(token)
        ? ResponseEntity.ok().build()
        : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @PostMapping("/refresh-token")
  @Operation(summary = "Refresh access token")
  public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
    String token = authorizationHeader.replace("Bearer ", "");
    String newToken = userService.validateAndRefreshToken(token);
    return ResponseEntity.ok(newToken);
  }

  @GetMapping("/users/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGEMENT')")
  @Operation(summary = "Get user by ID")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
    UserResponseDTO userResponseDTO = userService.getUserById(id);
    return ResponseEntity.ok(userResponseDTO);
  }

  @GetMapping("/internal/users/{id}")
  @Operation(summary = "Internal service lookup for auth user by ID")
  public ResponseEntity<UserResponseDTO> getInternalUserById(@PathVariable UUID id,
      @RequestHeader(value = "X-Internal-Service-Token", required = false) String serviceToken) {
    validateInternalServiceToken(serviceToken);
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @GetMapping("/users/username/{username}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGEMENT')")
  @Operation(summary = "Get user by username")
  public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
    UserResponseDTO userResponseDTO = userService.getUserByUsername(username);
    return ResponseEntity.ok(userResponseDTO);
  }

  @GetMapping("/users")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGEMENT')")
  @Operation(summary = "Get all users")
  public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    List<UserResponseDTO> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @PutMapping("/users/{id}")
  @PreAuthorize("hasRole('SUPER_ADMIN') or "
      + "(hasRole('MANAGEMENT') and "
      + "#userRequestDTO.role != 'SUPER_ADMIN' and "
      + "#userRequestDTO.role != 'MANAGEMENT')")
  @Operation(summary = "Update user")
  public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id,
      @Valid @RequestBody UserRequestDTO userRequestDTO) {
    UserResponseDTO userResponseDTO = userService.updateUser(id, userRequestDTO);
    return ResponseEntity.ok(userResponseDTO);
  }

  @DeleteMapping("/users/{id}")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  @Operation(summary = "Delete user")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  private void validateInternalServiceToken(String serviceToken) {
    if (serviceToken == null || !internalServiceToken.equals(serviceToken)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid internal service token");
    }
  }
}
