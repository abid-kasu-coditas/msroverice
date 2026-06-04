package com.eps.paymentservice.controller;

import com.eps.paymentservice.model.PaymentBlock;
import com.eps.paymentservice.service.PaymentBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments/blocks")
@Tag(name = "Payment Blocks", description = "APIs for managing payment-related service blocks")
public class PaymentBlockController {

    private final PaymentBlockService paymentBlockService;

    public PaymentBlockController(PaymentBlockService paymentBlockService) {
        this.paymentBlockService = paymentBlockService;
    }

    /**
     * Check if customer is blocked
     */
    @GetMapping("/status/{customerId}")
    @Operation(summary = "Check if customer is blocked", description = "Check if a customer has blocked services due to unpaid bills")
    public ResponseEntity<Map<String, Object>> checkBlockStatus(@PathVariable UUID customerId) {
        Boolean isBlocked = paymentBlockService.isCustomerBlocked(customerId);
        
        if (isBlocked) {
            Optional<PaymentBlock> block = paymentBlockService.getActiveBlock(customerId);
            if (block.isPresent()) {
                return ResponseEntity.ok(Map.of(
                        "customerId", customerId,
                        "isBlocked", true,
                        "blockReason", block.get().getBlockReason(),
                        "blockedAt", block.get().getBlockedAt(),
                        "blockId", block.get().getId()
                ));
            }
        }
        
        return ResponseEntity.ok(Map.of(
                "customerId", customerId,
                "isBlocked", false,
                "blockReason", "No active blocks"
        ));
    }

    /**
     * Get active block for customer
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get active block", description = "Get the active payment block for a customer")
    public ResponseEntity<PaymentBlock> getActiveBlock(@PathVariable UUID customerId) {
        Optional<PaymentBlock> block = paymentBlockService.getActiveBlock(customerId);
        return block.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all blocks for customer (including history)
     */
    @GetMapping("/customer/{customerId}/all")
    @Operation(summary = "Get all blocks for customer", description = "Get all payment blocks (including historical) for a customer")
    public ResponseEntity<List<PaymentBlock>> getAllBlocksForCustomer(@PathVariable UUID customerId) {
        List<PaymentBlock> blocks = paymentBlockService.getAllBlocksForCustomer(customerId);
        return ResponseEntity.ok(blocks);
    }

    /**
     * Block a customer
     */
    @PostMapping("/block")
    @Operation(summary = "Block customer", description = "Block a customer from using services due to unpaid bills")
    public ResponseEntity<PaymentBlock> blockCustomer(
            @RequestParam UUID customerId,
            @RequestParam UUID billId,
            @RequestParam String reason
    ) {
        PaymentBlock block = paymentBlockService.blockCustomer(customerId, billId, reason);
        return ResponseEntity.ok(block);
    }

    /**
     * Unblock a customer
     */
    @PostMapping("/unblock/{customerId}")
    @Operation(summary = "Unblock customer", description = "Unblock a customer to restore service access")
    public ResponseEntity<PaymentBlock> unblockCustomer(@PathVariable UUID customerId) {
        PaymentBlock block = paymentBlockService.unblockCustomer(customerId);
        if (block != null) {
            return ResponseEntity.ok(block);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get total blocked customers count
     */
    @GetMapping("/metrics/blocked-count")
    @Operation(summary = "Get blocked customers count", description = "Get total number of currently blocked customers")
    public ResponseEntity<Map<String, Long>> getBlockedCustomersCount() {
        long blockedCount = paymentBlockService.getBlockedCustomersCount();
        return ResponseEntity.ok(Map.of("blockedCustomers", blockedCount));
    }

    /**
     * Get block by ID
     */
    @GetMapping("/{blockId}")
    @Operation(summary = "Get block by ID", description = "Get a specific payment block by its ID")
    public ResponseEntity<PaymentBlock> getBlockById(@PathVariable UUID blockId) {
        Optional<PaymentBlock> block = paymentBlockService.getBlockById(blockId);
        return block.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
