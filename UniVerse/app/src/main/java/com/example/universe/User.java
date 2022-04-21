package com.example.universe;

public class User {

    public String firstName, lastName, email, major;
    public Boolean isOrganizer;
    public Integer[] interests;
    public Integer[] events;
<<<<<<< HEAD
    
    public Integer onboardingStep;
=======
>>>>>>> dd8eca44802012b8d68db22c5e3e11af62f014a2

    // Integer to determine where to resume onboarding if user logs out before complete.
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
