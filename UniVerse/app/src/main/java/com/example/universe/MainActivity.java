package com.example.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;


import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Button timeButton;
    private Button eventPhoto;
    private ShapeableImageView shapeableImageView;

    //hour and min variables
    int hour, min;

    //Event Photo selection variables
    private Integer PICK_IMAGE = 1;
    private Uri selectedImage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);

        timeButton = findViewById(R.id.hourPickerButton);

        eventPhoto = findViewById(R.id.eventPhotoPickerButton);

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

    public void openEventPhotoPicker(View view) {
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
}