package com.example.universe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class RegisterStudentInfo extends AppCompatActivity implements View.OnClickListener {

    private Spinner majorSpinner;
    private NumberPicker gradYear;
    private Button nextButton;
    private ShapeableImageView selectProfilePic, profilePic;
    private Integer PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student_info);

        majorSpinner = findViewById(R.id.studentRegistrationMajor);

        gradYear = findViewById(R.id.studentRegistrationGradYear);

        // Sets min graduation year to this year, sets max to 8 years from now.
        gradYear.setMinValue(LocalDate.now().getYear());
        gradYear.setMaxValue(LocalDate.now().getYear() + 8);

        nextButton = findViewById(R.id.studentRegistrationNextButton);
        nextButton.setOnClickListener(this);

        profilePic = findViewById(R.id.profilePicBackground);

        selectProfilePic = findViewById(R.id.selectProfilePic);
        selectProfilePic.setOnClickListener(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gradArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        majorSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.studentRegistrationNextButton) {
            nextScreen();
        }
        else if (view.getId() == R.id.selectProfilePic) {
           Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
           startActivityForResult(gallery, 3);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            profilePic.setImageURI(selectedImage);
        }
    }

    private void nextScreen() {
        String major = majorSpinner.getSelectedItem().toString();
        Integer year = gradYear.getValue();

        if (!major.equals("Choose a major...")) {
            // TODO: Load Interests Activity
        } else {
            Toast.makeText(this, "Please select a Field of Study", Toast.LENGTH_SHORT).show();
        }
    }
}
