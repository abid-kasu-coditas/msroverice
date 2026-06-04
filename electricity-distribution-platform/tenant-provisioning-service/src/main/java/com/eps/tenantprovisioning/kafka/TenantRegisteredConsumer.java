package com.eps.tenantprovisioning.kafka;

import com.eps.grpc.events.tenant.TenantRegisteredEvent;
import com.eps.tenantprovisioning.service.TenantSchemaProvisioningService;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TenantRegisteredConsumer {

  private final TenantSchemaProvisioningService provisioningService;

  public TenantRegisteredConsumer(TenantSchemaProvisioningService provisioningService) {
    this.provisioningService = provisioningService;
  }

  @KafkaListener(topics = "tenant-registered", groupId = "tenant-provisioning-service")
  public void onTenantRegistered(byte[] payload) throws InvalidProtocolBufferException {
    TenantRegisteredEvent event = TenantRegisteredEvent.parseFrom(payload);
    provisioningService.provisionTenant(event.getTenantCode());
  }
}
