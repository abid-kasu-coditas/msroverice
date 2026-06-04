package com.eps.platformbillingservice.event;

import com.eps.grpc.events.tenant.TenantSuspendedEvent;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PlatformBillingEventPublisher {

  private final KafkaTemplate<String, byte[]> kafkaTemplate;

  public PlatformBillingEventPublisher(KafkaTemplate<String, byte[]> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void tenantSuspended(String tenantCode, String reason) {
    TenantSuspendedEvent event = TenantSuspendedEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setTenantCode(tenantCode)
        .setReason(reason)
        .setSuspendedAt(LocalDateTime.now().toString())
        .build();
    kafkaTemplate.send("tenant-suspended", tenantCode, event.toByteArray());
  }
}
