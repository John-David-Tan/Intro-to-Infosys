package com.example.loginpage.User;

public class Timeslot {
    private boolean isSelected, isBooked;
    private String date, space;
    private int hour, minute;

    public Timeslot(boolean isBooked, int hour, int minute, String date) {
        this.isSelected = false;
        this.isBooked = isBooked;
        this.hour = hour;
        this.minute = minute;
        this.date = date;
    }

    public String getTimeLabel() {
        if (minute == 0){
            return String.format("%02d:%02d", hour, minute);
        } else {
            return String.format(":%02d", minute);
        }
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }

    public String getNextTimeLabel() {
        if (minute == 0){
            return String.format("%02d:%02d", hour, 30);
        } else {
            return String.format("%02d:%02d", hour+1, 0);
        }
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
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

    public String getDate() {return date;}
}
