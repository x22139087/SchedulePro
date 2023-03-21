import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import schedulePro.reminder.*;
import schedulePro.server.ReminderServer;
import schedulePro.services.ReminderServiceImpl;
import schedulePro.utils.InMemoryDatabase;
import schedulePro.utils.UserContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReminderServiceImplTest {
    private static final Logger logger = Logger.getLogger(ReminderServiceImplTest.class.getName());
    private InMemoryDatabase database;
    private ReminderServiceImpl service;
    private ReminderServiceGrpc.ReminderServiceStub stub;
    private ReminderServer server;
    private CountDownLatch serverLatch;

    @Before
    public void setup() throws InterruptedException {
        database = new InMemoryDatabase();
        service = new ReminderServiceImpl(database);
        server = new ReminderServer(8080, service);
        serverLatch = new CountDownLatch(1);

        // Start the server on a separate thread
        new Thread(() -> {
            try {
                server.start();
                serverLatch.countDown(); // Signal that the server has started
                server.blockUntilShutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // Wait until the server has started before creating the stub
        serverLatch.await();
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
        stub = ReminderServiceGrpc.newStub(channel);
    }

    @After
    public void teardown() throws InterruptedException {
        // Shutdown the server and wait until it has fully terminated
        server.stop();
        server.blockUntilShutdown();
    }

    @Test
    public void testScheduleReminder() throws InterruptedException {
        // Set up
        String userId = "user1";
        UserContext.setLoggedInUserId(userId);
        Reminder reminder = Reminder.newBuilder()
                .setTitle("Test Reminder")
                .setDescription("Test Description")
                .setTime(System.currentTimeMillis() + 10000)
                .build();
        ScheduleReminderRequest request = ScheduleReminderRequest.newBuilder().setReminder(reminder).build();
        StreamObserver<ScheduleReminderResponse> responseObserver = new StreamObserver<ScheduleReminderResponse>() {
            private ScheduleReminderResponse response;

            @Override
            public void onNext(ScheduleReminderResponse value) {
                this.response = value;
            }

            @Override
            public void onError(Throwable t) {}

            @Override
            public void onCompleted() {}
        };

        // Execute
        service.scheduleReminder(request, responseObserver);

        // Verify
        Reminder storedReminder = database.getReminder(userId, reminder.getId());
        assertNotNull(storedReminder);
        assertEquals(reminder.getTitle(), storedReminder.getTitle());
        assertEquals(reminder.getDescription(), storedReminder.getDescription());
        assertEquals(reminder.getTime(), storedReminder.getTime());
    }

    @Test
    public void testListReminders() throws InterruptedException {
        // Set up
        String userId = "user1";
        UserContext.setLoggedInUserId(userId);
        Reminder reminder1 = Reminder.newBuilder()
                .setTitle("Test Reminder 1")
                .setDescription("Test Description 1")
                .setTime(System.currentTimeMillis() + 10000)
                .build();
        Reminder reminder2 = Reminder.newBuilder()
                .setTitle("Test Reminder 2")
                .setDescription("Test Description 2")
                .setTime(System.currentTimeMillis() + 20000)
                .build();
        database.createReminder(userId, reminder1);
        database.createReminder(userId, reminder2);

        serverLatch = new CountDownLatch(1);

        StreamObserver<Reminder> responseObserver = new StreamObserver<Reminder>() {
            private List<Reminder> reminders = new ArrayList<>();

            @Override
            public void onNext(Reminder value) {
                reminders.add(value);
            }

            @Override
            public void onError(Throwable t) {}

            @Override
            public void onCompleted() {
                assertEquals(2, reminders.size());
                assertEquals(reminder1.getTitle(), reminders.get(0).getTitle());
                assertEquals(reminder2.getTitle(), reminders.get(1).getTitle());
                serverLatch.countDown();
            }
        };

        ListRemindersRequest request = ListRemindersRequest.newBuilder().setUserId(userId).build();
        stub.listReminders(request, responseObserver);

        serverLatch.await();
    }



}
