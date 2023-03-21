package schedulePro.reminder;

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
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: ReminderService.proto")
public final class ReminderServiceGrpc {

  private ReminderServiceGrpc() {}

  public static final String SERVICE_NAME = "schedulePro.reminder.ReminderService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<schedulePro.reminder.ScheduleReminderRequest,
      schedulePro.reminder.ScheduleReminderResponse> getScheduleReminderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ScheduleReminder",
      requestType = schedulePro.reminder.ScheduleReminderRequest.class,
      responseType = schedulePro.reminder.ScheduleReminderResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<schedulePro.reminder.ScheduleReminderRequest,
      schedulePro.reminder.ScheduleReminderResponse> getScheduleReminderMethod() {
    io.grpc.MethodDescriptor<schedulePro.reminder.ScheduleReminderRequest, schedulePro.reminder.ScheduleReminderResponse> getScheduleReminderMethod;
    if ((getScheduleReminderMethod = ReminderServiceGrpc.getScheduleReminderMethod) == null) {
      synchronized (ReminderServiceGrpc.class) {
        if ((getScheduleReminderMethod = ReminderServiceGrpc.getScheduleReminderMethod) == null) {
          ReminderServiceGrpc.getScheduleReminderMethod = getScheduleReminderMethod = 
              io.grpc.MethodDescriptor.<schedulePro.reminder.ScheduleReminderRequest, schedulePro.reminder.ScheduleReminderResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "schedulePro.reminder.ReminderService", "ScheduleReminder"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.reminder.ScheduleReminderRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.reminder.ScheduleReminderResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new ReminderServiceMethodDescriptorSupplier("ScheduleReminder"))
                  .build();
          }
        }
     }
     return getScheduleReminderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<schedulePro.reminder.ListRemindersRequest,
      schedulePro.reminder.Reminder> getListRemindersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListReminders",
      requestType = schedulePro.reminder.ListRemindersRequest.class,
      responseType = schedulePro.reminder.Reminder.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<schedulePro.reminder.ListRemindersRequest,
      schedulePro.reminder.Reminder> getListRemindersMethod() {
    io.grpc.MethodDescriptor<schedulePro.reminder.ListRemindersRequest, schedulePro.reminder.Reminder> getListRemindersMethod;
    if ((getListRemindersMethod = ReminderServiceGrpc.getListRemindersMethod) == null) {
      synchronized (ReminderServiceGrpc.class) {
        if ((getListRemindersMethod = ReminderServiceGrpc.getListRemindersMethod) == null) {
          ReminderServiceGrpc.getListRemindersMethod = getListRemindersMethod = 
              io.grpc.MethodDescriptor.<schedulePro.reminder.ListRemindersRequest, schedulePro.reminder.Reminder>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "schedulePro.reminder.ReminderService", "ListReminders"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.reminder.ListRemindersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.reminder.Reminder.getDefaultInstance()))
                  .setSchemaDescriptor(new ReminderServiceMethodDescriptorSupplier("ListReminders"))
                  .build();
          }
        }
     }
     return getListRemindersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<schedulePro.reminder.DeleteRequest,
      schedulePro.reminder.Response> getDeleteReminderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteReminder",
      requestType = schedulePro.reminder.DeleteRequest.class,
      responseType = schedulePro.reminder.Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<schedulePro.reminder.DeleteRequest,
      schedulePro.reminder.Response> getDeleteReminderMethod() {
    io.grpc.MethodDescriptor<schedulePro.reminder.DeleteRequest, schedulePro.reminder.Response> getDeleteReminderMethod;
    if ((getDeleteReminderMethod = ReminderServiceGrpc.getDeleteReminderMethod) == null) {
      synchronized (ReminderServiceGrpc.class) {
        if ((getDeleteReminderMethod = ReminderServiceGrpc.getDeleteReminderMethod) == null) {
          ReminderServiceGrpc.getDeleteReminderMethod = getDeleteReminderMethod = 
              io.grpc.MethodDescriptor.<schedulePro.reminder.DeleteRequest, schedulePro.reminder.Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "schedulePro.reminder.ReminderService", "DeleteReminder"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.reminder.DeleteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.reminder.Response.getDefaultInstance()))
                  .setSchemaDescriptor(new ReminderServiceMethodDescriptorSupplier("DeleteReminder"))
                  .build();
          }
        }
     }
     return getDeleteReminderMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ReminderServiceStub newStub(io.grpc.Channel channel) {
    return new ReminderServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ReminderServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ReminderServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ReminderServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ReminderServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ReminderServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Schedules a new reminder.
     * </pre>
     */
    public void scheduleReminder(schedulePro.reminder.ScheduleReminderRequest request,
        io.grpc.stub.StreamObserver<schedulePro.reminder.ScheduleReminderResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getScheduleReminderMethod(), responseObserver);
    }

    /**
     * <pre>
     * Retrieves all reminders for a user.
     * </pre>
     */
    public void listReminders(schedulePro.reminder.ListRemindersRequest request,
        io.grpc.stub.StreamObserver<schedulePro.reminder.Reminder> responseObserver) {
      asyncUnimplementedUnaryCall(getListRemindersMethod(), responseObserver);
    }

    /**
     * <pre>
     * Deletes a reminder.
     * </pre>
     */
    public void deleteReminder(schedulePro.reminder.DeleteRequest request,
        io.grpc.stub.StreamObserver<schedulePro.reminder.Response> responseObserver) {
      asyncUnimplementedUnaryCall(getDeleteReminderMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getScheduleReminderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                schedulePro.reminder.ScheduleReminderRequest,
                schedulePro.reminder.ScheduleReminderResponse>(
                  this, METHODID_SCHEDULE_REMINDER)))
          .addMethod(
            getListRemindersMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                schedulePro.reminder.ListRemindersRequest,
                schedulePro.reminder.Reminder>(
                  this, METHODID_LIST_REMINDERS)))
          .addMethod(
            getDeleteReminderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                schedulePro.reminder.DeleteRequest,
                schedulePro.reminder.Response>(
                  this, METHODID_DELETE_REMINDER)))
          .build();
    }
  }

  /**
   */
  public static final class ReminderServiceStub extends io.grpc.stub.AbstractStub<ReminderServiceStub> {
    private ReminderServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ReminderServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReminderServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ReminderServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Schedules a new reminder.
     * </pre>
     */
    public void scheduleReminder(schedulePro.reminder.ScheduleReminderRequest request,
        io.grpc.stub.StreamObserver<schedulePro.reminder.ScheduleReminderResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getScheduleReminderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Retrieves all reminders for a user.
     * </pre>
     */
    public void listReminders(schedulePro.reminder.ListRemindersRequest request,
        io.grpc.stub.StreamObserver<schedulePro.reminder.Reminder> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getListRemindersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Deletes a reminder.
     * </pre>
     */
    public void deleteReminder(schedulePro.reminder.DeleteRequest request,
        io.grpc.stub.StreamObserver<schedulePro.reminder.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeleteReminderMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ReminderServiceBlockingStub extends io.grpc.stub.AbstractStub<ReminderServiceBlockingStub> {
    private ReminderServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ReminderServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReminderServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ReminderServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Schedules a new reminder.
     * </pre>
     */
    public schedulePro.reminder.ScheduleReminderResponse scheduleReminder(schedulePro.reminder.ScheduleReminderRequest request) {
      return blockingUnaryCall(
          getChannel(), getScheduleReminderMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Retrieves all reminders for a user.
     * </pre>
     */
    public java.util.Iterator<schedulePro.reminder.Reminder> listReminders(
        schedulePro.reminder.ListRemindersRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getListRemindersMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Deletes a reminder.
     * </pre>
     */
    public schedulePro.reminder.Response deleteReminder(schedulePro.reminder.DeleteRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeleteReminderMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ReminderServiceFutureStub extends io.grpc.stub.AbstractStub<ReminderServiceFutureStub> {
    private ReminderServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ReminderServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReminderServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ReminderServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Schedules a new reminder.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<schedulePro.reminder.ScheduleReminderResponse> scheduleReminder(
        schedulePro.reminder.ScheduleReminderRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getScheduleReminderMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Deletes a reminder.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<schedulePro.reminder.Response> deleteReminder(
        schedulePro.reminder.DeleteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeleteReminderMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SCHEDULE_REMINDER = 0;
  private static final int METHODID_LIST_REMINDERS = 1;
  private static final int METHODID_DELETE_REMINDER = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ReminderServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ReminderServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SCHEDULE_REMINDER:
          serviceImpl.scheduleReminder((schedulePro.reminder.ScheduleReminderRequest) request,
              (io.grpc.stub.StreamObserver<schedulePro.reminder.ScheduleReminderResponse>) responseObserver);
          break;
        case METHODID_LIST_REMINDERS:
          serviceImpl.listReminders((schedulePro.reminder.ListRemindersRequest) request,
              (io.grpc.stub.StreamObserver<schedulePro.reminder.Reminder>) responseObserver);
          break;
        case METHODID_DELETE_REMINDER:
          serviceImpl.deleteReminder((schedulePro.reminder.DeleteRequest) request,
              (io.grpc.stub.StreamObserver<schedulePro.reminder.Response>) responseObserver);
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

  private static abstract class ReminderServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ReminderServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return schedulePro.reminder.ReminderServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ReminderService");
    }
  }

  private static final class ReminderServiceFileDescriptorSupplier
      extends ReminderServiceBaseDescriptorSupplier {
    ReminderServiceFileDescriptorSupplier() {}
  }

  private static final class ReminderServiceMethodDescriptorSupplier
      extends ReminderServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ReminderServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (ReminderServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ReminderServiceFileDescriptorSupplier())
              .addMethod(getScheduleReminderMethod())
              .addMethod(getListRemindersMethod())
              .addMethod(getDeleteReminderMethod())
              .build();
        }
      }
    }
    return result;
  }
}
