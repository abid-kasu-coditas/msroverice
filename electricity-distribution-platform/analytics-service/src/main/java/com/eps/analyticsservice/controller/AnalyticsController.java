package com.eps.analyticsservice.controller;

import com.eps.analyticsservice.dto.AnalyticsMetricDTO;
import com.eps.analyticsservice.mapper.AnalyticsMetricMapper;
import com.eps.analyticsservice.model.AnalyticsMetric;
import com.eps.analyticsservice.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "APIs for analytics and metrics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final AnalyticsMetricMapper analyticsMetricMapper;

    public AnalyticsController(AnalyticsService analyticsService, AnalyticsMetricMapper analyticsMetricMapper) {
        this.analyticsService = analyticsService;
        this.analyticsMetricMapper = analyticsMetricMapper;
    }

    /**
     * Get all metrics with pagination
     */
    @GetMapping("/metrics")
    @Operation(summary = "Get all metrics", description = "Retrieve all metrics with pagination")
    public ResponseEntity<Page<AnalyticsMetricDTO>> getAllMetrics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AnalyticsMetric> metrics = analyticsService.getAllMetrics(pageable);
        Page<AnalyticsMetricDTO> dtoPage = metrics.map(analyticsMetricMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Get metric by ID
     */
    @GetMapping("/metrics/{id}")
    @Operation(summary = "Get metric by ID", description = "Retrieve a specific metric")
    public ResponseEntity<AnalyticsMetricDTO> getMetricById(@PathVariable UUID id) {
        AnalyticsMetric metric = analyticsService.getMetricById(id);
        if (metric != null) {
            return ResponseEntity.ok(analyticsMetricMapper.toDTO(metric));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get metrics by type
     */
    @GetMapping("/metrics/type/{metricType}")
    @Operation(summary = "Get metrics by type", description = "Retrieve metrics filtered by type")
    public ResponseEntity<Page<AnalyticsMetricDTO>> getMetricsByType(
            @PathVariable String metricType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            AnalyticsMetric.MetricType type = AnalyticsMetric.MetricType.valueOf(metricType.toUpperCase());
            Pageable pageable = PageRequest.of(page, size);
            Page<AnalyticsMetric> metrics = analyticsService.getMetricsByType(type, pageable);
            Page<AnalyticsMetricDTO> dtoPage = metrics.map(analyticsMetricMapper::toDTO);
            return ResponseEntity.ok(dtoPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get metrics by customer
     */
    @GetMapping("/metrics/customer/{customerId}")
    @Operation(summary = "Get metrics by customer", description = "Retrieve all metrics for a customer")
    public ResponseEntity<Page<AnalyticsMetricDTO>> getMetricsByCustomer(
            @PathVariable UUID customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AnalyticsMetric> metrics = analyticsService.getMetricsByCustomer(customerId, pageable);
        Page<AnalyticsMetricDTO> dtoPage = metrics.map(analyticsMetricMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Get aggregated metrics summary
     */
    @GetMapping("/summary")
    @Operation(summary = "Get metrics summary", description = "Retrieve aggregated metrics summary")
    public ResponseEntity<Map<String, Object>> getMetricsSummary() {
        Map<String, Object> summary = Map.ofEntries(
                Map.entry("totalBillsGenerated", analyticsService.getMetricCountByType(AnalyticsMetric.MetricType.BILL_GENERATED)),
                Map.entry("totalPaymentsSuccess", analyticsService.getMetricCountByType(AnalyticsMetric.MetricType.PAYMENT_SUCCESS)),
                Map.entry("totalCustomersRegistered", analyticsService.getMetricCountByType(AnalyticsMetric.MetricType.CUSTOMER_REGISTERED)),
                Map.entry("totalConnectionsActivated", analyticsService.getMetricCountByType(AnalyticsMetric.MetricType.CONNECTION_ACTIVATED)),
                Map.entry("totalRevenueCollected", analyticsService.getSumByMetricType(AnalyticsMetric.MetricType.PAYMENT_SUCCESS)),
                Map.entry("averageBillAmount", analyticsService.getAverageByMetricType(AnalyticsMetric.MetricType.BILL_GENERATED)),
                Map.entry("averagePaymentAmount", analyticsService.getAverageByMetricType(AnalyticsMetric.MetricType.PAYMENT_SUCCESS))
        );
        return ResponseEntity.ok(summary);
    }

    /**
     * Get bill analytics
     */
    @GetMapping("/bills")
    @Operation(summary = "Get bill analytics", description = "Retrieve analytics related to bills")
    public ResponseEntity<Map<String, Object>> getBillAnalytics() {
        List<AnalyticsMetric> billMetrics = analyticsService.getMetricsListByType(AnalyticsMetric.MetricType.BILL_GENERATED);
        
        Map<String, Object> billAnalytics = Map.ofEntries(
                Map.entry("totalBillsGenerated", billMetrics.size()),
                Map.entry("totalBillAmount", billMetrics.stream()
                        .mapToDouble(m -> m.getMetricValue() != null ? m.getMetricValue() : 0)
                        .sum()),
                Map.entry("averageBillAmount", billMetrics.stream()
                        .mapToDouble(m -> m.getMetricValue() != null ? m.getMetricValue() : 0)
                        .average()
                        .orElse(0))
        );
        return ResponseEntity.ok(billAnalytics);
    }

    /**
     * Get payment analytics
     */
    @GetMapping("/payments")
    @Operation(summary = "Get payment analytics", description = "Retrieve analytics related to payments")
    public ResponseEntity<Map<String, Object>> getPaymentAnalytics() {
        List<AnalyticsMetric> paymentMetrics = analyticsService.getMetricsListByType(AnalyticsMetric.MetricType.PAYMENT_SUCCESS);
        
        Map<String, Object> paymentAnalytics = Map.ofEntries(
                Map.entry("totalPaymentsSuccess", paymentMetrics.size()),
                Map.entry("totalAmountCollected", paymentMetrics.stream()
                        .mapToDouble(m -> m.getMetricValue() != null ? m.getMetricValue() : 0)
                        .sum()),
                Map.entry("averagePaymentAmount", paymentMetrics.stream()
                        .mapToDouble(m -> m.getMetricValue() != null ? m.getMetricValue() : 0)
                        .average()
                        .orElse(0))
        );
        return ResponseEntity.ok(paymentAnalytics);
    }

    /**
     * Get customer analytics
     */
    @GetMapping("/customers")
    @Operation(summary = "Get customer analytics", description = "Retrieve analytics related to customers")
    public ResponseEntity<Map<String, Object>> getCustomerAnalytics() {
        long totalCustomers = analyticsService.getMetricCountByType(AnalyticsMetric.MetricType.CUSTOMER_REGISTERED);
        long totalConnections = analyticsService.getMetricCountByType(AnalyticsMetric.MetricType.CONNECTION_ACTIVATED);
        
        Map<String, Object> customerAnalytics = Map.ofEntries(
                Map.entry("totalCustomersRegistered", totalCustomers),
                Map.entry("totalConnectionsActivated", totalConnections),
                Map.entry("averageConnectionsPerCustomer", totalCustomers > 0 ? totalConnections / totalCustomers : 0)
        );
        return ResponseEntity.ok(customerAnalytics);
    }

    /**
     * Get metrics by date range
     */
    @GetMapping("/search")
    @Operation(summary = "Search metrics by date range", description = "Retrieve metrics between start and end dates")
    public ResponseEntity<List<AnalyticsMetricDTO>> searchByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = LocalDateTime.parse(endDate, formatter);
            
            List<AnalyticsMetric> metrics = analyticsService.getMetricsByDateRange(start, end);
            List<AnalyticsMetricDTO> dtoList = metrics.stream()
                    .map(analyticsMetricMapper::toDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete metric
     */
    @DeleteMapping("/metrics/{id}")
    @Operation(summary = "Delete metric", description = "Delete a specific metric")
    public ResponseEntity<Void> deleteMetric(@PathVariable UUID id) {
        analyticsService.deleteMetric(id);
        return ResponseEntity.noContent().build();
    }
}
