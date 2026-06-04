package com.eps.customerservice.event;

import com.eps.customerservice.model.Customer;
import com.eps.shared.tenant.TenantContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventPublisher {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public CustomerEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public void publishCustomerOnboarded(Customer customer) {
    try {
      kafkaTemplate.send("customer-onboarded", TenantContext.getCurrentTenant(),
          objectMapper.writeValueAsString(event(customer)));
    } catch (JsonProcessingException ignored) {
      // The customer is already saved; event delivery can be retried operationally.
    }
  }

  private Map<String, Object> event(Customer customer) {
    Map<String, Object> event = new LinkedHashMap<>();
    event.put("eventId", UUID.randomUUID().toString());
    event.put("eventType", "CUSTOMER_ONBOARDED");
    event.put("eventDate", LocalDateTime.now().toString());
    event.put("tenantCode", TenantContext.getCurrentTenant());
    event.put("customerId", customer.getId());
    event.put("accountNumber", customer.getAccountNumber());
    return event;
  }
}
