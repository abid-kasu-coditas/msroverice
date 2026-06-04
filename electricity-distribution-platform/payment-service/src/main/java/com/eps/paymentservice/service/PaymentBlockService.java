package com.eps.paymentservice.service;

import com.eps.paymentservice.model.PaymentBlock;
import com.eps.paymentservice.repository.PaymentBlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentBlockService {

    private final PaymentBlockRepository paymentBlockRepository;

    public PaymentBlockService(PaymentBlockRepository paymentBlockRepository) {
        this.paymentBlockRepository = paymentBlockRepository;
    }

    /**
     * Block a customer from services due to unpaid bills
     */
    public PaymentBlock blockCustomer(Long customerId, Long billId, String reason) {
        // Check if customer already has an active block
        Optional<PaymentBlock> existingBlock = paymentBlockRepository.findByCustomerIdAndIsBlockedTrue(customerId);
        if (existingBlock.isPresent()) {
            return existingBlock.get(); // Already blocked
        }

        // Create new block
        PaymentBlock block = new PaymentBlock(customerId, billId, reason);
        return paymentBlockRepository.save(block);
    }

    /**
     * Unblock a customer (when they pay the overdue amount)
     */
    public PaymentBlock unblockCustomer(Long customerId) {
        Optional<PaymentBlock> activeBlock = paymentBlockRepository.findByCustomerIdAndIsBlockedTrue(customerId);
        if (activeBlock.isPresent()) {
            PaymentBlock block = activeBlock.get();
            block.setIsBlocked(false);
            block.setUnblockedAt(LocalDateTime.now());
            block.setUpdatedAt(LocalDateTime.now());
            return paymentBlockRepository.save(block);
        }
        return null;
    }

    /**
     * Check if customer is currently blocked
     */
    public Boolean isCustomerBlocked(Long customerId) {
        return paymentBlockRepository.findByCustomerIdAndIsBlockedTrue(customerId).isPresent();
    }

    /**
     * Get active block for customer
     */
    public Optional<PaymentBlock> getActiveBlock(Long customerId) {
        return paymentBlockRepository.findByCustomerIdAndIsBlockedTrue(customerId);
    }

    /**
     * Get all blocks for customer (including historical)
     */
    public List<PaymentBlock> getAllBlocksForCustomer(Long customerId) {
        return paymentBlockRepository.findByCustomerId(customerId);
    }

    /**
     * Get total blocked customers count
     */
    public long getBlockedCustomersCount() {
        return paymentBlockRepository.countByIsBlockedTrue();
    }

    /**
     * Get block by bill ID
     */
    public Optional<PaymentBlock> getBlockByBillId(Long billId) {
        return paymentBlockRepository.findByBillId(billId);
    }

    /**
     * Get block by ID
     */
    public Optional<PaymentBlock> getBlockById(Long blockId) {
        return paymentBlockRepository.findById(blockId);
    }

    /**
     * Delete block
     */
    public void deleteBlock(Long blockId) {
        paymentBlockRepository.deleteById(blockId);
    }
}
