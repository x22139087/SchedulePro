package schedulePro.services;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServicePublisher {

    private static final String SERVICE_TYPE = "_http._tcp.local.";
    private static final int SERVICE_PORT = 8080;
    private static final String SERVICE_NAME = "MyService";

    public static void main(String[] args) {
        try {
            // Get the local host address
            InetAddress addr = InetAddress.getLocalHost();

            // Create a JmDNS instance
            JmDNS jmdns = JmDNS.create(addr);

            // Create a service info object with the service details
            ServiceInfo serviceInfo = ServiceInfo.create(SERVICE_TYPE, SERVICE_NAME, SERVICE_PORT, "description");

            // Register the service with JmDNS
            jmdns.registerService(serviceInfo);

            System.out.println("Service published on: " + addr.getHostAddress() + ":" + SERVICE_PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
