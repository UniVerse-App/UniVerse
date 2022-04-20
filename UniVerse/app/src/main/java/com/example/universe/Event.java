package com.example.universe;

public class Event {

    private String eventName, location, time, description, photo;
    private long timestamp;

    public Event() {

    }

    public Event(String eventName, String location, String time, long timestamp, String photo, String description) {
        this.location = location;
        this.eventName = eventName;
        this.time = time;
        this.timestamp = timestamp;
        this.photo = photo;
        this.description = description;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
}
