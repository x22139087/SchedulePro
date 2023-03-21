package schedulePro.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

public class ServiceDiscoveryGUI extends JFrame {
    private final JPanel contentPane;
    private final JTable serviceTable;
    private final DefaultTableModel serviceTableModel;
    private final JButton publishButton;
    private final JButton discoverButton;

    private JmDNS jmdns;
    private final List<ServiceInfo> discoveredServices = new ArrayList<>();

    public ServiceDiscoveryGUI() {
        setTitle("Service Discovery");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.NORTH);

        publishButton = new JButton("Publish Services");
        buttonPanel.add(publishButton);

        discoverButton = new JButton("Discover Services");
        buttonPanel.add(discoverButton);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        serviceTableModel = new DefaultTableModel(new Object[]{"Name", "Type", "Host", "Port"}, 0);
        serviceTable = new JTable(serviceTableModel);
        scrollPane.setViewportView(serviceTable);

        publishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ServiceInfo userInfo = ServiceInfo.create("_user._tcp.local.", "user_service", 8080, "User service");
                    jmdns.registerService(userInfo);

                    ServiceInfo calendarInfo = ServiceInfo.create("_calendar._tcp.local.", "calendar_service", 8181, "Calendar service");
                    jmdns.registerService(calendarInfo);

                    ServiceInfo meetingInfo = ServiceInfo.create("_meeting._tcp.local.", "meeting_service", 8282, "Meeting service");
                    jmdns.registerService(meetingInfo);

                    JOptionPane.showMessageDialog(contentPane, "Services published successfully.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        discoverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ServiceListener serviceListener = new ServiceListener() {
                    public void serviceAdded(ServiceEvent event) {
                        System.out.println("Service added: " + event.getInfo().getName());
                        discoveredServices.add(event.getInfo());
                        updateServiceTable();
                    }

                    public void serviceRemoved(ServiceEvent event) {
                        System.out.println("Service removed: " + event.getInfo().getName());
                        discoveredServices.remove(event.getInfo());
                        updateServiceTable();
                    }

                    public void serviceResolved(ServiceEvent event) {
                        System.out.println("Service resolved: " + event.getInfo().getName());
                        discoveredServices.remove(event.getInfo());
                        discoveredServices.add(event.getInfo());
                        updateServiceTable();
                    }
                };

                jmdns.addServiceListener("_user._tcp.local.", serviceListener);
                jmdns.addServiceListener("_calendar._tcp.local.", serviceListener);
                jmdns.addServiceListener("_meeting._tcp.local.", serviceListener);

                JOptionPane.showMessageDialog(contentPane, "Discovering services...");
            }
        });

        try {
            jmdns = JmDNS.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateServiceTable() {
        serviceTableModel.setRowCount(0);

        for (ServiceInfo service : discoveredServices) {
            String name = service.getName();
            String type = service.getType();
            String host = service.getHostAddress();
            int port = service.getPort();
            serviceTableModel.addRow(new Object[]{name, type, host, port});
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServiceDiscoveryGUI frame = new ServiceDiscoveryGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}