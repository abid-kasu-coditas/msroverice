package com.eps.grpc.meter;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.1)",
    comments = "Source: meter.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MeterGrpcServiceGrpc {

  private MeterGrpcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "eps.meter.MeterGrpcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.eps.grpc.meter.ConnectionRequest,
      com.eps.grpc.meter.ConnectionResponse> getGetConnectionDetailsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetConnectionDetails",
      requestType = com.eps.grpc.meter.ConnectionRequest.class,
      responseType = com.eps.grpc.meter.ConnectionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.grpc.meter.ConnectionRequest,
      com.eps.grpc.meter.ConnectionResponse> getGetConnectionDetailsMethod() {
    io.grpc.MethodDescriptor<com.eps.grpc.meter.ConnectionRequest, com.eps.grpc.meter.ConnectionResponse> getGetConnectionDetailsMethod;
    if ((getGetConnectionDetailsMethod = MeterGrpcServiceGrpc.getGetConnectionDetailsMethod) == null) {
      synchronized (MeterGrpcServiceGrpc.class) {
        if ((getGetConnectionDetailsMethod = MeterGrpcServiceGrpc.getGetConnectionDetailsMethod) == null) {
          MeterGrpcServiceGrpc.getGetConnectionDetailsMethod = getGetConnectionDetailsMethod =
              io.grpc.MethodDescriptor.<com.eps.grpc.meter.ConnectionRequest, com.eps.grpc.meter.ConnectionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetConnectionDetails"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.meter.ConnectionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.meter.ConnectionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MeterGrpcServiceMethodDescriptorSupplier("GetConnectionDetails"))
              .build();
        }
      }
    }
    return getGetConnectionDetailsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest,
      com.eps.grpc.meter.MeterTypeResponse> getGetMeterTypeRateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetMeterTypeRate",
      requestType = com.eps.grpc.common.IdRequest.class,
      responseType = com.eps.grpc.meter.MeterTypeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest,
      com.eps.grpc.meter.MeterTypeResponse> getGetMeterTypeRateMethod() {
    io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest, com.eps.grpc.meter.MeterTypeResponse> getGetMeterTypeRateMethod;
    if ((getGetMeterTypeRateMethod = MeterGrpcServiceGrpc.getGetMeterTypeRateMethod) == null) {
      synchronized (MeterGrpcServiceGrpc.class) {
        if ((getGetMeterTypeRateMethod = MeterGrpcServiceGrpc.getGetMeterTypeRateMethod) == null) {
          MeterGrpcServiceGrpc.getGetMeterTypeRateMethod = getGetMeterTypeRateMethod =
              io.grpc.MethodDescriptor.<com.eps.grpc.common.IdRequest, com.eps.grpc.meter.MeterTypeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMeterTypeRate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.common.IdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.meter.MeterTypeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MeterGrpcServiceMethodDescriptorSupplier("GetMeterTypeRate"))
              .build();
        }
      }
    }
    return getGetMeterTypeRateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MeterGrpcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MeterGrpcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MeterGrpcServiceStub>() {
        @java.lang.Override
        public MeterGrpcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MeterGrpcServiceStub(channel, callOptions);
        }
      };
    return MeterGrpcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MeterGrpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MeterGrpcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MeterGrpcServiceBlockingStub>() {
        @java.lang.Override
        public MeterGrpcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MeterGrpcServiceBlockingStub(channel, callOptions);
        }
      };
    return MeterGrpcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MeterGrpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MeterGrpcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MeterGrpcServiceFutureStub>() {
        @java.lang.Override
        public MeterGrpcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MeterGrpcServiceFutureStub(channel, callOptions);
        }
      };
    return MeterGrpcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getConnectionDetails(com.eps.grpc.meter.ConnectionRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.meter.ConnectionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetConnectionDetailsMethod(), responseObserver);
    }

    /**
     */
    default void getMeterTypeRate(com.eps.grpc.common.IdRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.meter.MeterTypeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMeterTypeRateMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service MeterGrpcService.
   */
  public static abstract class MeterGrpcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MeterGrpcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service MeterGrpcService.
   */
  public static final class MeterGrpcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<MeterGrpcServiceStub> {
    private MeterGrpcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MeterGrpcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MeterGrpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void getConnectionDetails(com.eps.grpc.meter.ConnectionRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.meter.ConnectionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetConnectionDetailsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getMeterTypeRate(com.eps.grpc.common.IdRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.meter.MeterTypeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMeterTypeRateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service MeterGrpcService.
   */
  public static final class MeterGrpcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MeterGrpcServiceBlockingStub> {
    private MeterGrpcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MeterGrpcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MeterGrpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.eps.grpc.meter.ConnectionResponse getConnectionDetails(com.eps.grpc.meter.ConnectionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetConnectionDetailsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.eps.grpc.meter.MeterTypeResponse getMeterTypeRate(com.eps.grpc.common.IdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMeterTypeRateMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service MeterGrpcService.
   */
  public static final class MeterGrpcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<MeterGrpcServiceFutureStub> {
    private MeterGrpcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MeterGrpcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MeterGrpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.grpc.meter.ConnectionResponse> getConnectionDetails(
        com.eps.grpc.meter.ConnectionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetConnectionDetailsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.grpc.meter.MeterTypeResponse> getMeterTypeRate(
        com.eps.grpc.common.IdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMeterTypeRateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_CONNECTION_DETAILS = 0;
  private static final int METHODID_GET_METER_TYPE_RATE = 1;

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
        case METHODID_GET_CONNECTION_DETAILS:
          serviceImpl.getConnectionDetails((com.eps.grpc.meter.ConnectionRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.grpc.meter.ConnectionResponse>) responseObserver);
          break;
        case METHODID_GET_METER_TYPE_RATE:
          serviceImpl.getMeterTypeRate((com.eps.grpc.common.IdRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.grpc.meter.MeterTypeResponse>) responseObserver);
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
          getGetConnectionDetailsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.grpc.meter.ConnectionRequest,
              com.eps.grpc.meter.ConnectionResponse>(
                service, METHODID_GET_CONNECTION_DETAILS)))
        .addMethod(
          getGetMeterTypeRateMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.grpc.common.IdRequest,
              com.eps.grpc.meter.MeterTypeResponse>(
                service, METHODID_GET_METER_TYPE_RATE)))
        .build();
  }

  private static abstract class MeterGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MeterGrpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.eps.grpc.meter.MeterProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MeterGrpcService");
    }
  }

  private static final class MeterGrpcServiceFileDescriptorSupplier
      extends MeterGrpcServiceBaseDescriptorSupplier {
    MeterGrpcServiceFileDescriptorSupplier() {}
  }

  private static final class MeterGrpcServiceMethodDescriptorSupplier
      extends MeterGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    MeterGrpcServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (MeterGrpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MeterGrpcServiceFileDescriptorSupplier())
              .addMethod(getGetConnectionDetailsMethod())
              .addMethod(getGetMeterTypeRateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
