package com.eps.meterservice.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.1)",
    comments = "Source: meter_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MeterServiceGrpc {

  private MeterServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "com.eps.meterservice.MeterService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.eps.meterservice.grpc.CreateMeterAccountRequest,
      com.eps.meterservice.grpc.CreateMeterAccountResponse> getCreateMeterAccountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateMeterAccount",
      requestType = com.eps.meterservice.grpc.CreateMeterAccountRequest.class,
      responseType = com.eps.meterservice.grpc.CreateMeterAccountResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.meterservice.grpc.CreateMeterAccountRequest,
      com.eps.meterservice.grpc.CreateMeterAccountResponse> getCreateMeterAccountMethod() {
    io.grpc.MethodDescriptor<com.eps.meterservice.grpc.CreateMeterAccountRequest, com.eps.meterservice.grpc.CreateMeterAccountResponse> getCreateMeterAccountMethod;
    if ((getCreateMeterAccountMethod = MeterServiceGrpc.getCreateMeterAccountMethod) == null) {
      synchronized (MeterServiceGrpc.class) {
        if ((getCreateMeterAccountMethod = MeterServiceGrpc.getCreateMeterAccountMethod) == null) {
          MeterServiceGrpc.getCreateMeterAccountMethod = getCreateMeterAccountMethod =
              io.grpc.MethodDescriptor.<com.eps.meterservice.grpc.CreateMeterAccountRequest, com.eps.meterservice.grpc.CreateMeterAccountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateMeterAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.meterservice.grpc.CreateMeterAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.meterservice.grpc.CreateMeterAccountResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MeterServiceMethodDescriptorSupplier("CreateMeterAccount"))
              .build();
        }
      }
    }
    return getCreateMeterAccountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.eps.meterservice.grpc.GetMeterReadingRequest,
      com.eps.meterservice.grpc.GetMeterReadingResponse> getGetMeterReadingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetMeterReading",
      requestType = com.eps.meterservice.grpc.GetMeterReadingRequest.class,
      responseType = com.eps.meterservice.grpc.GetMeterReadingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.meterservice.grpc.GetMeterReadingRequest,
      com.eps.meterservice.grpc.GetMeterReadingResponse> getGetMeterReadingMethod() {
    io.grpc.MethodDescriptor<com.eps.meterservice.grpc.GetMeterReadingRequest, com.eps.meterservice.grpc.GetMeterReadingResponse> getGetMeterReadingMethod;
    if ((getGetMeterReadingMethod = MeterServiceGrpc.getGetMeterReadingMethod) == null) {
      synchronized (MeterServiceGrpc.class) {
        if ((getGetMeterReadingMethod = MeterServiceGrpc.getGetMeterReadingMethod) == null) {
          MeterServiceGrpc.getGetMeterReadingMethod = getGetMeterReadingMethod =
              io.grpc.MethodDescriptor.<com.eps.meterservice.grpc.GetMeterReadingRequest, com.eps.meterservice.grpc.GetMeterReadingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMeterReading"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.meterservice.grpc.GetMeterReadingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.meterservice.grpc.GetMeterReadingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MeterServiceMethodDescriptorSupplier("GetMeterReading"))
              .build();
        }
      }
    }
    return getGetMeterReadingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.eps.meterservice.grpc.RecordMeterReadingRequest,
      com.eps.meterservice.grpc.RecordMeterReadingResponse> getRecordMeterReadingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RecordMeterReading",
      requestType = com.eps.meterservice.grpc.RecordMeterReadingRequest.class,
      responseType = com.eps.meterservice.grpc.RecordMeterReadingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.meterservice.grpc.RecordMeterReadingRequest,
      com.eps.meterservice.grpc.RecordMeterReadingResponse> getRecordMeterReadingMethod() {
    io.grpc.MethodDescriptor<com.eps.meterservice.grpc.RecordMeterReadingRequest, com.eps.meterservice.grpc.RecordMeterReadingResponse> getRecordMeterReadingMethod;
    if ((getRecordMeterReadingMethod = MeterServiceGrpc.getRecordMeterReadingMethod) == null) {
      synchronized (MeterServiceGrpc.class) {
        if ((getRecordMeterReadingMethod = MeterServiceGrpc.getRecordMeterReadingMethod) == null) {
          MeterServiceGrpc.getRecordMeterReadingMethod = getRecordMeterReadingMethod =
              io.grpc.MethodDescriptor.<com.eps.meterservice.grpc.RecordMeterReadingRequest, com.eps.meterservice.grpc.RecordMeterReadingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RecordMeterReading"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.meterservice.grpc.RecordMeterReadingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.meterservice.grpc.RecordMeterReadingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MeterServiceMethodDescriptorSupplier("RecordMeterReading"))
              .build();
        }
      }
    }
    return getRecordMeterReadingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MeterServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MeterServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MeterServiceStub>() {
        @java.lang.Override
        public MeterServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MeterServiceStub(channel, callOptions);
        }
      };
    return MeterServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MeterServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MeterServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MeterServiceBlockingStub>() {
        @java.lang.Override
        public MeterServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MeterServiceBlockingStub(channel, callOptions);
        }
      };
    return MeterServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MeterServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MeterServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MeterServiceFutureStub>() {
        @java.lang.Override
        public MeterServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MeterServiceFutureStub(channel, callOptions);
        }
      };
    return MeterServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createMeterAccount(com.eps.meterservice.grpc.CreateMeterAccountRequest request,
        io.grpc.stub.StreamObserver<com.eps.meterservice.grpc.CreateMeterAccountResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateMeterAccountMethod(), responseObserver);
    }

    /**
     */
    default void getMeterReading(com.eps.meterservice.grpc.GetMeterReadingRequest request,
        io.grpc.stub.StreamObserver<com.eps.meterservice.grpc.GetMeterReadingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMeterReadingMethod(), responseObserver);
    }

    /**
     */
    default void recordMeterReading(com.eps.meterservice.grpc.RecordMeterReadingRequest request,
        io.grpc.stub.StreamObserver<com.eps.meterservice.grpc.RecordMeterReadingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRecordMeterReadingMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service MeterService.
   */
  public static abstract class MeterServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MeterServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service MeterService.
   */
  public static final class MeterServiceStub
      extends io.grpc.stub.AbstractAsyncStub<MeterServiceStub> {
    private MeterServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MeterServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MeterServiceStub(channel, callOptions);
    }

    /**
     */
    public void createMeterAccount(com.eps.meterservice.grpc.CreateMeterAccountRequest request,
        io.grpc.stub.StreamObserver<com.eps.meterservice.grpc.CreateMeterAccountResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateMeterAccountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getMeterReading(com.eps.meterservice.grpc.GetMeterReadingRequest request,
        io.grpc.stub.StreamObserver<com.eps.meterservice.grpc.GetMeterReadingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMeterReadingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void recordMeterReading(com.eps.meterservice.grpc.RecordMeterReadingRequest request,
        io.grpc.stub.StreamObserver<com.eps.meterservice.grpc.RecordMeterReadingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRecordMeterReadingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service MeterService.
   */
  public static final class MeterServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MeterServiceBlockingStub> {
    private MeterServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MeterServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MeterServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.eps.meterservice.grpc.CreateMeterAccountResponse createMeterAccount(com.eps.meterservice.grpc.CreateMeterAccountRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateMeterAccountMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.eps.meterservice.grpc.GetMeterReadingResponse getMeterReading(com.eps.meterservice.grpc.GetMeterReadingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMeterReadingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.eps.meterservice.grpc.RecordMeterReadingResponse recordMeterReading(com.eps.meterservice.grpc.RecordMeterReadingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRecordMeterReadingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service MeterService.
   */
  public static final class MeterServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<MeterServiceFutureStub> {
    private MeterServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MeterServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MeterServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.meterservice.grpc.CreateMeterAccountResponse> createMeterAccount(
        com.eps.meterservice.grpc.CreateMeterAccountRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateMeterAccountMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.meterservice.grpc.GetMeterReadingResponse> getMeterReading(
        com.eps.meterservice.grpc.GetMeterReadingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMeterReadingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.meterservice.grpc.RecordMeterReadingResponse> recordMeterReading(
        com.eps.meterservice.grpc.RecordMeterReadingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRecordMeterReadingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_METER_ACCOUNT = 0;
  private static final int METHODID_GET_METER_READING = 1;
  private static final int METHODID_RECORD_METER_READING = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_METER_ACCOUNT:
          serviceImpl.createMeterAccount((com.eps.meterservice.grpc.CreateMeterAccountRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.meterservice.grpc.CreateMeterAccountResponse>) responseObserver);
          break;
        case METHODID_GET_METER_READING:
          serviceImpl.getMeterReading((com.eps.meterservice.grpc.GetMeterReadingRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.meterservice.grpc.GetMeterReadingResponse>) responseObserver);
          break;
        case METHODID_RECORD_METER_READING:
          serviceImpl.recordMeterReading((com.eps.meterservice.grpc.RecordMeterReadingRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.meterservice.grpc.RecordMeterReadingResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getCreateMeterAccountMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.meterservice.grpc.CreateMeterAccountRequest,
              com.eps.meterservice.grpc.CreateMeterAccountResponse>(
                service, METHODID_CREATE_METER_ACCOUNT)))
        .addMethod(
          getGetMeterReadingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.meterservice.grpc.GetMeterReadingRequest,
              com.eps.meterservice.grpc.GetMeterReadingResponse>(
                service, METHODID_GET_METER_READING)))
        .addMethod(
          getRecordMeterReadingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.meterservice.grpc.RecordMeterReadingRequest,
              com.eps.meterservice.grpc.RecordMeterReadingResponse>(
                service, METHODID_RECORD_METER_READING)))
        .build();
  }

  private static abstract class MeterServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MeterServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.eps.meterservice.grpc.MeterServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MeterService");
    }
  }

  private static final class MeterServiceFileDescriptorSupplier
      extends MeterServiceBaseDescriptorSupplier {
    MeterServiceFileDescriptorSupplier() {}
  }

  private static final class MeterServiceMethodDescriptorSupplier
      extends MeterServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    MeterServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MeterServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MeterServiceFileDescriptorSupplier())
              .addMethod(getCreateMeterAccountMethod())
              .addMethod(getGetMeterReadingMethod())
              .addMethod(getRecordMeterReadingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
