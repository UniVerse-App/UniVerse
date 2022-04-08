package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class RegisterStudentInfo extends AppCompatActivity implements View.OnClickListener {

    private Spinner majorSpinner;
    private NumberPicker gradYear;
    private Button nextButton;

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


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gradArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        majorSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.studentRegistrationNextButton) {
            nextScreen();
        }
    }

    private void nextScreen() {
        String major = majorSpinner.getSelectedItem().toString();
        Integer year = gradYear.getValue();

        if (!major.equals("Choose a major...")) {
            // TODO: Load Interests Activity
        }
    }
}
