package com.eps.billingservice.client;

import com.eps.grpc.common.IdRequest;
import com.eps.grpc.meter.ConnectionRequest;
import com.eps.grpc.meter.ConnectionResponse;
import com.eps.grpc.meter.MeterGrpcServiceGrpc;
import com.eps.grpc.meter.MeterTypeResponse;
import com.eps.shared.tenant.TenantContext;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MeterServiceClient {

    private final String meterServiceAddress;
    private final int meterServiceGrpcPort;

    public MeterServiceClient(
        @Value("${meter-service.address:localhost}") String meterServiceAddress,
        @Value("${meter-service.grpc-port:9082}") int meterServiceGrpcPort) {
        this.meterServiceAddress = meterServiceAddress;
        this.meterServiceGrpcPort = meterServiceGrpcPort;
    }

    public ConnectionResponse getConnectionDetails(Long connectionId) {
        return call(stub -> stub.getConnectionDetails(ConnectionRequest.newBuilder()
            .setConnectionId(connectionId)
            .setTenantId(tenantId())
            .build()), "Unable to fetch meter connection details for " + connectionId);
    }

    public MeterTypeResponse getMeterTypeRate(Long meterTypeId) {
        return call(stub -> stub.getMeterTypeRate(IdRequest.newBuilder()
            .setId(meterTypeId)
            .setTenantId(tenantId())
            .build()), "Unable to fetch meter type rate for " + meterTypeId);
    }

    private <T> T call(Function<MeterGrpcServiceGrpc.MeterGrpcServiceBlockingStub, T> operation,
                       String failureMessage) {
        ManagedChannel channel = ManagedChannelBuilder
            .forAddress(meterServiceAddress, meterServiceGrpcPort)
            .usePlaintext()
            .build();
        try {
            return operation.apply(MeterGrpcServiceGrpc.newBlockingStub(channel));
        } catch (StatusRuntimeException ex) {
            throw new IllegalStateException(failureMessage + ": " + ex.getMessage(), ex);
        } finally {
            channel.shutdown();
        }
    }

    private String tenantId() {
        String tenantId = TenantContext.getCurrentTenant();
        return tenantId == null ? "" : tenantId;
    }
}
