package com.example.universe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.time.LocalDate;
import java.util.UUID;

public class EditStudentInfo extends AppCompatActivity implements View.OnClickListener {

    private Spinner majorSpinner;
    private NumberPicker gradYear;
    private Button nextButton;
    private FloatingActionButton selectProfilePic;
    private ShapeableImageView profilePic;
    private Boolean newPic = false;

    // Profile pic selection/upload variables
    private Integer PICK_IMAGE = 1;
    private Uri selectedImage;

    // Firebase variables
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private FirebaseDatabase database;
    private User user;
    private Context thisContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisContext = getBaseContext();
        setContentView(R.layout.activity_edit_student_info);

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

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gradArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        majorSpinner.setAdapter(adapter);

        DatabaseReference userTable = database.getInstance().getReference().child("Users");
        String userId = mAuth.getUid();

        userTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventsSnapshot : snapshot.getChildren()) {
                    String key = eventsSnapshot.getKey().toString();
                    if (key.equals(userId)) {
                        user = eventsSnapshot.getValue(User.class);
                        gradYear.setValue(user.getGradYear());

                        // TODO: Translate Major String into position in array
                        // Set Major selected
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditStudentInfo.this, R.array.gradArray, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        majorSpinner.setAdapter(adapter);
                        int position = adapter.getPosition(user.getMajor());
                        majorSpinner.setSelection(position);

                        // Insert photo
                        StorageReference reference;
                        reference = mStorageRef.child("profilePictures/");
                        reference = reference.child(user.getProfilePicture());
                        Task<Uri> imageUrl = reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Success
                                Glide.with(thisContext).load(uri.toString())
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(profilePic);

                                selectedImage = uri;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Fail to get image url
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
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
            selectedImage = data.getData();
            Glide.with(EditStudentInfo.this).load(selectedImage.toString())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(profilePic);
            newPic = true;
        }
    }

    private void nextScreen() {
        String major = majorSpinner.getSelectedItem().toString();
        Integer year = gradYear.getValue();

        if (major.equals("Choose a major...")) {
            Toast.makeText(this, "Please select a Field of Study", Toast.LENGTH_SHORT).show();

        } else {
            String photoKey;
            if (newPic) {
                photoKey = uploadPicture();
            } else {
                photoKey = user.getProfilePicture();
            }
            updateUserInfo(photoKey);
            startActivity(new Intent(EditStudentInfo.this, Feed.class));

        }
    }

    // Updates user profile with photo key and other info
    private void updateUserInfo(String photoKey) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        dbRef.child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("major").setValue(majorSpinner.getSelectedItem().toString());
                snapshot.getRef().child("gradYear").setValue(gradYear.getValue());
                snapshot.getRef().child("profilePicture").setValue(photoKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("User", error.getMessage());
            }
        });

    }

    // Uploads photo to Firebase storage and returns key to access photo
    private String uploadPicture() {

        final String randomKey = UUID.randomUUID().toString();
        // Create a reference to 'images/randomKey'
        StorageReference profilePicRef = mStorageRef.child("profilePictures/" + randomKey);

        profilePicRef.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EditStudentInfo.this, "Profile pic uploaded!", Toast.LENGTH_SHORT);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditStudentInfo.this, "Profile pic could not be uploaded.", Toast.LENGTH_SHORT);
                    }
                });

        return randomKey;
    }
}
