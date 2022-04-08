package com.example.universe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private TextView backToLogin;
    private EditText loginEmail;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        backToLogin = findViewById(R.id.backToLogin);
        backToLogin.setOnClickListener(this);

        loginEmail = findViewById(R.id.loginEmail);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backToLogin) {
            startActivity(new Intent(this, LoginUser.class));
        }
        else if (view.getId() == R.id.submitButton){
            sendPasswordReset();
        }
    }

    private void sendPasswordReset() {
        String email = loginEmail.getText().toString().trim();

        if (email.isEmpty()) {
            loginEmail.setError("Please provide an email.");
            loginEmail.requestFocus();
            return;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Please provide a valid email.");
            loginEmail.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                    returnHome();
                }
            }
        });
    }

    private void returnHome() {
        startActivity(new Intent(this, LoginUser.class));
    }

}