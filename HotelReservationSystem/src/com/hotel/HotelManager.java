package com.hotel;

import java.util.*;
import javax.swing.*;

public class HotelManager {
    private HotelInfo hotelInfo;
    private java.util.Timer timer;
    private int occupiedRooms;
    private double totalRevenue;

    public HotelManager(HotelInfo hotelInfo) {
        this.hotelInfo = hotelInfo;
        this.timer = new java.util.Timer();
        this.occupiedRooms = 0;
        this.totalRevenue = 0;
        scheduleReportGeneration();
    }

    private void scheduleReportGeneration() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);

        Date time = calendar.getTime();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateReport();
            }
        }, time, 24 * 1000); // 24 saniyelik periyot
    }

    private void updateReport() {
        occupiedRooms = 0;
        totalRevenue = 0;

        for (Room room : hotelInfo.getRooms()) {
            if (room.isReserved()) {
                occupiedRooms++;
                totalRevenue += room.getPrice();
            }
        }
    }

    public int getOccupiedRooms() {
        return occupiedRooms;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}
