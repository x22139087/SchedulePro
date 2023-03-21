package schedulePro.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import schedulePro.reminder.*;
import schedulePro.utils.UserContext;

public class ReminderServiceClient {
    private final ReminderServiceGrpc.ReminderServiceBlockingStub blockingStub;
    private final ReminderServiceGrpc.ReminderServiceStub asyncStub;

    public ReminderServiceClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        blockingStub = ReminderServiceGrpc.newBlockingStub(channel);
        asyncStub = ReminderServiceGrpc.newStub(channel);
    }

    public ScheduleReminderResponse scheduleReminder(Reminder reminder) {
        ScheduleReminderRequest request = ScheduleReminderRequest.newBuilder().setReminder(reminder).build();
        return blockingStub.scheduleReminder(request);
    }

    public void listReminders(StreamObserver<Reminder> observer) {
        ListRemindersRequest request = ListRemindersRequest.newBuilder()
                .setUserId(UserContext.getLoggedInUserId())
                .build();
        asyncStub.listReminders(request, observer);
    }

    public boolean deleteReminder(String reminderId) {

        DeleteRequest request = DeleteRequest.newBuilder().setId(reminderId).build();
        Response response = blockingStub.deleteReminder(request);

        return response.getSuccess();
    }
}
