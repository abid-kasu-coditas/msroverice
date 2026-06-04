package com.eps.meterservice.grpc;

import com.eps.grpc.common.IdRequest;
import com.eps.grpc.meter.ConnectionRequest;
import com.eps.grpc.meter.ConnectionResponse;
import com.eps.grpc.meter.MeterGrpcServiceGrpc;
import com.eps.grpc.meter.MeterTypeResponse;
import com.eps.shared.tenant.TenantContext;
import com.eps.meterservice.model.MeterType;
import com.eps.meterservice.repository.MeterTypeRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class MeterRateGrpcService extends MeterGrpcServiceGrpc.MeterGrpcServiceImplBase {

  private final MeterTypeRepository meterTypeRepository;

  public MeterRateGrpcService(MeterTypeRepository meterTypeRepository) {
    this.meterTypeRepository = meterTypeRepository;
  }

  @Override
  public void getConnectionDetails(ConnectionRequest request,
      StreamObserver<ConnectionResponse> responseObserver) {
    withTenant(request.getTenantId(), () -> {
      responseObserver.onNext(ConnectionResponse.newBuilder()
          .setConnectionId(request.getConnectionId())
          .setCustomerId(request.getConnectionId())
          .setMeterId(request.getConnectionId())
          .setMeterTypeId(1)
          .setAccountNumber("CONN-" + request.getConnectionId())
          .setStatus("ACTIVE")
          .build());
      responseObserver.onCompleted();
    });
  }

  @Override
  public void getMeterTypeRate(IdRequest request,
      StreamObserver<MeterTypeResponse> responseObserver) {
    withTenant(request.getTenantId(), () -> {
      try {
        MeterType meterType = meterTypeRepository.findByIdAndActiveTrue(request.getId())
            .orElseThrow(() -> new IllegalArgumentException("Meter type not found: " + request.getId()));
        responseObserver.onNext(MeterTypeResponse.newBuilder()
            .setId(meterType.getId())
            .setCode(meterType.getCode())
            .setName(meterType.getName())
            .setRatePerUnit(meterType.getRatePerUnit())
            .build());
        responseObserver.onCompleted();
      } catch (RuntimeException ex) {
        responseObserver.onError(Status.NOT_FOUND
            .withDescription(ex.getMessage())
            .asRuntimeException());
      }
    });
  }

  private void withTenant(String tenantId, Runnable work) {
    if (tenantId != null && !tenantId.isBlank()) {
      TenantContext.setCurrentTenant(tenantId);
    }
    try {
      work.run();
    } finally {
      TenantContext.clear();
    }
  }
}
