package schedulePro.client;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;

public class JmDNSManager implements ServiceListener {
    private JmDNS jmdns;

    public JmDNSManager() {
        try {
            jmdns = JmDNS.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serviceAdded(ServiceEvent event) {
        // A new service has been added
        System.out.println("Service added: " + event.getName());
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        // A service has been removed
        System.out.println("Service removed: " + event.getName());
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        // A service has been resolved
        System.out.println("Service resolved: " + event.getName() + " " + event.getInfo());
    }

    public void registerService(int port) {
        try {
            // Register a service
            ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "my_service", port, "path=index.html");
            jmdns.registerService(serviceInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void discoverServices() {         // Discover services
        jmdns.addServiceListener("_http._tcp.local.", this);
    }
}
