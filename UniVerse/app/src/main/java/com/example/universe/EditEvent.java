package com.example.universe;

import static com.example.universe.PushNotification.CHANNEL_10_ID;
import static com.example.universe.PushNotification.CHANNEL_11_ID;
import static com.example.universe.PushNotification.CHANNEL_12_ID;
import static com.example.universe.PushNotification.CHANNEL_13_ID;
import static com.example.universe.PushNotification.CHANNEL_1_ID;
import static com.example.universe.PushNotification.CHANNEL_2_ID;
import static com.example.universe.PushNotification.CHANNEL_3_ID;
import static com.example.universe.PushNotification.CHANNEL_4_ID;
import static com.example.universe.PushNotification.CHANNEL_5_ID;
import static com.example.universe.PushNotification.CHANNEL_6_ID;
import static com.example.universe.PushNotification.CHANNEL_7_ID;
import static com.example.universe.PushNotification.CHANNEL_8_ID;
import static com.example.universe.PushNotification.CHANNEL_9_ID;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
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
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class EditEvent extends AppCompatActivity {

    private TextView eventName, eventLocation, organizerName, eventDescription;
    private NumberPicker numSeats;
    private Boolean newPicture = false; // For detecting user changed photo
    private DatePickerDialog datePickerDialog;
    private Button dateButton, timeButton, createButton, cancelButton;
    private ImageView backButton;
    private FloatingActionButton eventPhotoButton;
    private ShapeableImageView eventPhotoContainer;
    private long timestamp;
    private StorageReference mStorageRef;
    private DatabaseReference eventsTable;
    private FirebaseDatabase database;
    private Calendar cal = Calendar.getInstance();
    private NotificationManagerCompat notificationManager;
    private Spinner interestSpinner;
    private String eventID;
    private Event thisEvent;

    //hour and min variables
    int hour, min;

    //Event Photo selection variables
    private Integer PICK_IMAGE = 1;
    private Uri selectedImage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        initDatePicker();

        eventName = findViewById(R.id.event_name);
        eventLocation = findViewById((R.id.event_location));
        dateButton = findViewById(R.id.datePickerButton);
        timeButton = findViewById(R.id.hourPickerButton);
        organizerName = findViewById(R.id.event_organizer);
        eventDescription = findViewById(R.id.event_description);

        interestSpinner = findViewById(R.id.event_interests);

        numSeats = findViewById(R.id.event_seats);
        numSeats.setMaxValue(999);
        numSeats.setMinValue(0);

        createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent();
                sendOnChannels();
            }
        });

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelEvent();
                // sendOnChannels();
            }
        });


        backButton = findViewById(R.id.ic_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToEventInfo();
            }
        });

        eventPhotoButton = findViewById(R.id.selectEventPic);

        eventPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEventPhotoPicker();
            }
        });

        //Firebase
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Notification
        notificationManager = NotificationManagerCompat.from(this);

        // Get Event Record
        eventID = getIntent().getStringExtra("Event_ID");
        database = FirebaseDatabase.getInstance();
        eventsTable = database.getReference("Events");
        Query eventRecord = eventsTable.orderByKey().equalTo(eventID);
        eventRecord.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                thisEvent = snapshot.getValue(Event.class);
                cal.setTimeInMillis(thisEvent.getTimestamp());
                eventName.setText(thisEvent.getEventName());
                eventLocation.setText(thisEvent.getLocation());
                organizerName.setText(thisEvent.getOrganizerName());
                eventDescription.setText(thisEvent.getDescription());
                numSeats.setValue(thisEvent.getSeats());

                // Load existing photo
                eventPhotoContainer = findViewById(R.id.eventPhotoContainer);
                StorageReference photoRef = mStorageRef.child("eventPictures/" + thisEvent.getPhoto());
                Task<Uri> imageUrl = photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Success
                        Glide.with(EditEvent.this).load(uri.toString())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(eventPhotoContainer);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Fail to get image url
                    }
                });

                dateButton.setText(thisEvent.getDateString());
                timeButton.setText(thisEvent.getTimeString());

                // Set Interest selected
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditEvent.this, R.array.interestArray, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                interestSpinner.setAdapter(adapter);
                int position = adapter.getPosition(thisEvent.getEventInterest());
                interestSpinner.setSelection(position);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initDatePicker() {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String format = "MMMM d, yyyy";
                cal.set(year, month, day);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
                String selectedDateString = simpleDateFormat.format(cal.getTime()).trim();
                dateButton.setText(selectedDateString);
            }
        };

        datePickerDialog = new DatePickerDialog(this, 0, dateSetListener, year, month, day);
    }

    //Method to show Date Picker
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    //Method to get and show Time Picker
    public void openTimePicker(View view) {
        int selectedHour = cal.get(Calendar.HOUR_OF_DAY);
        int selectedMin = cal.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMin)
            {
                hour = selectedHour;
                min  = selectedMin;
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, min);
                SimpleDateFormat mSDF = new SimpleDateFormat("h:mm a", Locale.US);
                String time = mSDF.format(cal.getTime());
                timeButton.setText(time);
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, selectedHour, selectedMin, true);
        timePickerDialog.show();
    }

    public void openEventPhotoPicker() {
        Intent gallery =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 3);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && data != null) {
                selectedImage = data.getData();

                Glide.with(EditEvent.this).load(selectedImage.toString())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(eventPhotoContainer);
                newPicture = true;
            }
    }

    // Uploads photo to Firebase storage and returns key to access photo
    private String uploadPicture() {

        final String randomKey = UUID.randomUUID().toString();
        // Create a reference to 'images/randomKey'
        StorageReference profilePicRef = mStorageRef.child("eventPictures/" + randomKey);
        if (selectedImage != null) {
            profilePicRef.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditEvent.this, "Profile pic uploaded!", Toast.LENGTH_SHORT);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditEvent.this, "Profile pic could not be uploaded.", Toast.LENGTH_SHORT);
                        }
                    });
            return randomKey;
        }

        return "default.png";


    }

    //Create event method
    private void saveEvent() {
        String photoKey = thisEvent.getPhoto();
        if (newPicture) {
            photoKey = uploadPicture();
        }
        eventInfo(photoKey);
        backToEventInfo();
    }

    // Updates Event record with photo key and other info
    private void eventInfo(String photoKey) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        // Get timestamp
        timestamp = cal.getTimeInMillis();
        String organizerID = FirebaseAuth.getInstance().getUid();
        String key = UUID.randomUUID().toString();
        HashMap<String, Object> attendeeList = new HashMap<>();
        attendeeList.put(key, organizerID);
        Event event = new Event(eventName.getText().toString().trim(),
                                organizerName.getText().toString().trim(),
                                eventLocation.getText().toString().trim(),
                                timestamp,
                                photoKey,
                                eventDescription.getText().toString().trim(),
                                attendeeList, //Attendees
                                numSeats.getValue(),
                                FirebaseAuth.getInstance().getUid(), // Organizer ID
                                interestSpinner.getSelectedItem().toString()
                            );

        dbRef.child("Events").child(eventID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditEvent.this, "Event created!", Toast.LENGTH_LONG);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error", e.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Events", error.getMessage());
            }
        });
    }

    private void cancelEvent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditEvent.this);
        builder.setTitle("Cancel Event");
        builder.setMessage("Are you sure want to cancel the event? \n\nThis action is permanent!");
        builder.setIcon(R.drawable.ic_heroicon_warning);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                eventsTable.child(eventID).removeValue();
                Intent goToFeed=new Intent(EditEvent.this, Feed.class);
                goToFeed.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goToFeed);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void returnToFeed() {
        Intent intent = new Intent(this, Feed.class);
        startActivity(intent);
    }

    private void backToEventInfo() {
        Intent intent = new Intent(this, EventInfo.class);
        intent.putExtra("Event_ID", eventID);
        startActivity(intent);
    }

    private void sendOnChannels() {
        String channel_eventName = eventName.getText().toString();
        String channel_location  = eventLocation.getText().toString();
        String channel_description = eventDescription.getText().toString();
        String channel_date      = dateButton.getText().toString();

        //Select CHANNEL
        String CHANNEL = "";
        int interest = 1; //need to add a list of interests by numbers
        switch (interest) {
            case 1:  CHANNEL = CHANNEL_1_ID;
                break;
            case 2:  CHANNEL = CHANNEL_2_ID;
                break;
            case 3:  CHANNEL = CHANNEL_3_ID;
                break;
            case 4:  CHANNEL = CHANNEL_4_ID;
                break;
            case 5:  CHANNEL = CHANNEL_5_ID;
                break;
            case 6:  CHANNEL = CHANNEL_6_ID;
                break;
            case 7:  CHANNEL = CHANNEL_7_ID;
                break;
            case 8:  CHANNEL = CHANNEL_8_ID;
                break;
            case 9:  CHANNEL = CHANNEL_9_ID;
                break;
            case 10: CHANNEL = CHANNEL_10_ID;
                break;
            case 11: CHANNEL = CHANNEL_11_ID;
                break;
            case 12: CHANNEL = CHANNEL_12_ID;
                break;
            case 13: CHANNEL = CHANNEL_13_ID;
                break;
        }
        // Create an intent to lead to Feed
        Intent intent = new Intent(this, Feed.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        //Set notification's visualization
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL)
                .setContentTitle(channel_eventName)
                .setSmallIcon(R.drawable.ic_universelogo)
                .setContentText(channel_description)
                .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(channel_location + " " + channel_date))
                .setContentIntent(pendingIntent);

        notificationManager.notify(interest, notification.build());
    }
}



