package schedulePro.services;


import io.grpc.stub.StreamObserver;
import schedulePro.reminder.*;
import schedulePro.helpers.UserContext;
import schedulePro.db.InMemoryDatabase;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class ReminderServiceImpl extends ReminderServiceGrpc.ReminderServiceImplBase {

    // Define a private static logger object to log events
    private static final Logger logger = Logger.getLogger(ReminderServiceImpl.class.getName());

    // Define a private instance variable to hold an instance of the InMemoryDatabase class
    private final InMemoryDatabase database;

    // Constructor to initialize the instance variable
    public ReminderServiceImpl(InMemoryDatabase database) {
        this.database = database;
    }

    // Implement the scheduleReminder() method
    @Override
    public void scheduleReminder(ScheduleReminderRequest request, StreamObserver<ScheduleReminderResponse> responseObserver) {

        // Get the ID of the user from the UserContext class
        String userId = UserContext.getLoggedInUserId();

        // Get the reminder details from the request and set the ID and user ID
        Reminder reminder = request.getReminder().toBuilder()
                .setId(UUID.randomUUID().toString())
                .setUserId(userId)
                .build();

        // Create a new reminder in the database with the given details and the user ID as the owner
        database.createReminder(reminder.getUserId(), reminder);

        // Create a new response object with the ID of the new reminder and send it to the client
        responseObserver.onNext(ScheduleReminderResponse.newBuilder().setId(reminder.getId()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void listReminders(ListRemindersRequest request, StreamObserver<Reminder> responseObserver) {

        // Get the ID of the user from the request
        String userId = request.getUserId();

        // Get all the reminders for the user from the database and send each reminder to the client
        List<Reminder> reminders = database.getReminders(userId);
        for (Reminder reminder : reminders) {
            responseObserver.onNext(reminder);
        }

        // Notify the client that the operation has completed
        responseObserver.onCompleted();
    }

    // Implement the deleteReminder() method
    @Override
    public void deleteReminder(DeleteRequest request, StreamObserver<Response> responseObserver) {

        // Get the ID of the user and the reminder from the request
        String reminderId = request.getId();
        String userId = UserContext.getLoggedInUserId();

        // Get the reminder from the database
        Reminder reminder = database.getReminder(userId, reminderId);

        // If the reminder is not found, log an error and return
        if (reminder == null) {
            logger.info("Reminder not found.");
            return;
        }

        // Delete the reminder from the database and create a response object with the success status
        database.deleteReminder(userId, reminderId);
        responseObserver.onNext(Response.newBuilder().setSuccess(true).build());
        responseObserver.onCompleted();
    }
}
