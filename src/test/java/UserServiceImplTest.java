import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import schedulePro.server.UserServer;
import schedulePro.user.*;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {
    private static final Logger logger = Logger.getLogger(UserServiceImplTest.class.getName());

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "tobeImplemented";
    private static final String WRONG_PASSWORD = "wrongpassword";

    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    private ManagedChannel channel;

    @BeforeEach
    void setUp() {
        channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        blockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    @AfterEach
    void tearDown() {
        channel.shutdown();
    }

    @Test
    void testRegister() {
        RegisterRequest request = RegisterRequest.newBuilder()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .build();

        RegisterResponse response = blockingStub.register(request);

        logger.info(response.getMessage());
        assertNotNull(response.getMessage());
    }

    @Test
    void testLogin() {
        RegisterRequest registerRequest = RegisterRequest.newBuilder()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .build();

        RegisterResponse registerResponse = blockingStub.register(registerRequest);

        LoginRequest loginRequest = LoginRequest.newBuilder()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .build();

        LoginResponse loginResponse = blockingStub.login(loginRequest);

        assertNotNull(loginResponse.getToken());
    }

    @Test
    void testLoginWithWrongPassword() {
        RegisterRequest registerRequest = RegisterRequest.newBuilder()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .build();

        RegisterResponse registerResponse = blockingStub.register(registerRequest);

        LoginRequest loginRequest = LoginRequest.newBuilder()
                .setUsername(USERNAME)
                .setPassword(WRONG_PASSWORD)
                .build();

        try {
            LoginResponse loginResponse = blockingStub.login(loginRequest);
        } catch (Exception e) {
            assertEquals("PERMISSION_DENIED", e.getMessage());
        }
    }

    @Test
    void testLogout() {
        RegisterRequest registerRequest = RegisterRequest.newBuilder()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .build();

        RegisterResponse registerResponse = blockingStub.register(registerRequest);

        LoginRequest loginRequest = LoginRequest.newBuilder()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .build();

        LoginResponse loginResponse = blockingStub.login(loginRequest);

        LogoutRequest logoutRequest = LogoutRequest.newBuilder().setUserId(loginResponse.getToken()).build();

        LogoutResponse logoutResponse = blockingStub.logout(logoutRequest);

        assertTrue(logoutResponse.getSuccess());
    }
}
