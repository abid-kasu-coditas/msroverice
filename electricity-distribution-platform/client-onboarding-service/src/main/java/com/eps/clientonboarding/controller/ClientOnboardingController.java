package com.eps.clientonboarding.controller;

import com.eps.clientonboarding.dto.ClientOnboardingRequestDTO;
import com.eps.clientonboarding.dto.ClientOnboardingResponseDTO;
import com.eps.clientonboarding.service.ClientOnboardingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/onboarding")
@Tag(name = "Client Onboarding", description = "API for onboarding new client companies with schema creation")
public class ClientOnboardingController {

  private final ClientOnboardingService clientOnboardingService;

  public ClientOnboardingController(ClientOnboardingService clientOnboardingService) {
    this.clientOnboardingService = clientOnboardingService;
  }

  @PostMapping("/register")
  @Operation(summary = "Onboard a new client company with dynamic schema creation")
  public ResponseEntity<ClientOnboardingResponseDTO> onboardClient(
      @Valid @RequestBody ClientOnboardingRequestDTO dto) {
    ClientOnboardingResponseDTO response = clientOnboardingService.onboardClient(dto);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  @Operation(summary = "Get all onboarded clients")
  public ResponseEntity<List<ClientOnboardingResponseDTO>> getAllClients() {
    List<ClientOnboardingResponseDTO> clients = clientOnboardingService.getAllClients();
    return ResponseEntity.ok(clients);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get client by ID")
  public ResponseEntity<ClientOnboardingResponseDTO> getClientById(@PathVariable UUID id) {
    ClientOnboardingResponseDTO client = clientOnboardingService.getClientById(id);
    return ResponseEntity.ok(client);
  }

  @GetMapping("/sales-poc/{salesPocId}")
  @Operation(summary = "Get onboarded clients assigned to a Sales POC")
  public ResponseEntity<List<ClientOnboardingResponseDTO>> getClientsBySalesPoc(
      @PathVariable UUID salesPocId) {
    List<ClientOnboardingResponseDTO> clients =
        clientOnboardingService.getClientsBySalesPoc(salesPocId);
    return ResponseEntity.ok(clients);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Offboard a client (deletes schema)")
  public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
    clientOnboardingService.deleteClient(id);
    return ResponseEntity.noContent().build();
  }
}
