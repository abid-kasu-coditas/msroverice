package com.eps.paymentservice.event;

import com.eps.paymentservice.model.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(PaymentEventPublisher.class);
    private static final String PAYMENT_PROCESSED_TOPIC = "payment-received";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishPaymentProcessed(Payment payment) {
        try {
            kafkaTemplate.send(PAYMENT_PROCESSED_TOPIC, payment.getCustomerId().toString(),
                objectMapper.writeValueAsString(buildEvent(payment)));
        } catch (JsonProcessingException ex) {
            logger.error("Unable to serialize payment processed event for payment {}", payment.getId(), ex);
        }
    }

    private Map<String, Object> buildEvent(Payment payment) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("paymentId", payment.getId().toString());
        data.put("billId", payment.getBillId().toString());
        data.put("customerId", payment.getCustomerId().toString());
        data.put("amount", payment.getAmount());
        data.put("method", payment.getMethod().name());
        data.put("status", payment.getStatus().name());
        data.put("transactionId", payment.getTransactionId());

        Map<String, Object> event = new LinkedHashMap<>();
        event.put("eventId", UUID.randomUUID().toString());
        event.put("eventDate", LocalDateTime.now().toString());
        event.put("eventType", "PAYMENT_RECEIVED");
        event.put("data", data);
        return event;
    }
}
