package com.example.event;

import java.util.ArrayList;

public class Events {
    //Attributes
    public StringBuffer eventName;
    public StringBuffer location;
    public ArrayList<String> attendees;
    public StringBuffer eventPicture;
    public StringBuffer organizer;
    public int eventID;
    public boolean isPublic;
    public int interests[];
    //StringBuffer for date and time temporarily, until confirm DATETIME lib
    public StringBuffer date;
    public StringBuffer eventDescription;

    //constructor
    public Events (StringBuffer eventName, StringBuffer location, int attendees[],
                   StringBuffer eventPicture, StringBuffer organizer, int eventID, boolean isPublic, int interests[],
                   StringBuffer date, StringBuffer eventDescription) {
        this.eventName = eventName;
        this.location  = location;
        this.attendees = new ArrayList<String>();
        this.eventPicture = eventPicture;
        this.organizer = organizer;
        this.eventID   = eventID;
        this.isPublic  = isPublic;
        this.interests = new int[20];
        this.date      = date;
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