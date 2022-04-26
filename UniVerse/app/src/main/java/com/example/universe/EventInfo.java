package com.example.universe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventInfo extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference eventsTable, usersTable;
    private TextView eventTitle, organizerName, eventLocation, eventDescription, eventDate, eventTime, editButton, eventSeats;
    private String eventID, userId;
    private ImageView eventPhoto;
    private Context thisContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
    }

    @Override
    protected void onStart() {
        super.onStart();

        eventID = getIntent().getStringExtra("Event_ID");
        mDatabase = FirebaseDatabase.getInstance();
        eventsTable = mDatabase.getReference("Events");
        usersTable = mDatabase.getReference("Users");

        // Get logged in user record.
        userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference userRecord = usersTable.child(userId);

        eventTitle = findViewById(R.id.createEvent_title);
        organizerName = findViewById(R.id.event_organizer);
        eventLocation = findViewById(R.id.event_location);
        eventDescription = findViewById(R.id.event_description);
        eventDate = findViewById(R.id.DateEvent);
        eventTime = findViewById(R.id.TimeEvent);
        eventPhoto = findViewById(R.id.eventPhoto);
        eventSeats = findViewById(R.id.event_seats);

        editButton = findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventInfo.this, EditEvent.class);
                intent.putExtra("Event_ID", eventID);
                startActivity(intent);
            }
        });

        thisContext = getBaseContext();


        // Load event data into view
        getEventData();

        ImageView backButton = findViewById(R.id.backEventInfo);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventInfo.this, Feed.class));
            }
        });

        Button RSVP_Button = findViewById(R.id.RSVP_button);
        RSVP_Button.setTag(1);
        RSVP_Button.setText("RSVP");
        Query query = userRecord.child("eventsAttending").orderByValue().equalTo(eventID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot event : snapshot.getChildren()) {
                    if (event.getValue().equals(eventID)) {
                        RSVP_Button.setTag(0);
                        RSVP_Button.setText("Cancel RSVP");
                        RSVP_Button.setBackgroundColor(getResources().getColor(R.color.accentColor800));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RSVP_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int status = (Integer) view.getTag();
                if(status == 1)
                {
                    RSVP_Button.setText("Cancel RSVP");
                    RSVP_Button.setBackgroundColor(getResources().getColor(R.color.accentColor800));
                    view.setTag(0);

                    // Append user id to Event record
                    DatabaseReference eventUsersRef = eventsTable.child(eventID).child("eventAttendees");
                    DatabaseReference pushUserRef = eventUsersRef.push();
                    pushUserRef.setValue(userId);

                    // Append event id to User record
                    DatabaseReference userRef = userRecord.child("eventsAttending");
                    DatabaseReference pushEventRef = userRef.push();
                    pushEventRef.setValue(eventID);

                }
                else
                {
                    RSVP_Button.setText("RSVP");
                    RSVP_Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    view.setTag(1);

                    // Remove user id from Event record
                    DatabaseReference eventUsersRef = eventsTable.child(eventID).child("eventAttendees");
                    DatabaseReference eRef = eventUsersRef.orderByValue().equalTo(userId).getRef();
                    eRef.removeValue();

                    // Remove event id from User record
                    DatabaseReference userRef = userRecord.child("eventsAttending");
                    DatabaseReference uRef = userRef.orderByValue().equalTo(eventID).getRef();
                    uRef.removeValue();

                }
            }
        });
    }

    private void getEventData()
    {
        eventsTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventsSnapshot : snapshot.getChildren()) {
                    String key = eventsSnapshot.getKey().toString();
                    if(key.equals(eventID)) {
                        Event event = eventsSnapshot.getValue(Event.class);
                        eventTitle.setText(event.getEventName());
                        organizerName.setText(event.getOrganizerName());
                        eventLocation.setText(event.getLocation());
                        eventDescription.setText(event.getDescription());
                        eventDate.setText(event.getDateString());
                        eventTime.setText(event.getTimeString());

                        //TODO: FIX SEATS REMAINING CALCULATION
                        if (event.getSeats() == 0) {
                            eventSeats.setText("Unlimited");
                        } else {
                            Integer numAttendees = 0;
                            if (event.getEventAttendees() != null) {
                                numAttendees = event.getEventAttendees().size();
                                Integer remainingSeats = event.getSeats() - numAttendees;
                                String seatsString = (remainingSeats.toString() + "more");
                                eventSeats.setText(seatsString);
                            }
                        eventSeats.setText(event.getSeats());
                        }
                        // Enable edit button
                        if (userId.equals(event.getOrganizerID())) {
                            editButton.setVisibility(View.VISIBLE);
                        }

                        // Insert photo
                        StorageReference reference;
                        reference = FirebaseStorage.getInstance().getReference().child("eventPictures/");
                        reference = reference.child(event.getPhoto());
                        Task<Uri> imageUrl = reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Success
                                Glide.with(thisContext).load(uri.toString())
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(eventPhoto);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Fail to get image url
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EventInfo.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}