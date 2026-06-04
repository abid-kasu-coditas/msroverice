package com.eps.meterservice.controller;

import com.eps.meterservice.dto.MeterAccountRequestDTO;
import com.eps.meterservice.dto.MeterAccountResponseDTO;
import com.eps.meterservice.dto.MeterReadingRequestDTO;
import com.eps.meterservice.dto.MeterReadingResponseDTO;
import com.eps.meterservice.service.MeterService;
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
@RequestMapping("/api/meters")
@Tag(name = "Meter", description = "API for managing meter accounts and readings")
public class MeterController {

    private final MeterService meterService;

    public MeterController(MeterService meterService) {
        this.meterService = meterService;
    }

    @GetMapping
    @Operation(summary = "Get all meter accounts")
    public ResponseEntity<List<MeterAccountResponseDTO>> getMeterAccounts(
        @RequestParam(required = false) UUID connectionId) {
        if (connectionId != null) {
            return ResponseEntity.ok(meterService.getMeterAccountsByConnectionId(connectionId));
        }
        return ResponseEntity.ok(meterService.getMeterAccounts());
    }

    @PostMapping
    @Operation(summary = "Create a new meter account")
    public ResponseEntity<MeterAccountResponseDTO> createMeterAccount(
        @Valid @RequestBody MeterAccountRequestDTO request) {
        return ResponseEntity.ok(meterService.createMeterAccount(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get meter account by ID")
    public ResponseEntity<MeterAccountResponseDTO> getMeterAccountById(@PathVariable UUID id) {
        return ResponseEntity.ok(meterService.getMeterAccountById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a meter account")
    public ResponseEntity<MeterAccountResponseDTO> updateMeterAccount(@PathVariable UUID id,
        @Valid @RequestBody MeterAccountRequestDTO request) {
        return ResponseEntity.ok(meterService.updateMeterAccount(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a meter account")
    public ResponseEntity<Void> deleteMeterAccount(@PathVariable UUID id) {
        meterService.deleteMeterAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/readings")
    @Operation(summary = "Record a meter reading")
    public ResponseEntity<MeterReadingResponseDTO> recordMeterReading(@PathVariable UUID id,
        @Valid @RequestBody MeterReadingRequestDTO request) {
        return ResponseEntity.ok(meterService.recordMeterReading(id, request));
    }

    @GetMapping("/{id}/readings")
    @Operation(summary = "Get readings for a meter account")
    public ResponseEntity<List<MeterReadingResponseDTO>> getReadings(@PathVariable UUID id) {
        return ResponseEntity.ok(meterService.getReadings(id));
    }

    @GetMapping("/{id}/readings/latest")
    @Operation(summary = "Get latest reading for a meter account")
    public ResponseEntity<MeterReadingResponseDTO> getLatestReading(@PathVariable UUID id) {
        return ResponseEntity.ok(meterService.getLatestReading(id));
    }
}
