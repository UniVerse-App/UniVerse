package com.example.universe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class InterestsSelection extends AppCompatActivity {

    public static final String TAG = "INFO";

    private ChipGroup chipGroup;
    private Chip newChip;
    private Button button;
    private boolean tr;
    private List<Integer> checkedChipIDs;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests_selection);

        int checkedChip = 0;
        chipGroup = findViewById(R.id.InterestChipsGroup);
        button = findViewById(R.id.Confirm);
        button.setEnabled(false);
        tr = true;
        checkedChipIDs = new ArrayList<>();
        populateChips();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
                DatabaseReference userEntry  = userRef.child(mAuth.getInstance().getUid());

                userEntry.child("Interests").setValue(chipGroup.getCheckedChipIds())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // If successful, proceed to interests page.
                        if (task.isSuccessful()) {
                            userEntry.child("onboardingStep").setValue(3);
                            startActivity(new Intent(InterestsSelection.this, Feed.class));
                        } else {
                            Toast.makeText(InterestsSelection.this, "Failed", Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });
    }

    private List<Integer> checkIDs(List<Integer> checkedChipIDs, ChipGroup chipGroup){
        checkedChipIDs = chipGroup.getCheckedChipIds();
        return checkedChipIDs;
    }

    private Chip buildInterestChip(Drawable chipIcon) {
        Chip chip = new Chip(InterestsSelection.this);
        chip.setCheckable(true);
        chip.setChipIcon(chipIcon);
        chip.setChipStrokeColorResource(R.color.accentColor200);
        chip.setChipStrokeWidth(4);
        chip.setTextSize(20);
        chip.setTextColor(getColor(R.color.accentColor800));
        chip.setChipIconTintResource(R.color.accentColor800);
        chip.setChipBackgroundColorResource(R.color.accentColor100);
        chip.setId(ViewCompat.generateViewId());
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedChipIDs = chipGroup.getCheckedChipIds();
                if(checkedChipIDs.size() >= 3)
                    button.setEnabled(true);
                else
                    button.setEnabled(false);
            }
        });
        return chip;

    }

    private void populateChips() {

        chipGroup = findViewById(R.id.InterestChipsGroup);

        DatabaseReference interestTable = FirebaseDatabase.getInstance().getReference("Interests");

        interestTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot interestSnapshot: snapshot.getChildren()) {
                    String icon = interestSnapshot.child("Icon").getValue(String.class);
                    @SuppressLint("UseCompatLoadingForDrawables") Drawable chipIcon = getDrawable(getResources().getIdentifier("ic_baseline_"+icon+"_24", "drawable", getPackageName()));
                    Chip chip = buildInterestChip(chipIcon);
                    chip.setText(interestSnapshot.child("Name").getValue(String.class));
                    chip.setId(Integer.parseInt(interestSnapshot.getKey()));
                    chipGroup.addView(chip);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

    }
}