package com.example.universe;

import java.util.HashMap;

public class Notification {
    private String type, eventName, eventId, eventTime;
    private HashMap<String, String> userArray;

    public Notification() {}

    public Notification(String type, String eventName, String eventId, String eventTime, HashMap<String, String> userArray) {
        this.type = type;
        this.eventName = eventName;
        this.eventId = eventId;
        this.eventTime = eventTime;
        this.userArray = userArray;
    }

    public HashMap<String, String> getUserArray() {
        return userArray;
    }

    public void setUserArray(HashMap<String, String> userArray) {
        this.userArray = userArray;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

