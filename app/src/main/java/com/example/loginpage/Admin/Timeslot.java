package com.example.loginpage.Admin;

import androidx.annotation.NonNull;

public class Timeslot {
    private boolean isSelected, isBooked;
    private String date;
    private int hour, minute;

    public Timeslot(boolean isBooked, int hour, int minute) {
        this.isSelected = false;
        this.isBooked = isBooked;
        this.hour = hour;
        this.minute = minute;
    }

    public String getTimeLabel() {
        if (minute == 0){
            return String.format("%02d:%02d", hour, minute);
        } else {
            return String.format(":%02d", minute);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}
