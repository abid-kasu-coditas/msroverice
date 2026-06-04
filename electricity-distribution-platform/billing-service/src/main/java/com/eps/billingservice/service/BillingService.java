package com.eps.billingservice.service;

import com.eps.billingservice.dto.BillRequestDTO;
import com.eps.billingservice.dto.BillResponseDTO;
import com.eps.billingservice.event.BillEventPublisher;
import com.eps.billingservice.exception.BillNotFoundException;
import com.eps.billingservice.exception.BillNumberAlreadyExistsException;
import com.eps.billingservice.mapper.BillMapper;
import com.eps.billingservice.model.Bill;
import com.eps.billingservice.model.BillStatus;
import com.eps.billingservice.repository.BillRepository;
import com.eps.billingservice.client.MeterServiceClient;
import com.eps.billingservice.client.PaymentBlockServiceClient;
import java.time.LocalDate;
import java.util.List;
import com.eps.billingservice.exception.CustomerBlockedException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    private static final Logger logger = LoggerFactory.getLogger(BillingService.class);

    private static final double DEFAULT_RATE_PER_UNIT = 7.0;
    private static final double TAX_RATE = 0.18;

    private final BillRepository billRepository;
    private final BillEventPublisher billEventPublisher;
    private final MeterServiceClient meterServiceClient;
    private final PaymentBlockServiceClient paymentBlockServiceClient;

    public BillingService(
        BillRepository billRepository,
        BillEventPublisher billEventPublisher,
        MeterServiceClient meterServiceClient,
        PaymentBlockServiceClient paymentBlockServiceClient
    ) {
        this.billRepository = billRepository;
        this.billEventPublisher = billEventPublisher;
        this.meterServiceClient = meterServiceClient;
        this.paymentBlockServiceClient = paymentBlockServiceClient;
    }

    public List<BillResponseDTO> getBills() {
        return billRepository.findAll().stream().map(BillMapper::toDTO).toList();
    }

    public List<BillResponseDTO> getBillsByCustomerId(Long customerId) {
        return billRepository.findByCustomerId(customerId).stream().map(BillMapper::toDTO).toList();
    }

    public List<BillResponseDTO> getBillsByStatus(BillStatus status) {
        return billRepository.findByStatus(status).stream().map(BillMapper::toDTO).toList();
    }

    public BillResponseDTO createBill(BillRequestDTO request) {
        // Check if customer is blocked due to non-payment
        if (paymentBlockServiceClient.isCustomerBlocked(request.getCustomerId())) {
            throw new CustomerBlockedException(
                "Cannot create bill for blocked customer: " + request.getCustomerId() +
                ". Customer must clear outstanding payments first."
            );
        }

        String billNumber = resolveBillNumber(request.getBillNumber());
        if (billRepository.existsByBillNumber(billNumber)) {
            throw new BillNumberAlreadyExistsException("A bill with number " + billNumber + " already exists");
        }

        enrichUnitsFromMeterReading(request);
        Bill bill = buildBill(request, billNumber);
        Bill savedBill = billRepository.save(bill);
        billEventPublisher.publishBillGenerated(savedBill);
        return BillMapper.toDTO(savedBill);
    }

    public BillResponseDTO getBillById(Long id) {
        return BillMapper.toDTO(findBill(id));
    }

    public BillResponseDTO updateBill(Long id, BillRequestDTO request) {
        Bill bill = findBill(id);
        String billNumber = resolveBillNumber(request.getBillNumber(), bill.getBillNumber());

        if (billRepository.existsByBillNumberAndIdNot(billNumber, id)) {
            throw new BillNumberAlreadyExistsException("A bill with number " + billNumber + " already exists");
        }

        Bill recalculatedBill = buildBill(request, billNumber);
        bill.setCustomerId(recalculatedBill.getCustomerId());
        bill.setMeterId(recalculatedBill.getMeterId());
        bill.setBillNumber(recalculatedBill.getBillNumber());
        bill.setBillDate(recalculatedBill.getBillDate());
        bill.setDueDate(recalculatedBill.getDueDate());
        bill.setUnitsConsumed(recalculatedBill.getUnitsConsumed());
        bill.setBaseAmount(recalculatedBill.getBaseAmount());
        bill.setTaxes(recalculatedBill.getTaxes());
        bill.setPenalties(recalculatedBill.getPenalties());
        bill.setDiscounts(recalculatedBill.getDiscounts());
        bill.setTotalAmount(recalculatedBill.getTotalAmount());
        bill.setStatus(request.getStatus() == null ? bill.getStatus() : request.getStatus());

        return BillMapper.toDTO(billRepository.save(bill));
    }

    public BillResponseDTO applyPayment(Long id, Double amount) {
        Bill bill = findBill(id);
        if (amount >= bill.getTotalAmount()) {
            bill.setStatus(BillStatus.PAID);
        } else if (amount > 0) {
            bill.setStatus(BillStatus.PARTIALLY_PAID);
        }
        return BillMapper.toDTO(billRepository.save(bill));
    }

    public void deleteBill(Long id) {
        if (!billRepository.existsById(id)) {
            throw new BillNotFoundException("Bill not found with ID: " + id);
        }
        billRepository.deleteById(id);
    }

    private Bill findBill(Long id) {
        return billRepository.findById(id)
            .orElseThrow(() -> new BillNotFoundException("Bill not found with ID: " + id));
    }

    private Bill buildBill(BillRequestDTO request, String billNumber) {
        LocalDate billDate = request.getBillDate() == null ? LocalDate.now() : request.getBillDate();
        LocalDate dueDate = request.getDueDate() == null ? billDate.plusDays(21) : request.getDueDate();
        double ratePerUnit = request.getRatePerUnit() == null ? DEFAULT_RATE_PER_UNIT : request.getRatePerUnit();
        double penalties = request.getPenalties() == null ? 0.0 : request.getPenalties();
        double discounts = request.getDiscounts() == null ? 0.0 : request.getDiscounts();
        double baseAmount = request.getUnitsConsumed() * ratePerUnit;
        double taxes = baseAmount * TAX_RATE;
        double totalAmount = baseAmount + taxes + penalties - discounts;

        return new Bill(
            request.getCustomerId(),
            request.getMeterId(),
            billNumber,
            billDate,
            dueDate,
            request.getUnitsConsumed(),
            baseAmount,
            taxes,
            penalties,
            discounts,
            Math.max(totalAmount, 0.0),
            request.getStatus()
        );
    }

    private void enrichUnitsFromMeterReading(BillRequestDTO request) {
        try {
            JsonNode meterReading = meterServiceClient.getMeterReadings(request.getMeterId());
            if (meterReading == null) {
                return;
            }
            if (meterReading.hasNonNull("unitsConsumed")) {
                request.setUnitsConsumed(meterReading.get("unitsConsumed").asDouble());
                return;
            }
            if (meterReading.hasNonNull("currentReading") && meterReading.hasNonNull("previousReading")) {
                int unitsConsumed = MeterServiceClient.calculateUnitsConsumed(
                    meterReading.get("currentReading").asInt(),
                    meterReading.get("previousReading").asInt()
                );
                request.setUnitsConsumed((double) unitsConsumed);
            }
        } catch (RuntimeException ex) {
            logger.warn("Meter reading enrichment skipped for meter {}: {}", request.getMeterId(), ex.getMessage());
        }
    }

    private String resolveBillNumber(String requestedBillNumber) {
        return resolveBillNumber(requestedBillNumber, "BILL-" + System.currentTimeMillis());
    }

    private String resolveBillNumber(String requestedBillNumber, String fallback) {
        if (requestedBillNumber == null || requestedBillNumber.isBlank()) {
            return fallback;
        }
        return requestedBillNumber;
    }
}
