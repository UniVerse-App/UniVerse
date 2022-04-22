package com.example.universe;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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


import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
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
    private long timestamp;
    private StorageReference mStorageRef;
    private FirebaseDatabase database;
    private Calendar cal = Calendar.getInstance();

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
        numSeats.setMaxValue(999);
        numSeats.setMinValue(0);

        createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent();
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

        eventPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEventPhotoPicker();
            }
        });

        //Firebase
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMin)
            {
                hour = selectedHour;
                min  = selectedMin;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,min));
                cal.set(Calendar.HOUR, hour);
                cal.set(Calendar.MINUTE, min);
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
        if (selectedImage != null) {
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
            return randomKey;
        }

        return "default.png";


    }

    // Updates Event record with photo key and other info
    private void eventInfo(String photoKey) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();
        final String randomKey = UUID.randomUUID().toString();

        dbRef.child("Events").child(randomKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateEvent.this, "Event created!", Toast.LENGTH_LONG);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error", e.toString());
                    }
                });
                //snapshot.getRef().child("location").setValue(eventLocation.getText().toString().trim());
                // snapshot.getRef().child("date").setValue(dateButton.getText().toString());
                // snapshot.getRef().child("time").setValue(timeButton.getText().toString());
                //snapshot.getRef().child("organizerName").setValue(organizerName.getText().toString().trim());
                //snapshot.getRef().child("description").setValue(eventDescription.getText().toString().trim());
                //snapshot.getRef().child("numStudent").setValue(numSeats.getValue());
                //snapshot.getRef().child("photo").setValue(photoKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Events", error.getMessage());
            }
        });
    }


    //Create event method
    private void saveEvent() {
        String photoKey = uploadPicture();
            eventInfo(photoKey);
            startActivity(new Intent(this, Feed.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            startActivity(new Intent(this, Feed.class));
            notification();
    }

    private void cancelEvent() {
        startActivity(new Intent(this, Feed.class));
    }


    private void notification() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "name", importance);
            channel.setDescription("description");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle(eventName.getText().toString())
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText(eventDescription.getText().toString());

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, builder.build());
    }
}



