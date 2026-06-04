package com.eps.platformservice.event;

import com.eps.grpc.events.tenant.TenantRegisteredEvent;
import com.eps.grpc.events.tenant.TenantSuspendedEvent;
import com.eps.platformservice.model.Tenant;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TenantEventPublisher {

  private final KafkaTemplate<String, byte[]> kafkaTemplate;

  public TenantEventPublisher(KafkaTemplate<String, byte[]> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publishRegistered(Tenant tenant) {
    TenantRegisteredEvent event = TenantRegisteredEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setTenantCode(tenant.getCode())
        .setCompanyName(tenant.getCompanyName())
        .setContactEmail(tenant.getContactEmail())
        .setRegisteredAt(tenant.getRegisteredAt().toString())
        .build();
    kafkaTemplate.send("tenant-registered", tenant.getCode(), event.toByteArray());
  }

  public void publishSuspended(Tenant tenant) {
    TenantSuspendedEvent event = TenantSuspendedEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setTenantCode(tenant.getCode())
        .setReason(tenant.getSuspensionReason() == null ? "" : tenant.getSuspensionReason())
        .setSuspendedAt(LocalDateTime.now().toString())
        .build();
    kafkaTemplate.send("tenant-suspended", tenant.getCode(), event.toByteArray());
  }
}
