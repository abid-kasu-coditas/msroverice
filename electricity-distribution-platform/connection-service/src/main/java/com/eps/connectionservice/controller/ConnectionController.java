package com.eps.connectionservice.controller;

import com.eps.connectionservice.dto.ConnectionRequestDTO;
import com.eps.connectionservice.dto.ConnectionResponseDTO;
import com.eps.connectionservice.model.ConnectionStatus;
import com.eps.connectionservice.service.ConnectionService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/connections")
@Tag(name = "Connection", description = "API for managing electricity connections")
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping
    @Operation(summary = "Get all connections")
    public ResponseEntity<List<ConnectionResponseDTO>> getConnections(@RequestParam(required = false) UUID customerId) {
        if (customerId != null) {
            return ResponseEntity.ok(connectionService.getConnectionsByCustomerId(customerId));
        }
        return ResponseEntity.ok(connectionService.getConnections());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get connections by status")
    public ResponseEntity<List<ConnectionResponseDTO>> getConnectionsByStatus(@PathVariable ConnectionStatus status) {
        return ResponseEntity.ok(connectionService.getConnectionsByStatus(status));
    }

    @PostMapping
    @Operation(summary = "Create a new connection")
    public ResponseEntity<ConnectionResponseDTO> createConnection(@Valid @RequestBody ConnectionRequestDTO request) {
        return ResponseEntity.ok(connectionService.createConnection(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get connection by ID")
    public ResponseEntity<ConnectionResponseDTO> getConnectionById(@PathVariable UUID id) {
        return ResponseEntity.ok(connectionService.getConnectionById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a connection")
    public ResponseEntity<ConnectionResponseDTO> updateConnection(@PathVariable UUID id,
        @Valid @RequestBody ConnectionRequestDTO request) {
        return ResponseEntity.ok(connectionService.updateConnection(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a connection")
    public ResponseEntity<Void> deleteConnection(@PathVariable UUID id) {
        connectionService.deleteConnection(id);
        return ResponseEntity.noContent().build();
    }
}
