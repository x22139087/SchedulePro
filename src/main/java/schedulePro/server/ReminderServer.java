package schedulePro.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import schedulePro.services.ReminderServiceImpl;

import java.io.IOException;

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
