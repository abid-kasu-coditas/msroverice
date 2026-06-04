package com.eps.grpc.geography;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.1)",
    comments = "Source: geography.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GeographyServiceGrpc {

  private GeographyServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "eps.geography.GeographyService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest,
      com.eps.grpc.geography.AreaResponse> getGetAreaByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAreaById",
      requestType = com.eps.grpc.common.IdRequest.class,
      responseType = com.eps.grpc.geography.AreaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest,
      com.eps.grpc.geography.AreaResponse> getGetAreaByIdMethod() {
    io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest, com.eps.grpc.geography.AreaResponse> getGetAreaByIdMethod;
    if ((getGetAreaByIdMethod = GeographyServiceGrpc.getGetAreaByIdMethod) == null) {
      synchronized (GeographyServiceGrpc.class) {
        if ((getGetAreaByIdMethod = GeographyServiceGrpc.getGetAreaByIdMethod) == null) {
          GeographyServiceGrpc.getGetAreaByIdMethod = getGetAreaByIdMethod =
              io.grpc.MethodDescriptor.<com.eps.grpc.common.IdRequest, com.eps.grpc.geography.AreaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAreaById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.common.IdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.geography.AreaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GeographyServiceMethodDescriptorSupplier("GetAreaById"))
              .build();
        }
      }
    }
    return getGetAreaByIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest,
      com.eps.grpc.geography.CityResponse> getGetCityByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetCityById",
      requestType = com.eps.grpc.common.IdRequest.class,
      responseType = com.eps.grpc.geography.CityResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest,
      com.eps.grpc.geography.CityResponse> getGetCityByIdMethod() {
    io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest, com.eps.grpc.geography.CityResponse> getGetCityByIdMethod;
    if ((getGetCityByIdMethod = GeographyServiceGrpc.getGetCityByIdMethod) == null) {
      synchronized (GeographyServiceGrpc.class) {
        if ((getGetCityByIdMethod = GeographyServiceGrpc.getGetCityByIdMethod) == null) {
          GeographyServiceGrpc.getGetCityByIdMethod = getGetCityByIdMethod =
              io.grpc.MethodDescriptor.<com.eps.grpc.common.IdRequest, com.eps.grpc.geography.CityResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetCityById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.common.IdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.geography.CityResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GeographyServiceMethodDescriptorSupplier("GetCityById"))
              .build();
        }
      }
    }
    return getGetCityByIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest,
      com.eps.grpc.common.ValidateResponse> getValidateAreaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateArea",
      requestType = com.eps.grpc.common.IdRequest.class,
      responseType = com.eps.grpc.common.ValidateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest,
      com.eps.grpc.common.ValidateResponse> getValidateAreaMethod() {
    io.grpc.MethodDescriptor<com.eps.grpc.common.IdRequest, com.eps.grpc.common.ValidateResponse> getValidateAreaMethod;
    if ((getValidateAreaMethod = GeographyServiceGrpc.getValidateAreaMethod) == null) {
      synchronized (GeographyServiceGrpc.class) {
        if ((getValidateAreaMethod = GeographyServiceGrpc.getValidateAreaMethod) == null) {
          GeographyServiceGrpc.getValidateAreaMethod = getValidateAreaMethod =
              io.grpc.MethodDescriptor.<com.eps.grpc.common.IdRequest, com.eps.grpc.common.ValidateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateArea"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.common.IdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.eps.grpc.common.ValidateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GeographyServiceMethodDescriptorSupplier("ValidateArea"))
              .build();
        }
      }
    }
    return getValidateAreaMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GeographyServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GeographyServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GeographyServiceStub>() {
        @java.lang.Override
        public GeographyServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GeographyServiceStub(channel, callOptions);
        }
      };
    return GeographyServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GeographyServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GeographyServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GeographyServiceBlockingStub>() {
        @java.lang.Override
        public GeographyServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GeographyServiceBlockingStub(channel, callOptions);
        }
      };
    return GeographyServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GeographyServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GeographyServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GeographyServiceFutureStub>() {
        @java.lang.Override
        public GeographyServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GeographyServiceFutureStub(channel, callOptions);
        }
      };
    return GeographyServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getAreaById(com.eps.grpc.common.IdRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.geography.AreaResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAreaByIdMethod(), responseObserver);
    }

    /**
     */
    default void getCityById(com.eps.grpc.common.IdRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.geography.CityResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetCityByIdMethod(), responseObserver);
    }

    /**
     */
    default void validateArea(com.eps.grpc.common.IdRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.common.ValidateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateAreaMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service GeographyService.
   */
  public static abstract class GeographyServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return GeographyServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service GeographyService.
   */
  public static final class GeographyServiceStub
      extends io.grpc.stub.AbstractAsyncStub<GeographyServiceStub> {
    private GeographyServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GeographyServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GeographyServiceStub(channel, callOptions);
    }

    /**
     */
    public void getAreaById(com.eps.grpc.common.IdRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.geography.AreaResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAreaByIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getCityById(com.eps.grpc.common.IdRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.geography.CityResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetCityByIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void validateArea(com.eps.grpc.common.IdRequest request,
        io.grpc.stub.StreamObserver<com.eps.grpc.common.ValidateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateAreaMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service GeographyService.
   */
  public static final class GeographyServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<GeographyServiceBlockingStub> {
    private GeographyServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GeographyServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GeographyServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.eps.grpc.geography.AreaResponse getAreaById(com.eps.grpc.common.IdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAreaByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.eps.grpc.geography.CityResponse getCityById(com.eps.grpc.common.IdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetCityByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.eps.grpc.common.ValidateResponse validateArea(com.eps.grpc.common.IdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateAreaMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service GeographyService.
   */
  public static final class GeographyServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<GeographyServiceFutureStub> {
    private GeographyServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GeographyServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GeographyServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.grpc.geography.AreaResponse> getAreaById(
        com.eps.grpc.common.IdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAreaByIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.grpc.geography.CityResponse> getCityById(
        com.eps.grpc.common.IdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetCityByIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.eps.grpc.common.ValidateResponse> validateArea(
        com.eps.grpc.common.IdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateAreaMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_AREA_BY_ID = 0;
  private static final int METHODID_GET_CITY_BY_ID = 1;
  private static final int METHODID_VALIDATE_AREA = 2;

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
        case METHODID_GET_AREA_BY_ID:
          serviceImpl.getAreaById((com.eps.grpc.common.IdRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.grpc.geography.AreaResponse>) responseObserver);
          break;
        case METHODID_GET_CITY_BY_ID:
          serviceImpl.getCityById((com.eps.grpc.common.IdRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.grpc.geography.CityResponse>) responseObserver);
          break;
        case METHODID_VALIDATE_AREA:
          serviceImpl.validateArea((com.eps.grpc.common.IdRequest) request,
              (io.grpc.stub.StreamObserver<com.eps.grpc.common.ValidateResponse>) responseObserver);
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
          getGetAreaByIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.grpc.common.IdRequest,
              com.eps.grpc.geography.AreaResponse>(
                service, METHODID_GET_AREA_BY_ID)))
        .addMethod(
          getGetCityByIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.grpc.common.IdRequest,
              com.eps.grpc.geography.CityResponse>(
                service, METHODID_GET_CITY_BY_ID)))
        .addMethod(
          getValidateAreaMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.eps.grpc.common.IdRequest,
              com.eps.grpc.common.ValidateResponse>(
                service, METHODID_VALIDATE_AREA)))
        .build();
  }

  private static abstract class GeographyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GeographyServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.eps.grpc.geography.GeographyProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GeographyService");
    }
  }

  private static final class GeographyServiceFileDescriptorSupplier
      extends GeographyServiceBaseDescriptorSupplier {
    GeographyServiceFileDescriptorSupplier() {}
  }

  private static final class GeographyServiceMethodDescriptorSupplier
      extends GeographyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    GeographyServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (GeographyServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GeographyServiceFileDescriptorSupplier())
              .addMethod(getGetAreaByIdMethod())
              .addMethod(getGetCityByIdMethod())
              .addMethod(getValidateAreaMethod())
              .build();
        }
      }
    }
    return result;
  }
}
