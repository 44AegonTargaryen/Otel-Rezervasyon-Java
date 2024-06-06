package com.hotel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookingScreen extends JFrame {
    private JTextField customerNameField;
    private JTextField roomNumberField;
    private HotelInfo hotelInfo;
    private Room room;

    public BookingScreen(HotelInfo hotelInfo, Room room) {
        this.hotelInfo = hotelInfo;
        this.room = room;

        setTitle("Hotel Reservation System - Booking");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel ve bileşenleri oluşturma
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(60, 63, 65));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        // Müşteri adı
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        JLabel customerNameLabel = new JLabel("Customer Name:");
        customerNameLabel.setForeground(Color.WHITE);
        panel.add(customerNameLabel, constraints);

        constraints.gridx = 1;
        customerNameField = new JTextField(15);
        panel.add(customerNameField, constraints);

        // Oda numarası
        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel roomNumberLabel = new JLabel("Room Number:");
        roomNumberLabel.setForeground(Color.WHITE);
        panel.add(roomNumberLabel, constraints);

        constraints.gridx = 1;
        roomNumberField = new JTextField(String.valueOf(room.getRoomNumber()));
        roomNumberField.setEditable(false);
        panel.add(roomNumberField, constraints);

        // Rezervasyon butonu
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        JButton bookButton = new JButton("Book");
        bookButton.setBackground(new Color(30, 144, 255));
        bookButton.setForeground(Color.WHITE);
        panel.add(bookButton, constraints);

        bookButton.addActionListener(e -> {
            String customerName = customerNameField.getText();
            if (customerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Customer name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String sql = "UPDATE rooms SET is_reserved = 1, customer_name = ? WHERE room_number = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, customerName);
                    statement.setInt(2, room.getRoomNumber());
                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this, "Room " + room.getRoomNumber() + " booked for " + customerName);
                        room.reserve();
                        new MainScreen(hotelInfo).setVisible(true); // Ana ekrana geri dön
                        dispose(); // BookingScreen ekranını kapat
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to book the room.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
    }
}
