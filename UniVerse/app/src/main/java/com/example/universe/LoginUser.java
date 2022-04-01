package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginUser extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        register = (TextView) findViewById(R.id.registerUser);
        register.setOnClickListener(this);

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.registerUser) {
            startActivity(new Intent(this, RegisterUser.class));
        }

        else if (view.getId() == R.id.forgotPassword) {
                //TODO: Forgot password activity;
        }
    }
}