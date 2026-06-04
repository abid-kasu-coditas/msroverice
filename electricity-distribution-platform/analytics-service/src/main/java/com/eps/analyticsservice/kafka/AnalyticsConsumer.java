package com.eps.analyticsservice.kafka;

import com.eps.analyticsservice.model.AnalyticsMetric;
import com.eps.analyticsservice.service.AnalyticsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AnalyticsConsumer {

    private final AnalyticsService analyticsService;
    private final ObjectMapper objectMapper;

    public AnalyticsConsumer(AnalyticsService analyticsService, ObjectMapper objectMapper) {
        this.analyticsService = analyticsService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "bill-generated", groupId = "analytics-service-group")
    public void consumeBillGenerated(String message) {
        try {
            JsonNode eventData = objectMapper.readTree(message);
            UUID customerId = UUID.fromString(eventData.get("customerId").asText());
            UUID billId = UUID.fromString(eventData.get("billId").asText());
            Double totalAmount = eventData.get("totalAmount").asDouble();
            
            analyticsService.recordBillGeneration(customerId, billId, totalAmount);
            System.out.println("Bill generation metric recorded for bill: " + billId);
        } catch (Exception e) {
            System.err.println("Error processing bill-generated event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "payment-success", groupId = "analytics-service-group")
    public void consumePaymentSuccess(String message) {
        try {
            JsonNode eventData = objectMapper.readTree(message);
            UUID customerId = UUID.fromString(eventData.get("customerId").asText());
            UUID paymentId = UUID.fromString(eventData.get("paymentId").asText());
            Double amount = eventData.get("amount").asDouble();
            
            analyticsService.recordPayment(customerId, paymentId, amount);
            System.out.println("Payment metric recorded for payment: " + paymentId);
        } catch (Exception e) {
            System.err.println("Error processing payment-success event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "customer-registered", groupId = "analytics-service-group")
    public void consumeCustomerRegistered(String message) {
        try {
            JsonNode eventData = objectMapper.readTree(message);
            UUID customerId = UUID.fromString(eventData.get("customerId").asText());
            
            analyticsService.recordCustomerRegistration(customerId);
            System.out.println("Customer registration metric recorded for customer: " + customerId);
        } catch (Exception e) {
            System.err.println("Error processing customer-registered event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "connection-activated", groupId = "analytics-service-group")
    public void consumeConnectionActivated(String message) {
        try {
            JsonNode eventData = objectMapper.readTree(message);
            UUID customerId = UUID.fromString(eventData.get("customerId").asText());
            UUID connectionId = UUID.fromString(eventData.get("connectionId").asText());
            
            analyticsService.recordConnectionActivation(customerId, connectionId);
            System.out.println("Connection activation metric recorded for connection: " + connectionId);
        } catch (Exception e) {
            System.err.println("Error processing connection-activated event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = {
        "tenant-registered", "tenant-provisioned", "tenant-suspended",
        "customer-onboarded", "bill-generated", "payment-received",
        "complaint-raised", "complaint-resolved", "complaint-escalated"
    }, groupId = "analytics-tenant-stats-group")
    public void recordTenantStat(String message, org.apache.kafka.clients.consumer.ConsumerRecord<String, String> record) {
        String tenantCode = record.key() == null ? "platform" : record.key();
        analyticsService.recordTenantEvent(tenantCode, record.topic());
    }
}
