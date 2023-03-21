package schedulePro.user;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * User authentication service
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: UserService.proto")
public final class UserServiceGrpc {

  private UserServiceGrpc() {}

  public static final String SERVICE_NAME = "user.UserService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<schedulePro.user.LoginRequest,
      schedulePro.user.LoginResponse> getLoginMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Login",
      requestType = schedulePro.user.LoginRequest.class,
      responseType = schedulePro.user.LoginResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<schedulePro.user.LoginRequest,
      schedulePro.user.LoginResponse> getLoginMethod() {
    io.grpc.MethodDescriptor<schedulePro.user.LoginRequest, schedulePro.user.LoginResponse> getLoginMethod;
    if ((getLoginMethod = UserServiceGrpc.getLoginMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getLoginMethod = UserServiceGrpc.getLoginMethod) == null) {
          UserServiceGrpc.getLoginMethod = getLoginMethod = 
              io.grpc.MethodDescriptor.<schedulePro.user.LoginRequest, schedulePro.user.LoginResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "user.UserService", "Login"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.user.LoginRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.user.LoginResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("Login"))
                  .build();
          }
        }
     }
     return getLoginMethod;
  }

  private static volatile io.grpc.MethodDescriptor<schedulePro.user.LogoutRequest,
      schedulePro.user.LogoutResponse> getLogoutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Logout",
      requestType = schedulePro.user.LogoutRequest.class,
      responseType = schedulePro.user.LogoutResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<schedulePro.user.LogoutRequest,
      schedulePro.user.LogoutResponse> getLogoutMethod() {
    io.grpc.MethodDescriptor<schedulePro.user.LogoutRequest, schedulePro.user.LogoutResponse> getLogoutMethod;
    if ((getLogoutMethod = UserServiceGrpc.getLogoutMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getLogoutMethod = UserServiceGrpc.getLogoutMethod) == null) {
          UserServiceGrpc.getLogoutMethod = getLogoutMethod = 
              io.grpc.MethodDescriptor.<schedulePro.user.LogoutRequest, schedulePro.user.LogoutResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "user.UserService", "Logout"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.user.LogoutRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.user.LogoutResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("Logout"))
                  .build();
          }
        }
     }
     return getLogoutMethod;
  }

  private static volatile io.grpc.MethodDescriptor<schedulePro.user.RegisterRequest,
      schedulePro.user.RegisterResponse> getRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Register",
      requestType = schedulePro.user.RegisterRequest.class,
      responseType = schedulePro.user.RegisterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<schedulePro.user.RegisterRequest,
      schedulePro.user.RegisterResponse> getRegisterMethod() {
    io.grpc.MethodDescriptor<schedulePro.user.RegisterRequest, schedulePro.user.RegisterResponse> getRegisterMethod;
    if ((getRegisterMethod = UserServiceGrpc.getRegisterMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getRegisterMethod = UserServiceGrpc.getRegisterMethod) == null) {
          UserServiceGrpc.getRegisterMethod = getRegisterMethod = 
              io.grpc.MethodDescriptor.<schedulePro.user.RegisterRequest, schedulePro.user.RegisterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "user.UserService", "Register"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.user.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.user.RegisterResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("Register"))
                  .build();
          }
        }
     }
     return getRegisterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<schedulePro.user.Empty,
      schedulePro.user.User> getListLoggedInUsersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListLoggedInUsers",
      requestType = schedulePro.user.Empty.class,
      responseType = schedulePro.user.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<schedulePro.user.Empty,
      schedulePro.user.User> getListLoggedInUsersMethod() {
    io.grpc.MethodDescriptor<schedulePro.user.Empty, schedulePro.user.User> getListLoggedInUsersMethod;
    if ((getListLoggedInUsersMethod = UserServiceGrpc.getListLoggedInUsersMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getListLoggedInUsersMethod = UserServiceGrpc.getListLoggedInUsersMethod) == null) {
          UserServiceGrpc.getListLoggedInUsersMethod = getListLoggedInUsersMethod = 
              io.grpc.MethodDescriptor.<schedulePro.user.Empty, schedulePro.user.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "user.UserService", "ListLoggedInUsers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.user.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.user.User.getDefaultInstance()))
                  .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("ListLoggedInUsers"))
                  .build();
          }
        }
     }
     return getListLoggedInUsersMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserServiceStub newStub(io.grpc.Channel channel) {
    return new UserServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new UserServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new UserServiceFutureStub(channel);
  }

  /**
   * <pre>
   * User authentication service
   * </pre>
   */
  public static abstract class UserServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Authenticates the user with the provided credentials.
     * </pre>
     */
    public void login(schedulePro.user.LoginRequest request,
        io.grpc.stub.StreamObserver<schedulePro.user.LoginResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getLoginMethod(), responseObserver);
    }

    /**
     * <pre>
     * Logs out the currently authenticated user.
     * </pre>
     */
    public void logout(schedulePro.user.LogoutRequest request,
        io.grpc.stub.StreamObserver<schedulePro.user.LogoutResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getLogoutMethod(), responseObserver);
    }

    /**
     * <pre>
     * Registers a new user.
     * </pre>
     */
    public void register(schedulePro.user.RegisterRequest request,
        io.grpc.stub.StreamObserver<schedulePro.user.RegisterResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterMethod(), responseObserver);
    }

    /**
     * <pre>
     * Streaming bidirectionally the list of logged in users
     * </pre>
     */
    public void listLoggedInUsers(schedulePro.user.Empty request,
        io.grpc.stub.StreamObserver<schedulePro.user.User> responseObserver) {
      asyncUnimplementedUnaryCall(getListLoggedInUsersMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getLoginMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                schedulePro.user.LoginRequest,
                schedulePro.user.LoginResponse>(
                  this, METHODID_LOGIN)))
          .addMethod(
            getLogoutMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                schedulePro.user.LogoutRequest,
                schedulePro.user.LogoutResponse>(
                  this, METHODID_LOGOUT)))
          .addMethod(
            getRegisterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                schedulePro.user.RegisterRequest,
                schedulePro.user.RegisterResponse>(
                  this, METHODID_REGISTER)))
          .addMethod(
            getListLoggedInUsersMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                schedulePro.user.Empty,
                schedulePro.user.User>(
                  this, METHODID_LIST_LOGGED_IN_USERS)))
          .build();
    }
  }

  /**
   * <pre>
   * User authentication service
   * </pre>
   */
  public static final class UserServiceStub extends io.grpc.stub.AbstractStub<UserServiceStub> {
    private UserServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UserServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UserServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Authenticates the user with the provided credentials.
     * </pre>
     */
    public void login(schedulePro.user.LoginRequest request,
        io.grpc.stub.StreamObserver<schedulePro.user.LoginResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Logs out the currently authenticated user.
     * </pre>
     */
    public void logout(schedulePro.user.LogoutRequest request,
        io.grpc.stub.StreamObserver<schedulePro.user.LogoutResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getLogoutMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Registers a new user.
     * </pre>
     */
    public void register(schedulePro.user.RegisterRequest request,
        io.grpc.stub.StreamObserver<schedulePro.user.RegisterResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Streaming bidirectionally the list of logged in users
     * </pre>
     */
    public void listLoggedInUsers(schedulePro.user.Empty request,
        io.grpc.stub.StreamObserver<schedulePro.user.User> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getListLoggedInUsersMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * User authentication service
   * </pre>
   */
  public static final class UserServiceBlockingStub extends io.grpc.stub.AbstractStub<UserServiceBlockingStub> {
    private UserServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UserServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UserServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Authenticates the user with the provided credentials.
     * </pre>
     */
    public schedulePro.user.LoginResponse login(schedulePro.user.LoginRequest request) {
      return blockingUnaryCall(
          getChannel(), getLoginMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Logs out the currently authenticated user.
     * </pre>
     */
    public schedulePro.user.LogoutResponse logout(schedulePro.user.LogoutRequest request) {
      return blockingUnaryCall(
          getChannel(), getLogoutMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Registers a new user.
     * </pre>
     */
    public schedulePro.user.RegisterResponse register(schedulePro.user.RegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Streaming bidirectionally the list of logged in users
     * </pre>
     */
    public java.util.Iterator<schedulePro.user.User> listLoggedInUsers(
        schedulePro.user.Empty request) {
      return blockingServerStreamingCall(
          getChannel(), getListLoggedInUsersMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * User authentication service
   * </pre>
   */
  public static final class UserServiceFutureStub extends io.grpc.stub.AbstractStub<UserServiceFutureStub> {
    private UserServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UserServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UserServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Authenticates the user with the provided credentials.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<schedulePro.user.LoginResponse> login(
        schedulePro.user.LoginRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Logs out the currently authenticated user.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<schedulePro.user.LogoutResponse> logout(
        schedulePro.user.LogoutRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getLogoutMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Registers a new user.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<schedulePro.user.RegisterResponse> register(
        schedulePro.user.RegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LOGIN = 0;
  private static final int METHODID_LOGOUT = 1;
  private static final int METHODID_REGISTER = 2;
  private static final int METHODID_LIST_LOGGED_IN_USERS = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final UserServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(UserServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LOGIN:
          serviceImpl.login((schedulePro.user.LoginRequest) request,
              (io.grpc.stub.StreamObserver<schedulePro.user.LoginResponse>) responseObserver);
          break;
        case METHODID_LOGOUT:
          serviceImpl.logout((schedulePro.user.LogoutRequest) request,
              (io.grpc.stub.StreamObserver<schedulePro.user.LogoutResponse>) responseObserver);
          break;
        case METHODID_REGISTER:
          serviceImpl.register((schedulePro.user.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<schedulePro.user.RegisterResponse>) responseObserver);
          break;
        case METHODID_LIST_LOGGED_IN_USERS:
          serviceImpl.listLoggedInUsers((schedulePro.user.Empty) request,
              (io.grpc.stub.StreamObserver<schedulePro.user.User>) responseObserver);
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

  private static abstract class UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UserServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return schedulePro.user.UserServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UserService");
    }
  }

  private static final class UserServiceFileDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier {
    UserServiceFileDescriptorSupplier() {}
  }

  private static final class UserServiceMethodDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    UserServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (UserServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UserServiceFileDescriptorSupplier())
              .addMethod(getLoginMethod())
              .addMethod(getLogoutMethod())
              .addMethod(getRegisterMethod())
              .addMethod(getListLoggedInUsersMethod())
              .build();
        }
      }
    }
    return result;
  }
}
