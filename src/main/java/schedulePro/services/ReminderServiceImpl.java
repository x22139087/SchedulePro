package schedulePro.services;


import io.grpc.stub.StreamObserver;
import schedulePro.reminder.*;
import schedulePro.utils.UserContext;
import schedulePro.utils.InMemoryDatabase;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReminderServiceImpl extends ReminderServiceGrpc.ReminderServiceImplBase {
    private static final Logger logger = Logger.getLogger(ReminderServiceImpl.class.getName());
    private final InMemoryDatabase database;

    public ReminderServiceImpl(InMemoryDatabase database) {
        this.database = database;
    }

    @Override
    public void scheduleReminder(ScheduleReminderRequest request, StreamObserver<ScheduleReminderResponse> responseObserver) {
        Reminder reminder = request.getReminder().toBuilder()
                .setId(UUID.randomUUID().toString())
                .setUserId(UserContext.getLoggedInUserId())
                .build();
        database.createReminder(reminder.getUserId(), reminder);
        responseObserver.onNext(ScheduleReminderResponse.newBuilder().setId(reminder.getId()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void listReminders(ListRemindersRequest request, StreamObserver<Reminder> responseObserver) {
        List<Reminder> reminders = database.getReminders(request.getUserId());
        for (Reminder reminder : reminders) {
            responseObserver.onNext(reminder);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void deleteReminder(DeleteRequest request, StreamObserver<Response> responseObserver) {
        String reminderId = request.getId();
        String userId = UserContext.getLoggedInUserId();
        Reminder reminder = database.getReminder(userId, reminderId);
        if (reminder == null) {
            logger.info("Reminder not found.");
            return;
        }

        database.deleteReminder(userId, reminderId);
        responseObserver.onNext(Response.newBuilder().setSuccess(true).build());
        responseObserver.onCompleted();
    }
}
