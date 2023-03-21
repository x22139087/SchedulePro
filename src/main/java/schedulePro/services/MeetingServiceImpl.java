package schedulePro.services;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

import schedulePro.meeting.*;
import schedulePro.utils.InMemoryDatabase;
import schedulePro.utils.UserContext;

public class MeetingServiceImpl extends MeetingServiceGrpc.MeetingServiceImplBase {

    private static final Logger logger = Logger.getLogger(MeetingServiceImpl.class.getName());

    private final InMemoryDatabase database;

    public MeetingServiceImpl(InMemoryDatabase database) {
        this.database = database;
    }

    @Override
    public void scheduleMeeting(ScheduleMeetingRequest request, StreamObserver<ScheduleMeetingResponse> responseObserver) {
        String organizerId = UserContext.getLoggedInUserId();
        if (organizerId == null) {
            responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
            return;
        }

        Meeting meeting = request.getMeeting();
        database.createMeeting(organizerId, meeting.toBuilder().build());
        responseObserver.onNext(ScheduleMeetingResponse.newBuilder().setId(meeting.getId()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void listMeetings(Empty request, StreamObserver<Meeting> responseObserver) {
        String userId = UserContext.getLoggedInUserId();
        if (userId == null) {
            responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
            return;
        }

        for (Meeting meeting : database.getAllMeetings(userId)) {
            responseObserver.onNext(meeting);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void addParticipant(AddParticipantRequest request, StreamObserver<SharedResponse> responseObserver) {
        String organizerId = UserContext.getLoggedInUserId();
        if (organizerId == null) {
            responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
            return;
        }

        String meetingId = request.getMeetingId();
        String participantId = request.getParticipantId();
        Meeting meeting = database.getMeeting(organizerId, meetingId);
        if (meeting == null) {
            responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND));
            return;
        }

        meeting = meeting.toBuilder().addParticipants(participantId).build();
        database.updateMeeting(organizerId, meeting);
        responseObserver.onNext(SharedResponse.newBuilder().setSuccess(true).build());
        responseObserver.onCompleted();
    }
}
