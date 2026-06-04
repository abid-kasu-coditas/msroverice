package com.eps.analyticsservice.service;

import com.eps.analyticsservice.model.AnalyticsMetric;
import com.eps.analyticsservice.repository.AnalyticsMetricRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AnalyticsService {

    private final AnalyticsMetricRepository analyticsMetricRepository;

    public AnalyticsService(AnalyticsMetricRepository analyticsMetricRepository) {
        this.analyticsMetricRepository = analyticsMetricRepository;
    }

    /**
     * Record a new metric
     */
    public AnalyticsMetric recordMetric(AnalyticsMetric metric) {
        metric.setRecordedAt(LocalDateTime.now());
        metric.setCreatedAt(LocalDateTime.now());
        return analyticsMetricRepository.save(metric);
    }

    /**
     * Get metric by ID
     */
    public AnalyticsMetric getMetricById(UUID id) {
        return analyticsMetricRepository.findById(id).orElse(null);
    }

    /**
     * Get all metrics with pagination
     */
    public Page<AnalyticsMetric> getAllMetrics(Pageable pageable) {
        return analyticsMetricRepository.findAll(pageable);
    }

    /**
     * Get metrics by type
     */
    public Page<AnalyticsMetric> getMetricsByType(
            AnalyticsMetric.MetricType metricType,
            Pageable pageable
    ) {
        return analyticsMetricRepository.findByMetricType(metricType, pageable);
    }

    /**
     * Get metrics by customer
     */
    public Page<AnalyticsMetric> getMetricsByCustomer(UUID customerId, Pageable pageable) {
        return analyticsMetricRepository.findByCustomerId(customerId, pageable);
    }

    /**
     * Get all metrics by type (non-paginated)
     */
    public List<AnalyticsMetric> getMetricsListByType(AnalyticsMetric.MetricType metricType) {
        return analyticsMetricRepository.findByMetricType(metricType);
    }

    /**
     * Get metrics by date range
     */
    public List<AnalyticsMetric> getMetricsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return analyticsMetricRepository.findByRecordedAtBetween(startDate, endDate);
    }

    /**
     * Get count of metrics by type
     */
    public long getMetricCountByType(AnalyticsMetric.MetricType metricType) {
        return analyticsMetricRepository.countByMetricType(metricType);
    }

    /**
     * Get sum of metric values by type
     */
    public Double getSumByMetricType(AnalyticsMetric.MetricType metricType) {
        return analyticsMetricRepository.sumByMetricType(metricType);
    }

    /**
     * Get average of metric values by type
     */
    public Double getAverageByMetricType(AnalyticsMetric.MetricType metricType) {
        return analyticsMetricRepository.averageByMetricType(metricType);
    }

    /**
     * Record bill generation metric
     */
    public AnalyticsMetric recordBillGeneration(UUID customerId, UUID billId, Double billAmount) {
        AnalyticsMetric metric = new AnalyticsMetric(
                AnalyticsMetric.MetricType.BILL_GENERATED,
                "Bill generated for customer: " + customerId
        );
        metric.setCustomerId(customerId);
        metric.setBillId(billId);
        metric.setMetricValue(billAmount);
        metric.setMetricUnit("INR");
        return recordMetric(metric);
    }

    /**
     * Record payment metric
     */
    public AnalyticsMetric recordPayment(UUID customerId, UUID paymentId, Double paymentAmount) {
        AnalyticsMetric metric = new AnalyticsMetric(
                AnalyticsMetric.MetricType.PAYMENT_SUCCESS,
                "Payment received from customer: " + customerId
        );
        metric.setCustomerId(customerId);
        metric.setPaymentId(paymentId);
        metric.setMetricValue(paymentAmount);
        metric.setMetricUnit("INR");
        return recordMetric(metric);
    }

    /**
     * Record customer registration metric
     */
    public AnalyticsMetric recordCustomerRegistration(UUID customerId) {
        AnalyticsMetric metric = new AnalyticsMetric(
                AnalyticsMetric.MetricType.CUSTOMER_REGISTERED,
                "New customer registered: " + customerId
        );
        metric.setCustomerId(customerId);
        return recordMetric(metric);
    }

    /**
     * Record connection activation metric
     */
    public AnalyticsMetric recordConnectionActivation(UUID customerId, UUID connectionId) {
        AnalyticsMetric metric = new AnalyticsMetric(
                AnalyticsMetric.MetricType.CONNECTION_ACTIVATED,
                "Connection activated for customer: " + customerId
        );
        metric.setCustomerId(customerId);
        metric.setConnectionId(connectionId);
        return recordMetric(metric);
    }

    /**
     * Delete metric
     */
    public void deleteMetric(UUID id) {
        analyticsMetricRepository.deleteById(id);
    }
}
