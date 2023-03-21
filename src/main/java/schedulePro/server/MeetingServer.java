package schedulePro.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import schedulePro.meeting.MeetingServiceGrpc;
import schedulePro.services.MeetingServiceImpl;
import schedulePro.utils.InMemoryDatabase;

import java.io.IOException;

public class MeetingServer {
    private final int port;
    private final Server server;

    public MeetingServer(int port, MeetingServiceGrpc.MeetingServiceImplBase service) {
        this.port = port;
        server = ServerBuilder.forPort(port)
                .addService(service)
                .build();
    }

    public void start()  {
        try {
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            MeetingServer.this.stop();
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

    public static void main(String[] args) throws IOException, InterruptedException {
        InMemoryDatabase database = new InMemoryDatabase();
        MeetingServiceImpl service = new MeetingServiceImpl(database);
        MeetingServer server = new MeetingServer(8080, service);

        server.start();
        server.blockUntilShutdown();
    }
}
