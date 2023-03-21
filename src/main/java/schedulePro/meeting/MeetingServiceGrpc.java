package schedulePro.meeting;

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
    comments = "Source: MeetingService.proto")
public final class MeetingServiceGrpc {

  private MeetingServiceGrpc() {}

  public static final String SERVICE_NAME = "schedulePro.meeting.MeetingService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<schedulePro.meeting.ScheduleMeetingRequest,
      schedulePro.meeting.ScheduleMeetingResponse> getScheduleMeetingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ScheduleMeeting",
      requestType = schedulePro.meeting.ScheduleMeetingRequest.class,
      responseType = schedulePro.meeting.ScheduleMeetingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<schedulePro.meeting.ScheduleMeetingRequest,
      schedulePro.meeting.ScheduleMeetingResponse> getScheduleMeetingMethod() {
    io.grpc.MethodDescriptor<schedulePro.meeting.ScheduleMeetingRequest, schedulePro.meeting.ScheduleMeetingResponse> getScheduleMeetingMethod;
    if ((getScheduleMeetingMethod = MeetingServiceGrpc.getScheduleMeetingMethod) == null) {
      synchronized (MeetingServiceGrpc.class) {
        if ((getScheduleMeetingMethod = MeetingServiceGrpc.getScheduleMeetingMethod) == null) {
          MeetingServiceGrpc.getScheduleMeetingMethod = getScheduleMeetingMethod = 
              io.grpc.MethodDescriptor.<schedulePro.meeting.ScheduleMeetingRequest, schedulePro.meeting.ScheduleMeetingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "schedulePro.meeting.MeetingService", "ScheduleMeeting"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.meeting.ScheduleMeetingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.meeting.ScheduleMeetingResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MeetingServiceMethodDescriptorSupplier("ScheduleMeeting"))
                  .build();
          }
        }
     }
     return getScheduleMeetingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<schedulePro.meeting.Empty,
      schedulePro.meeting.Meeting> getListMeetingsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListMeetings",
      requestType = schedulePro.meeting.Empty.class,
      responseType = schedulePro.meeting.Meeting.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<schedulePro.meeting.Empty,
      schedulePro.meeting.Meeting> getListMeetingsMethod() {
    io.grpc.MethodDescriptor<schedulePro.meeting.Empty, schedulePro.meeting.Meeting> getListMeetingsMethod;
    if ((getListMeetingsMethod = MeetingServiceGrpc.getListMeetingsMethod) == null) {
      synchronized (MeetingServiceGrpc.class) {
        if ((getListMeetingsMethod = MeetingServiceGrpc.getListMeetingsMethod) == null) {
          MeetingServiceGrpc.getListMeetingsMethod = getListMeetingsMethod = 
              io.grpc.MethodDescriptor.<schedulePro.meeting.Empty, schedulePro.meeting.Meeting>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "schedulePro.meeting.MeetingService", "ListMeetings"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.meeting.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.meeting.Meeting.getDefaultInstance()))
                  .setSchemaDescriptor(new MeetingServiceMethodDescriptorSupplier("ListMeetings"))
                  .build();
          }
        }
     }
     return getListMeetingsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<schedulePro.meeting.AddParticipantRequest,
      schedulePro.meeting.SharedResponse> getAddParticipantMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddParticipant",
      requestType = schedulePro.meeting.AddParticipantRequest.class,
      responseType = schedulePro.meeting.SharedResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<schedulePro.meeting.AddParticipantRequest,
      schedulePro.meeting.SharedResponse> getAddParticipantMethod() {
    io.grpc.MethodDescriptor<schedulePro.meeting.AddParticipantRequest, schedulePro.meeting.SharedResponse> getAddParticipantMethod;
    if ((getAddParticipantMethod = MeetingServiceGrpc.getAddParticipantMethod) == null) {
      synchronized (MeetingServiceGrpc.class) {
        if ((getAddParticipantMethod = MeetingServiceGrpc.getAddParticipantMethod) == null) {
          MeetingServiceGrpc.getAddParticipantMethod = getAddParticipantMethod = 
              io.grpc.MethodDescriptor.<schedulePro.meeting.AddParticipantRequest, schedulePro.meeting.SharedResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "schedulePro.meeting.MeetingService", "AddParticipant"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.meeting.AddParticipantRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.meeting.SharedResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MeetingServiceMethodDescriptorSupplier("AddParticipant"))
                  .build();
          }
        }
     }
     return getAddParticipantMethod;
  }

  private static volatile io.grpc.MethodDescriptor<schedulePro.meeting.RemoveParticipantRequest,
      schedulePro.meeting.SharedResponse> getRemoveParticipantMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RemoveParticipant",
      requestType = schedulePro.meeting.RemoveParticipantRequest.class,
      responseType = schedulePro.meeting.SharedResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<schedulePro.meeting.RemoveParticipantRequest,
      schedulePro.meeting.SharedResponse> getRemoveParticipantMethod() {
    io.grpc.MethodDescriptor<schedulePro.meeting.RemoveParticipantRequest, schedulePro.meeting.SharedResponse> getRemoveParticipantMethod;
    if ((getRemoveParticipantMethod = MeetingServiceGrpc.getRemoveParticipantMethod) == null) {
      synchronized (MeetingServiceGrpc.class) {
        if ((getRemoveParticipantMethod = MeetingServiceGrpc.getRemoveParticipantMethod) == null) {
          MeetingServiceGrpc.getRemoveParticipantMethod = getRemoveParticipantMethod = 
              io.grpc.MethodDescriptor.<schedulePro.meeting.RemoveParticipantRequest, schedulePro.meeting.SharedResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "schedulePro.meeting.MeetingService", "RemoveParticipant"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.meeting.RemoveParticipantRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.meeting.SharedResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MeetingServiceMethodDescriptorSupplier("RemoveParticipant"))
                  .build();
          }
        }
     }
     return getRemoveParticipantMethod;
  }

  private static volatile io.grpc.MethodDescriptor<schedulePro.meeting.MeetingNotification,
      schedulePro.meeting.SharedResponse> getNotifyParticipantsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "NotifyParticipants",
      requestType = schedulePro.meeting.MeetingNotification.class,
      responseType = schedulePro.meeting.SharedResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<schedulePro.meeting.MeetingNotification,
      schedulePro.meeting.SharedResponse> getNotifyParticipantsMethod() {
    io.grpc.MethodDescriptor<schedulePro.meeting.MeetingNotification, schedulePro.meeting.SharedResponse> getNotifyParticipantsMethod;
    if ((getNotifyParticipantsMethod = MeetingServiceGrpc.getNotifyParticipantsMethod) == null) {
      synchronized (MeetingServiceGrpc.class) {
        if ((getNotifyParticipantsMethod = MeetingServiceGrpc.getNotifyParticipantsMethod) == null) {
          MeetingServiceGrpc.getNotifyParticipantsMethod = getNotifyParticipantsMethod = 
              io.grpc.MethodDescriptor.<schedulePro.meeting.MeetingNotification, schedulePro.meeting.SharedResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "schedulePro.meeting.MeetingService", "NotifyParticipants"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.meeting.MeetingNotification.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  schedulePro.meeting.SharedResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MeetingServiceMethodDescriptorSupplier("NotifyParticipants"))
                  .build();
          }
        }
     }
     return getNotifyParticipantsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MeetingServiceStub newStub(io.grpc.Channel channel) {
    return new MeetingServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MeetingServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MeetingServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MeetingServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MeetingServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class MeetingServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Schedules a new meeting.
     * </pre>
     */
    public void scheduleMeeting(schedulePro.meeting.ScheduleMeetingRequest request,
        io.grpc.stub.StreamObserver<schedulePro.meeting.ScheduleMeetingResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getScheduleMeetingMethod(), responseObserver);
    }

    /**
     * <pre>
     * Retrieves all meetings for the current user.
     * </pre>
     */
    public void listMeetings(schedulePro.meeting.Empty request,
        io.grpc.stub.StreamObserver<schedulePro.meeting.Meeting> responseObserver) {
      asyncUnimplementedUnaryCall(getListMeetingsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Adds a participant to a meeting.
     * </pre>
     */
    public void addParticipant(schedulePro.meeting.AddParticipantRequest request,
        io.grpc.stub.StreamObserver<schedulePro.meeting.SharedResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAddParticipantMethod(), responseObserver);
    }

    /**
     * <pre>
     * Removes a participant from a meeting.
     * </pre>
     */
    public void removeParticipant(schedulePro.meeting.RemoveParticipantRequest request,
        io.grpc.stub.StreamObserver<schedulePro.meeting.SharedResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveParticipantMethod(), responseObserver);
    }

    /**
     * <pre>
     * Notifies all participants of a meeting that it is starting and ending.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<schedulePro.meeting.MeetingNotification> notifyParticipants(
        io.grpc.stub.StreamObserver<schedulePro.meeting.SharedResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(getNotifyParticipantsMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getScheduleMeetingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                schedulePro.meeting.ScheduleMeetingRequest,
                schedulePro.meeting.ScheduleMeetingResponse>(
                  this, METHODID_SCHEDULE_MEETING)))
          .addMethod(
            getListMeetingsMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                schedulePro.meeting.Empty,
                schedulePro.meeting.Meeting>(
                  this, METHODID_LIST_MEETINGS)))
          .addMethod(
            getAddParticipantMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                schedulePro.meeting.AddParticipantRequest,
                schedulePro.meeting.SharedResponse>(
                  this, METHODID_ADD_PARTICIPANT)))
          .addMethod(
            getRemoveParticipantMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                schedulePro.meeting.RemoveParticipantRequest,
                schedulePro.meeting.SharedResponse>(
                  this, METHODID_REMOVE_PARTICIPANT)))
          .addMethod(
            getNotifyParticipantsMethod(),
            asyncClientStreamingCall(
              new MethodHandlers<
                schedulePro.meeting.MeetingNotification,
                schedulePro.meeting.SharedResponse>(
                  this, METHODID_NOTIFY_PARTICIPANTS)))
          .build();
    }
  }

  /**
   */
  public static final class MeetingServiceStub extends io.grpc.stub.AbstractStub<MeetingServiceStub> {
    private MeetingServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MeetingServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MeetingServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MeetingServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Schedules a new meeting.
     * </pre>
     */
    public void scheduleMeeting(schedulePro.meeting.ScheduleMeetingRequest request,
        io.grpc.stub.StreamObserver<schedulePro.meeting.ScheduleMeetingResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getScheduleMeetingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Retrieves all meetings for the current user.
     * </pre>
     */
    public void listMeetings(schedulePro.meeting.Empty request,
        io.grpc.stub.StreamObserver<schedulePro.meeting.Meeting> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getListMeetingsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Adds a participant to a meeting.
     * </pre>
     */
    public void addParticipant(schedulePro.meeting.AddParticipantRequest request,
        io.grpc.stub.StreamObserver<schedulePro.meeting.SharedResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAddParticipantMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Removes a participant from a meeting.
     * </pre>
     */
    public void removeParticipant(schedulePro.meeting.RemoveParticipantRequest request,
        io.grpc.stub.StreamObserver<schedulePro.meeting.SharedResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveParticipantMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Notifies all participants of a meeting that it is starting and ending.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<schedulePro.meeting.MeetingNotification> notifyParticipants(
        io.grpc.stub.StreamObserver<schedulePro.meeting.SharedResponse> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(getNotifyParticipantsMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class MeetingServiceBlockingStub extends io.grpc.stub.AbstractStub<MeetingServiceBlockingStub> {
    private MeetingServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MeetingServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MeetingServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MeetingServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Schedules a new meeting.
     * </pre>
     */
    public schedulePro.meeting.ScheduleMeetingResponse scheduleMeeting(schedulePro.meeting.ScheduleMeetingRequest request) {
      return blockingUnaryCall(
          getChannel(), getScheduleMeetingMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Retrieves all meetings for the current user.
     * </pre>
     */
    public java.util.Iterator<schedulePro.meeting.Meeting> listMeetings(
        schedulePro.meeting.Empty request) {
      return blockingServerStreamingCall(
          getChannel(), getListMeetingsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Adds a participant to a meeting.
     * </pre>
     */
    public schedulePro.meeting.SharedResponse addParticipant(schedulePro.meeting.AddParticipantRequest request) {
      return blockingUnaryCall(
          getChannel(), getAddParticipantMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Removes a participant from a meeting.
     * </pre>
     */
    public schedulePro.meeting.SharedResponse removeParticipant(schedulePro.meeting.RemoveParticipantRequest request) {
      return blockingUnaryCall(
          getChannel(), getRemoveParticipantMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MeetingServiceFutureStub extends io.grpc.stub.AbstractStub<MeetingServiceFutureStub> {
    private MeetingServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MeetingServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MeetingServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MeetingServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Schedules a new meeting.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<schedulePro.meeting.ScheduleMeetingResponse> scheduleMeeting(
        schedulePro.meeting.ScheduleMeetingRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getScheduleMeetingMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Adds a participant to a meeting.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<schedulePro.meeting.SharedResponse> addParticipant(
        schedulePro.meeting.AddParticipantRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAddParticipantMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Removes a participant from a meeting.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<schedulePro.meeting.SharedResponse> removeParticipant(
        schedulePro.meeting.RemoveParticipantRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveParticipantMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SCHEDULE_MEETING = 0;
  private static final int METHODID_LIST_MEETINGS = 1;
  private static final int METHODID_ADD_PARTICIPANT = 2;
  private static final int METHODID_REMOVE_PARTICIPANT = 3;
  private static final int METHODID_NOTIFY_PARTICIPANTS = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MeetingServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MeetingServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SCHEDULE_MEETING:
          serviceImpl.scheduleMeeting((schedulePro.meeting.ScheduleMeetingRequest) request,
              (io.grpc.stub.StreamObserver<schedulePro.meeting.ScheduleMeetingResponse>) responseObserver);
          break;
        case METHODID_LIST_MEETINGS:
          serviceImpl.listMeetings((schedulePro.meeting.Empty) request,
              (io.grpc.stub.StreamObserver<schedulePro.meeting.Meeting>) responseObserver);
          break;
        case METHODID_ADD_PARTICIPANT:
          serviceImpl.addParticipant((schedulePro.meeting.AddParticipantRequest) request,
              (io.grpc.stub.StreamObserver<schedulePro.meeting.SharedResponse>) responseObserver);
          break;
        case METHODID_REMOVE_PARTICIPANT:
          serviceImpl.removeParticipant((schedulePro.meeting.RemoveParticipantRequest) request,
              (io.grpc.stub.StreamObserver<schedulePro.meeting.SharedResponse>) responseObserver);
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
        case METHODID_NOTIFY_PARTICIPANTS:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.notifyParticipants(
              (io.grpc.stub.StreamObserver<schedulePro.meeting.SharedResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class MeetingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MeetingServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return schedulePro.meeting.MeetingServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MeetingService");
    }
  }

  private static final class MeetingServiceFileDescriptorSupplier
      extends MeetingServiceBaseDescriptorSupplier {
    MeetingServiceFileDescriptorSupplier() {}
  }

  private static final class MeetingServiceMethodDescriptorSupplier
      extends MeetingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MeetingServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (MeetingServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MeetingServiceFileDescriptorSupplier())
              .addMethod(getScheduleMeetingMethod())
              .addMethod(getListMeetingsMethod())
              .addMethod(getAddParticipantMethod())
              .addMethod(getRemoveParticipantMethod())
              .addMethod(getNotifyParticipantsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
