package schedulePro.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import schedulePro.calendar.AddEventResponse;
import schedulePro.calendar.SubscribeToEventResponse;
import schedulePro.reminder.*;
import schedulePro.utils.UserContext;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;


@FunctionalInterface
interface ReminderCompletionFunc {
    void onComplete(AddEventResponse response, Throwable throwable);
}


public class ReminderServiceClient {
    // Declare the blocking and async stubs for the ReminderService
    private final ReminderServiceGrpc.ReminderServiceBlockingStub blockingStub;
    private final ReminderServiceGrpc.ReminderServiceStub asyncStub;

    // Constructor for the ReminderServiceClient
    public ReminderServiceClient(String host, int port) {
        // Create a new ManagedChannel for the given host and port, and use plaintext communication
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        // Instantiate the blocking and async stubs using the channel
        blockingStub = ReminderServiceGrpc.newBlockingStub(channel);
        asyncStub = ReminderServiceGrpc.newStub(channel);
    }

    // Schedule a reminder with the given Reminder object and return the ScheduleReminderResponse
    public ScheduleReminderResponse scheduleReminder(Reminder reminder) {
        ScheduleReminderRequest request = ScheduleReminderRequest.newBuilder().setReminder(reminder).build();
        return blockingStub.scheduleReminder(request);
    }

    // Get a string list of all reminders for the current logged in user and return it
    public String listReminders() {
        // Create a ListRemindersRequest with the current user's ID
        ListRemindersRequest request = ListRemindersRequest.newBuilder()
                .setUserId(UserContext.getLoggedInUserId())
                .build();

        // Get an iterator of Reminder objects using the blocking stub and the request
        Iterator<Reminder> remindersIterator = blockingStub.listReminders(request);

        // Create a StringBuilder to store the list of reminders as a string
        StringBuilder remindersBuilder = new StringBuilder("Reminders:\n");

        // Loop through the iterator and append each reminder's information to the StringBuilder
        while (remindersIterator.hasNext()) {
            Reminder reminder = remindersIterator.next();

            remindersBuilder.append("ID: ").append(reminder.getId())
                    .append("\nTitle: ").append(reminder.getTitle())
                    .append("\nDescription: ").append(reminder.getDescription())
                    .append("\nTime: ").append(LocalDateTime.ofEpochSecond(reminder.getTime(), 0, ZoneOffset.UTC))
                    .append("\n\n");
        }

        return remindersBuilder.toString();
    }

    // Delete a reminder with the given ID and return a boolean indicating success or failure
    public boolean deleteReminder(String reminderId) {

        DeleteRequest request = DeleteRequest.newBuilder().setId(reminderId).build();
        Response response = blockingStub.deleteReminder(request);

        return response.getSuccess();
    }

    // Show a dialog to delete a reminder
    void deleteReminder() throws Exception {
        String reminderId = JOptionPane.showInputDialog("Enter Reminder ID:");
        if (reminderId == null || reminderId.isEmpty()) {
            throw new Exception("Invalid reminder ID.");
        }

        ReminderServiceClient reminderClient = new ReminderServiceClient("localhost", 50053);
        boolean response = reminderClient.deleteReminder(reminderId);

        if (response) {
            JOptionPane.showMessageDialog(null, "Reminder with ID: " + reminderId + " deleted.");
        } else {
            JOptionPane.showMessageDialog(null, "Error setting reminder", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    /**
     * Displays a dialog to schedule a new reminder
     */
    void showScheduleReminderDialog() {

        // Create a panel with fields to input reminder details
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dateTimeField = new JTextField("yyyy-MM-dd HH:mm:ss");
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Date/Time (yyyy-MM-dd HH:mm:ss):"));
        panel.add(dateTimeField);

        // Display the panel in a dialog and wait for the user to click "OK" or "Cancel"
        int result = JOptionPane.showConfirmDialog(null, panel, "Set Reminder", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
        // Retrieve the values entered by the user
            String title = titleField.getText();
            String description = descriptionField.getText();
            String dateTimeStr = dateTimeField.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
            // Create a new Reminder object and set its properties
            Reminder reminder = Reminder.newBuilder()
                    .setId(UserContext.getLoggedInUserId())
                    .setTitle(title)
                    .setDescription(description)
                    .setTime(dateTime.toEpochSecond(ZoneOffset.UTC))
                    .build();

            // Send the new reminder to the server and display the result to the user
            ScheduleReminderResponse response = scheduleReminder(reminder);
            if (!response.getId().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Reminder set with ID: " + response.getId());
            } else {
                JOptionPane.showMessageDialog(null, "Error setting reminder", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     Shows a dialog to display the list of reminders for the logged-in user.
     Throws an exception if the user is not logged in.
     @param component the parent component of the dialog
     @throws Exception if the user is not logged in
     */
    void showListRemindersDialog(Component component) throws Exception {

        // Get the ID of the logged-in user
        String userId = UserContext.getLoggedInUserId();

        // Throw an exception if the user is not logged in
        if (userId == null) {
            throw new Exception("You must be logged in to view reminders.");
        }

        // Get the list of reminders and display them in a scrollable text area
        String remindersList = listReminders();
        JTextArea remindersListArea = new JTextArea(remindersList);
        remindersListArea.setEditable(false);
        remindersListArea.setLineWrap(true);
        remindersListArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(remindersListArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // Show the reminders in a dialog box with the given title and message type
        JOptionPane.showMessageDialog(component, new JScrollPane(remindersListArea), "Reminders", JOptionPane.PLAIN_MESSAGE);
    }
}
