package schedulePro.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import schedulePro.services.UserServiceImpl;
import schedulePro.utils.InMemoryDatabase;

import java.io.IOException;
import java.util.logging.Logger;

public class UserServer {
    // Create a logger to log server events
    private static final Logger logger = Logger.getLogger(UserServer.class.getName());

    // Define the port number and server instance
    private final int port;
    private final Server server;

    // Constructor that takes in a port number and database instance
    public UserServer(int port, InMemoryDatabase database) throws IOException {
        // Create a new server instance with the given port and add the UserServiceImpl service to it
        this(ServerBuilder.forPort(port), port, database);
    }

    // Constructor that takes in a server builder, port number, and database instance
    public UserServer(ServerBuilder<?> serverBuilder, int port, InMemoryDatabase database) {
        // Set the port number and create a new instance of UserServiceImpl
        this.port = port;
        UserServiceImpl userService = new UserServiceImpl(database);
        // Build the server instance with the given server builder and add the UserServiceImpl service to it
        server = serverBuilder.addService(userService).build();
    }

    // Start the server
    public void start() throws IOException {
        server.start();
        // Log that the server has started and is listening on the given port number
        logger.info("Server started, listening on " + port);
        // Add a shutdown hook to stop the server when the JVM is shutting down
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            UserServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    // Stop the server
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    // Block until the server is terminated
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    // Main method to start the server
    public static void main(String[] args) throws Exception {
        // Create a new instance of InMemoryDatabase and UserServer
        InMemoryDatabase database = new InMemoryDatabase();
        UserServer server = new UserServer(50051, database);
        // Start the server and block until it is terminated
        server.start();
        server.blockUntilShutdown();
    }
}
