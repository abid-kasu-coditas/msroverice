package com.eps.grpc.billing;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.1)",
    comments = "Source: billing.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BillingGrpcServiceGrpc {

  private BillingGrpcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "eps.billing.BillingGrpcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.eps.grpc.billing.MeterReadingRequest,
      com.eps.grpc.billing.BillResponse> getGenerateBillMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GenerateBill",
      requestType = com.eps.grpc.billing.MeterReadingRequest.class,
      responseType = com.eps.grpc.billing.BillResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.grpc.billing.MeterReadingRequest,
      com.eps.grpc.billing.BillResponse> getGenerateBillMethod() {
    io.grpc.MethodDescriptor<com.eps.grpc.billing.MeterReadingRequest, com.eps.grpc.billing.BillResponse> getGenerateBillMethod;
    if ((getGenerateBillMethod = BillingGrpcServiceGrpc.getGenerateBillMethod) == null) {
      synchronized (BillingGrpcServiceGrpc.class) {
        if ((getGenerateBillMethod = BillingGrpcServiceGrpc.getGenerateBillMethod) == null) {
          BillingGrpcServiceGrpc.getGenerateBillMethod = getGenerateBillMethod =
              io.grpc.MethodDescriptor.<com.eps.grpc.billing.MeterReadingRequest, com.eps.grpc.billing.BillResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GenerateBill"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.billing.MeterReadingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.billing.BillResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BillingGrpcServiceMethodDescriptorSupplier("GenerateBill"))
              .build();
        }
      }
    }
    return getGenerateBillMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.eps.grpc.billing.BillStatusRequest,
      com.eps.grpc.billing.BillStatusResponse> getGetBillStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBillStatus",
      requestType = com.eps.grpc.billing.BillStatusRequest.class,
      responseType = com.eps.grpc.billing.BillStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.grpc.billing.BillStatusRequest,
      com.eps.grpc.billing.BillStatusResponse> getGetBillStatusMethod() {
    io.grpc.MethodDescriptor<com.eps.grpc.billing.BillStatusRequest, com.eps.grpc.billing.BillStatusResponse> getGetBillStatusMethod;
    if ((getGetBillStatusMethod = BillingGrpcServiceGrpc.getGetBillStatusMethod) == null) {
      synchronized (BillingGrpcServiceGrpc.class) {
        if ((getGetBillStatusMethod = BillingGrpcServiceGrpc.getGetBillStatusMethod) == null) {
          BillingGrpcServiceGrpc.getGetBillStatusMethod = getGetBillStatusMethod =
              io.grpc.MethodDescriptor.<com.eps.grpc.billing.BillStatusRequest, com.eps.grpc.billing.BillStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBillStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.billing.BillStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.billing.BillStatusResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BillingGrpcServiceMethodDescriptorSupplier("GetBillStatus"))
              .build();
        }
      }
    }
    return getGetBillStatusMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BillingGrpcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BillingGrpcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BillingGrpcServiceStub>() {
        @java.lang.Override
        public BillingGrpcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BillingGrpcServiceStub(channel, callOptions);
        }
      };
    return BillingGrpcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BillingGrpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BillingGrpcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BillingGrpcServiceBlockingStub>() {
        @java.lang.Override
        public BillingGrpcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BillingGrpcServiceBlockingStub(channel, callOptions);
        }
      };
    return BillingGrpcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BillingGrpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BillingGrpcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BillingGrpcServiceFutureStub>() {
        @java.lang.Override
        public BillingGrpcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BillingGrpcServiceFutureStub(channel, callOptions);
        }
      };
    return BillingGrpcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void generateBill(com.eps.grpc.billing.MeterReadingRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.billing.BillResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGenerateBillMethod(), responseObserver);
    }

    /**
     */
    default void getBillStatus(com.eps.grpc.billing.BillStatusRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.billing.BillStatusResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBillStatusMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BillingGrpcService.
   */
  public static abstract class BillingGrpcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return BillingGrpcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BillingGrpcService.
   */
  public static final class BillingGrpcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<BillingGrpcServiceStub> {
    private BillingGrpcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BillingGrpcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BillingGrpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void generateBill(com.eps.grpc.billing.MeterReadingRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.billing.BillResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGenerateBillMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBillStatus(com.eps.grpc.billing.BillStatusRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.billing.BillStatusResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBillStatusMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BillingGrpcService.
   */
  public static final class BillingGrpcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BillingGrpcServiceBlockingStub> {
    private BillingGrpcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BillingGrpcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BillingGrpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.eps.grpc.billing.BillResponse generateBill(com.eps.grpc.billing.MeterReadingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGenerateBillMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.eps.grpc.billing.BillStatusResponse getBillStatus(com.eps.grpc.billing.BillStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBillStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BillingGrpcService.
   */
  public static final class BillingGrpcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<BillingGrpcServiceFutureStub> {
    private BillingGrpcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BillingGrpcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BillingGrpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.grpc.billing.BillResponse> generateBill(
        com.eps.grpc.billing.MeterReadingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGenerateBillMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.grpc.billing.BillStatusResponse> getBillStatus(
        com.eps.grpc.billing.BillStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBillStatusMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GENERATE_BILL = 0;
  private static final int METHODID_GET_BILL_STATUS = 1;

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
        case METHODID_GENERATE_BILL:
          serviceImpl.generateBill((com.eps.grpc.billing.MeterReadingRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.grpc.billing.BillResponse>) responseObserver);
          break;
        case METHODID_GET_BILL_STATUS:
          serviceImpl.getBillStatus((com.eps.grpc.billing.BillStatusRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.grpc.billing.BillStatusResponse>) responseObserver);
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
          getGenerateBillMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.grpc.billing.MeterReadingRequest,
              com.eps.grpc.billing.BillResponse>(
                service, METHODID_GENERATE_BILL)))
        .addMethod(
          getGetBillStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.grpc.billing.BillStatusRequest,
              com.eps.grpc.billing.BillStatusResponse>(
                service, METHODID_GET_BILL_STATUS)))
        .build();
  }

  private static abstract class BillingGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BillingGrpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.eps.grpc.billing.BillingProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BillingGrpcService");
    }
  }

  private static final class BillingGrpcServiceFileDescriptorSupplier
      extends BillingGrpcServiceBaseDescriptorSupplier {
    BillingGrpcServiceFileDescriptorSupplier() {}
  }

  private static final class BillingGrpcServiceMethodDescriptorSupplier
      extends BillingGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    BillingGrpcServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (BillingGrpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BillingGrpcServiceFileDescriptorSupplier())
              .addMethod(getGenerateBillMethod())
              .addMethod(getGetBillStatusMethod())
              .build();
        }
      }
    }
    return result;
  }
}
