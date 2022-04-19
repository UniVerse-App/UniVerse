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
