package com.hotel;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame {
    private HotelInfo hotelInfo;
    private HotelManager hotelManager;
    private JPanel roomsPanel;

    public MainScreen(HotelInfo hotelInfo) {
        this.hotelInfo = hotelInfo;
        this.hotelManager = new HotelManager(hotelInfo);

        setTitle("Hotel Reservation System - Main Screen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel ve bileşenleri oluşturma
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 63, 65));

        JLabel welcomeLabel = new JLabel("Welcome to " + hotelInfo.getName() + " in " + hotelInfo.getLocation(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        panel.add(welcomeLabel, BorderLayout.NORTH);

        // Otel bilgileri
        JTextArea hotelDetails = new JTextArea();
        hotelDetails.setText("Location: " + hotelInfo.getLocation() +
                "\nRoom Count: " + hotelInfo.getRoomCount() +
                "\nDaily Rate: $" + hotelInfo.getDailyRate());
        hotelDetails.setEditable(false);
        hotelDetails.setBackground(new Color(60, 63, 65));
        hotelDetails.setForeground(Color.WHITE);
        panel.add(hotelDetails, BorderLayout.CENTER);

        // Oda bilgileri paneli
        roomsPanel = new JPanel();
        roomsPanel.setLayout(new GridLayout(5, 2, 10, 10));
        roomsPanel.setBackground(new Color(60, 63, 65));
        refreshRooms();

        panel.add(new JScrollPane(roomsPanel), BorderLayout.CENTER);

        // "View Reservations" butonu ekleme
        JButton viewReservationsButton = new JButton("View Reservations");
        viewReservationsButton.setBackground(new Color(30, 144, 255));
        viewReservationsButton.setForeground(Color.WHITE);
        viewReservationsButton.addActionListener(e -> new ReservationScreen(hotelInfo, this).setVisible(true));
        panel.add(viewReservationsButton, BorderLayout.SOUTH);

        // "View Report" butonu ekleme
        JButton viewReportButton = new JButton("View Report");
        viewReportButton.setBackground(new Color(30, 144, 255));
        viewReportButton.setForeground(Color.WHITE);
        viewReportButton.addActionListener(e -> showReport());
        panel.add(viewReportButton, BorderLayout.NORTH);

        add(panel);
    }

    public void refreshRooms() {
        roomsPanel.removeAll();
        hotelInfo.loadRooms(); // Odaları yeniden yükle
        for (Room room : hotelInfo.getRooms()) {
            JButton roomButton = new JButton("Room " + room.getRoomNumber() + "\n" + room.getRoomType() + "\n$" + room.getPrice());
            roomButton.setBackground(room.isReserved() ? new Color(255, 69, 0) : new Color(30, 144, 255));
            roomButton.setForeground(Color.WHITE);
            roomButton.setPreferredSize(new Dimension(100, 100));
            roomButton.addActionListener(e -> {
                if (!room.isReserved()) {
                    int confirm = JOptionPane.showConfirmDialog(MainScreen.this, "Do you want to reserve Room " + room.getRoomNumber() + "?", "Confirm Reservation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        room.reserve();
                        new BookingScreen(hotelInfo, room).setVisible(true);
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(MainScreen.this, "Room " + room.getRoomNumber() + " is already reserved.", "Room Reserved", JOptionPane.WARNING_MESSAGE);
                }
            });
            roomsPanel.add(roomButton);
        }
        roomsPanel.revalidate();
        roomsPanel.repaint();
    }

    private void showReport() {
        int occupiedRooms = hotelManager.getOccupiedRooms();
        double totalRevenue = hotelManager.getTotalRevenue();

        StringBuilder report = new StringBuilder();
        report.append("Report\n");
        report.append("======\n\n");
        report.append("Occupied Rooms: ").append(occupiedRooms).append("\n");
        report.append("Total Revenue: $").append(totalRevenue).append("\n");

        JOptionPane.showMessageDialog(this, report.toString(), "Room Occupancy Report", JOptionPane.INFORMATION_MESSAGE);
    }
}
