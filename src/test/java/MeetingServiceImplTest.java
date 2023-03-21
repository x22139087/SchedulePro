import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import schedulePro.meeting.*;
import schedulePro.server.MeetingServer;
import schedulePro.server.UserServer;
import schedulePro.services.MeetingServiceImpl;
import schedulePro.utils.InMemoryDatabase;
import schedulePro.utils.UserContext;

public class MeetingServiceImplTest {
    private static final Logger logger = Logger.getLogger(MeetingServiceImplTest.class.getName());
    private InMemoryDatabase database;
    private MeetingServiceImpl service;
    private MeetingServiceGrpc.MeetingServiceStub stub;
    private MeetingServer server;
    private CountDownLatch serverLatch;
    @Before
    public void setup() throws InterruptedException {
        database = new InMemoryDatabase();
        service = new MeetingServiceImpl(database);
        server = new MeetingServer(8080, service);
        serverLatch = new CountDownLatch(1);

        // Start the server on a separate thread
        new Thread(() -> {
            try {
                server.start();
                serverLatch.countDown(); // Signal that the server has started
                server.blockUntilShutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // Wait until the server has started before creating the stub
        serverLatch.await();
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
        stub = MeetingServiceGrpc.newStub(channel);
    }

    @After
    public void teardown() throws InterruptedException {
        // Shutdown the server and wait until it has fully terminated
        server.stop();
        server.blockUntilShutdown();
    }

//    @RunWith(JUnitPlatform.class)
    @Test
    public void testScheduleMeeting() throws InterruptedException {
        // Set up
        String organizerId = "user1";
        UserContext.setLoggedInUserId(organizerId);
        Meeting meeting = Meeting.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setTitle("Test Meeting")
                .setDescription("Test Description")
                .setStartTime(System.currentTimeMillis() + 10000)
                .setEndTime(System.currentTimeMillis() + 20000)
                .addParticipants("user2")
                .build();
       serverLatch = new CountDownLatch(1);
        ScheduleMeetingRequest request = ScheduleMeetingRequest.newBuilder().setMeeting(meeting).build();
        StreamObserver<ScheduleMeetingResponse> responseObserver = new StreamObserver<ScheduleMeetingResponse>() {
            private ScheduleMeetingResponse response;

            @Override
            public void onNext(ScheduleMeetingResponse value) {
                this.response = value;
                logger.info(response.getId());
                serverLatch.countDown();
            }

            @Override
            public void onError(Throwable t) {
                serverLatch.countDown();
            }

            @Override
            public void onCompleted() {
                logger.info(response.getId());
                assertNotNull(response);
                assertNotNull(response.getId());

                String id = database.getAllMeetings(organizerId).get(0).getId();
                assertEquals(id, meeting.getId());
                serverLatch.countDown();
            }
        };

        // Execute
        stub.scheduleMeeting(request, responseObserver);
        serverLatch.await();
    }

    @Test
    public void testScheduleMeetingWithoutLogin() {

        database = new InMemoryDatabase();
        service = new MeetingServiceImpl(database);
        MeetingServer server = new MeetingServer(8080, service);
        server.start();
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
        stub = MeetingServiceGrpc.newStub(channel);

        // Set up
        UserContext.setLoggedInUserId(null);
        Meeting meeting = Meeting.newBuilder()
                .setTitle("Test Meeting")
                .setDescription("Test Description")
                .setStartTime(System.currentTimeMillis() + 10000)
                .setEndTime(System.currentTimeMillis() + 20000)
                .addParticipants("user2")
                .build();
        ScheduleMeetingRequest request = ScheduleMeetingRequest.newBuilder().setMeeting(meeting).build();
        StreamObserver<ScheduleMeetingResponse> responseObserver = new StreamObserver<ScheduleMeetingResponse>() {
            @Override
            public void onNext(ScheduleMeetingResponse value) {}

            @Override
            public void onError(Throwable t) {
                assertNotNull(t);
                assertEquals(Status.PERMISSION_DENIED.getCode(), ((StatusRuntimeException) t).getStatus().getCode());
            }

            @Override
            public void onCompleted() {}
        };

        // Execute
        stub.scheduleMeeting(request, responseObserver);
    }

    @Test
    public void testListMeetings() throws InterruptedException {

        // Set up
        String userId = "user1";
        UserContext.setLoggedInUserId(userId);
        Meeting meeting1 = Meeting.newBuilder()
                .setTitle("Test Meeting 1")
                .setDescription("Test Description 1")
                .setStartTime(System.currentTimeMillis() + 10000)
                .setEndTime(System.currentTimeMillis() + 20000)
                .addParticipants("user2")
                .build();
        Meeting meeting2 = Meeting.newBuilder()
                .setTitle("Test Meeting 2")
                .setDescription("Test Description 2")
                .setStartTime(System.currentTimeMillis() + 20000)
                .setEndTime(System.currentTimeMillis() + 30000)
                .addParticipants("user2")
                .build();
        database.createMeeting(userId, meeting1);
        database.createMeeting(userId, meeting2);

        serverLatch = new CountDownLatch(1);

        StreamObserver<Meeting> responseObserver = new StreamObserver<Meeting>() {
            private List<Meeting> meetings = new ArrayList<>();

            @Override
            public void onNext(Meeting value) {
                meetings.add(value);
                serverLatch.countDown();
            }

            @Override
            public void onError(Throwable t) {  serverLatch.countDown(); }

            @Override
            public void onCompleted() {
                assertEquals(2, meetings.size());
                assertEquals(meeting1.getTitle(), meetings.get(0).getTitle());
                assertEquals(meeting2.getTitle(), meetings.get(1).getTitle());
                serverLatch.countDown();
            }
        };

        // Execute
        stub.listMeetings(Empty.newBuilder().build(), responseObserver);

        serverLatch.await();
    }

    @Test
    public void testListMeetingsWithoutLogin() {
        // Set up
        UserContext.setLoggedInUserId(null);
        StreamObserver<Meeting> responseObserver = new StreamObserver<Meeting>() {
            @Override
            public void onNext(Meeting value) {}

            @Override
            public void onError(Throwable t) {
                assertNotNull(t);
                assertEquals(Status.PERMISSION_DENIED.getCode(), ((StatusRuntimeException) t).getStatus().getCode());
            }

            @Override
            public void onCompleted() {}
        };

        // Execute
        stub.listMeetings(Empty.newBuilder().build(), responseObserver);
    }

    @Test
    public void testAddParticipant() throws InterruptedException {
        // Set up
        String organizerId = "user1";
        String participantId = "user2";
        UserContext.setLoggedInUserId(organizerId);
        Meeting meeting = Meeting.newBuilder()
                .setId("meeting1")
                .setTitle("Test Meeting")
                .setDescription("Test Description")
                .setStartTime(System.currentTimeMillis() + 10000)
                .setEndTime(System.currentTimeMillis() + 20000)
                .addParticipants(organizerId)
                .build();
        database.createMeeting(organizerId, meeting);
        String meetingId = meeting.getId();
        AddParticipantRequest request = AddParticipantRequest.newBuilder()
                .setMeetingId(meetingId)
                .setParticipantId(participantId)
                .build();
        serverLatch = new CountDownLatch(1);
        StreamObserver<SharedResponse> responseObserver = new StreamObserver<SharedResponse>() {
            private SharedResponse response;

            @Override
            public void onNext(SharedResponse value) {
                this.response = value;
                serverLatch.countDown();
            }

            @Override
            public void onError(Throwable t) {}

            @Override
            public void onCompleted() {}
        };

        // Execute
        stub.addParticipant(request, responseObserver);
        serverLatch.await();

        // Verify
        Meeting updatedMeeting = database.getMeeting(organizerId, meetingId);
        assertNotNull(updatedMeeting);
        assertTrue(updatedMeeting.getParticipantsList().contains(participantId));
    }

}
