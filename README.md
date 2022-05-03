# UniVerse

**UniVerse** is a social networking application designed to assist students and organizers alike in creating, managing, finding, and attending on-campus events. The app’s core feature is a personalized “feed” that will display upcoming events and activities relevant to each user. From the feed, users will be able to view details on each event and RSVP to events they wish to attend.

**UniVerse** is written entirely in Java, and its dependencies include FirebaseAuth, FirebaseStorage, FirebaseDatabase, and Glide.

[Heroicons](https://heroicons.com/) were used in addition to the base material icons provided in Android Studio.

---

**UniVerse** was created as a term project for an ABET accredited Fundamentals of Software Engineering course by:

Adam Emerson - [@AdamGEmerson](https://github.com/AdamGEmerson)

Josh Boudria - [@JoshGBoudria](https://github.com/JoshGBoudria)

Kevin Flores - [@kevin8360](https://github.com/kevin8630)

Vo Ho Anh Tuan - [@VoHoAnhTuan](https://github.com/VoHoAnhTuan)


## Folders

`UniVerse/` contains the apps codebase in its entireity.

`Documentation/` contains additional files and reference material, generated per the prerequisites of the course.  This includes the SRA, UMLs, and other files such as wireframes.


## Known Issues

Within the app there are several known issues that we were unable to address given the time constraints associated with a term project.  We hope that one day we will have the time to revisit the following issues:


- [ ] Links from schedule to Event Info page occasionally don’t load event info (needs further investigation)

- [ ] Account registration requires email verification, but UTA mail servers will block the verification email, meaning @mavs.uta.edu and @uta.edu emails will not work for registration.

- [ ] Fields on the Event Creation screen are not validated, which can cause unexpected behavior if the user leaves a field blank.

- [ ] Images for event cards dynamically loaded on the feed sometimes don’t load right away.  They will load without issue after navigating away from the feed and then returning.

- [ ] Keys for our database are set to be valid until 05/31/22.  Access after that will likely fail due to inability to load data from Firebase.

- [ ] Occasionally text will overflow in the event info description if you are the event organizer.  The addition of the “edit event” button causes the RSVP button to overlap the description field




