package com.example.universe;

import java.util.ArrayList;

public class Events {
    //Attributes
    public StringBuffer eventName;
    public StringBuffer location;
    public boolean isPublic;
    public ArrayList<String> attendees;
    public StringBuffer eventPicture;
    public StringBuffer organizer;
    public StringBuffer summary;
    public int eventID;
    public int interests[];
    public StringBuffer date;
    public StringBuffer time;
    public StringBuffer eventDescription;

    //constructor

    public Events() {
    }

    public Events (StringBuffer eventName, StringBuffer location, int attendees[],
                   StringBuffer eventPicture, StringBuffer organizer, StringBuffer summary,
                   int eventID, boolean isPublic, int interests[],
                   StringBuffer date, StringBuffer time, StringBuffer eventDescription) {
        this.eventName = eventName;
        this.location  = location;
        this.attendees = new ArrayList<String>();
        this.eventPicture = eventPicture;
        this.organizer = organizer;
        this.summary   = summary;
        this.eventID   = eventID;
        this.isPublic  = isPublic;
        this.interests = new int[20];
        this.date      = date;
        this.time      = time;
        this.eventDescription = eventDescription;
    }

    //Setters and getters


    public StringBuffer getEventName() {
        return eventName;
    }

    public void setEventName(StringBuffer eventName) {
        this.eventName = eventName;
    }

    public StringBuffer getLocation() {
        return location;
    }

    public void setLocation(StringBuffer location) {
        this.location = location;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public ArrayList<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<String> attendees) {
        this.attendees = attendees;
    }

    public StringBuffer getEventPicture() {
        return eventPicture;
    }

    public void setEventPicture(StringBuffer eventPicture) {
        this.eventPicture = eventPicture;
    }

    public StringBuffer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(StringBuffer organizer) {
        this.organizer = organizer;
    }

    public StringBuffer getSummary() {
        return summary;
    }

    public void setSummary(StringBuffer summary) {
        this.summary = summary;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int[] getInterests() {
        return interests;
    }

    public void setInterests(int[] interests) {
        this.interests = interests;
    }

    public StringBuffer getDate() {
        return date;
    }

    public void setDate(StringBuffer date) {
        this.date = date;
    }

    public StringBuffer getTime() {
        return time;
    }

    public void setTime(StringBuffer time) {
        this.time = time;
    }

    public StringBuffer getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(StringBuffer eventDescription) {
        this.eventDescription = eventDescription;
    }

    //Methods
    //This method to return info
    public void eventInfo() {

    }

    //This method to create a new event
    public void createEvents() {
        //Events events = new Events(/*the information the organizer entered*/);
    }

    //This method to edit the event
    public void editEvents(){
    }
}
