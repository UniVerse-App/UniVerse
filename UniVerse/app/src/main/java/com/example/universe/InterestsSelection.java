package com.example.universe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

public class InterestsSelection extends AppCompatActivity {

    public static final String TAG = "INFO";

    private ChipGroup chipGroup;
    private Chip newChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests_selection);

        chipGroup = findViewById(R.id.InterestChipsGroup);
        populateChips();

    }

    private Chip buildInterestChip() {
        Chip chip = new Chip(InterestsSelection.this);
        chip.setCheckable(true);
        chip.setChipIcon(getDrawable(R.drawable.ic_baseline_science_24));
        chip.setChipStrokeColorResource(R.color.accentColor200);
        chip.setChipStrokeWidth(4);
        chip.setTextColor(getColor(R.color.accentColor800));
        chip.setChipIconTintResource(R.color.accentColor800);
        chip.setChipBackgroundColorResource(R.color.accentColor100);
        chip.setId(ViewCompat.generateViewId());

        return chip;

    }

    private void populateChips() {

        chipGroup = findViewById(R.id.InterestChipsGroup);

        DatabaseReference interestTable = FirebaseDatabase.getInstance().getReference("Interests");

        interestTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot interestSnapshot: snapshot.getChildren()) {
                    Chip chip = buildInterestChip();
                    chip.setText(interestSnapshot.child("Name").getValue(String.class));
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