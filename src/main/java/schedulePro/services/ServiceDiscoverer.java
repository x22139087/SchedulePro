package schedulePro.services;


import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServiceDiscoverer {

    private static final String SERVICE_TYPE = "_http._tcp.local.";
    private static final String SERVICE_NAME = "MyService";

    public static void main(String[] args) {
        try {
            // Get the local host address
            InetAddress addr = InetAddress.getLocalHost();

            // Create a JmDNS instance
            JmDNS jmdns = JmDNS.create(addr);

            // Create a service listener to listen for service events
            ServiceListener serviceListener = new ServiceListener() {
                @Override
                public void serviceAdded(ServiceEvent event) {
                    System.out.println("Service added: " + event.getInfo());
                }

                @Override
                public void serviceRemoved(ServiceEvent event) {
                    System.out.println("Service removed: " + event.getInfo());
                }

                @Override
                public void serviceResolved(ServiceEvent event) {
                    System.out.println("Service resolved: " + event.getInfo());
                }
            };

            // Add the service listener to JmDNS
            jmdns.addServiceListener(SERVICE_TYPE, serviceListener);

            // Wait for service events
            System.out.println("Waiting for service events...");
            Thread.sleep(Long.MAX_VALUE);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
