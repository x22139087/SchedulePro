package schedulePro.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import schedulePro.meeting.MeetingServiceGrpc;
import schedulePro.services.MeetingServiceImpl;
import schedulePro.db.InMemoryDatabase;

import java.io.IOException;

// Define the MeetingServer class
public class MeetingServer {
    // Define instance variables to hold the port number and the gRPC server instance
    private final int port;
    private final Server server;

    // Constructor to initialize the port number and the gRPC server instance with the given service
    public MeetingServer(int port, MeetingServiceGrpc.MeetingServiceImplBase service) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(service)
                .build();
    }

    // Method to start the gRPC server
    public void start() {
        try {
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Print a message to indicate that the server has started and listening on the specified port
        System.out.println("Server started, listening on " + port);

        // Add a shutdown hook to gracefully stop the server when the JVM is shutting down
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            MeetingServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    // Method to stop the gRPC server
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    // Method to block until the gRPC server is terminated
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    // Main method to start the server and block until it is terminated
    public static void main(String[] args) throws IOException, InterruptedException {
        // Create a new instance of the InMemoryDatabase class, a new instance of the MeetingServiceImpl class with the database instance, and a new instance of the MeetingServer class with the given port number and the service instance
        InMemoryDatabase database = new InMemoryDatabase();
        MeetingServiceImpl service = new MeetingServiceImpl(database);
        MeetingServer server = new MeetingServer(8080, service);

        // Start the server and block until it is terminated
        server.start();
        server.blockUntilShutdown();
    }
}
