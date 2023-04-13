package schedulePro.services;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import schedulePro.calendar.*;
import schedulePro.server.UserServer;
import schedulePro.utils.InMemoryDatabase;
import schedulePro.utils.UserContext;

import java.util.UUID;
import java.util.logging.Logger;

public class CalendarServiceImpl extends CalendarServiceGrpc.CalendarServiceImplBase {
    // Define a private instance variable to hold an instance of the InMemoryDatabase class
    private final InMemoryDatabase database;

    // Constructor to initialize the instance variable
    public CalendarServiceImpl(InMemoryDatabase database) {
        this.database = database;
    }

    // Define a private static logger object to log events
    private static final Logger logger = Logger.getLogger(UserServer.class.getName());

    // Implement the addEvents() method
    @Override
    public StreamObserver<AddEventRequest> addEvents(StreamObserver<AddEventResponse> responseObserver) {

        // Return a new StreamObserver object
        return new StreamObserver<AddEventRequest>() {

            // Implement the onNext() method
            @Override
            public void onNext(AddEventRequest addEventRequest) {

                // Get the user ID from the UserContext class
                String userId = UserContext.getLoggedInUserId();

                // Get the event details from the request
                String title = addEventRequest.getTitle();
                String description = addEventRequest.getDescription();
                long startTime = addEventRequest.getStartTime();
                long endTime = addEventRequest.getEndTime();

                // Create a new Event object using the event details and add the user ID as an attendee
                Event event = Event.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setTitle(title)
                        .setDescription(description)
                        .setStartTime(startTime)
                        .setEndTime(endTime)
                        .addAttendee(userId)
                        .build();

                // Add the new event to the database
                database.addCalendarEvent(userId, event);

                // Create a new AddEventResponse object with the ID of the new event and send it to the client
                AddEventResponse response = AddEventResponse.newBuilder().setId(event.getId()).build();
                responseObserver.onNext(response);
            }

            // Implement the onError() method
            @Override
            public void onError(Throwable throwable) {
                // Print the error stack trace and send the error to the client
                throwable.printStackTrace();
                responseObserver.onError(throwable);
            }

            // Implement the onCompleted() method
            @Override
            public void onCompleted() {
                // Notify the client that the operation has completed
                responseObserver.onCompleted();
            }
        };
    }

    // Implement the getEvents() method
    @Override
    public StreamObserver<GetEventRequest> getEvents(StreamObserver<Event> responseObserver) {

        // Get the user ID from the UserContext class
        String userId = UserContext.getLoggedInUserId();

        // Return a new StreamObserver object
        return new StreamObserver<GetEventRequest>() {

            // Implement the onNext() method
            @Override
            public void onNext(GetEventRequest getEventRequest) {

                // Get the ID of the requested event from the request
                String eventId = getEventRequest.getId();

                // Search the database for the event with the specified ID
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

                // Log the event details
                logger.info(event.getTitle());
                logger.info(eventId);

                // Send the event details to the client
                responseObserver.onNext(event);
            }

            // Implement the onError() method
            @Override
            public void onError(Throwable throwable) {
                // Send the error to the client
                responseObserver.onError(throwable);
            }

            // Implement the onCompleted() method
            @Override
            public void onCompleted() {
                // Notify the client that the operation has completed
                responseObserver.onCompleted();
            }
        };
    }

    // Implement the listEvents() method
    @Override
    public void listEvents(Empty request, StreamObserver<Event> responseObserver) {

        // Get the user ID from the UserContext class
        String userId = UserContext.getLoggedInUserId();

        // If the user is not logged in, send an error to the client and return
        if (userId == null) {
            responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
            return;
        }

        // Loop through all the events in the user's calendar and send each event to the client
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

        // Notify the client that the operation has completed
        responseObserver.onCompleted();
    }

    // Implement the subscribeToEvents() method
    @Override
    public StreamObserver<SubscribeToEventRequest> subscribeToEvents(StreamObserver<SubscribeToEventResponse> responseObserver) {

        // Get the user ID from the UserContext class
        String userId = UserContext.getLoggedInUserId();

        // If the user is not logged in, send an error to the client and return a no-op StreamObserver object
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

        // Return a new StreamObserver object
        return new StreamObserver<SubscribeToEventRequest>() {

            // Implement the onNext() method
            @Override
            public void onNext(SubscribeToEventRequest request) {

                // Get the ID of the event to subscribe to from the request
                String eventId = request.getEventId();

                // Add the user to the event in the database and create a response object with the result
                boolean success = database.addUserToEvent(eventId, userId);
                SubscribeToEventResponse response = SubscribeToEventResponse.newBuilder()
                        .setId(eventId)
                        .setSuccess(success)
                        .build();

                // Send the response to the client
                responseObserver.onNext(response);
            }

            // Implement the onError() method
            @Override
            public void onError(Throwable throwable) {
                // Send the error to the client
                responseObserver.onError(throwable);
            }

            // Implement the onCompleted() method
            @Override
            public void onCompleted() {
                // Notify the client that the operation has completed
                responseObserver.onCompleted();
            }
        };
    }
}