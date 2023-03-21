package schedulePro.services;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import schedulePro.user.*;
import schedulePro.utils.InMemoryDatabase;

import java.util.*;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

        private final InMemoryDatabase database;

        public UserServiceImpl(InMemoryDatabase database) {
            this.database = database;
        }

        @Override
        public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
            String username = request.getUsername();
            String password = request.getPassword();

            User user = database.getUserByUsername(username);
            if (user == null) {
                responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND));
                return;
            }

            if (!"tobeImplemented".equals(password)) {
                responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
                return;
            }

            String sessionId = UUID.randomUUID().toString();
            database.addUserSession(sessionId, user.getId());

            LoginResponse response = LoginResponse.newBuilder()
                    .setToken(sessionId)
                    .setUserId(user.getId())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void logout(LogoutRequest request, StreamObserver<LogoutResponse> responseObserver) {
            String userId = request.getUserId();
            String sessionId = database.getSessionId(userId);
            if (sessionId == null) {
                responseObserver.onError(new StatusRuntimeException(Status.PERMISSION_DENIED));
                return;
            }

            boolean success = database.removeUserSession(userId);
            if (!success) {
                responseObserver.onError(new StatusRuntimeException(Status.INTERNAL));
                return;
            }

            LogoutResponse response = LogoutResponse.newBuilder().setSuccess(true).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
            String username = request.getUsername();
            String password = request.getPassword();

            User existingUser = database.getUserByUsername(username);
            if (existingUser != null) {
                responseObserver.onError(new StatusRuntimeException(Status.ALREADY_EXISTS));
                return;
            }

            User user = User.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setUsername(username)
                    .build();
            database.addUser(user);

            RegisterResponse response = RegisterResponse.newBuilder()
                    .setMessage("User registered successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

    }

