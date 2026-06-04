package com.eps.billingservice.grpc;

import com.eps.billingservice.dto.BillRequestDTO;
import com.eps.billingservice.dto.BillResponseDTO;
import com.eps.billingservice.service.BillingService;
import com.eps.grpc.billing.BillResponse;
import com.eps.grpc.billing.BillingGrpcServiceGrpc;
import com.eps.grpc.billing.MeterReadingRequest;
import com.eps.shared.tenant.TenantContext;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BillingGrpcServiceImpl extends BillingGrpcServiceGrpc.BillingGrpcServiceImplBase {

  private final BillingService billingService;

  public BillingGrpcServiceImpl(BillingService billingService) {
    this.billingService = billingService;
  }

  @Override
  public void generateBill(MeterReadingRequest request, StreamObserver<BillResponse> responseObserver) {
    if (request.getTenantId() != null && !request.getTenantId().isBlank()) {
      TenantContext.setCurrentTenant(request.getTenantId());
    }
    try {
      BillRequestDTO billRequest = new BillRequestDTO();
      billRequest.setCustomerId(request.getConnectionId());
      billRequest.setMeterId(request.getConnectionId());
      billRequest.setUnitsConsumed(request.getReadingValue());
      BillResponseDTO bill = billingService.createBill(billRequest);
      responseObserver.onNext(BillResponse.newBuilder()
          .setBillId(bill.getId())
          .setBillNumber(bill.getBillNumber())
          .setUnitsConsumed(bill.getUnitsConsumed())
          .setTotalAmount(bill.getTotalAmount())
          .setStatus(bill.getStatus().name())
          .build());
      responseObserver.onCompleted();
    } finally {
      TenantContext.clear();
    }
  }
}
