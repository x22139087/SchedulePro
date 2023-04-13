package schedulePro.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import schedulePro.user.*;

import javax.swing.*;
import java.awt.*;
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

    public Boolean showLoginDialog(Component parentComponent) {
        String username = JOptionPane.showInputDialog("Enter username:");
        String password = JOptionPane.showInputDialog("Enter password:");

        Status.Code code = login(username, password);

        switch (code) {
            case OK:
                JOptionPane.showMessageDialog(parentComponent, "Login successful!");
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
                    String message = showRegisterDialog(parentComponent);

                    if (message != null && message.isEmpty()) {
                        JOptionPane.showMessageDialog(parentComponent, "Register failed!", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    return true;
                }

                return false;

            case PERMISSION_DENIED:
                JOptionPane.showMessageDialog(parentComponent, "Login failed, permission denied due to a bad combination of username and password", "Error", JOptionPane.ERROR_MESSAGE);
                return false;

        }
        return true;
    }

    private String showRegisterDialog(Component parentComponent) {
        String username = JOptionPane.showInputDialog("Enter username:");
        String password = JOptionPane.showInputDialog("Enter password:");

        UserServiceClient userClient = new UserServiceClient("localhost", 50050);
        RegisterResponse response = userClient.register(username, password);
        String message = response.getMessage();

        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "Register failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        JOptionPane.showMessageDialog(parentComponent, "Register successful!");
        return message;
    }

    public void shutdown() {
        channel.shutdown();
    }
}
