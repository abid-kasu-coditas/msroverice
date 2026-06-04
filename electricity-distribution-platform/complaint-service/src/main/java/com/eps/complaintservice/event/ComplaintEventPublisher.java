package com.eps.complaintservice.event;

import com.eps.complaintservice.model.Complaint;
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
public class ComplaintEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(ComplaintEventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ComplaintEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishComplaintCreated(Complaint complaint) {
        publish("complaint-created", "COMPLAINT_CREATED", complaint);
    }

    public void publishComplaintResolved(Complaint complaint) {
        publish("complaint-resolved", "COMPLAINT_RESOLVED", complaint);
    }

    public void publishComplaintAssigned(Complaint complaint) {
        publish("complaint-assigned", "COMPLAINT_ASSIGNED", complaint);
    }

    private void publish(String topic, String eventType, Complaint complaint) {
        try {
            kafkaTemplate.send(topic, complaint.getCustomerId().toString(),
                objectMapper.writeValueAsString(buildEvent(eventType, complaint)));
        } catch (JsonProcessingException ex) {
            logger.error("Unable to serialize complaint event for complaint {}", complaint.getId(), ex);
        }
    }

    private Map<String, Object> buildEvent(String eventType, Complaint complaint) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("complaintId", complaint.getId().toString());
        data.put("customerId", complaint.getCustomerId().toString());
        data.put("category", complaint.getCategory().name());
        data.put("status", complaint.getStatus().name());
        data.put("state", complaint.getState());
        data.put("district", complaint.getDistrict());
        data.put("city", complaint.getCity());
        data.put("assignedTechnicianId", complaint.getAssignedTechnicianId());
        data.put("assignedByUserId", complaint.getAssignedByUserId());
        data.put("assignedAt", complaint.getAssignedAt());
        data.put("resolution", complaint.getResolution());

        Map<String, Object> event = new LinkedHashMap<>();
        event.put("eventId", UUID.randomUUID().toString());
        event.put("eventDate", LocalDateTime.now().toString());
        event.put("eventType", eventType);
        event.put("data", data);
        return event;
    }
}
