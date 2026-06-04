package com.eps.grpc.tenantuser;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.1)",
    comments = "Source: tenant_user.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TenantUserServiceGrpc {

  private TenantUserServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "eps.tenantuser.TenantUserService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest,
      com.eps.grpc.tenantuser.TechnicianResponse> getGetTechnicianByAreaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTechnicianByArea",
      requestType = com.eps.grpc.common.IdRequest.class,
      responseType = com.eps.grpc.tenantuser.TechnicianResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest,
      com.eps.grpc.tenantuser.TechnicianResponse> getGetTechnicianByAreaMethod() {
    io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest, com.eps.grpc.tenantuser.TechnicianResponse> getGetTechnicianByAreaMethod;
    if ((getGetTechnicianByAreaMethod = TenantUserServiceGrpc.getGetTechnicianByAreaMethod) == null) {
      synchronized (TenantUserServiceGrpc.class) {
        if ((getGetTechnicianByAreaMethod = TenantUserServiceGrpc.getGetTechnicianByAreaMethod) == null) {
          TenantUserServiceGrpc.getGetTechnicianByAreaMethod = getGetTechnicianByAreaMethod =
              io.grpc.MethodDescriptor.<com.eps.grpc.common.IdRequest, com.eps.grpc.tenantuser.TechnicianResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTechnicianByArea"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.common.IdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.tenantuser.TechnicianResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TenantUserServiceMethodDescriptorSupplier("GetTechnicianByArea"))
              .build();
        }
      }
    }
    return getGetTechnicianByAreaMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest,
      com.eps.grpc.tenantuser.BPOEmployeeResponse> getGetBPOEmployeeByCityMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBPOEmployeeByCity",
      requestType = com.eps.grpc.common.IdRequest.class,
      responseType = com.eps.grpc.tenantuser.BPOEmployeeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest,
      com.eps.grpc.tenantuser.BPOEmployeeResponse> getGetBPOEmployeeByCityMethod() {
    io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest, com.eps.grpc.tenantuser.BPOEmployeeResponse> getGetBPOEmployeeByCityMethod;
    if ((getGetBPOEmployeeByCityMethod = TenantUserServiceGrpc.getGetBPOEmployeeByCityMethod) == null) {
      synchronized (TenantUserServiceGrpc.class) {
        if ((getGetBPOEmployeeByCityMethod = TenantUserServiceGrpc.getGetBPOEmployeeByCityMethod) == null) {
          TenantUserServiceGrpc.getGetBPOEmployeeByCityMethod = getGetBPOEmployeeByCityMethod =
              io.grpc.MethodDescriptor.<com.eps.grpc.common.IdRequest, com.eps.grpc.tenantuser.BPOEmployeeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBPOEmployeeByCity"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.common.IdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.tenantuser.BPOEmployeeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TenantUserServiceMethodDescriptorSupplier("GetBPOEmployeeByCity"))
              .build();
        }
      }
    }
    return getGetBPOEmployeeByCityMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.eps.grpc.tenantuser.ManagerRequest,
      com.eps.grpc.tenantuser.ManagerResponse> getGetManagerByLevelMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetManagerByLevel",
      requestType = com.eps.grpc.tenantuser.ManagerRequest.class,
      responseType = com.eps.grpc.tenantuser.ManagerResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.grpc.tenantuser.ManagerRequest,
      com.eps.grpc.tenantuser.ManagerResponse> getGetManagerByLevelMethod() {
    io.grpc.MethodDescriptor<com.eps.grpc.tenantuser.ManagerRequest, com.eps.grpc.tenantuser.ManagerResponse> getGetManagerByLevelMethod;
    if ((getGetManagerByLevelMethod = TenantUserServiceGrpc.getGetManagerByLevelMethod) == null) {
      synchronized (TenantUserServiceGrpc.class) {
        if ((getGetManagerByLevelMethod = TenantUserServiceGrpc.getGetManagerByLevelMethod) == null) {
          TenantUserServiceGrpc.getGetManagerByLevelMethod = getGetManagerByLevelMethod =
              io.grpc.MethodDescriptor.<com.eps.grpc.tenantuser.ManagerRequest, com.eps.grpc.tenantuser.ManagerResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetManagerByLevel"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.tenantuser.ManagerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.tenantuser.ManagerResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TenantUserServiceMethodDescriptorSupplier("GetManagerByLevel"))
              .build();
        }
      }
    }
    return getGetManagerByLevelMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TenantUserServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TenantUserServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TenantUserServiceStub>() {
        @java.lang.Override
        public TenantUserServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TenantUserServiceStub(channel, callOptions);
        }
      };
    return TenantUserServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TenantUserServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TenantUserServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TenantUserServiceBlockingStub>() {
        @java.lang.Override
        public TenantUserServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TenantUserServiceBlockingStub(channel, callOptions);
        }
      };
    return TenantUserServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TenantUserServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TenantUserServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TenantUserServiceFutureStub>() {
        @java.lang.Override
        public TenantUserServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TenantUserServiceFutureStub(channel, callOptions);
        }
      };
    return TenantUserServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getTechnicianByArea(com.eps.grpc.common.IdRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.tenantuser.TechnicianResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTechnicianByAreaMethod(), responseObserver);
    }

    /**
     */
    default void getBPOEmployeeByCity(com.eps.grpc.common.IdRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.tenantuser.BPOEmployeeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBPOEmployeeByCityMethod(), responseObserver);
    }

    /**
     */
    default void getManagerByLevel(com.eps.grpc.tenantuser.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.tenantuser.ManagerResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetManagerByLevelMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TenantUserService.
   */
  public static abstract class TenantUserServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TenantUserServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TenantUserService.
   */
  public static final class TenantUserServiceStub
      extends io.grpc.stub.AbstractAsyncStub<TenantUserServiceStub> {
    private TenantUserServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TenantUserServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TenantUserServiceStub(channel, callOptions);
    }

    /**
     */
    public void getTechnicianByArea(com.eps.grpc.common.IdRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.tenantuser.TechnicianResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTechnicianByAreaMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBPOEmployeeByCity(com.eps.grpc.common.IdRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.tenantuser.BPOEmployeeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBPOEmployeeByCityMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getManagerByLevel(com.eps.grpc.tenantuser.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.tenantuser.ManagerResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetManagerByLevelMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TenantUserService.
   */
  public static final class TenantUserServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TenantUserServiceBlockingStub> {
    private TenantUserServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TenantUserServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TenantUserServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.eps.grpc.tenantuser.TechnicianResponse getTechnicianByArea(com.eps.grpc.common.IdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTechnicianByAreaMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.eps.grpc.tenantuser.BPOEmployeeResponse getBPOEmployeeByCity(com.eps.grpc.common.IdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBPOEmployeeByCityMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.eps.grpc.tenantuser.ManagerResponse getManagerByLevel(com.eps.grpc.tenantuser.ManagerRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetManagerByLevelMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TenantUserService.
   */
  public static final class TenantUserServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<TenantUserServiceFutureStub> {
    private TenantUserServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TenantUserServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TenantUserServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.grpc.tenantuser.TechnicianResponse> getTechnicianByArea(
        com.eps.grpc.common.IdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTechnicianByAreaMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.grpc.tenantuser.BPOEmployeeResponse> getBPOEmployeeByCity(
        com.eps.grpc.common.IdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBPOEmployeeByCityMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.grpc.tenantuser.ManagerResponse> getManagerByLevel(
        com.eps.grpc.tenantuser.ManagerRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetManagerByLevelMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_TECHNICIAN_BY_AREA = 0;
  private static final int METHODID_GET_BPOEMPLOYEE_BY_CITY = 1;
  private static final int METHODID_GET_MANAGER_BY_LEVEL = 2;

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
        case METHODID_GET_TECHNICIAN_BY_AREA:
          serviceImpl.getTechnicianByArea((com.eps.grpc.common.IdRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.grpc.tenantuser.TechnicianResponse>) responseObserver);
          break;
        case METHODID_GET_BPOEMPLOYEE_BY_CITY:
          serviceImpl.getBPOEmployeeByCity((com.eps.grpc.common.IdRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.grpc.tenantuser.BPOEmployeeResponse>) responseObserver);
          break;
        case METHODID_GET_MANAGER_BY_LEVEL:
          serviceImpl.getManagerByLevel((com.eps.grpc.tenantuser.ManagerRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.grpc.tenantuser.ManagerResponse>) responseObserver);
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
          getGetTechnicianByAreaMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.grpc.common.IdRequest,
              com.eps.grpc.tenantuser.TechnicianResponse>(
                service, METHODID_GET_TECHNICIAN_BY_AREA)))
        .addMethod(
          getGetBPOEmployeeByCityMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.grpc.common.IdRequest,
              com.eps.grpc.tenantuser.BPOEmployeeResponse>(
                service, METHODID_GET_BPOEMPLOYEE_BY_CITY)))
        .addMethod(
          getGetManagerByLevelMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.grpc.tenantuser.ManagerRequest,
              com.eps.grpc.tenantuser.ManagerResponse>(
                service, METHODID_GET_MANAGER_BY_LEVEL)))
        .build();
  }

  private static abstract class TenantUserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TenantUserServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.eps.grpc.tenantuser.TenantUserProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TenantUserService");
    }
  }

  private static final class TenantUserServiceFileDescriptorSupplier
      extends TenantUserServiceBaseDescriptorSupplier {
    TenantUserServiceFileDescriptorSupplier() {}
  }

  private static final class TenantUserServiceMethodDescriptorSupplier
      extends TenantUserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TenantUserServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (TenantUserServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TenantUserServiceFileDescriptorSupplier())
              .addMethod(getGetTechnicianByAreaMethod())
              .addMethod(getGetBPOEmployeeByCityMethod())
              .addMethod(getGetManagerByLevelMethod())
              .build();
        }
      }
    }
    return result;
  }
}
