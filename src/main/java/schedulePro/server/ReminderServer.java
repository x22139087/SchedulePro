package schedulePro.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import schedulePro.services.ReminderServiceImpl;

import java.io.IOException;

/**
 * This class is the server for the Reminder service. It creates and starts a gRPC server using the provided service and port.
 * It has methods to start and stop the server, as well as block until the server is shut down.
 */
public class ReminderServer {
    private Server server;

    public ReminderServer(int port, ReminderServiceImpl service) {
        server = ServerBuilder.forPort(port)
                .addService(service)
                .build();
    }

    public void start() throws IOException {
        server.start();
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
}
