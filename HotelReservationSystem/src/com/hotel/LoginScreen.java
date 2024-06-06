package com.hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen() {
        setTitle("Hotel Reservation System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel oluşturma
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(60, 63, 65));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        // Kullanıcı adı
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        panel.add(usernameLabel, constraints);

        constraints.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, constraints);

        // Şifre
        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        panel.add(passwordLabel, constraints);

        constraints.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, constraints);

        // Login butonu
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(30, 144, 255));
        loginButton.setForeground(Color.WHITE);
        panel.add(loginButton, constraints);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kullanıcı adı ve şifre kontrolü yapılmaksızın giriş
                // Otel bilgilerini veritabanından çek
                HotelInfo hotelInfo = new HotelInfo();

                // Ana ekrana geçiş
                new MainScreen(hotelInfo).setVisible(true);
                dispose();
            }
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}
