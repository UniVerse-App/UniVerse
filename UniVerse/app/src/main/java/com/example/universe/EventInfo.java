package com.example.universe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EventInfo extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference eventsTable, usersTable;
    private TextView eventTitle, organizerName, eventLocation, eventDescription, eventDate, eventTime, editButton, eventSeats;
    private String eventID, userId;
    private ImageView eventPhoto;
    private Context thisContext;
    private Button RSVP_Button;
    private Boolean userAttending = false;

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
                EventInfo.this.finish();
            }
        });

        Query query = userRecord.child("eventsAttending").orderByValue().equalTo(eventID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot event : snapshot.getChildren()) {
                    if (event.getValue().equals(eventID)) {
                        // User has already RSVPd
                        userAttending = true;
                        updateRSVPButton();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RSVP_Button = findViewById(R.id.RSVP_button);
        RSVP_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int status = (Integer) view.getTag();
                if(status == 1)
                {
                    // Append user id to Event record
                    DatabaseReference eventUsersRef = eventsTable.child(eventID).child("eventAttendees");
                    DatabaseReference pushUserRef = eventUsersRef.push();
                    pushUserRef.setValue(userId);

                    // Append event id to User record
                    DatabaseReference userRef = userRecord.child("eventsAttending");
                    DatabaseReference pushEventRef = userRef.push();
                    pushEventRef.setValue(eventID);

                    userAttending = true;

                } else {

                    // Remove user id from Event record
                    DatabaseReference eventRef = eventsTable.child(eventID).child("eventAttendees");
                    Query eventQuery = eventRef.orderByValue().equalTo(userId);
                    eventQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot user: snapshot.getChildren()) {
                                user.getRef().removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    // Remove event id from User record
                    DatabaseReference userRef = userRecord.child("eventsAttending");
                    Query userQuery = userRef.orderByValue().equalTo(eventID);
                    userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot event: snapshot.getChildren()) {
                                event.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    userAttending = false;
                }
            }
        });
        updateRSVPButton();
    }

    private void updateRSVPButton() {
        if( userAttending ){
            RSVP_Button.setTag(0);
            RSVP_Button.setEnabled(true);
            RSVP_Button.setText("Cancel RSVP");
            RSVP_Button.setBackgroundColor(getResources().getColor(R.color.accentColor800));
        }
        else if (eventSeats.getText() == "None") {
            RSVP_Button.setText("EVENT FULL");
            RSVP_Button.setEnabled(false);
            RSVP_Button.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.material_grey_300));
        } else {
            RSVP_Button.setTag(1);
            RSVP_Button.setText("RSVP");
            RSVP_Button.setEnabled(true);
            RSVP_Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
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
                        eventDate.setText(event.getDateString() + " - " + event.getTimeString());
                        eventSeats.setText(event.getRemainingSeats());

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

                        // Check RSVP Button
                        updateRSVPButton();
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