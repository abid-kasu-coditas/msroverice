package com.eps.meterreadingservice.service;

import com.eps.grpc.billing.BillResponse;
import com.eps.grpc.billing.BillingGrpcServiceGrpc;
import com.eps.meterreadingservice.dto.MeterReadingResponse;
import com.eps.meterreadingservice.dto.SubmitMeterReadingRequest;
import com.eps.meterreadingservice.model.MeterReading;
import com.eps.meterreadingservice.repository.MeterReadingRepository;
import com.eps.shared.tenant.TenantContext;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeterReadingSubmissionService {

  private final MeterReadingRepository meterReadingRepository;
  private final String billingAddress;
  private final int billingPort;

  public MeterReadingSubmissionService(MeterReadingRepository meterReadingRepository,
      @Value("${billing-service.address:localhost}") String billingAddress,
      @Value("${billing-service.grpc-port:9090}") int billingPort) {
    this.meterReadingRepository = meterReadingRepository;
    this.billingAddress = billingAddress;
    this.billingPort = billingPort;
  }

  @Transactional
  public MeterReadingResponse submit(SubmitMeterReadingRequest request) {
    MeterReading reading = new MeterReading();
    reading.setConnectionId(request.connectionId());
    reading.setBillerId(request.billerId());
    reading.setReadingValue(request.readingValue());
    reading.setPreviousReadingValue(request.previousReadingValue());
    reading.setReadAt(request.readAt());
    MeterReading saved = meterReadingRepository.save(reading);

    BillResponse bill = generateBill(saved);
    return new MeterReadingResponse(
        saved.getId(),
        saved.getConnectionId(),
        saved.getReadingValue(),
        saved.getReadAt(),
        bill.getBillId() == 0 ? null : bill.getBillId(),
        bill.getBillNumber().isBlank() ? null : bill.getBillNumber());
  }

  public List<MeterReading> findByConnection(Long connectionId) {
    return meterReadingRepository.findByConnectionIdOrderByReadAtDesc(connectionId);
  }

  private BillResponse generateBill(MeterReading reading) {
    ManagedChannel channel = ManagedChannelBuilder.forAddress(billingAddress, billingPort)
        .usePlaintext()
        .build();
    try {
      com.eps.grpc.billing.MeterReadingRequest request =
          com.eps.grpc.billing.MeterReadingRequest.newBuilder()
              .setConnectionId(reading.getConnectionId())
              .setReadingValue(reading.getReadingValue().doubleValue())
              .setReadAt(reading.getReadAt().toString())
              .setTenantId(TenantContext.getCurrentTenant() == null ? "" : TenantContext.getCurrentTenant())
              .build();
      return BillingGrpcServiceGrpc.newBlockingStub(channel).generateBill(request);
    } catch (RuntimeException ex) {
      return BillResponse.newBuilder().setStatus("BILLING_UNAVAILABLE").build();
    } finally {
      channel.shutdown();
    }
  }
}
