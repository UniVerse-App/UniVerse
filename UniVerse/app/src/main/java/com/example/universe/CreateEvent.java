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
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class CreateEvent extends AppCompatActivity {

    private TextView eventName, eventLocation, organizerName, eventDescription;
    private NumberPicker numSeats;
    private DatePickerDialog datePickerDialog;
    private Button dateButton, timeButton, createButton;
    private ImageView backButton;
    private ShapeableImageView eventPhotoButton, eventPhotoContainer;
    private StorageReference mStorageRef;
    private FirebaseDatabase database;
    private NotificationManagerCompat notificationManager;

    //hour and min variables
    int hour, min;

    //Event Photo selection variables
    private Integer PICK_IMAGE = 1;
    private Uri selectedImage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        initDatePicker();

        eventName = findViewById(R.id.event_name);
        eventLocation = findViewById((R.id.event_location));
        dateButton = findViewById(R.id.datePickerButton);
        timeButton = findViewById(R.id.hourPickerButton);
        organizerName = findViewById(R.id.event_organizer);
        eventDescription = findViewById(R.id.event_description);

        // TODO: Fix number picker
        numSeats = findViewById(R.id.event_seats);
        numSeats.setMaxValue(9999);
        numSeats.setMinValue(0);

        createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent();
                sendOnChannels();
            }
        });

        backButton = findViewById(R.id.ic_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelEvent();
            }
        });

        eventPhotoButton = findViewById(R.id.selectEventPic);

        eventPhotoContainer = findViewById(R.id.eventPhotoPickerButton);
        eventPhotoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEventPhotoPicker();
            }
        });

        //Firebase
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Notification
        notificationManager = NotificationManagerCompat.from(this);

    }


    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day,month,year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, 0, dateSetListener, year, month, day);

    }

    // TODO: Change date functionality to use epoch
    //Method to return Date as a string
    private String makeDateString(int day, int month, int year) {
        return month + "/" + day + "/" + year;
    }

    //Method to show Date Picker
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    //Method to get and show Time Picker
    public void openTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMin)
            {
                hour = selectedHour;
                min  = selectedMin;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,min));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, min, true);
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
            eventPhotoContainer.setImageURI(selectedImage);
        }
    }

    // Uploads photo to Firebase storage and returns key to access photo
    private String uploadPicture() {

        final String randomKey = UUID.randomUUID().toString();
        // Create a reference to 'images/randomKey'
        StorageReference profilePicRef = mStorageRef.child("eventPictures/" + randomKey);

//        if (selectedImage == null) {
//            profilePicRef = mStorageRef.child("eventPictures/default.png");
//        } else {

            profilePicRef.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CreateEvent.this, "Profile pic uploaded!", Toast.LENGTH_SHORT);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateEvent.this, "Profile pic could not be uploaded.", Toast.LENGTH_SHORT);
                        }
                    });
        //}
        return randomKey;
    }

    // Updates user profile with photo key and other info
    private void eventInfo(String photoKey) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();
        final String randomKey = UUID.randomUUID().toString();

        dbRef.child("Events").child(randomKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("eventName").setValue(eventName.getText().toString().trim());
                snapshot.getRef().child("location").setValue(eventLocation.getText().toString().trim());
                snapshot.getRef().child("organizerName").setValue(organizerName.getText().toString().trim());
                snapshot.getRef().child("description").setValue(eventDescription.getText().toString().trim());
                snapshot.getRef().child("date").setValue(dateButton.getText().toString());
                snapshot.getRef().child("time").setValue(timeButton.getText().toString());
                snapshot.getRef().child("numStudent").setValue(numSeats.getValue());
                snapshot.getRef().child("photo").setValue(photoKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Event", error.getMessage());
            }
        });

    }


    //Create event method
    private void saveEvent() {
        String photoKey = uploadPicture();
            eventInfo(photoKey);
            startActivity(new Intent(this, Feed.class));
    }

    private void cancelEvent() {
        startActivity(new Intent(this, Feed.class));
    }

    private void sendOnChannels() {
        String channel_eventName = eventName.getText().toString();
        String channel_location  = eventLocation.getText().toString();
        String channel_description = eventDescription.getText().toString();
        String channel_date      = dateButton.getText().toString();

        //Select CHANNEL
        String CHANNEL = "1"; // set default is 1
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
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL)
                .setContentTitle(channel_eventName)
                .setSmallIcon(R.drawable.uta_logo)
                .setContentText(channel_description)
                .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(channel_location + " " + channel_date));

        notificationManager.notify(interest, notification.build());
    }
}



