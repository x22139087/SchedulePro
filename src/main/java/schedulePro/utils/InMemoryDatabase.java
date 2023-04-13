package schedulePro.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import schedulePro.calendar.Event;
import schedulePro.meeting.Meeting;
import schedulePro.reminder.Reminder;
import schedulePro.user.User;

public class InMemoryDatabase {
    private final Map<String, List<Event>> calendarDatabase = new HashMap<>();
    private final Map<String, List<Meeting>> meetingDatabase = new HashMap<>();
    private final Map<String, User> userDatabase = new HashMap<>();
    private final Map<String, String> userSessions = new HashMap<>();
    private Map<String, List<Reminder>> remindersDatabase = new HashMap<>();
    private Map<String, Event> eventsDatabase = new HashMap<>();
    private Map<String, List<String>> eventAttendees = new HashMap<>();

    public InMemoryDatabase() {
        generateTestData();
    }

    public void generateTestData() {
        // Create users
        User user1 = User.newBuilder()
                .setId("1")
                .setUsername("user1")
                .build();

        User user2 = User.newBuilder()
                .setId("2")
                .setUsername("user2")
                .build();

        User user3 = User.newBuilder()
                .setId("3")
                .setUsername("user3")
                .build();

        User user4 = User.newBuilder()
                .setId("4")
                .setUsername("user4")
                .build();

        addUser(user1);
        addUser(user2);
        addUser(user3);
        addUser(user4);

        // Set logged-in user
        UserContext.setLoggedInUserId(user1.getId());

        // Create events
        long startTime = System.currentTimeMillis();
        Event event1 = Event.newBuilder()
                .setId("1")
                .setTitle("Event 1")
                .setDescription("Event 1 Description")
                .setStartTime(startTime)
                .setEndTime(startTime + 2 * 60 * 60 * 1000)
                .build();

        addCalendarEvent(user1.getId(), event1);
        addCalendarEvent(user2.getId(), event1);
        addCalendarEvent(user3.getId(), event1);
        addCalendarEvent(user4.getId(), event1);

        Event event2 = Event.newBuilder()
                .setId("2")
                .setTitle("Event 2")
                .setDescription("Event 2 Description")
                .setStartTime(startTime + 24 * 60 * 60 * 1000)
                .setEndTime(startTime + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)
                .build();

        addCalendarEvent(user1.getId(), event2);
        addCalendarEvent(user2.getId(), event2);
        addCalendarEvent(user3.getId(), event2);
        addCalendarEvent(user4.getId(), event2);

        Event event3 = Event.newBuilder()
                .setId("3")
                .setTitle("Event 3")
                .setDescription("Event 3 Description")
                .setStartTime(startTime + 2 * 24 * 60 * 60 * 1000)
                .setEndTime(startTime + 2 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)
                .build();

        addCalendarEvent(user1.getId(), event3);
        addCalendarEvent(user2.getId(), event3);
        addCalendarEvent(user3.getId(), event3);
        addCalendarEvent(user4.getId(), event3);

        Event event4 = Event.newBuilder()
                .setId("4")
                .setTitle("Event 4")
                .setDescription("Event 4 Description")
                .setStartTime(startTime + 4 * 24 * 60 * 60 * 1000)
                .setEndTime(startTime + 4 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)
                .build();

        addCalendarEvent(user1.getId(), event4);
        addCalendarEvent(user2.getId(), event4);
        addCalendarEvent(user3.getId(), event4);
        addCalendarEvent(user4.getId(), event4);

        Event event5 = Event.newBuilder()
                .setId("5")
                .setTitle("Event 5")
                .setDescription("Event 5 Description")
                .setStartTime(startTime + 5 * 24 * 60 * 60 * 1000)
                .setEndTime(startTime + 5 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)
                .build();

        addCalendarEvent(user1.getId(), event5);
        addCalendarEvent(user2.getId(), event5);
        addCalendarEvent(user3.getId(), event5);
        addCalendarEvent(user4.getId(), event5);

// Create meetings
        Meeting meeting1 = Meeting.newBuilder()
                .setId("1")
                .setTitle("Meeting 1")
                .setDescription("Meeting 1 Description")
                .setStartTime(startTime + 3600000 )
                .setEndTime(startTime + 10800000)
                .build();

        createMeeting(user1.getId(), meeting1);
        createMeeting(user2.getId(), meeting1);
        createMeeting(user3.getId(), meeting1);
        createMeeting(user4.getId(), meeting1);

        Meeting meeting2 = Meeting.newBuilder()
                .setId("2")
                .setTitle("Meeting 2")
                .setDescription("Meeting 2 Description")
                .setStartTime(startTime + 180000000)
                .setEndTime(startTime + 189600000)
                .build();

        createMeeting(user1.getId(), meeting2);
        createMeeting(user2.getId(), meeting2);
        createMeeting(user3.getId(), meeting2);
        createMeeting(user4.getId(), meeting2);

        Meeting meeting3 = Meeting.newBuilder()
                .setId("3")
                .setTitle("Meeting 3")
                .setDescription("Meeting 3 Description")
                .setStartTime(startTime + 259200000 )
                .setEndTime(startTime + 266400000)
                .build();

        createMeeting(user1.getId(), meeting3);
        createMeeting(user2.getId(), meeting3);
        createMeeting(user3.getId(), meeting3);
        createMeeting(user4.getId(), meeting3);

        Meeting meeting4 = Meeting.newBuilder()
                .setId("4")
                .setTitle("Meeting 4")
                .setDescription("Meeting 4 Description")
                .setStartTime(startTime + 345600000)
                .setEndTime(startTime + 352800000)
                .build();

        createMeeting(user1.getId(), meeting4);
        createMeeting(user2.getId(), meeting4);
        createMeeting(user3.getId(), meeting4);
        createMeeting(user4.getId(), meeting4);

        Meeting meeting5 = Meeting.newBuilder()
                .setId("5")
                .setTitle("Meeting 5")
                .setDescription("Meeting 5 Description")
                .setStartTime(startTime + 432600000 )
                .setEndTime(startTime + 432000000)
                .build();

        createMeeting(user1.getId(), meeting5);
        createMeeting(user2.getId(), meeting5);
        createMeeting(user3.getId(), meeting5);
        createMeeting(user4.getId(), meeting5);

        // Generate 5 reminders for user1 and user3

        LocalDateTime now = LocalDateTime.now();

// Reminder for user1
        Reminder reminder1 = Reminder.newBuilder()
                .setId("1")
                .setTitle("Reminder 1")
                .setDescription("Reminder 1 description")
                .setTime(now.plusMinutes(10).toEpochSecond(ZoneOffset.UTC))
                .build();

        createReminder(user1.getId(), reminder1);

// Reminder for user3
        Reminder reminder2 = Reminder.newBuilder()
                .setId("2")
                .setTitle("Reminder 2")
                .setDescription("Reminder 2 description")
                .setTime(now.plusMinutes(20).toEpochSecond(ZoneOffset.UTC))
                .build();

        createReminder(user3.getId(), reminder2);

// Reminder for user1
        Reminder reminder3 = Reminder.newBuilder()
                .setId("3")
                .setTitle("Reminder 3")
                .setDescription("Reminder 3 description")
                .setTime(now.plusMinutes(30).toEpochSecond(ZoneOffset.UTC))
                .build();

        createReminder(user1.getId(), reminder3);

// Reminder for user3
        Reminder reminder4 = Reminder.newBuilder()
                .setId("4")
                .setTitle("Reminder 4")
                .setDescription("Reminder 4 description")
                .setTime(now.plusMinutes(40).toEpochSecond(ZoneOffset.UTC))
                .build();

        createReminder(user3.getId(), reminder4);

// Reminder for user1
        Reminder reminder5 = Reminder.newBuilder()
                .setId("5")
                .setTitle("Reminder 5")
                .setDescription("Reminder 5 description")
                .setTime(now.plusMinutes(50).toEpochSecond(ZoneOffset.UTC))
                .build();

        createReminder(user1.getId(), reminder5);

// Reminder for user3
        Reminder reminder6 = Reminder.newBuilder()
                .setId("6")
                .setTitle("Reminder 6")
                .setDescription("Reminder 6 description")
                .setTime(now.plusMinutes(60).toEpochSecond(ZoneOffset.UTC))
                .build();

        createReminder(user3.getId(), reminder6);

// Reminder for user1
        Reminder reminder7 = Reminder.newBuilder()
                .setId("7")
                .setTitle("Reminder 7")
                .setDescription("Reminder 7 description")
                .setTime(now.plusMinutes(70).toEpochSecond(ZoneOffset.UTC))
                .build();

        createReminder(user1.getId(), reminder7);

// Reminder for user3
        Reminder reminder8 = Reminder.newBuilder()
                .setId("8")
                .setTitle("Reminder 8")
                .setDescription("Reminder 8 description")
                .setTime(now.plusMinutes(80).toEpochSecond(ZoneOffset.UTC))
                .build();

        createReminder(user3.getId(), reminder8);

// Reminder for user1
        Reminder reminder9 = Reminder.newBuilder()
                .setId("9")
                .setTitle("Reminder 9")
                .setDescription("Reminder 9 description")
                .setTime(now.plusMinutes(90).toEpochSecond(ZoneOffset.UTC))
                .build();

        createReminder(user1.getId(), reminder9);

// Reminder for user3
        Reminder reminder10 = Reminder.newBuilder()
                .setId("10")
                .setTitle("Reminder 10")
                .setDescription("Reminder 10 description")
                .setTime(now.plusMinutes(100).toEpochSecond(ZoneOffset.UTC))
                .build();

        createReminder(user3.getId(), reminder10);

    }


        public void addUser(User user) {
        userDatabase.put(user.getId(), user);
    }

    public User getUser(String id) {
        return userDatabase.get(id);
    }

    public User getUserByUsername(String username) {
        for (User user : userDatabase.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void addUserSession(String userId, String sessionId) {
        userSessions.put(userId, sessionId);
    }

    public boolean removeUserSession(String userId) {
        String value = userSessions.remove(userId) ;
        return value != null && !value.isEmpty();
    }

    public String getSessionId(String userId) {
        return userSessions.get(userId);
    }

    public void addCalendarEvent(String userId, Event event) {
        List<Event> events = calendarDatabase.getOrDefault(userId, new ArrayList<>());
        events.add(event);
        calendarDatabase.put(userId, events);
    }

    public List<Event> getCalendarEvents(String userId) {
        return calendarDatabase.getOrDefault(userId, new ArrayList<>());
    }

    public synchronized void updateCalendarEvent(String userId, Event event) {
        if (!calendarDatabase.containsKey(userId)) {
            calendarDatabase.put(userId, new ArrayList<>());
        }

        List<Event> events = calendarDatabase.get(userId);
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId().equals(event.getId())) {
                events.set(i, event);
                return;
            }
        }
        events.add(event);
    }

    public boolean addUserToEvent(String eventId, String userId) {
        if (!calendarDatabase.containsKey(userId)) {
            return false;
        }

        List<Event> events = calendarDatabase.get(userId);
        for (Event.Builder eventBuilder : events.stream().map(Event::toBuilder).collect(Collectors.toList())) {
            if (eventBuilder.getId().equals(eventId)) {
                eventBuilder.addAttendee(userId);
                Event updatedEvent = eventBuilder.build();
                events.removeIf(event -> event.getId().equals(updatedEvent.getId()));
                events.add(updatedEvent);
                calendarDatabase.put(userId, events);

                List<String> attendees = eventAttendees.getOrDefault(eventId, new ArrayList<>());
                attendees.add(userId);
                eventAttendees.put(eventId, attendees );
                return true;
            }
        }
        return false;
    }

    public List<String> getAttendeesForEvent(String eventId) {
        return eventAttendees.getOrDefault(eventId, Collections.emptyList());
    }

    public boolean removeCalendarEvent(String userId, String eventId) {
        List<Event> events = calendarDatabase.get(userId);
        if (events == null) {
            return false;
        }

        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (event.getId().equals(eventId)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    public void createMeeting(String id, Meeting meeting) {
        List<Meeting> meetings = meetingDatabase.getOrDefault(id, new ArrayList<>());
        meetings.add(meeting);
        meetingDatabase.put(id, meetings);
    }

    public Meeting getMeeting(String id, String meetingId) {
        List<Meeting> meetings = meetingDatabase.getOrDefault(id, new ArrayList<>());
        for (Meeting meeting : meetings) {
            if (meeting.getId().equals(meetingId)) {
                return meeting;
            }
        }
        return null;
    }

    public List<Meeting> getAllMeetings(String id) {
        return meetingDatabase.getOrDefault(id, new ArrayList<>());
    }

    public void updateMeeting(String id, Meeting meeting) {
        List<Meeting> meetings = meetingDatabase.getOrDefault(id, new ArrayList<>());
        for (int i = 0; i < meetings.size(); i++) {
            if (meetings.get(i).getId().equals(meeting.getId())) {
                meetings.set(i, meeting);
                break;
            }
        }
        meetingDatabase.put(id, meetings);
    }

    public void deleteMeeting(String id, String meetingId) {
        List<Meeting> meetings = meetingDatabase.getOrDefault(id, new ArrayList<>());
        meetings.removeIf(meeting -> meeting.getId().equals(meetingId));
        meetingDatabase.put(id, meetings);
    }

    public void createReminder(String userId, Reminder reminder) {
        if (!remindersDatabase.containsKey(userId)) {
            remindersDatabase.put(userId, new ArrayList<>());
        }
        remindersDatabase.get(userId).add(reminder);
    }

    public List<Reminder> getReminders(String userId) {
        return remindersDatabase.getOrDefault(userId, new ArrayList<>());
    }

    public void deleteReminder(String id, String reminderId) {
        List<Reminder> meetings = remindersDatabase.getOrDefault(id, new ArrayList<>());
        meetings.removeIf(meeting -> meeting.getId().equals(reminderId));
        remindersDatabase.put(id, meetings);
    }

    public Reminder getReminder(String userId, String reminderId) {
        List<Reminder> userReminders = remindersDatabase.getOrDefault(userId, new ArrayList<>());
        for (Reminder reminder : userReminders) {
            if (reminder.getId().equals(reminderId)) {
                return reminder;
            }
        }
        return null;
    }

}
