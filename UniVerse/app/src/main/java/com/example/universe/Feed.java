package com.example.universe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

public class Feed extends AppCompatActivity{

   private BottomNavigationView bottomNavigationView;
   private NavController navController;
   private RecyclerView mEventList;
   private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        FragmentManager supportFragmentManger = getSupportFragmentManager();

        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManger.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Event");
        mDatabase.keepSynced(true);

        mEventList = (RecyclerView)findViewById(R.id.recycleView);
        mEventList.setHasFixedSize(true);
        mEventList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Event,EventViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>
                (Event.class,R.layout.event_card, EventViewHolder.class,mDatabase) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder viewHolder, int position, @NonNull Event model) {
                viewHolder.setEventName(model.getEventName());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setImage(getApplicationContext(),model.getPhoto());
            }

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };

        mEventList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public EventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setEventName(String eventName) {
            TextView post_eventName = (TextView) mView.findViewById(R.id.eventTitle);
            post_eventName.setText(eventName);
        }

        public void setDesc(String desc) {
            TextView post_desc = (TextView) mView.findViewById(R.id.eventDesc);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image)
        {
            ImageView post_Image = (ImageView) mView.findViewById(R.id.eventImage);
            Picasso.with(ctx).load(image).into(post_Image);

        }
    }
}