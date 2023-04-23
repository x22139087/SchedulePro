package schedulePro.services;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

import schedulePro.meeting.*;
import schedulePro.db.InMemoryDatabase;
import schedulePro.helpers.UserContext;

public class MeetingServiceImpl extends MeetingServiceGrpc.MeetingServiceImplBase {
    // Define a private static logger object to log events
    private static final Logger logger = Logger.getLogger(MeetingServiceImpl.class.getName());

    // Define a private instance variable to hold an instance of the InMemoryDatabase class
    private final InMemoryDatabase database;

    // Constructor to initialize the instance variable
    public MeetingServiceImpl(InMemoryDatabase database) {
        this.database = database;
    }

    // Implement the scheduleMeeting() method
    @Override
    public void scheduleMeeting(ScheduleMeetingRequest request, StreamObserver<ScheduleMeetingResponse> responseObserver) {

        // Get the ID of the organizer from the UserContext class
        String organizerId = UserContext.getLoggedInUserId();

        // If the user is not logged in, send an error to the client and return
        if (organizerId == null) {
            responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
            return;
        }

        // Get the meeting details from the request
        Meeting meeting = request.getMeeting();

        // Create a new meeting in the database with the given details and the organizer ID as the owner
        database.createMeeting(organizerId, meeting.toBuilder().build());

        // Create a new response object with the ID of the new meeting and send it to the client
        responseObserver.onNext(ScheduleMeetingResponse.newBuilder().setId(meeting.getId()).build());
        responseObserver.onCompleted();
    }

    // Implement the listMeetings() method
    @Override
    public void listMeetings(Empty request, StreamObserver<Meeting> responseObserver) {

        // Get the ID of the user from the UserContext class
        String userId = UserContext.getLoggedInUserId();

        // If the user is not logged in, send an error to the client and return
        if (userId == null) {
            responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
            return;
        }

        // Loop through all the meetings in the user's database and send each meeting to the client
        for (Meeting meeting : database.getAllMeetings(userId)) {
            responseObserver.onNext(meeting);
        }

        // Notify the client that the operation has completed
        responseObserver.onCompleted();
    }

    // Implement the addParticipant() method
    @Override
    public void addParticipant(AddParticipantRequest request, StreamObserver<SharedResponse> responseObserver) {

        // Get the ID of the organizer from the UserContext class
        String organizerId = UserContext.getLoggedInUserId();

        // If the user is not logged in, send an error to the client and return
        if (organizerId == null) {
            responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
            return;
        }

        // Get the IDs of the meeting and the participant from the request
        String meetingId = request.getMeetingId();
        String participantId = request.getParticipantId();

        // Get the meeting from the database
        Meeting meeting = database.getMeeting(organizerId, meetingId);

        // If the meeting is not found, send an error to the client and return
        if (meeting == null) {
            responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND));
            return;
        }

        // Add the participant to the meeting in the database and create a response object with the result
        meeting = meeting.toBuilder().addParticipants(participantId).build();
        database.updateMeeting(organizerId,    meeting);

        // Create a new response object with the success status and send it to the client
        responseObserver.onNext(SharedResponse.newBuilder().setSuccess(true).build());
        responseObserver.onCompleted();
    }
}
