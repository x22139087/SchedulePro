import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.Test;
import schedulePro.calendar.*;
import schedulePro.server.CalendarServer;
import schedulePro.services.CalendarServiceImpl;
import schedulePro.db.InMemoryDatabase;
import schedulePro.helpers.UserContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class CalendarServiceImplTest {

    @Test
    public void testAddEvents() throws Exception {
        UserContext.setLoggedInUserId("user123");
        CountDownLatch latch = new CountDownLatch(1);
        InMemoryDatabase database = new InMemoryDatabase();
        CalendarServiceImpl calendarService = new CalendarServiceImpl(database);
        CalendarServer server = new CalendarServer(8081, database);
        server.start();

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081).usePlaintext().build();
        CalendarServiceGrpc.CalendarServiceStub stub = CalendarServiceGrpc.newStub(channel);

        StreamObserver<AddEventRequest> requestObserver = stub.addEvents(new StreamObserver<AddEventResponse>() {
            private String eventId;

            @Override
            public void onNext(AddEventResponse addEventResponse) {
                eventId = addEventResponse.getId();
            }

            @Override
            public void onError(Throwable throwable) {
                fail("Unexpected error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                // Verify the event was added to the database
                String userId = UserContext.getLoggedInUserId();
                Event event = database.getCalendarEvents(userId).stream().filter(event1 -> event1.getId().equals(eventId)).findFirst().orElse(null);
                assertNotNull(event);
                assertEquals(event.getTitle(), "Test Event");
                assertEquals(event.getDescription(), "Test Description");
                assertEquals(event.getStartTime(), 1648200000000L);
                assertEquals(event.getEndTime(), 1648203600000L);
                assertEquals(event.getAttendeeCount(), 1);
                assertEquals(event.getAttendee(0), userId);

                latch.countDown();
            }
        });

        requestObserver.onNext(AddEventRequest.newBuilder()
                .setTitle("Test Event")
                .setDescription("Test Description")
                .setStartTime(1648200000000L)
                .setEndTime(1648203600000L)
                .build());

        requestObserver.onCompleted();

        latch.await();
    }




    @Test
    public void testListEvents() throws Exception {

        InMemoryDatabase database = new InMemoryDatabase();
        UserContext.setLoggedInUserId("user123");
        database.addUserSession("session123", "user123");

        Event event1 = Event.newBuilder()
                .setTitle("Test Event 1")
                .setDescription("Test Description 1")
                .setStartTime(1648200000000L)
                .setEndTime(1648203600000L)
                .addAttendee("user123")
                .build();
        database.addCalendarEvent("user123", event1);

        Event event2 = Event.newBuilder()
                .setTitle("Test Event 2")
                .setDescription("Test Description 2")
                .setStartTime(1648300000000L)
                .setEndTime(1648303600000L)
                .addAttendee("user123")
                .build();
        database.addCalendarEvent("user123", event2);

        CalendarServiceImpl calendarService = new CalendarServiceImpl(database);
        CalendarServer server = new CalendarServer(8080, database);
        server.start();

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
        CalendarServiceGrpc.CalendarServiceStub stub = CalendarServiceGrpc.newStub(channel);

        List<Event> events = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(2);

        StreamObserver<Event> responseObserver = new StreamObserver<Event>() {
            @Override
            public void onNext(Event event) {
                events.add(event);
                latch.countDown();
            }

            @Override
            public void onError(Throwable throwable) {
                fail("Unexpected error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                // Do nothing
            }
        };

        stub.listEvents(Empty.newBuilder().build(), responseObserver);

        latch.await();

        assertEquals(2, events.size());

        Event receivedEvent1 = events.get(0);
        assertEquals(event1.getId(), receivedEvent1.getId());
        assertEquals(event1.getTitle(), receivedEvent1.getTitle());
        assertEquals(event1.getDescription(), receivedEvent1.getDescription());
        assertEquals(event1.getStartTime(), receivedEvent1.getStartTime());
        assertEquals(event1.getEndTime(), receivedEvent1.getEndTime());
        assertEquals(event1.getAttendeeCount(), receivedEvent1.getAttendeeCount());
        assertEquals(event1.getAttendee(0), receivedEvent1.getAttendee(0));

        Event receivedEvent2 = events.get(1);
        assertEquals(event2.getId(), receivedEvent2.getId());
        assertEquals(event2.getTitle(), receivedEvent2.getTitle());
        assertEquals(event2.getDescription(), receivedEvent2.getDescription());
        assertEquals(event2.getStartTime(), receivedEvent2.getStartTime());
        assertEquals(event2.getEndTime(), receivedEvent2.getEndTime());
        assertEquals(event2.getAttendeeCount(), receivedEvent2.getAttendeeCount());
        assertEquals(event2.getAttendee(0), receivedEvent2.getAttendee(0));

        latch.await();
    }

    @Test
    public void testSubscribeToEvents() throws Exception {
        // Set up test data and server
        InMemoryDatabase database = new InMemoryDatabase();
        UserContext.setLoggedInUserId("user123");
        database.addUserSession("session123", "user123");

        Event event = Event.newBuilder()
                .setId("event1")
                .setTitle("Test Event")
                .setDescription("Test Description")
                .setStartTime(1648200000000L)
                .setEndTime(1648203600000L)
                .build();
        database.addCalendarEvent("user123", event);

        CalendarServiceImpl calendarService = new CalendarServiceImpl(database);
        CalendarServer server = new CalendarServer(8080, database);
        server.start();

        // Set up gRPC client and stream observer
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
        CalendarServiceGrpc.CalendarServiceStub stub = CalendarServiceGrpc.newStub(channel);
        List<SubscribeToEventResponse> responses = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<SubscribeToEventRequest> requestObserver = stub.subscribeToEvents(new StreamObserver<SubscribeToEventResponse>() {
            @Override
            public void onNext(SubscribeToEventResponse response) {
                responses.add(response);
            }

            @Override
            public void onError(Throwable throwable) {
                fail("Unexpected error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });

        // Make gRPC request to subscribe to the event
        String eventId = event.getId();
        requestObserver.onNext(SubscribeToEventRequest.newBuilder().setEventId(eventId).build());
        requestObserver.onCompleted();

        // Wait for response
        latch.await();

        // Verify response
        assertEquals(1, responses.size());
        SubscribeToEventResponse response = responses.get(0);
        assertTrue(response.getSuccess());

        // Verify that user is now subscribed to the event in the database
        List<String> attendees = database.getAttendeesForEvent(eventId);
        assertEquals(1, attendees.size());
        assertEquals("user123", attendees.get(0));
    }


}


