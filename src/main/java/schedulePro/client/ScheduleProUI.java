package schedulePro.client;

import io.grpc.*;
import io.grpc.stub.StreamObserver;
import schedulePro.calendar.*;
import schedulePro.calendar.Event;
import schedulePro.meeting.*;
import schedulePro.meeting.Empty;
import schedulePro.meeting.SharedResponse;
import schedulePro.reminder.*;
import schedulePro.services.CalendarServiceImpl;
import schedulePro.services.MeetingServiceImpl;
import schedulePro.services.ReminderServiceImpl;
import schedulePro.services.UserServiceImpl;
import schedulePro.user.RegisterResponse;
import schedulePro.user.UserServiceGrpc;
import schedulePro.utils.InMemoryDatabase;
import schedulePro.utils.UserContext;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Logger;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ScheduleProUI extends JFrame implements ServiceListener {

    private static final long serialVersionUID = 1L;

    private final InMemoryDatabase database;

    private final Map<String, ServiceEvent> serviceEvents = new HashMap<>();
    private String selectedServiceType = "";
    private JPanel contentPane;
    private JLabel lblStatus;
    private JComboBox<String> cbServices;
    private JButton btnDiscover;
    private JButton btnConnect;

    private JPanel buttonsPanel;

    private JmDNS jmdns;
    private ServiceEvent event;

    private static final Logger logger = Logger.getLogger(ScheduleProUI.class.getName());

    private CalendarServiceGrpc.CalendarServiceBlockingStub calendarServiceBlockingStub;
    private ReminderServiceGrpc.ReminderServiceStub reminderServiceStub;
    private ReminderServiceGrpc.ReminderServiceBlockingStub reminderServiceBlockingStub;
    private String userId;
    private CalendarServiceGrpc.CalendarServiceStub calendarServiceStub;
    private MeetingServiceGrpc.MeetingServiceBlockingStub meetingServiceBlockingStub;
    private MeetingServiceGrpc.MeetingServiceStub meetingServiceStub;
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;


    public static void main(String[] args) {
        ScheduleProUI serviceGUI = new ScheduleProUI();
        serviceGUI.registerServices();
        serviceGUI.setVisible(true);
    }

    /**
     * Create the frame.
     */
    public ScheduleProUI() {

        database = new InMemoryDatabase();
        startServers();
        try {
            jmdns = JmDNS.create();
        } catch (IOException e) {
            e.printStackTrace();
        }


        boolean success = true;
        String userId = UserContext.getLoggedInUserId();

        if (userId.isEmpty()) {
            success = showLoginDialog();
        }

        if (success) {

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBounds(100, 100, 650, 600);
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            setContentPane(contentPane);
            contentPane.setLayout(new BorderLayout());

            // Header panel
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel headerLabel = new JLabel("Smart Scheduler");
            headerLabel.setFont(headerLabel.getFont().deriveFont(24.0f));
            headerPanel.add(headerLabel);
            contentPane.add(headerPanel, BorderLayout.NORTH);

            // Service discovery
            cbServices = new JComboBox<String>();
            cbServices.setEnabled(false);
            cbServices.setBounds(0,0,150, 30);

            Box servicesPanel = Box.createHorizontalBox();
            servicesPanel.setBounds(0,0, 150, 30);
            JButton connectButton = new JButton("Connect");
            connectButton.setEnabled(false);
            connectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    connectToService("_reminder");
                }
            });

            servicesPanel.add(cbServices);
            servicesPanel.add(connectButton);
            contentPane.add(servicesPanel, BorderLayout.CENTER);


            // Buttons panel
            buttonsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            contentPane.add(buttonsPanel, BorderLayout.SOUTH);

            /**
             * serviceList = new JList<>(new String[]{"Calendar Service", "Meeting Service", "Reminder Service"});
             * serviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
             * serviceList.setEnabled(false);
             * serviceList.setVisibleRowCount(3);
             * serviceList.setCellRenderer(new ServiceListRenderer());
             * serviceList.addListSelectionListener(new ServiceListSelectionListener());
             * contentPane.add(new JScrollPane(serviceList), BorderLayout.WEST);
             */

            discoverServices();
        }
    }

    /**
     * private void connectToService(String serviceType) {
     *     String selectedService = serviceList.getSelectedValue();
     *     if (selectedService == null) {
     *         JOptionPane.showMessageDialog(ScheduleProUI.this, "Please select a service to connect to.", "Error", JOptionPane.ERROR_MESSAGE);
     *         return;
     *     }
     *
     *     boolean connected = connectToGrpcService(selectedService, serviceType, ports.get(serviceType + "." + selectedService + "._tcp.local."));
     *     updateServiceButtons(serviceType, connected);
     * }
     *
     * private void updateServiceButtons(String serviceType, boolean connected) {
     *     int index = serviceList.getSelectedIndex();
     *     if (index < 0) {
     *         return;
     *     }
     *     DefaultListModel<String> model = (DefaultListModel<String>) serviceList.getModel();
     *     String selectedService = model.getElementAt(index);
     *     model.setElementAt(selectedService.split(" ")[0] + " " + (connected ? "Connected" : "Connect"), index);
     * }
     *
     */

    private void reminderActions(JPanel buttonsPanel) {

        buttonsPanel.removeAll();
        JPanel reminderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel reminderLabel = new JLabel("Reminder Service:");
        reminderPanel.add(reminderLabel);
        buttonsPanel.add(reminderPanel);

        JButton scheduleReminderButton = new JButton("Schedule Reminder");
        scheduleReminderButton.setActionCommand("Schedule Reminder");
        scheduleReminderButton.addActionListener(new ButtonClickListener());
        scheduleReminderButton.setEnabled(false);
        buttonsPanel.add(scheduleReminderButton);

        JButton listRemindersButton = new JButton("List Reminders");
        listRemindersButton.setActionCommand("List Reminders");
        listRemindersButton.addActionListener(new ButtonClickListener());
        listRemindersButton.setEnabled(false);
        buttonsPanel.add(listRemindersButton);

        JButton deleteRemindersButton = new JButton("Delete Reminder");
        deleteRemindersButton.setActionCommand("Delete Reminder");
        deleteRemindersButton.addActionListener(new ButtonClickListener());
        deleteRemindersButton.setEnabled(false);
        buttonsPanel.add(deleteRemindersButton);
    }

    private void meetingActions(JPanel buttonsPanel) {
        buttonsPanel.removeAll();
        JPanel meetingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel meetingLabel = new JLabel("Meeting Service:");
        meetingPanel.add(meetingLabel);
        buttonsPanel.add(meetingPanel);

        JButton scheduleMeetingButton = new JButton("Schedule Meeting");
        scheduleMeetingButton.setActionCommand("Schedule Meeting");
        scheduleMeetingButton.addActionListener(new ButtonClickListener());
        scheduleMeetingButton.setEnabled(false);
        buttonsPanel.add(scheduleMeetingButton);

        JButton listMeetingsButton = new JButton("List Meetings");
        listMeetingsButton.setActionCommand("List Meetings");
        listMeetingsButton.addActionListener(new ButtonClickListener());
        listMeetingsButton.setEnabled(false);
        buttonsPanel.add(listMeetingsButton);

        JButton addParticipantButton = new JButton("Add Participant");
        addParticipantButton.setActionCommand("Add Participant");
        addParticipantButton.addActionListener(new ButtonClickListener());
        addParticipantButton.setEnabled(false);
        buttonsPanel.add(addParticipantButton);
    }

    private void calendarActions(JPanel buttonsPanel) {
        buttonsPanel.removeAll();
        JPanel calendarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel calendarLabel = new JLabel("Calendar Service:");
        calendarPanel.add(calendarLabel);
        buttonsPanel.add(calendarPanel);

        JButton scheduleButton = new JButton("Schedule Event");
        scheduleButton.setActionCommand("Schedule Event");
        scheduleButton.addActionListener(new ButtonClickListener());
        scheduleButton.setEnabled(false);
        buttonsPanel.add(scheduleButton);

        JButton listEventsButton = new JButton("List Events");
        listEventsButton.setActionCommand("List Events");
        listEventsButton.addActionListener(new ButtonClickListener());
        listEventsButton.setEnabled(false);
        buttonsPanel.add(listEventsButton);

        JButton subscribeToEventButton = new JButton("Subscribe to Event");
        subscribeToEventButton.setActionCommand("Subscribe to event");
        subscribeToEventButton.addActionListener(new ButtonClickListener());
        subscribeToEventButton.setEnabled(false);
        buttonsPanel.add(subscribeToEventButton);
    }

    private void discoverServices() {
        jmdns.addServiceListener("_calendar._tcp.local.", this);
        jmdns.addServiceListener("_meeting._tcp.local.", this);
        jmdns.addServiceListener("_reminder._tcp.local.", this);
    }

    private void registerServices() {
        try {
            jmdns.registerService(ServiceInfo.create("_calendar._tcp.local.", "CalendarService", 50051, "Calendar service"));
            jmdns.registerService(ServiceInfo.create("_meeting._tcp.local.", "MeetingService", 50052, "Meeting service"));
            jmdns.registerService(ServiceInfo.create("_reminder._tcp.local.", "ReminderService", 50053, "Reminder service"));
        } catch (Exception e) {
            e.printStackTrace();
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

    private ManagedChannel createChannel(int port) {
        return ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();
    }

    private void connectToService(String serviceType) {
        selectedServiceType = serviceType;
        ServiceEvent event = serviceEvents.get(serviceType);
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", event.getInfo().getPort()).usePlaintext().build();
        switch (serviceType) {
            case "CalendarService":
                calendarServiceBlockingStub = CalendarServiceGrpc.newBlockingStub(channel);
                calendarServiceStub = CalendarServiceGrpc.newStub(channel);
                // Calendar service buttons
                calendarActions(buttonsPanel);
                break;
            case "MeetingService":
                meetingServiceBlockingStub = MeetingServiceGrpc.newBlockingStub(channel);
                meetingServiceStub = MeetingServiceGrpc.newStub(channel);
                // Calendar service buttons
                meetingActions(buttonsPanel);
                break;
            case "ReminderService":
                reminderServiceBlockingStub = ReminderServiceGrpc.newBlockingStub(channel);
                reminderServiceStub = ReminderServiceGrpc.newStub(channel);
                // Calendar service buttons
                reminderActions(buttonsPanel);
                break;
        }
        updateButtonStatus();
    }

    private void updateButtonStatus() {
        boolean calendarConnected = calendarServiceBlockingStub != null;
        boolean meetingConnected = meetingServiceBlockingStub != null;
        boolean reminderConnected = reminderServiceBlockingStub != null;
        boolean connected = calendarConnected && meetingConnected && reminderConnected;
        JButton calendarConnectButton = (JButton) ((JPanel) ((JPanel) contentPane.getComponent(1)).getComponent(0)).getComponent(1);
        calendarConnectButton.setEnabled(!calendarConnected);

        JButton meetingConnectButton = (JButton) ((JPanel) ((JPanel) contentPane.getComponent(1)).getComponent(1)).getComponent(1);
        meetingConnectButton.setEnabled(!meetingConnected);

        JButton reminderConnectButton = (JButton) ((JPanel) ((JPanel) contentPane.getComponent(1)).getComponent(2)).getComponent(1);
        reminderConnectButton.setEnabled(!reminderConnected);

        JButton scheduleButton = (JButton) ((JPanel) contentPane.getComponent(1)).getComponent(3);
        scheduleButton.setEnabled(connected);

        JButton listEventsButton = (JButton) ((JPanel) contentPane.getComponent(1)).getComponent(4);
        listEventsButton.setEnabled(connected);

        JButton subscribeToEventButton = (JButton) ((JPanel) contentPane.getComponent(1)).getComponent(5);
        subscribeToEventButton.setEnabled(connected);

        JButton scheduleMeetingButton = (JButton) ((JPanel) contentPane.getComponent(1)).getComponent(7);
        scheduleMeetingButton.setEnabled(connected);

        JButton listMeetingsButton = (JButton) ((JPanel) contentPane.getComponent(1)).getComponent(8);
        listMeetingsButton.setEnabled(connected);

        JButton addParticipantButton = (JButton) ((JPanel) contentPane.getComponent(1)).getComponent(9);
        addParticipantButton.setEnabled(connected);

        JButton scheduleReminderButton = (JButton) ((JPanel) contentPane.getComponent(1)).getComponent(11);
        scheduleReminderButton.setEnabled(connected);

        JButton listRemindersButton = (JButton) ((JPanel) contentPane.getComponent(1)).getComponent(12);
        listRemindersButton.setEnabled(connected);

        JButton deleteRemindersButton = (JButton) ((JPanel) contentPane.getComponent(1)).getComponent(13);
        deleteRemindersButton.setEnabled(connected);

        cbServices.setEnabled(!connected);
    }

        @Override
    public void serviceAdded(ServiceEvent event) { }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        cbServices.removeItem(event.getName());
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        String serviceName = event.getName();
        String serviceType = event.getType();

        cbServices.addItem(serviceType + "." + serviceName);
        serviceEvents.put(event.getName(), event);

        if (cbServices.getItemCount() > 0) {
            cbServices.setEnabled(true);
            btnConnect.setEnabled(true);
        }
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            switch (command) {
                case "Schedule event":
                    // Code to schedule a meeting
                    scheduleEvent();
                    break;
                case "List Events":
                    // Code to list meetings
                    listEvents();
                    break;
                case "Subscribe to event":
                    subscribeToEvent();
                    break;

                case "Schedule Reminder":
                    // Code to set reminder
                    showScheduleReminderDialog();
                    lblStatus.setText("Schedule Reminder");
                    break;
                case "List Reminders":
                    lblStatus.setText("List of Reminders");
                    showListRemindersDialog();
                    break;
                case "Delete reminder":
                    deleteReminder();
                    break;

                case "Schedule meeting":
                    scheduleMeeting();
                    break;
                case "List meeting":
                    listMeetings();
                    break;
                case "Add participant":
                    addParticipantToMeeting();

                    break;
            }
        }
    }

    private void listMeetings() {
        String userId = UserContext.getLoggedInUserId();

        if (userId == null) {
            lblStatus.setText("You must be logged in to view meetings.");
            return;
        }

        Empty listMeetingsRequest = Empty.newBuilder().build();
        Iterator<Meeting> meetingsIterator = meetingServiceBlockingStub.listMeetings(listMeetingsRequest);

        StringBuilder meetingsListBuilder = new StringBuilder("Meetings:\n");
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

        if (meetingsListBuilder.length() == 0) {
            meetingsListBuilder.append("No meetings found.");
        }

        JTextArea meetingsListArea = new JTextArea(meetingsListBuilder.toString());
        meetingsListArea.setEditable(false);
        meetingsListArea.setLineWrap(true);
        meetingsListArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(meetingsListArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(null, scrollPane, "Meetings List", JOptionPane.PLAIN_MESSAGE);
    }

    private void addParticipantToMeeting() {
        String meetingId = JOptionPane.showInputDialog("Enter Meeting ID:");
        String participantId = JOptionPane.showInputDialog("Enter Participant ID:");
        AddParticipantRequest addParticipantRequest = AddParticipantRequest.newBuilder()
                .setMeetingId(meetingId)
                .setParticipantId(participantId)
                .build();

        SharedResponse response = meetingServiceBlockingStub.addParticipant(addParticipantRequest);
        if (response.getSuccess()) {
            lblStatus.setText("Participant added successfully");
        } else {
            lblStatus.setText("Failed to add participant");
        }
    }

    private void scheduleMeeting() {
        String title = JOptionPane.showInputDialog("Enter Meeting Title:");
        String description = JOptionPane.showInputDialog("Enter Meeting Description:");
        String startDateTime = JOptionPane.showInputDialog("Enter Meeting Start Time (yyyy-MM-dd HH:mm:ss):");
        String endDateTime = JOptionPane.showInputDialog("Enter Meeting End Time (yyyy-MM-dd HH:mm:ss):");
        String location = JOptionPane.showInputDialog("Enter Meeting Location:");
        String participants = JOptionPane.showInputDialog("Enter Participant IDs (separated by commas):");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
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

            MeetingServiceClient client = new MeetingServiceClient("localhost", 50052);
            ScheduleMeetingResponse response = client.scheduleMeeting(meeting);

        } catch (DateTimeParseException ex) {
            ex.printStackTrace();
            lblStatus.setText("Invalid date/time format");
        }
    }

    private Boolean showLoginDialog() {
        String username = JOptionPane.showInputDialog("Enter username:");
        String password = JOptionPane.showInputDialog("Enter password:");

        UserServiceClient userClient = new UserServiceClient("localhost", 50050);
        Status.Code code = userClient.login(username, password);

        switch (code) {
            case OK:
                JOptionPane.showMessageDialog(this, "Login successful!");
                return true;
            case NOT_FOUND:
                int choice = JOptionPane.showOptionDialog(
                        null,
                        "User not found. Do you want to register?",
                        "Registration",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Register", "Cancel"},
                        "Register"
                );

                if (choice == 0) {
                    String message = showRegisterDialog();

                    if (message != null && message.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Register failed!", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    return true;
                }

                return false;

            case PERMISSION_DENIED:
                JOptionPane.showMessageDialog(this, "Login failed, permission denied due to a bad combination of username and password", "Error", JOptionPane.ERROR_MESSAGE);
                return false;

        }
        return true;
    }


    private String showRegisterDialog() {
        String username = JOptionPane.showInputDialog("Enter username:");
        String password = JOptionPane.showInputDialog("Enter password:");

        UserServiceClient userClient = new UserServiceClient("localhost", 50050);
        RegisterResponse response = userClient.register(username, password);
        String message = response.getMessage();

        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Register failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        JOptionPane.showMessageDialog(this, "Register successful!");
        return message;
    }

    private void deleteReminder() {
        String reminderId = JOptionPane.showInputDialog("Enter Reminder ID:");
        if (reminderId == null || reminderId.isEmpty()) {
            lblStatus.setText("Invalid reminder ID.");
            return;
        }

        ReminderServiceClient reminderClient = new ReminderServiceClient("localhost", 50053);
        boolean response = reminderClient.deleteReminder(reminderId);

        if (response) {
            JOptionPane.showMessageDialog(null, "Reminder with ID: " + reminderId + " deleted.");
        } else {
            JOptionPane.showMessageDialog(null, "Error setting reminder", "Error", JOptionPane.ERROR_MESSAGE);
        }

        lblStatus.setText("Delete reminder");
    }

    private void showScheduleReminderDialog() {
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

        int result = JOptionPane.showConfirmDialog(null, panel, "Set Reminder", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String dateTimeStr = dateTimeField.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);

            Reminder reminder = Reminder.newBuilder()
                    .setId(UserContext.getLoggedInUserId())
                    .setTitle(title)
                    .setDescription(description)
                    .setTime(dateTime.toEpochSecond(ZoneOffset.UTC))
                    .build();

            ReminderServiceClient reminderClient = new ReminderServiceClient("localhost", 50053);
            ScheduleReminderResponse response = reminderClient.scheduleReminder(reminder);

            if (!response.getId().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Reminder set with ID: " + response.getId());
            } else {
                JOptionPane.showMessageDialog(null, "Error setting reminder", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void showListRemindersDialog() {
        String userId = UserContext.getLoggedInUserId();

        if (userId == null) {
            lblStatus.setText("You must be logged in to view reminders.");
            return;
        }

        ListRemindersRequest listRemindersRequest = ListRemindersRequest.newBuilder().setUserId(userId).build();
        Iterator<Reminder> remindersIterator = reminderServiceBlockingStub.listReminders(listRemindersRequest);

        StringBuilder remindersListBuilder = new StringBuilder("Reminders:\n");
        while (remindersIterator.hasNext()) {
            Reminder reminder = remindersIterator.next();
            if (reminder.getUserId().equals(userId)) {
                remindersListBuilder.append("ID: ").append(reminder.getId())
                        .append("\nTitle: ").append(reminder.getTitle())
                        .append("\nDescription: ").append(reminder.getDescription())
                        .append("\nReminder Time: ").append(LocalDateTime.ofEpochSecond(reminder.getTime(), 0, ZoneOffset.UTC))
                        .append("\n\n");
            }
        }

        if (remindersListBuilder.length() == 0) {
            remindersListBuilder.append("No reminders found.");
        }

        JTextArea remindersListArea = new JTextArea(remindersListBuilder.toString());
        remindersListArea.setEditable(false);
        remindersListArea.setLineWrap(true);
        remindersListArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(remindersListArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(null, scrollPane, "Reminders List", JOptionPane.PLAIN_MESSAGE);
    }


    private void scheduleEvent() {
        String title = JOptionPane.showInputDialog("Enter Meeting Title:");
        String description = JOptionPane.showInputDialog("Enter Meeting Description:");
        String startDateTime = JOptionPane.showInputDialog("Enter Meeting Start Time (yyyy-MM-dd HH:mm:ss):");
        String endDateTime = JOptionPane.showInputDialog("Enter Meeting End Time (yyyy-MM-dd HH:mm:ss):");
        String location = JOptionPane.showInputDialog("Enter Meeting Location:");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
            LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);
            ZoneId zoneId = ZoneId.systemDefault();
            ZoneOffset zoneOffset = zoneId.getRules().getOffset(LocalDateTime.now());

            StreamObserver<AddEventRequest> requestObserver = calendarServiceStub.addEvents(new StreamObserver<AddEventResponse>() {
                @Override
                public void onNext(AddEventResponse addEventResponse) {
                    lblStatus.setText("Meeting scheduled with ID: " + addEventResponse.getId());
                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    lblStatus.setText("Error scheduling meeting: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    // Nothing to do here
                }
            });

            String userId = UserContext.getLoggedInUserId();

            AddEventRequest request = AddEventRequest.newBuilder()
                    .setTitle(title)
                    .setDescription(description)
                    .setStartTime(start.toEpochSecond(zoneOffset))
                    .setEndTime(end.toEpochSecond(zoneOffset))
                    .build();

            requestObserver.onNext(request);
            requestObserver.onCompleted();
        } catch (DateTimeParseException ex) {
            ex.printStackTrace();
            lblStatus.setText("Invalid date/time format");
        }
    }

    private void listEvents() {
        String userId = UserContext.getLoggedInUserId();

        if (userId == null) {
            lblStatus.setText("You must be logged in to view events.");
            return;
        }


        schedulePro.calendar.Empty listEventsRequest = schedulePro.calendar.Empty.newBuilder().build();
        Iterator<Event> eventsIterator = calendarServiceBlockingStub.listEvents(listEventsRequest);

        StringBuilder eventsListBuilder = new StringBuilder("Events:\n");
        while (eventsIterator.hasNext()) {
            Event event = eventsIterator.next();
            eventsListBuilder.append("ID: ").append(event.getId())
                    .append("\nTitle: ").append(event.getTitle())
                    .append("\nDescription: ").append(event.getDescription())
                    .append("\nStart Time: ").append(LocalDateTime.ofEpochSecond(event.getStartTime(), 0, ZoneOffset.UTC))
                    .append("\nEnd Time: ").append(LocalDateTime.ofEpochSecond(event.getEndTime(), 0, ZoneOffset.UTC))
                    .append("\n\n");
        }

        if (eventsListBuilder.length() == 0) {
            eventsListBuilder.append("No events found.");
        }

        JTextArea eventsListArea = new JTextArea(eventsListBuilder.toString());
        eventsListArea.setEditable(false);
        eventsListArea.setLineWrap(true);
        eventsListArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(eventsListArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(null, scrollPane, "Events List", JOptionPane.PLAIN_MESSAGE);
    }

    private void subscribeToEvent() {
        String eventId = JOptionPane.showInputDialog("Enter Event ID:");
        if (eventId == null || eventId.isEmpty()) {
            lblStatus.setText("Invalid event ID.");
            return;
        }

        StreamObserver<SubscribeToEventRequest> requestObserver = calendarServiceStub.subscribeToEvents(new StreamObserver<SubscribeToEventResponse>() {
            @Override
            public void onNext(SubscribeToEventResponse response) {
                if (response.getSuccess()) {
                    lblStatus.setText("Subscribed to event with ID: " + response.getId());
                } else {
                    lblStatus.setText("Error subscribing to event with ID: " + response.getId());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                lblStatus.setText("Error subscribing to event: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                // Nothing to do here
            }
        });

        SubscribeToEventRequest request = SubscribeToEventRequest.newBuilder()
                .setEventId(eventId)
                .build();

        requestObserver.onNext(request);
        requestObserver.onCompleted();

        lblStatus.setText("Subscribe to event");
    }
}