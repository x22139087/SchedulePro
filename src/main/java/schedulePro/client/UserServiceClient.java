package schedulePro.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import schedulePro.user.*;

import java.util.logging.Logger;

public class UserServiceClient {
    private final ManagedChannel channel;
    private final UserServiceGrpc.UserServiceBlockingStub blockingStub;

    private static final Logger logger = Logger.getLogger(ScheduleProUI.class.getName());


    public UserServiceClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public Status.Code login(String username, String password) {
        LoginRequest request = LoginRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();

        try {
            LoginResponse response = blockingStub.login(request);

            if (response.getToken().isEmpty() || response.getUserId().isEmpty()) {
                return Status.Code.NOT_FOUND;
            }

        } catch (StatusRuntimeException e) {
            return e.getStatus().getCode();

        }
        return  Status.Code.OK;
    }

    public LogoutResponse logout(String userId) {
        LogoutRequest request = LogoutRequest.newBuilder()
                .setUserId(userId)
                .build();

        return blockingStub.logout(request);
    }

    public RegisterResponse register(String username, String password) {
        RegisterRequest request = RegisterRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();

        return blockingStub.register(request);
    }

    public void shutdown() {
        channel.shutdown();
    }
}
