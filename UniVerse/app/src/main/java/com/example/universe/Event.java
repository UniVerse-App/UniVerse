package com.example.universe;

public class Event {
    private String eventName;
    private String description;
    private String photo;

    public Event(String eventName, String description, String photo) {
        this.eventName = eventName;
        this.description = description;
        this.photo = photo;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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
