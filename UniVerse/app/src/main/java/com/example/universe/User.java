package com.example.universe;

public class User {

    public String firstName, lastName, email, major;
    public Boolean isOrganizer;
    public Integer[] interests;
    public Integer[] events;
    
    public Integer onboardingStep;

    public User() {

    }

    public User(String firstName, String lastName, String email, Integer onboardingStep) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.onboardingStep = onboardingStep;
    }

}
