package com.example.universe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventInfo extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mref;
    private TextView eventTitle;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        eventID = getIntent().getStringExtra("Event_ID");
        mDatabase = FirebaseDatabase.getInstance();
        mref = mDatabase.getReference("Events");
        eventTitle = findViewById(R.id.createEvent_title);
        getdata();

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
        RSVP_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int status = (Integer) view.getTag();
                if(status == 1)
                {
                    RSVP_Button.setText("Cancel RSVP");
                    view.setTag(0);
                }
                else
                {
                    RSVP_Button.setText("RSVP");
                    view.setTag(1);
                }
            }
        });
    }

    private void getdata()
    {
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventsSnapshot : snapshot.getChildren()) {
                    String key = eventsSnapshot.getKey().toString();
                    if(key.equals(eventID)) {
                        Event event = eventsSnapshot.getValue(Event.class);
                        eventTitle.setText(event.getEventName());
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