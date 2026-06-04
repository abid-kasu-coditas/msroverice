package com.eps.billingservice.controller;

import com.eps.billingservice.dto.BillRequestDTO;
import com.eps.billingservice.dto.BillResponseDTO;
import com.eps.billingservice.model.BillStatus;
import com.eps.billingservice.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bills")
@Tag(name = "Billing", description = "API for bill generation and management")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping
    @Operation(summary = "Get all bills")
    public ResponseEntity<List<BillResponseDTO>> getBills(@RequestParam(required = false) Long customerId) {
        if (customerId != null) {
            return ResponseEntity.ok(billingService.getBillsByCustomerId(customerId));
        }
        return ResponseEntity.ok(billingService.getBills());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get bills by status")
    public ResponseEntity<List<BillResponseDTO>> getBillsByStatus(@PathVariable BillStatus status) {
        return ResponseEntity.ok(billingService.getBillsByStatus(status));
    }

    @PostMapping
    @Operation(summary = "Generate a new bill")
    public ResponseEntity<BillResponseDTO> createBill(@Valid @RequestBody BillRequestDTO request) {
        return ResponseEntity.ok(billingService.createBill(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get bill by ID")
    public ResponseEntity<BillResponseDTO> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getBillById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a bill")
    public ResponseEntity<BillResponseDTO> updateBill(@PathVariable Long id,
        @Valid @RequestBody BillRequestDTO request) {
        return ResponseEntity.ok(billingService.updateBill(id, request));
    }

    @PatchMapping("/{id}/payment-status")
    @Operation(summary = "Apply a payment amount to bill status")
    public ResponseEntity<BillResponseDTO> applyPayment(@PathVariable Long id, @RequestParam Double amount) {
        return ResponseEntity.ok(billingService.applyPayment(id, amount));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a bill")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billingService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}
