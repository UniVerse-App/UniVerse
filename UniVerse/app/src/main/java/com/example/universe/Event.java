package com.example.universe;

import java.lang.reflect.Array;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Event {

    private String eventName, organizerName, location, description, photo, organizerID, eventInterest;
    private Integer seats;
    private long timestamp;
    private Calendar c;
    private ArrayList<String> users;


    public Event() {

    }

    public Event(String eventName, String organizerName,String location, long timestamp, String photo, String description, ArrayList<String> users, Integer seats, String organizerID, String interest) {
        this.location = location;
        this.seats = seats;
        this.users = users;
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.timestamp = timestamp;
        this.photo = photo;
        this.description = description;
        this.organizerID = organizerID;
        this.eventInterest = interest;

    }

    public String getEventName() {
        return eventName;
    }

    public void setName(String name) {
        this.eventName = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<String> getUsers() { return users; }

    public void setUsers(ArrayList<String> users) { this.users = users; }

    public Integer getSeats() { return seats; }

    public void setSeats(Integer seats) { this.seats = seats; }

    public String getOrganizerName() { return organizerName; }

    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }

    public String getEventInterest() { return eventInterest;}

    public void setEventInterest(String interest) { this.eventInterest = interest;}

    public String getTimeString() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(this.timestamp);
        String timeString = String.format(Locale.getDefault(), "%02d:%02d", c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
        return timeString;
    }

    public String getDateString() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(this.timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        String dateString = simpleDateFormat.format(c.getTime()).trim();
        return dateString;
    }

    public int getDayOfMonth() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(this.timestamp);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public String getMonthAbr() {
        String[] mons = new DateFormatSymbols().getShortMonths();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(this.timestamp);
        String monthAbr = mons[c.get(Calendar.MONTH)].toUpperCase();
        return monthAbr;
    }
}
