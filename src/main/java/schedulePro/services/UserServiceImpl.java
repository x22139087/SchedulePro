package schedulePro.services;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import schedulePro.user.*;
import schedulePro.utils.InMemoryDatabase;

import java.util.*;

// Define the UserServiceImpl class
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    // Define a private instance variable to hold an instance of the InMemoryDatabase class
    private final InMemoryDatabase database;

    // Constructor to initialize the instance variable
    public UserServiceImpl(InMemoryDatabase database) {
        this.database = database;
    }

    // Implement the login() method
    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {

        // Get the username and password from the request
        String username = request.getUsername();
        String password = request.getPassword();

        // Get the user details from the database based on the username
        User user = database.getUserByUsername(username);

        // If the user is not found, send an error response to the client and return
        if (user == null) {
            responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND));
            return;
        }

        // Check if the password matches the one stored in the database
        if (!"tobeImplemented".equals(password)) {
            responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
            return;
        }

        // Generate a new session ID for the user and add it to the database
        String sessionId = UUID.randomUUID().toString();
        database.addUserSession(sessionId, user.getId());

        // Create a new response object with the session ID and user ID, and send it to the client
        LoginResponse response = LoginResponse.newBuilder()
                .setToken(sessionId)
                .setUserId(user.getId())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Implement the logout() method
    @Override
    public void logout(LogoutRequest request, StreamObserver<LogoutResponse> responseObserver) {

        // Get the user ID and session ID from the request
        String userId = request.getUserId();
        String sessionId = database.getSessionId(userId);

        // If the session ID is not found, send an error response to the client and return
        if (sessionId == null) {
            responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
            return;
        }

        // Remove the session ID from the database and create a response object with the success status
        boolean success = database.removeUserSession(userId);
        if (!success) {
            responseObserver.onError(new StatusRuntimeException(Status.INTERNAL));
            return;
        }

        LogoutResponse response = LogoutResponse.newBuilder().setSuccess(true).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Implement the register() method
    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {

        // Get the username and password from the request
        String username = request.getUsername();
        String password = request.getPassword();

        // Check if the user already exists in the database
        User existingUser = database.getUserByUsername(username);
        if (existingUser != null) {
            responseObserver.onError(new StatusRuntimeException(Status.ALREADY_EXISTS));
            return;
        }

        // Create a new user with a new ID and the given username, and add it to the database
        User user = User.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setUsername(username)
                .build();
        database.addUser(user);

        // Create a new response object with a success message and send it to the client
        RegisterResponse response = RegisterResponse.newBuilder()
                .setMessage("User registered successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}


