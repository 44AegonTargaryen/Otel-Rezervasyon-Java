package com.hotel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class HotelInfo {
    private String name;
    private String location;
    private int roomCount;
    private double dailyRate;
    private ArrayList<Room> rooms;

    public HotelInfo() {
        loadHotelInfo();
        loadRooms();
    }

    private void loadHotelInfo() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT name, location, room_count, daily_rate FROM hotel_info";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    name = resultSet.getString("name");
                    location = resultSet.getString("location");
                    roomCount = resultSet.getInt("room_count");
                    dailyRate = resultSet.getDouble("daily_rate");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void loadRooms() {
        rooms = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT room_number, room_type, price, is_reserved FROM rooms";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    RoomType roomType = RoomType.valueOf(resultSet.getString("room_type")); // RoomType olarak değiştirdik
                    double price = resultSet.getDouble("price");
                    boolean isReserved = resultSet.getBoolean("is_reserved");
                    rooms.add(new Room(roomNumber, roomType, price, isReserved));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
}
