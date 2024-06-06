package com.hotel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationScreen extends JFrame {
    private HotelInfo hotelInfo;
    private DefaultListModel<String> reservationListModel;
    private JList<String> reservationList;
    private MainScreen mainScreen;

    public ReservationScreen(HotelInfo hotelInfo, MainScreen mainScreen) {
        this.hotelInfo = hotelInfo;
        this.mainScreen = mainScreen;

        setTitle("Hotel Reservation System - Reservations");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel ve bileşenleri oluşturma
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 63, 65));

        reservationListModel = new DefaultListModel<>();
        reservationList = new JList<>(reservationListModel);
        reservationList.setBackground(new Color(60, 63, 65));
        reservationList.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(reservationList);

        JButton cancelButton = new JButton("Cancel Reservation");
        cancelButton.setBackground(new Color(255, 69, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> cancelReservation());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(cancelButton, BorderLayout.SOUTH);

        add(panel);

        loadReservations();
    }

    private void loadReservations() {
        reservationListModel.clear();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT room_number, room_type, customer_name FROM rooms WHERE is_reserved = 1";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    String roomType = resultSet.getString("room_type");
                    String customerName = resultSet.getString("customer_name");
                    reservationListModel.addElement("Room " + roomNumber + " (" + roomType + ") - " + customerName);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelReservation() {
        String selectedValue = reservationList.getSelectedValue();
        if (selectedValue == null) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to cancel.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int roomNumber = Integer.parseInt(selectedValue.split(" ")[1]);

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "UPDATE rooms SET is_reserved = 0, customer_name = NULL WHERE room_number = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, roomNumber);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Reservation for Room " + roomNumber + " has been canceled.");
                    loadReservations();
                    mainScreen.refreshRooms(); // Ana ekranı güncelle
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to cancel the reservation.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
