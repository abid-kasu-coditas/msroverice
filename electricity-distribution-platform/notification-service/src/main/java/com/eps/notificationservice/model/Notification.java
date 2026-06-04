package com.eps.notificationservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "event_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "email_body", columnDefinition = "TEXT")
    private String emailBody;

    @Column(name = "sms_body", columnDefinition = "TEXT")
    private String smsBody;

    @Column(name = "email_status")
    @Enumerated(EnumType.STRING)
    private NotificationStatus emailStatus = NotificationStatus.PENDING;

    @Column(name = "sms_status")
    @Enumerated(EnumType.STRING)
    private NotificationStatus smsStatus = NotificationStatus.PENDING;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "tenant_code")
    private String tenantCode;

    // Constructors
    public Notification() {
    }

    public Notification(Long customerId, EventType eventType, String message) {
        this.customerId = customerId;
        this.eventType = eventType;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }

    public NotificationStatus getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(NotificationStatus emailStatus) {
        this.emailStatus = emailStatus;
    }

    public NotificationStatus getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(NotificationStatus smsStatus) {
        this.smsStatus = smsStatus;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    // Enums
    public enum EventType {
        BILL_GENERATED,
        PAYMENT_SUCCESS,
        PAYMENT_FAILED,
        COMPLAINT_REGISTERED,
        COMPLAINT_RESOLVED,
        CUSTOMER_REGISTERED,
        CONNECTION_ACTIVATED,
        CONNECTION_REJECTED,
        SERVICE_BLOCKED,
        SERVICE_UNBLOCKED
    }

    public enum NotificationStatus {
        PENDING,
        SENT,
        FAILED,
        RETRY
    }
}
