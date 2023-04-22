package schedulePro.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import schedulePro.services.CalendarServiceImpl;
import schedulePro.services.MeetingServiceImpl;
import schedulePro.services.ReminderServiceImpl;
import schedulePro.services.UserServiceImpl;
import schedulePro.utils.InMemoryDatabase;
import schedulePro.utils.UserContext;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ScheduleProUI extends JFrame implements ServiceListener {

    /*
    This is a private final InMemoryDatabase object used by the ScheduleProUI class to store data.
    */
    private final InMemoryDatabase database;

    /*
    This is a map object that stores the ServiceEvent objects associated with each service type.
    It is used by the connectToService method to retrieve the correct ServiceEvent object for a given service type.
    */
    private final Map<String, ServiceEvent> serviceEvents = new HashMap<>();

    /*
    This is a JPanel object that represents the main content pane of the ScheduleProUI.
    It is used to add all other UI components to the frame.
    */
    private JPanel contentPane;
    /*

    This is a JLabel object that displays the status of the application (e.g., "Connected to calendar service").
    */
    private JLabel lblStatus;
    /*

    This is a JComboBox object that allows the user to select a service from a list of available services.
    */
    private JComboBox<String> cbServices;
    /*

    This is a JList object that displays a list of available services.
    */
    private JList<String> serviceList;
    /*

    This is a JButton object that is used to connect to the selected service.
    */
    private JButton btnConnect;
    /*

    This is a DefaultListModel object that stores the names of available services.
    It is used by the serviceList JList to display the list of available services.
    */
    private final DefaultListModel<String> listModel;
    /*

    This is a JPanel object that contains the buttons for the selected service (e.g., "Schedule Meeting", "List Meetings").
    It is updated dynamically based on the selected service.
    */
    private JPanel buttonsPanel;
    /*

    This is a JmDNS object that is used to discover and register services.
    */
    private JmDNS jmdns;
    /*

    This is a Logger object that is used for logging messages.
    */
    private static final Logger logger = Logger.getLogger(ScheduleProUI.class.getName());
    /*

    These are client objects used to communicate with the calendar, reminder, and meeting services.
    They are created by the connectToService method.
    */
    private CalendarServiceClient calendarService;
    private ReminderServiceClient reminderService;
    private MeetingServiceClient meetingService;
    /*

    This is a client object used to communicate with the user service.
    It is used to show the login dialog if the user is not logged in.
    */
    private UserServiceClient userService;


    public static void main(String[] args) {
        ScheduleProUI serviceGUI = new ScheduleProUI();

        serviceGUI.userService = new UserServiceClient("localhost", 50050);
        serviceGUI.registerServices();
        serviceGUI.setVisible(true);
    }

    /**
     * Create the frame.
     */
    public ScheduleProUI() {
        // Initialize an InMemoryDatabase and a DefaultListModel for the UI
        database = new InMemoryDatabase();
        listModel = new DefaultListModel<>();

        // Start servers
        startServers();

        try {
            // Initialize a new JmDNS instance
            jmdns = JmDNS.create();
        } catch (IOException e) {
            // Print stack trace if an IOException occurs
            e.printStackTrace();
        }

            // Set the default success flag to true and retrieve the currently logged in user's ID
        boolean success = true;
        String userId = UserContext.getLoggedInUserId();

        // Check if the user is currently logged in
        if (userId.isEmpty()) {
        // If not, show the login dialog and set the success flag to the result
            success = userService.showLoginDialog(this);
        }

        // If the user is logged in or the login is successful, continue with setting up the UI
        if (success) {
            // Set the default close operation and dimensions of the JFrame
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBounds(100, 100, 650, 600);
            // Create a new JPanel for the content pane and set its layout to BorderLayout
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            setContentPane(contentPane);
            contentPane.setLayout(new BorderLayout());

            // Create a new JLabel for the status of the service connection
            lblStatus = new JLabel();

            // Create a new JButton for connecting to a selected service
            JButton connectButton = new JButton("Connect");
            connectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get the selected service from the JList
                    String value = serviceList.getSelectedValue();
                    if (value != null && !value.isEmpty()) {
                        // Split the selected service by the '.' delimiter to retrieve its name
                        String serviceName = value.split("\\.")[0];
                        // Connect to the selected service
                        connectToService(serviceName);
                    }
                }
            });
            // Add the connect button to the top of the content pane
            contentPane.add(connectButton, BorderLayout.NORTH);

            // Create a new JPanel for the buttons and set its layout to a grid with 2 columns
            buttonsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            // Add an empty border around the buttons panel
            buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            // Add the buttons panel to the right side of the content pane
            contentPane.add(buttonsPanel, BorderLayout.EAST);

            // Create a new JList for displaying available services
            serviceList = new JList<>(listModel);
            // Set the selection mode to single selection
            serviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            // Enable the JList and set its visible row count to 3
            serviceList.setEnabled(true);
            serviceList.setVisibleRowCount(3);
            // Set the fixed cell width to 250
            serviceList.setFixedCellWidth(250);
            // Add the service list to the left side of the content pane inside a JScrollPane
            contentPane.add(new JScrollPane(serviceList), BorderLayout.WEST);

            // Discover available services and add them to the JList
            discoverServices();
        }
    }

    /*
    This method updates the buttons displayed in the buttonsPanel by removing all existing buttons and adding three new buttons.
    The first button is for scheduling a new reminder, the second button is for listing existing reminders,
    and the third button is for deleting reminders.
    The ButtonClickListener class is used to handle button clicks for all three buttons.
    After updating the buttons, the UI is reloaded by calling revalidate() and repaint() on the contentPane.
    */
    private void reminderActions(JPanel buttonsPanel) {

        // Remove all existing buttons from the buttonsPanel
        buttonsPanel.removeAll();

        // Create a new button for scheduling a reminder and add it to the buttonsPanel
        JButton scheduleReminderButton = new JButton("Schedule Reminder");
        scheduleReminderButton.setActionCommand("Schedule Reminder");
        scheduleReminderButton.addActionListener(new ButtonClickListener());
        buttonsPanel.add(scheduleReminderButton);

        // Create a new button for listing reminders and add it to the buttonsPanel
        JButton listRemindersButton = new JButton("List Reminders");
        listRemindersButton.setActionCommand("List Reminders");
        listRemindersButton.addActionListener(new ButtonClickListener());
        buttonsPanel.add(listRemindersButton);

        // Create a new button for deleting reminders and add it to the buttonsPanel
        JButton deleteRemindersButton = new JButton("Delete Reminder");
        deleteRemindersButton.setActionCommand("Delete Reminder");
        deleteRemindersButton.addActionListener(new ButtonClickListener());
        buttonsPanel.add(deleteRemindersButton);

        // Reload the UI by calling revalidate() and repaint() on the contentPane
        contentPane.revalidate();
        contentPane.repaint();
    }

    /*
        This method updates the buttons displayed in the buttonsPanel by removing all existing buttons and adding three new buttons.
        The first button is for scheduling a new meeting, the second button is for listing existing meetings,
        and the third button is for adding participants to a meeting.
        The ButtonClickListener class is used to handle button clicks for all three buttons.
        After updating the buttons, the UI is reloaded by calling revalidate() and repaint() on the contentPane.
    */
    private void meetingActions(JPanel buttonsPanel) {

        // Remove all existing buttons from the buttonsPanel
        buttonsPanel.removeAll();

        // Create a new button for scheduling a meeting and add it to the buttonsPanel
        JButton scheduleMeetingButton = new JButton("Schedule Meeting");
        scheduleMeetingButton.setActionCommand("Schedule Meeting");
        scheduleMeetingButton.addActionListener(new ButtonClickListener());
        buttonsPanel.add(scheduleMeetingButton);

        // Create a new button for listing meetings and add it to the buttonsPanel
        JButton listMeetingsButton = new JButton("List Meetings");
        listMeetingsButton.setActionCommand("List Meetings");
        listMeetingsButton.addActionListener(new ButtonClickListener());
        buttonsPanel.add(listMeetingsButton);

        // Create a new button for adding participants to a meeting and add it to the buttonsPanel
        JButton addParticipantButton = new JButton("Add Participant");
        addParticipantButton.setActionCommand("Add Participant");
        addParticipantButton.addActionListener(new ButtonClickListener());
        buttonsPanel.add(addParticipantButton);

        // Reload the UI by calling revalidate() and repaint() on the contentPane
        contentPane.revalidate();
        contentPane.repaint();
    }


    /*
        This method updates the buttons displayed in the buttonsPanel by removing all existing buttons and adding three new buttons.
        The first button is for scheduling a new event, the second button is for listing existing events, and the third button is for subscribing to events.
        The ButtonClickListener class is used to handle button clicks for all three buttons.
        After updating the buttons, the UI is reloaded by calling revalidate() and repaint() on the contentPane.
    */
    private void calendarActions(JPanel buttonsPanel) {

        // Remove all existing buttons from the buttonsPanel
        buttonsPanel.removeAll();

        // Create a new button for scheduling an event and add it to the buttonsPanel
        JButton scheduleButton = new JButton("Schedule Event");
        scheduleButton.setActionCommand("Schedule Event");
        scheduleButton.addActionListener(new ButtonClickListener());
        buttonsPanel.add(scheduleButton);

        // Create a new button for listing events and add it to the buttonsPanel
        JButton listEventsButton = new JButton("List Events");
        listEventsButton.setActionCommand("List Events");
        listEventsButton.addActionListener(new ButtonClickListener());
        buttonsPanel.add(listEventsButton);

        // Create a new button for subscribing to events and add it to the buttonsPanel
        JButton subscribeToEventButton = new JButton("Subscribe to Event");
        subscribeToEventButton.setActionCommand("Subscribe to event");
        subscribeToEventButton.addActionListener(new ButtonClickListener());
        buttonsPanel.add(subscribeToEventButton);

        // Reload the UI by calling revalidate() and repaint() on the contentPane
        contentPane.revalidate();
        contentPane.repaint();
    }

    /*
        This method adds a service listener for three different services: calendar, meeting, and reminder.
        When a new service is found, the listener will trigger a call to update the JList of available services with the new service.
    */
    private void discoverServices() {
        // Add a service listener for the calendar, meeting, and reminder services
        jmdns.addServiceListener("_calendar._tcp.local.", this);
        jmdns.addServiceListener("_meeting._tcp.local.", this);
        jmdns.addServiceListener("_reminder._tcp.local.", this);
    }
    /*

    This method registers three different services: calendar, meeting, and reminder.
    If an exception occurs during registration, a stack trace will be printed.
    */
    private void registerServices() {
        try {
            // Register the calendar, meeting, and reminder services with their respective names, ports, and descriptions
            jmdns.registerService(ServiceInfo.create("_calendar._tcp.local.", "CalendarService", 50051, "Calendar service"));
            jmdns.registerService(ServiceInfo.create("_meeting._tcp.local.", "MeetingService", 50052, "Meeting service"));
            jmdns.registerService(ServiceInfo.create("_reminder._tcp.local.", "ReminderService", 50053, "Reminder service"));
        } catch (Exception e) {
            // If an exception occurs during registration, print the stack trace
            e.printStackTrace();
        }
    }
    /*

    This method creates a ManagedChannel object using the provided port number and returns it.
    The channel is configured to use plaintext.
    */
    private ManagedChannel createChannel(int port) {
        return ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();
    }

    /*
     This method connects to a service of the specified service type by creating a ManagedChannel object using the service's port number.
     The switch statement is used to determine the type of service and create a corresponding client object (i.e., CalendarServiceClient, MeetingServiceClient, or ReminderServiceClient).
     After creating the client object, the method calls the corresponding action method (i.e., calendarActions, meetingActions, or reminderActions) to update the UI with the appropriate buttons for the selected service.
    */
    private void connectToService(String serviceType) {
        // Get the ServiceEvent for the specified service type
        ServiceEvent event = serviceEvents.get(serviceType);

        // Create a ManagedChannel object using the service's port number
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", event.getInfo().getPort()).usePlaintext().build();

                // Use a switch statement to determine the type of service and create a corresponding client object
        switch (serviceType) {
            case "_calendar":
                // Create a new CalendarServiceClient and update the UI with the calendar service buttons
                calendarService = new CalendarServiceClient("localhost", event.getInfo().getPort());
                calendarActions(buttonsPanel);
                break;
            case "_meeting":
                // Create a new MeetingServiceClient and update the UI with the meeting service buttons
                meetingService = new MeetingServiceClient("localhost", event.getInfo().getPort());
                meetingActions(buttonsPanel);
                break;
            case "_reminder":
                // Create a new ReminderServiceClient and update the UI with the reminder service buttons
                reminderService = new ReminderServiceClient("localhost", event.getInfo().getPort());
                reminderActions(buttonsPanel);
                break;
        }
    }
    /*
        This method implements the ServiceListener interface's serviceAdded method, which is called when a new service is discovered.
        Since this method does not have any implementation, it is left blank.
    */
    @Override
    public void serviceAdded(ServiceEvent event) {
    }
    /*
        This method implements the ServiceListener interface's serviceRemoved method, which is called when a service is removed.
        It removes the specified service's name from the JList of available services.
    */
    @Override
    public void serviceRemoved(ServiceEvent event) {
        cbServices.removeItem(event.getName());
    }

    /*
    This method implements the ServiceListener interface's serviceResolved method, which is called when a service is resolved (i.e., its details are available).
    It retrieves the service's name and type, adds the ServiceEvent to the serviceEvents map, adds the service's name and type to the JList of available services, and enables the connect button if there is at least one service in the JList.
    */
    @Override
    public void serviceResolved(ServiceEvent event) {
        // Get the service's name and type
        String serviceName = event.getName();
        String serviceType = event.getType();

        // Add the ServiceEvent to the serviceEvents map
        serviceEvents.put(serviceType.split("\\.")[0], event);

        // Add the service's name and type to the JList of available services
        listModel.addElement(serviceType + "." + serviceName);

        // Enable the connect button if there is at least one service in the JList
        if (cbServices.getItemCount() > 0) {
            cbServices.setEnabled(true);
            btnConnect.setEnabled(true);
        }
    }

    private void startServers() {

        // Start the user service server
        Server userServiceServer = ServerBuilder.forPort(50050)
                .addService(new UserServiceImpl(database))
                .build();

        // Start the calendar service server
        Server calendarServiceServer = ServerBuilder.forPort(50051)
                .addService(new CalendarServiceImpl(database))
                .build();

        // Start the meeting service server
        Server meetingServiceServer = ServerBuilder.forPort(50052)
                .addService(new MeetingServiceImpl(database))
                .build();

        // Start the reminder service server
        Server reminderServiceServer = ServerBuilder.forPort(50053)
                .addService(new ReminderServiceImpl(database))
                .build();

        try {
            userServiceServer.start();
            logger.info("User service started, listening on " + userServiceServer.getPort());
            calendarServiceServer.start();
            logger.info("Calendar service started, listening on " + calendarServiceServer.getPort());
            meetingServiceServer.start();
            logger.info("Meeting service started, listening on " + meetingServiceServer.getPort());
            reminderServiceServer.start();
            logger.info("Reminder service started, listening on " + reminderServiceServer.getPort());
        } catch (IOException e) {
            logger.severe("Failed to start server: " + e.getMessage());
        }
    }


    /*
    This is an ActionListener object that listens for button click events and calls the appropriate method based on the button's action command.
    Depending on the action command of the button clicked, this object calls one of the following methods: scheduleEvent, displayAllEvents, subscribeToEvent, scheduleReminder, displayAllReminders, deleteReminder, scheduleMeeting, displayAllMeetings, or addParticipantToMeeting.
    */
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            switch (command) {
                case "Schedule Event":
                    scheduleEvent();
                    break;
                case "List Events":
                    displayAllEvents();
                    break;
                case "Subscribe to event":
                    subscribeToEvent();
                    break;

                case "Schedule Reminder":
                    scheduleReminder();
                    break;
                case "List Reminders":
                    displayAllReminders();
                    break;
                case "Delete Reminder":
                    deleteReminder();
                    break;
                case "Schedule Meeting":
                    scheduleMeeting();
                    break;
                case "List Meetings":
                    displayAllMeetings();
                    break;
                case "Add Participant":
                    addParticipantToMeeting();
                    break;
            }
        }
    }

    /*

 This method is called when the "Schedule Meeting" button is clicked and calls the scheduleMeeting method of the MeetingServiceClient object.
 */
    private void scheduleMeeting() {
        meetingService.showScheduleMeetingUI();
    }
    /*

    This method is called when the "Schedule Event" button is clicked and calls the scheduleEvent method of the CalendarServiceClient object.
    */
    private void scheduleEvent() {
        calendarService.showScheduleEventUI();
    }
    /*

    This method is called when the "Schedule Reminder" button is clicked and calls the showScheduleReminderDialog method of the ReminderServiceClient object.
    */
    private void scheduleReminder() {
        reminderService.showScheduleReminderUI();
    }
    /*

    This method is called when the "List Events" button is clicked and calls the listAllEvents method of the CalendarServiceClient object.
    */
    private void displayAllEvents() {
        try {
            calendarService.showEventsListUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /*

    This method is called when the "Subscribe to event" button is clicked and calls the subscribe method of the CalendarServiceClient object.
    */
    private void subscribeToEvent() {
        try {
            calendarService.showSubscribeToEventsUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /*

    This method is called when the "Add Participant" button is clicked and calls the addParticipantToMeeting method of the MeetingServiceClient object.
    */
    private void addParticipantToMeeting() {
        try {
            meetingService.showAddParticipantToMeetingUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /*

    This method is called when the "List Meetings" button is clicked and calls the listAllMeetings method of the MeetingServiceClient object.
    */
    private void displayAllMeetings() {
        try {
            meetingService.showMeetingsListUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /*

    This method is called when the "Delete Reminder" button is clicked and calls the deleteReminder method of the ReminderServiceClient object.
    */
    private void deleteReminder() {
        try {
            reminderService.showDeleteReminderUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /*

    This method is called when the "List Reminders" button is clicked and calls the showListRemindersDialog method of the ReminderServiceClient object.
    */
    private void displayAllReminders() {
        lblStatus.setText("List of Reminders");
        try {
            reminderService.showListRemindersUI(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}