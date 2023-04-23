package schedulePro.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import schedulePro.calendar.AddEventResponse;
import schedulePro.meeting.*;
import schedulePro.helpers.UserContext;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;

/**
 Functional interface for defining a callback function to be executed when a meeting is completed.
 */
@FunctionalInterface
interface MeetingCompletionFunc {
    /**
    Method to be executed upon completion of a meeting.
    @param response the response from the meeting service
    @param throwable any exceptions or errors that occurred during the meeting process
    */
    void onComplete(AddEventResponse response, Throwable throwable);
}

public class MeetingServiceClient {
    // Declare a variable to hold the blocking stub
    private final MeetingServiceGrpc.MeetingServiceBlockingStub blockingStub;

    // Constructor to create a channel and initialize the blocking stub
    public MeetingServiceClient(String host, int port) {
        // Create a channel to the specified host and port
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        // Initialize the blocking stub with the channel
        blockingStub = MeetingServiceGrpc.newBlockingStub(channel);
    }

    // Method to schedule a meeting using the blocking stub
    public ScheduleMeetingResponse showScheduleMeetingUI(Meeting meeting) {
        // Create a request object with the meeting to be scheduled
        ScheduleMeetingRequest request = ScheduleMeetingRequest.newBuilder().setMeeting(meeting).build();
        // Call the scheduleMeeting method of the blocking stub with the request object and return the response
        return blockingStub.scheduleMeeting(request);
    }

    /**
     Lists all meetings of the user currently logged in and returns a string representation of the list.
     @return a string containing a list of meetings.
     */
    public String listMeetings() {
        // Create an Empty request to send to the server
        Empty listMeetingsRequest = Empty.newBuilder().build();

        // Use the blocking stub to make a gRPC call to list the meetings and get an iterator of Meeting objects.
        Iterator<Meeting> meetingsIterator = blockingStub.listMeetings(listMeetingsRequest);

        // Use a StringBuilder to build the string representation of the meetings list.
        StringBuilder meetingsListBuilder = new StringBuilder("Meetings:\n");

        // Loop through the iterator and append the details of each meeting to the StringBuilder
        while (meetingsIterator.hasNext()) {
            Meeting meeting = meetingsIterator.next();
            meetingsListBuilder.append("ID: ").append(meeting.getId())
                    .append("\nTitle: ").append(meeting.getTitle())
                    .append("\nDescription: ").append(meeting.getDescription())
                    .append("\nStart Time: ").append(LocalDateTime.ofEpochSecond(meeting.getStartTime(), 0, ZoneOffset.UTC))
                    .append("\nEnd Time: ").append(LocalDateTime.ofEpochSecond(meeting.getEndTime(), 0, ZoneOffset.UTC))
                    .append("\nParticipants: ").append(meeting.getParticipantsList())
                    .append("\n\n");
        }

        // If the StringBuilder is still empty, no meetings were found.
        if (meetingsListBuilder.length() == 0) {
            meetingsListBuilder.append("No meetings found.");
        }

        // Return the string representation of the meetings list.
        return meetingsListBuilder.toString();
    }

    /**
     * Adds a participant to a meeting.
     *
     * @param meetingId the ID of the meeting to add the participant to
     * @param participantId the ID of the participant to add to the meeting
     * @return true if the participant was added successfully, false otherwise
     */
    public boolean addParticipant(String meetingId, String participantId) {
        // Build the request with the meeting ID and participant ID
        AddParticipantRequest request = AddParticipantRequest.newBuilder()
                .setMeetingId(meetingId)
                .setParticipantId(participantId)
                .build();

        // Call the gRPC method to add the participant to the meeting
        SharedResponse response = blockingStub.addParticipant(request);

        // Return true if the participant was added successfully, false otherwise
        return response.getSuccess();
    }

    /**
     Displays a list of all meetings for the logged-in user in a dialog box.
     If the user is not logged in, throws an exception.
     @throws Exception if the user is not logged in
     */
    void showMeetingsListUI() throws Exception {
        String userId = UserContext.getLoggedInUserId();

        if (userId == null) {
            throw new Exception("You must be logged in to view meetings.");
        }

        String meetingsList = listMeetings();
        JTextArea meetingsListArea = new JTextArea(meetingsList);
        meetingsListArea.setEditable(false);
        meetingsListArea.setLineWrap(true);
        meetingsListArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(meetingsListArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // Show the meetings list in a dialog
        JOptionPane.showMessageDialog(null, scrollPane, "Meetings List", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     Prompts the user to enter a meeting ID and participant ID, then adds the participant to the specified meeting.
     @throws Exception if the user is not logged in or the participant cannot be added to the meeting
     */
    void showAddParticipantToMeetingUI() throws Exception {
        String meetingId = JOptionPane.showInputDialog("Enter Meeting ID:");
        String participantId = JOptionPane.showInputDialog("Enter Participant ID:");

        boolean success = addParticipant(meetingId, participantId);

        if (success) {
            JOptionPane.showMessageDialog(null, "Participant added successfully");
        } else {
            throw new Exception("Failed to add participant");
        }
    }

    /**
     Displays a dialog to schedule a meeting by taking input from the user, creates a new meeting object with the
     user input, and calls the scheduleMeeting() method of the MeetingServiceGrpc to schedule the meeting
     @return void
     */
    void showScheduleMeetingUI() {

        // Create a panel with fields to get user input
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField startDateTimeField = new JTextField("yyyy-MM-dd HH:mm:ss");
        JTextField endDateTimeField = new JTextField("yyyy-MM-dd HH:mm:ss");
        JTextField locationField = new JTextField();
        JTextField participantIDsField = new JTextField();
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Start Date/Time (yyyy-MM-dd HH:mm:ss):"));
        panel.add(startDateTimeField);
        panel.add(new JLabel("End Date/Time (yyyy-MM-dd HH:mm:ss):"));
        panel.add(endDateTimeField);
        panel.add(new JLabel("Location"));
        panel.add(locationField);
        panel.add(new JLabel("Participant IDs (separated by commas)"));
        panel.add(participantIDsField);

        // Get user input and create a meeting object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int result = JOptionPane.showConfirmDialog(null, panel, "Schedule a meeting", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String startDateTime = startDateTimeField.getText();
            String endDateTime = endDateTimeField.getText();
            String participants = participantIDsField.getText();
            LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
            LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);
            ZoneId zoneId = ZoneId.systemDefault();
            ZoneOffset zoneOffset = zoneId.getRules().getOffset(LocalDateTime.now());

            String organizerId = UserContext.getLoggedInUserId();

            Meeting meeting = Meeting.newBuilder()
                    .setTitle(title)
                    .setDescription(description)
                    .setStartTime(start.toEpochSecond(zoneOffset))
                    .setEndTime(end.toEpochSecond(zoneOffset))
                    .addAllParticipants(Arrays.asList(participants.split(",")))
                    .build();

            // Call the scheduleMeeting() method of the MeetingServiceGrpc to schedule the meeting
            ScheduleMeetingResponse response = showScheduleMeetingUI(meeting);

            if (!response.getId().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Meeting scheduled with ID: " + response.getId());
            } else {
                JOptionPane.showMessageDialog(null, "Error scheduling the meeting", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
