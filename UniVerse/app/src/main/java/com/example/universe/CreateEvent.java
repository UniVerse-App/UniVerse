package com.example.universe;

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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.lang.reflect.Field;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Locale;
import java.util.UUID;

public class CreateEvent extends AppCompatActivity {

    private EditText eventName, location, organizerName, summary, description, numStudent;
    private Switch aSwitch;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Button timeButton;
    private ShapeableImageView eventPhoto;
    private ShapeableImageView shapeableImageView;
    private Button saveButton;
    private Button cancelButton;
    private StorageReference mStorageRef;
    private FirebaseDatabase database;

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

        eventName = findViewById(R.id.eventNameBox);

        location = findViewById(R.id.locationBox);

        aSwitch = findViewById(R.id.switchButton);

        dateButton = findViewById(R.id.datePickerButton);

        timeButton = findViewById(R.id.hourPickerButton);

        organizerName = findViewById(R.id.organizerName);

        summary = findViewById(R.id.summaryDescription);

        description = findViewById(R.id.descriptionBox);

        numStudent = findViewById(R.id.numStudentBox);

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent();
            }
        });

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelEvent();
            }
        });

        eventPhoto = findViewById(R.id.eventPhotoPickerButton);

        eventPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEventPhotoPicker();
            }
        });



        //Firebase
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
                eventPhoto.setImageURI(selectedImage);
        }
    }

    // Uploads photo to Firebase storage and returns key to access photo
    private String uploadPicture() {

        final String randomKey = UUID.randomUUID().toString();
        StorageReference eventPhotoRef;
        //When user doesn't choose an img
        if(selectedImage==null){
            eventPhotoRef = mStorageRef.child("eventPictures/default.png");

        } else {
            // Create a reference to 'images/randomKey'
            eventPhotoRef = mStorageRef.child("eventPictures/" + randomKey);
        }

            eventPhotoRef.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CreateEvent.this, "Event photo uploaded!", Toast.LENGTH_SHORT);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure( Exception e) {
                            Toast.makeText(CreateEvent.this, "Event photo could not be uploaded.", Toast.LENGTH_SHORT);
                        }
                    });

        return randomKey;
    }

    // Updates user event photo with photo key and other info
    private void eventInfo(String photoKey) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();
        final String randomKey = UUID.randomUUID().toString();

        dbRef.child("Event").child(randomKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("eventName").setValue(eventName.getText().toString().trim());
                snapshot.getRef().child("location").setValue(location.getText().toString().trim());
                snapshot.getRef().child("isPublic").setValue(aSwitch.isChecked());
                snapshot.getRef().child("date").setValue(dateButton.getText().toString());
                snapshot.getRef().child("time").setValue(timeButton.getText().toString());
                snapshot.getRef().child("organizerName").setValue(organizerName.getText().toString().trim());
                snapshot.getRef().child("summary").setValue(summary.getText().toString().trim());
                snapshot.getRef().child("description").setValue(description.getText().toString().trim());
                snapshot.getRef().child("numStudent").setValue(numStudent.getText().toString().trim());
                snapshot.getRef().child("photo").setValue(photoKey);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Event", error.getMessage());
            }
        });

    }

    //Error warning if text box left NULL method
    public void errorWarning(View v)
    {
        if(eventName.getText().length()==0)
        {
            eventName.setError("Field cannot be left blank.");
        }
        else if (location.getText().length()==0)
        {
            location.setError("Field cannot be left blank.");
        }
        else if (organizerName.getText().length()==0)
        {
            organizerName.setError("Field cannot be left blank.");
        }
        else if (summary.getText().length()==0)
        {
            summary.setError("Field cannot be left blank.");
        }
        else if (description.getText().length()==0)
        {
            description.setError(("Field cannot be left blank."));
        }
        else if (numStudent.getText().length()==0)
        {
            numStudent.setError("Field cannot be left blank.");
        }
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
}


