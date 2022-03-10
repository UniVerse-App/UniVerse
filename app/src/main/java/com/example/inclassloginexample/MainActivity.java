package com.example.inclassloginexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create links to objects from activity_main.xml
        EditText username_field = findViewById(R.id.user_field);
        EditText password_field = findViewById(R.id.password_field);
        Button login_button = findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = username_field.getText().toString();
                String password = password_field.getText().toString();

                if (username.matches("abc") && password.matches("123")) {
                    // Success
                    Toast toast = Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT);
                    toast.show();

                    // Go to next screen
                    //Intent next_screen = new Intent(getApplicationContext().this.Home)
                }
                else {
                    // Try again
                    Toast toast = Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}