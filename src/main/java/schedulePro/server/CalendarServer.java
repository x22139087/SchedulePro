package schedulePro.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import schedulePro.services.CalendarServiceImpl;
import schedulePro.utils.InMemoryDatabase;

import java.io.IOException;

public class CalendarServer {
    private final int port;
    private final Server server;

    public CalendarServer(int port, InMemoryDatabase database) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(new CalendarServiceImpl(database))
                .build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gRPC server since JVM is shutting down");
            CalendarServer.this.stop();
            System.out.println("Server shut down");
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

    public static void main(String[] args) throws IOException, InterruptedException {
        InMemoryDatabase database = new InMemoryDatabase();
        CalendarServer server = new CalendarServer(50051, database);

        server.start();
        server.blockUntilShutdown();
    }
}
