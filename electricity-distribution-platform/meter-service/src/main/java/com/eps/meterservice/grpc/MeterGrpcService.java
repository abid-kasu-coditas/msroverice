package com.eps.meterservice.grpc;

import com.eps.meterservice.dto.MeterAccountRequestDTO;
import com.eps.meterservice.dto.MeterAccountResponseDTO;
import com.eps.meterservice.dto.MeterReadingRequestDTO;
import com.eps.meterservice.dto.MeterReadingResponseDTO;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class MeterGrpcService extends MeterServiceGrpc.MeterServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(MeterGrpcService.class);

    private final com.eps.meterservice.service.MeterService meterService;

    public MeterGrpcService(com.eps.meterservice.service.MeterService meterService) {
        this.meterService = meterService;
    }

    @Override
    public void createMeterAccount(CreateMeterAccountRequest request,
                                   StreamObserver<CreateMeterAccountResponse> responseObserver) {
        try {
            MeterAccountRequestDTO meterRequest = new MeterAccountRequestDTO(
                UUID.fromString(request.getConnectionId()),
                request.getMeterSerialNumber(),
                request.getMeterType(),
                null,
                null
            );
            MeterAccountResponseDTO meterAccount = meterService.createMeterAccount(meterRequest);
            CreateMeterAccountResponse response = CreateMeterAccountResponse.newBuilder()
                .setMeterId(meterAccount.getId().toString())
                .setStatus(meterAccount.getStatus().name())
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            logger.error("gRPC createMeterAccount failed", ex);
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getMeterReading(GetMeterReadingRequest request,
                                StreamObserver<GetMeterReadingResponse> responseObserver) {
        try {
            MeterReadingResponseDTO reading = meterService.getLatestReading(UUID.fromString(request.getMeterId()));
            GetMeterReadingResponse response = GetMeterReadingResponse.newBuilder()
                .setCurrentReading(reading.getCurrentReading())
                .setPreviousReading(reading.getPreviousReading())
                .setUnitsConsumed(reading.getUnitsConsumed())
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            logger.error("gRPC getMeterReading failed", ex);
            responseObserver.onError(Status.NOT_FOUND.withDescription(ex.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void recordMeterReading(RecordMeterReadingRequest request,
                                   StreamObserver<RecordMeterReadingResponse> responseObserver) {
        try {
            MeterReadingRequestDTO readingRequest = new MeterReadingRequestDTO(
                request.getReadingValue(),
                null,
                null
            );
            MeterReadingResponseDTO reading = meterService.recordMeterReading(
                UUID.fromString(request.getMeterId()),
                readingRequest
            );
            RecordMeterReadingResponse response = RecordMeterReadingResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Reading recorded with ID: " + reading.getId())
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            logger.error("gRPC recordMeterReading failed", ex);
            RecordMeterReadingResponse response = RecordMeterReadingResponse.newBuilder()
                .setSuccess(false)
                .setMessage(ex.getMessage())
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
