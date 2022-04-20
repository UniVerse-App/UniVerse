package com.example.universe;

public class Event {

    private String eventName;
    private String location;
    private String time;
    private long timestamp;

    public Event() {

    }

    public Event(String eventName, String location, String time, long timestamp) {
        this.location = location;
        this.eventName = eventName;
        this.time = time;
        this.timestamp = timestamp;
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
}
