package schedulePro.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import schedulePro.calendar.*;
import schedulePro.calendar.Event;
import schedulePro.helpers.UserContext;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;


@FunctionalInterface
interface CompletionFunc {
    void onComplete(AddEventResponse response, Throwable throwable);
}

@FunctionalInterface
interface SubscribeCompletionFunc {
    void onComplete(SubscribeToEventResponse response, Throwable throwable);
}

public class CalendarServiceClient {

    // Creating stubs for async and blocking communication with the server
    private final CalendarServiceGrpc.CalendarServiceStub asyncStub;
    private final CalendarServiceGrpc.CalendarServiceBlockingStub blockingStub;

    // Constructor for the client
    public CalendarServiceClient(String host, int port) {
        // Creating a channel to connect to the server
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        // Creating the stubs using the channel
        blockingStub = CalendarServiceGrpc.newBlockingStub(channel);
        asyncStub = CalendarServiceGrpc.newStub(channel);
    }

    // Method for scheduling an event
    private void showScheduleEventUI(AddEventRequest request, CompletionFunc func) {
        // Creating a stream observer to handle responses from the server
        StreamObserver<AddEventRequest> requestObserver = asyncStub.addEvents(new StreamObserver<AddEventResponse>() {
            // Handling successful responses
            @Override
            public void onNext(AddEventResponse addEventResponse) {
                func.onComplete(addEventResponse, null);
            }
            // Handling errors
            @Override
            public void onError(Throwable throwable) {
                func.onComplete(null, throwable);
            }
            // Handling completion of the request
            @Override
            public void onCompleted() {
                func.onComplete(null, null);
            }
        });
        // Sending the request to the server
        requestObserver.onNext(request);
        requestObserver.onCompleted();
    }

    // Method for listing all events
    private String listEvents() {
        // Creating an empty request to send to the server
        schedulePro.calendar.Empty listEventsRequest = schedulePro.calendar.Empty.newBuilder().build();
        // Retrieving an iterator for the events
        Iterator<Event> eventsIterator = blockingStub.listEvents(listEventsRequest);

        StringBuilder eventsListBuilder = new StringBuilder("Events:\n");
        // Building a string with details of each event
        while (eventsIterator.hasNext()) {
            Event event = eventsIterator.next();
            eventsListBuilder.append("ID: ").append(event.getId())
                    .append("\nTitle: ").append(event.getTitle())
                    .append("\nDescription: ").append(event.getDescription())
                    .append("\nStart Time: ").append(LocalDateTime.ofEpochSecond(event.getStartTime(), 0, ZoneOffset.UTC))
                    .append("\nEnd Time: ").append(LocalDateTime.ofEpochSecond(event.getEndTime(), 0, ZoneOffset.UTC))
                    .append("\n\n");
        }
        // Handling the case where no events were found
        if (eventsListBuilder.length() == 0) {
            eventsListBuilder.append("No events found.");
        }
        // Returning the events list as a string
        return eventsListBuilder.toString();
    }

    // Method for subscribing to an event
    private void subscribeToEvent(SubscribeToEventRequest request, SubscribeCompletionFunc func) {
        // Creating a stream observer to handle responses from the server
        StreamObserver<SubscribeToEventRequest> requestObserver = asyncStub.subscribeToEvents(new StreamObserver<SubscribeToEventResponse>() {
            // Handling successful responses
            @Override
            public void onNext(SubscribeToEventResponse response) {
                func.onComplete(response, null);
            }
            // Handling errors
            @Override
            public void onError(Throwable throwable) {
                func.onComplete(null, throwable);
            }
            // Handling completion of the request
            @Override
            public void onCompleted() {
                func.onComplete(null, null);
            }
        });
        // Sending the request to the server
        requestObserver.onNext(request);
        requestObserver.onCompleted();
    }


    // This method displays a dialog box for scheduling a new event by taking user inputs for the event's title, description, start and end time.
    void showScheduleEventUI() {
        // create a new panel with a grid layout
        JPanel panel = new JPanel(new GridLayout(0, 1));

        // create text fields for title, description, start time and end time
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField startDateTimeField = new JTextField("yyyy-MM-dd HH:mm:ss");
        JTextField endDateTimeField = new JTextField("yyyy-MM-dd HH:mm:ss");

        // add labels and fields to the panel
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Start Date/Time (yyyy-MM-dd HH:mm:ss):"));
        panel.add(startDateTimeField);
        panel.add(new JLabel("End Date/Time (yyyy-MM-dd HH:mm:ss):"));
        panel.add(endDateTimeField);

        // create a date-time formatter for parsing the input start and end time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // display the panel in a dialog box and get the result from user
        int result = JOptionPane.showConfirmDialog(null, panel, "Schedule an event", JOptionPane.OK_CANCEL_OPTION);

        // if user clicks OK, create a new event and send it to the server for scheduling
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String startDateTime = startDateTimeField.getText();
            String endDateTime = endDateTimeField.getText();

            LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
            LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);
            ZoneId zoneId = ZoneId.systemDefault();
            ZoneOffset zoneOffset = zoneId.getRules().getOffset(LocalDateTime.now());

            // create a request with event details
            AddEventRequest request = AddEventRequest.newBuilder()
                    .setTitle(title)
                    .setDescription(description)
                    .setStartTime(start.toEpochSecond(zoneOffset))
                    .setEndTime(end.toEpochSecond(zoneOffset))
                    .build();

            // send the request to the server for scheduling and display the result to the user
            showScheduleEventUI(request, (response, throwable) -> {
                if (!response.getId().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Meeting scheduled with ID: " + response.getId());
                } else {
                    JOptionPane.showMessageDialog(null, "Error scheduling the meeting", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    // This method retrieves all events from the server and displays them in a scrollable dialog box.
    void showEventsListUI() throws Exception {
        // get the logged in user's id
        String userId = UserContext.getLoggedInUserId();

        // if user is not logged in, throw an exception
        if (userId == null) {
            throw new Exception("You must be logged in to view events.");
        }

        // get a string representation of all events from the server
        String eventList = listEvents();
        // create a text area to display the events
        JTextArea eventsListArea = new JTextArea(eventList);
        eventsListArea.setEditable(false);
        eventsListArea.setLineWrap(true);
        eventsListArea.setWrapStyleWord(true);

        // create a scroll pane for the text area and display it in a dialog box
        JScrollPane scrollPane = new JScrollPane(eventsListArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(null, scrollPane, "Events List", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     Method to handle event subscription for the user.
     Prompts the user to enter an event ID to subscribe to.
     Throws an exception if the entered event ID is invalid or empty.
     Builds a new SubscribeToEventRequest object using the entered event ID.
     Calls the subscribeToEvent method and passes the request object along with a CompletionFunc to handle the response.
     */
    void showSubscribeToEventsUI() throws Exception {

        // Prompt user to enter event ID
        String eventId = JOptionPane.showInputDialog("Enter Event ID:");

        // Throw exception if the entered event ID is invalid or empty
        if (eventId == null || eventId.isEmpty()) {
            throw new Exception("Invalid event ID.");
        }

        // Build SubscribeToEventRequest object with entered event ID
        SubscribeToEventRequest request = SubscribeToEventRequest.newBuilder()
                .setEventId(eventId)
                .build();

        // Call subscribeToEvent method with request object and CompletionFunc
        subscribeToEvent(request, (response, throwable) -> {
        // No further action is taken in this code block, response handling is implemented in the CompletionFunc passed to the method.
        });
    }

}
