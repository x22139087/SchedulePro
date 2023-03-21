package schedulePro.services;

import io.grpc.Context;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import schedulePro.calendar.*;
import schedulePro.server.UserServer;
import schedulePro.utils.InMemoryDatabase;
import schedulePro.utils.ServerContextKeys;
import schedulePro.utils.UserContext;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class CalendarServiceImpl extends CalendarServiceGrpc.CalendarServiceImplBase {
    private final InMemoryDatabase database;
    public CalendarServiceImpl(InMemoryDatabase database) {
        this.database = database;
    }
    private static final Logger logger = Logger.getLogger(UserServer.class.getName());

    @Override
    public StreamObserver<AddEventRequest> addEvents(StreamObserver<AddEventResponse> responseObserver) {
        return new StreamObserver<AddEventRequest>() {
            @Override
            public void onNext(AddEventRequest addEventRequest) {
                String userId = UserContext.getLoggedInUserId();
                String title = addEventRequest.getTitle();
                String description = addEventRequest.getDescription();
                long startTime = addEventRequest.getStartTime();
                long endTime = addEventRequest.getEndTime();

                Event event = Event.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setTitle(title)
                        .setDescription(description)
                        .setStartTime(startTime)
                        .setEndTime(endTime)
                        .addAttendee(userId)
                        .build();

                database.addCalendarEvent(userId, event);

                AddEventResponse response = AddEventResponse.newBuilder().setId(event.getId()).build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<GetEventRequest> getEvents(StreamObserver<Event> responseObserver) {
        String userId = UserContext.getLoggedInUserId();

        return new StreamObserver<GetEventRequest>() {
            @Override
            public void onNext(GetEventRequest getEventRequest) {
                String eventId = getEventRequest.getId();
                Event event = database.getCalendarEvents(userId).stream()
                        .filter(e -> e.getId().equals(eventId))
                        .map(e -> Event.newBuilder()
                                .setId(e.getId())
                                .setTitle(e.getTitle())
                                .setDescription(e.getDescription())
                                .setStartTime(e.getStartTime())
                                .setEndTime(e.getEndTime())
                                .addAttendee(userId)
                                .build())
                        .findFirst()
                        .orElseThrow(() -> new StatusRuntimeException(Status.NOT_FOUND));
                logger.info(event.getTitle());
                logger.info(eventId);
                responseObserver.onNext(event);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void listEvents(Empty request, StreamObserver<Event> responseObserver) {
        String userId = UserContext.getLoggedInUserId();
        if (userId == null) {
            responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
            return;
        }

        for (Event event : database.getCalendarEvents(userId)) {
            Event response = Event.newBuilder()
                    .setId(event.getId())
                    .setTitle(event.getTitle())
                    .setDescription(event.getDescription())
                    .setStartTime(event.getStartTime())
                    .setEndTime(event.getEndTime())
                    .addAttendee(userId)
                    .build();
            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<SubscribeToEventRequest> subscribeToEvents(StreamObserver<SubscribeToEventResponse> responseObserver) {
        String userId = UserContext.getLoggedInUserId();
        if (userId == null) {
            responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
            return new StreamObserver<SubscribeToEventRequest>() {
                @Override
                public void onNext(SubscribeToEventRequest subscribeToEventRequest) {}

                @Override
                public void onError(Throwable throwable) {}

                @Override
                public void onCompleted() {}
            };
        }

        return new StreamObserver<SubscribeToEventRequest>() {
            @Override
            public void onNext(SubscribeToEventRequest request) {
                String eventId = request.getEventId();
                boolean success = database.addUserToEvent(eventId, userId);
                SubscribeToEventResponse response = SubscribeToEventResponse.newBuilder()
                        .setId(eventId)
                        .setSuccess(success)
                        .build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}