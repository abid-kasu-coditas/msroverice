package com.eps.billingservice.event;

import com.eps.billingservice.model.Bill;
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
public class BillEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(BillEventPublisher.class);
    private static final String BILL_GENERATED_TOPIC = "bill-generated";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public BillEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishBillGenerated(Bill bill) {
        try {
            kafkaTemplate.send(BILL_GENERATED_TOPIC, bill.getCustomerId().toString(), objectMapper.writeValueAsString(buildEvent(bill)));
        } catch (JsonProcessingException ex) {
            logger.error("Unable to serialize bill generated event for bill {}", bill.getId(), ex);
        }
    }

    private Map<String, Object> buildEvent(Bill bill) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("billId", bill.getId().toString());
        data.put("customerId", bill.getCustomerId().toString());
        data.put("meterId", bill.getMeterId().toString());
        data.put("billNumber", bill.getBillNumber());
        data.put("totalAmount", bill.getTotalAmount());
        data.put("dueDate", bill.getDueDate().toString());

        Map<String, Object> event = new LinkedHashMap<>();
        event.put("eventId", UUID.randomUUID().toString());
        event.put("eventDate", LocalDateTime.now().toString());
        event.put("eventType", "BILL_GENERATED");
        event.put("data", data);
        return event;
    }
}
