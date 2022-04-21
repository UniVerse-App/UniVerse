package com.example.universe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginUser extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgotPassword;
    private EditText loginEmail, loginPassword;
    private Button loginButton;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        register = findViewById(R.id.registerUser);
        register.setOnClickListener(this);

        forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.registerUser) {
            startActivity(new Intent(this, RegisterUser.class));
        }

        else if (view.getId() == R.id.forgotPassword) {
            startActivity(new Intent(this, ForgotPassword.class));
        }

        else if (view.getId() == R.id.loginButton) {
            userLogin();
        }
    }

    private void userLogin() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

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

        else if (password.isEmpty()) {
            loginPassword.setError("Please provide a password.");
            loginPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (!user.isEmailVerified()) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginUser.this,
                                            "Verification email sent to " + user.getEmail(),
                                            Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(LoginUser.this,
                                            "Failed to send verification email.",
                                            Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(LoginUser.this, "Welcome back!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        // This mess of a line gets the current "onboardingStep" from the User entry in FB to
                        // determine where we should redirect

                        Integer onboardingStep = Integer.parseInt(FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(mAuth.getUid()).child("onboardingStep").toString());

                        switch (onboardingStep) {
                            case 1:
                                startActivity(new Intent(LoginUser.this, RegisterStudentInfo.class));
                                break;
                            case 2:
                                startActivity(new Intent(LoginUser.this, InterestsSelection.class));
                                break;
                            default:
                                startActivity(new Intent(LoginUser.this, Feed.class));
                                break;
                        }

                    }
                }
                else {
                    Toast.makeText(LoginUser.this, "Login failed, please try again.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}