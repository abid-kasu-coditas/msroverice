package com.eps.notificationservice.kafka;

import com.eps.notificationservice.model.Notification;
import com.eps.notificationservice.service.NotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public NotificationConsumer(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "bill-generated", groupId = "notification-service-group")
    public void consumeBillGenerated(String message) {
        try {
            JsonNode eventData = objectMapper.readTree(message);
            UUID customerId = UUID.fromString(eventData.get("customerId").asText());
            
            Notification notification = new Notification();
            notification.setCustomerId(customerId);
            notification.setEventType(Notification.EventType.BILL_GENERATED);
            notification.setMessage("New bill generated: " + eventData.get("billId").asText());
            notification.setEmailBody("Your electricity bill is ready. Bill Amount: ₹" + eventData.get("totalAmount").asText());
            notification.setSmsBody("Your electricity bill is ready");
            notification.setEmailStatus(Notification.NotificationStatus.PENDING);
            notification.setSmsStatus(Notification.NotificationStatus.PENDING);
            
            notificationService.createNotification(notification);
            System.out.println("Bill Generated Notification stored for customer: " + customerId);
        } catch (Exception e) {
            System.err.println("Error processing bill-generated event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "payment-success", groupId = "notification-service-group")
    public void consumePaymentSuccess(String message) {
        try {
            JsonNode eventData = objectMapper.readTree(message);
            UUID customerId = UUID.fromString(eventData.get("customerId").asText());
            
            Notification notification = new Notification();
            notification.setCustomerId(customerId);
            notification.setEventType(Notification.EventType.PAYMENT_SUCCESS);
            notification.setMessage("Payment received successfully: " + eventData.get("transactionNumber").asText());
            notification.setEmailBody("Your payment of ₹" + eventData.get("amount").asText() + " has been received successfully");
            notification.setSmsBody("Payment confirmed. Thank you!");
            notification.setEmailStatus(Notification.NotificationStatus.PENDING);
            notification.setSmsStatus(Notification.NotificationStatus.PENDING);
            
            notificationService.createNotification(notification);
            System.out.println("Payment Success Notification stored for customer: " + customerId);
        } catch (Exception e) {
            System.err.println("Error processing payment-success event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "complaint-registered", groupId = "notification-service-group")
    public void consumeComplaintRegistered(String message) {
        try {
            JsonNode eventData = objectMapper.readTree(message);
            UUID customerId = UUID.fromString(eventData.get("customerId").asText());
            
            Notification notification = new Notification();
            notification.setCustomerId(customerId);
            notification.setEventType(Notification.EventType.COMPLAINT_REGISTERED);
            notification.setMessage("Complaint registered: " + eventData.get("complaintId").asText());
            notification.setEmailBody("Your complaint has been registered. Reference ID: " + eventData.get("complaintId").asText() + "\nDescription: " + eventData.get("description").asText());
            notification.setSmsBody("Your complaint has been registered. Reference ID: " + eventData.get("complaintId").asText());
            notification.setEmailStatus(Notification.NotificationStatus.PENDING);
            notification.setSmsStatus(Notification.NotificationStatus.PENDING);
            
            notificationService.createNotification(notification);
            System.out.println("Complaint Registered Notification stored for customer: " + customerId);
        } catch (Exception e) {
            System.err.println("Error processing complaint-registered event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "customer-registered", groupId = "notification-service-group")
    public void consumeCustomerRegistered(String message) {
        try {
            JsonNode eventData = objectMapper.readTree(message);
            UUID customerId = UUID.fromString(eventData.get("customerId").asText());
            
            Notification notification = new Notification();
            notification.setCustomerId(customerId);
            notification.setEventType(Notification.EventType.CUSTOMER_REGISTERED);
            notification.setMessage("Welcome to our electricity service platform");
            notification.setEmailBody("Welcome! You have successfully registered with our electricity service platform. Your Customer ID: " + customerId);
            notification.setSmsBody("Welcome to our service! Your Customer ID: " + customerId);
            notification.setEmailStatus(Notification.NotificationStatus.PENDING);
            notification.setSmsStatus(Notification.NotificationStatus.PENDING);
            
            notificationService.createNotification(notification);
            System.out.println("Customer Registered Notification stored for customer: " + customerId);
        } catch (Exception e) {
            System.err.println("Error processing customer-registered event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "connection-activated", groupId = "notification-service-group")
    public void consumeConnectionActivated(String message) {
        try {
            JsonNode eventData = objectMapper.readTree(message);
            UUID customerId = UUID.fromString(eventData.get("customerId").asText());
            
            Notification notification = new Notification();
            notification.setCustomerId(customerId);
            notification.setEventType(Notification.EventType.CONNECTION_ACTIVATED);
            notification.setMessage("Electricity connection activated: " + eventData.get("connectionNumber").asText());
            notification.setEmailBody("Your electricity connection has been activated. Connection Number: " + eventData.get("connectionNumber").asText());
            notification.setSmsBody("Your connection is now active!");
            notification.setEmailStatus(Notification.NotificationStatus.PENDING);
            notification.setSmsStatus(Notification.NotificationStatus.PENDING);
            
            notificationService.createNotification(notification);
            System.out.println("Connection Activated Notification stored for customer: " + customerId);
        } catch (Exception e) {
            System.err.println("Error processing connection-activated event: " + e.getMessage());
        }
    }
}
