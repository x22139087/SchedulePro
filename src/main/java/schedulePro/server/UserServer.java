package schedulePro.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import schedulePro.services.UserServiceImpl;
import schedulePro.utils.InMemoryDatabase;

import java.io.IOException;
import java.util.logging.Logger;

public class UserServer {
    private static final Logger logger = Logger.getLogger(UserServer.class.getName());

    private final int port;
    private final Server server;

    public UserServer(int port, InMemoryDatabase database) throws IOException {
        this(ServerBuilder.forPort(port), port, database);
    }

    public UserServer(ServerBuilder<?> serverBuilder, int port, InMemoryDatabase database) {
        this.port = port;
        UserServiceImpl userService = new UserServiceImpl(database);
        server = serverBuilder.addService(userService)
//                .addService(ProtoReflectionService.newInstance())
                .build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            UserServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        InMemoryDatabase database = new InMemoryDatabase();
        UserServer server = new UserServer(50051, database);
        server.start();
        server.blockUntilShutdown();
    }
}
