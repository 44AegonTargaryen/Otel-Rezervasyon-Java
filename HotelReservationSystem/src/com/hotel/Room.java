package com.hotel;

public class Room {
    private int roomNumber;
    private RoomType roomType;
    private double price;
    private boolean isReserved;

    public Room(int roomNumber, RoomType roomType, double price, boolean isReserved) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.isReserved = isReserved;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void reserve() {
        isReserved = true;
    }

    public void cancelReservation() {
        isReserved = false;
    }

    public void checkOut() {
        isReserved = false;
    }
}
