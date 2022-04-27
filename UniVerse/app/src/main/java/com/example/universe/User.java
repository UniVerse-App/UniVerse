package com.example.universe;

import com.google.firebase.database.core.EventTarget;

import java.util.HashMap;

public class User {

    public String firstName, lastName, email, major;
    public Boolean isOrganizer;
    public Integer[] interests;
    public HashMap<String,String> eventsAttending;

    // Integer to determine where to resume onboarding if user logs out before complete.
    public Integer onboardingStep;

    public User() {

    }

    public User(String firstName, String lastName, String email, Integer onboardingStep, HashMap<String, String> eventsAttending) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.onboardingStep = onboardingStep;
        this.eventsAttending = eventsAttending;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getOnboardingStep() {
        return onboardingStep;
    }

    public void setOnboardingStep(Integer onboardingStep) {
        this.onboardingStep = onboardingStep;
    }

    public HashMap<String, String> getEventsAttending() { return eventsAttending;}

    public void setEventsAttending(HashMap<String, String> eventsAttending) {
        this.eventsAttending = eventsAttending;
    }
}
