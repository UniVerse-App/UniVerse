package com.example.universe;

import java.util.HashMap;

public class User {

    public String firstName, lastName, email, major, profilePicture;
    public Integer gradYear;
    public Boolean isOrganizer;
    public Integer[] interests;
    public HashMap<String,String> eventsAttending;

    // Integer to determine where to resume onboarding if user logs out before complete.
    public Integer onboardingStep;

    public User() {

    }

    public User(String firstName, String lastName, String email, Integer gradYear, String major, Integer onboardingStep, HashMap<String, String> eventsAttending, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.onboardingStep = onboardingStep;
        this.eventsAttending = eventsAttending;
        this.gradYear = gradYear;
        this.major = major;
        this.profilePicture = profilePicture;
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

    public Integer getGradYear() {
        return gradYear;
    }

    public void setGradYear(Integer gradYear) {
        this.gradYear = gradYear;
    }

    public void setEventsAttending(HashMap<String, String> eventsAttending) {
        this.eventsAttending = eventsAttending;
    }

    public String getMajor() {
        return major;
    }

    public void setInterests(Integer[] interests) {
        this.interests = interests;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
