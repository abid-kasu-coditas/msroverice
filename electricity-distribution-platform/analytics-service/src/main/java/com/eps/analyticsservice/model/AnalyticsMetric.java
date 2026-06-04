package com.eps.analyticsservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "analytics_metrics")
public class AnalyticsMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "metric_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MetricType metricType;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "connection_id")
    private UUID connectionId;

    @Column(name = "bill_id")
    private UUID billId;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "metric_value")
    private Double metricValue;

    @Column(name = "metric_unit")
    private String metricUnit;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "tenant_id")
    private UUID tenantId;

    // Constructors
    public AnalyticsMetric() {
    }

    public AnalyticsMetric(MetricType metricType, String description) {
        this.metricType = metricType;
        this.description = description;
        this.recordedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(UUID connectionId) {
        this.connectionId = connectionId;
    }

    public UUID getBillId() {
        return billId;
    }

    public void setBillId(UUID billId) {
        this.billId = billId;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public Double getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(Double metricValue) {
        this.metricValue = metricValue;
    }

    public String getMetricUnit() {
        return metricUnit;
    }

    public void setMetricUnit(String metricUnit) {
        this.metricUnit = metricUnit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    // Enums
    public enum MetricType {
        BILL_GENERATED,
        BILL_COUNT,
        PAYMENT_SUCCESS,
        PAYMENT_FAILED,
        PAYMENT_AMOUNT,
        CUSTOMER_REGISTERED,
        CUSTOMER_COUNT,
        CONNECTION_ACTIVATED,
        CONNECTION_REJECTED,
        METER_READING,
        COMPLAINT_REGISTERED,
        COMPLAINT_RESOLVED,
        SERVICE_BLOCKED,
        REVENUE_COLLECTED
    }
}
