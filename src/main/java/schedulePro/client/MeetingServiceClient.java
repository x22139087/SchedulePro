package schedulePro.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import schedulePro.meeting.*;

public class MeetingServiceClient {
    private final MeetingServiceGrpc.MeetingServiceBlockingStub blockingStub;
    private final MeetingServiceGrpc.MeetingServiceStub asyncStub;

    public MeetingServiceClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        blockingStub = MeetingServiceGrpc.newBlockingStub(channel);
        asyncStub = MeetingServiceGrpc.newStub(channel);
    }

    public ScheduleMeetingResponse scheduleMeeting(Meeting meeting) {
        ScheduleMeetingRequest request = ScheduleMeetingRequest.newBuilder().setMeeting(meeting).build();
        return blockingStub.scheduleMeeting(request);
    }

    public void listMeetings(StreamObserver<Meeting> observer) {
        Empty request = Empty.newBuilder().build();
        asyncStub.listMeetings(request, observer);
    }

    public boolean addParticipant(String meetingId, String participantId) {
        AddParticipantRequest request = AddParticipantRequest.newBuilder()
                .setMeetingId(meetingId)
                .setParticipantId(participantId)
                .build();
        SharedResponse response = blockingStub.addParticipant(request);

        return response.getSuccess();
    }
}
