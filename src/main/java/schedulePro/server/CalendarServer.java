package schedulePro.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import schedulePro.services.CalendarServiceImpl;
import schedulePro.db.InMemoryDatabase;

import java.io.IOException;

// Define the CalendarServer class
public class CalendarServer {
    // Define instance variables to hold the port number and the gRPC server instance
    private final int port;
    private final Server server;

    // Constructor to initialize the port number and the gRPC server instance
    public CalendarServer(int port, InMemoryDatabase database) {
        this.port = port;

        // Create a new gRPC server builder with the given port number and add the CalendarServiceImpl to it
        this.server = ServerBuilder.forPort(port)
                .addService(new CalendarServiceImpl(database))
                .build();
    }

    // Method to start the gRPC server
    public void start() throws IOException {
        // Start the server and print a message to indicate that it has started listening on the specified port
        server.start();
        System.out.println("Server started, listening on " + port);

        // Add a shutdown hook to gracefully stop the server when the JVM is shutting down
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gRPC server since JVM is shutting down");
            CalendarServer.this.stop();
            System.out.println("Server shut down");
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
        // Create a new instance of the InMemoryDatabase class and a new instance of the CalendarServer class with the given port number and the database instance
        InMemoryDatabase database = new InMemoryDatabase();
        CalendarServer server = new CalendarServer(50051, database);

        // Start the server and block until it is terminated
        server.start();
        server.blockUntilShutdown();
    }
}
