package com.eps.notificationservice.kafka;

import com.eps.notificationservice.model.Notification;
import com.eps.notificationservice.service.NotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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
            JsonNode root = objectMapper.readTree(message);
            JsonNode data = data(root);
            Long customerId = data.get("customerId").asLong();
            String billId = data.get("billId").asText();
            String totalAmount = data.get("totalAmount").asText();

            store(customerId, tenantCode(root), Notification.EventType.BILL_GENERATED,
                "New bill generated: " + billId,
                "Your electricity bill is ready. Bill Amount: Rs. " + totalAmount,
                "Your electricity bill is ready");
        } catch (Exception e) {
            System.err.println("Error processing bill-generated event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "payment-received", groupId = "notification-service-group")
    public void consumePaymentReceived(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            JsonNode data = data(root);
            Long customerId = data.get("customerId").asLong();
            String transactionId = data.hasNonNull("transactionId") ? data.get("transactionId").asText() : "N/A";
            String amount = data.get("amount").asText();

            store(customerId, tenantCode(root), Notification.EventType.PAYMENT_SUCCESS,
                "Payment received successfully: " + transactionId,
                "Your payment of Rs. " + amount + " has been received successfully",
                "Payment confirmed. Thank you!");
        } catch (Exception e) {
            System.err.println("Error processing payment-received event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "customer-onboarded", groupId = "notification-service-group")
    public void consumeCustomerOnboarded(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            Long customerId = root.get("customerId").asLong();

            store(customerId, tenantCode(root), Notification.EventType.CUSTOMER_REGISTERED,
                "Welcome to our electricity service platform",
                "Welcome! You have successfully registered with our electricity service platform. Your Customer ID: "
                    + customerId,
                "Welcome to our service! Your Customer ID: " + customerId);
        } catch (Exception e) {
            System.err.println("Error processing customer-onboarded event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = {
        "tenant-provisioned", "complaint-raised", "complaint-resolved", "complaint-escalated",
        "tenant-suspended"
    }, groupId = "notification-service-log-group")
    public void consumeOperationalNotifications(String message, ConsumerRecord<String, String> record) {
        System.out.println("Notification stub for topic " + record.topic()
            + " tenant/key=" + record.key());
    }

    private void store(Long customerId, String tenantCode, Notification.EventType eventType,
                       String message, String emailBody, String smsBody) {
        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setTenantCode(tenantCode);
        notification.setEventType(eventType);
        notification.setMessage(message);
        notification.setEmailBody(emailBody);
        notification.setSmsBody(smsBody);
        notification.setEmailStatus(Notification.NotificationStatus.PENDING);
        notification.setSmsStatus(Notification.NotificationStatus.PENDING);
        notificationService.createNotification(notification);
        System.out.println(eventType + " notification stored for customer: " + customerId);
    }

    private JsonNode data(JsonNode root) {
        return root.has("data") && root.get("data").isObject() ? root.get("data") : root;
    }

    private String tenantCode(JsonNode root) {
        return root.hasNonNull("tenantCode") ? root.get("tenantCode").asText() : null;
    }
}
