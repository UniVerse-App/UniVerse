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

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView banner;
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword;
    private Button registerUser;
    private ProgressBar progressBar;
    
    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        
        mAuth = FirebaseAuth.getInstance();

        banner = findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = findViewById(R.id.registerButton);
        registerUser.setOnClickListener(this);

        editTextFirstName = findViewById(R.id.registerFirstName);
        editTextLastName = findViewById(R.id.registerLastName);
        editTextEmail = findViewById(R.id.registerEmail);
        editTextPassword = findViewById(R.id.registerPassword);

        progressBar = findViewById(R.id.progressBar);
    }
    
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.registerButton) {
            registerUser();
        }
        
        else if (view.getId() == R.id.banner) {
            startActivity(new Intent(this, LoginUser.class));
        }
    }
    
    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        
        // If form validation is true, attempt to create user in firebase auth and realtime database
        if (validateInput(firstName, lastName, email, password, emailSuffix)) {
            progressBar.setVisibility(View.VISIBLE);
            
            // Create user in firebase auth.
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If create user in auth successful, proceed
                        if (task.isSuccessful()) {

                            // Create user object with form data.
                            Integer onboardingStep = 1;
                            User user = new User(firstName, lastName, email, onboardingStep);

                            // Write user object to database using auth uID as key, thus linking auth entry and user entry by the same uID
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    // If successful, proceed to interests page.
                                    if (task.isSuccessful()) {
                                        Toast.makeText(
                                                RegisterUser.this,
                                                "User has been registered succesfully!",
                                                Toast.LENGTH_LONG
                                        ).show();

                                        if (!emailSuffix.equals("uta.mavs.edu")) {

                                            // Redirect to RegisterStudentInfo
                                            startActivity(new Intent(RegisterUser.this, RegisterStudentInfo.class));

                                        } else if (emailSuffix.equals("uta.edu")) {

                                            // TODO: Redirect to Organizer Registration

                                        }


                                    } else {
                                        Toast.makeText(
                                                RegisterUser.this,
                                                "User registered failed! Try again...",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                    
                                    
                                } else {
                                    Toast.makeText(
                                                   RegisterUser.this,
                                                   "User registered failed! Try again...",
                                                   Toast.LENGTH_LONG
                                                   ).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Toast.makeText(
                                       RegisterUser.this,
                                       "User registered failed! Try again...",
                                       Toast.LENGTH_LONG
                                       ).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    
                }
            });
        }
        
    }
    
    private boolean validateInput(String firstName, String lastName, String email, String password, String email_suffix) {
        // Field not empty
        if (firstName.isEmpty()) {
            editTextFirstName.setError("First Name is required!");
            editTextFirstName.requestFocus();
            return false;
        }
        
        // Field not empty
        if (lastName.isEmpty()) {
            editTextLastName.setError("Last Name is required!");
            editTextLastName.requestFocus();
            return false;
        }
        
        // Field not empty
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return false;
        }
        
        // Valid email pattern
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
            return false;
        }
        
        // Field not empty
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return false;
        }
        
        // Valid password pattern - Min 8 chars, At least one uppercase, one digit, one special character.
        if (!password.matches("^(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
            if (password.length() < 8) {
                editTextPassword.setError("Password must be at least 8 characters.");
                editTextPassword.requestFocus();
                return false;
            }
            else if (!password.matches("^(?=.*?[A-Z]).{8,}$")) {
                editTextPassword.setError("Password must contain at least one uppercase letter.");
                editTextPassword.requestFocus();
                return false;
            }
            else if (!password.matches("(?=.*?[0-9]).{8,}$")) {
                editTextPassword.setError("Password must contain at least one digit.");
                editTextPassword.requestFocus();
                return false;
            }
            else if (!password.matches("(?=.*?[#?!@$%^&*-]).{8,}$")) {
                editTextPassword.setError("Password must contain at least one special character. #?!@$%^&*-");
                editTextPassword.requestFocus();
                return false;
            }
        }
        
        // Is UTA email?
        //        if (email_suffix.equals("mavs.uta.edu") || email_suffix.equals("uta.edu")) {
        //
        //            // All validation checks passed.
        //            return true;
        //
        //        } else {
        //            editTextEmail.setError("Currently we only support UTA emails.");
        //            editTextEmail.requestFocus();
        //            return false;
        //        }
        return true;
    }
}