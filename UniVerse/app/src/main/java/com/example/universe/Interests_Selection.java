package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

public class Interests_Selection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseReference myDataB = FirebaseDatabase.getInstance().getReference("Interests");
        Chip chip = new Chip(this);
        int numChild = 1;
        myDataB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                while (numChild != dataSnapshot.getChildrenCount()) {
                    ChipGroup chipGroup = findViewById(R.id.InterestChips);
                    String value = dataSnapshot.child(Integer.toString(numChild)).child("Name").getValue(String.class);
                    chip.setText(value);
                    chip.setCheckable(true);
                    chipGroup.addView(chip);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}