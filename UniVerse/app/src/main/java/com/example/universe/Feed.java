package com.example.universe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Feed extends AppCompatActivity{

   private BottomNavigationView bottomNavigationView;
   private NavController navController;
   public RecyclerView mEventList;
   FirebaseDatabase mDatabase;
   DatabaseReference mref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        FragmentManager supportFragmentManger = getSupportFragmentManager();

        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManger.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        mDatabase = FirebaseDatabase.getInstance();
        mref = mDatabase.getReference("Event");
        mEventList = (RecyclerView)findViewById(R.id.recycleView);
        mEventList.setHasFixedSize(true);
        mEventList.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feed.this, CreateEvent.class);
                startActivity(intent);
            }
        });

        FloatingActionButton backToTop = findViewById(R.id.backToTop);
        backToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEventList.smoothScrollToPosition(0);
            }
        });
        mEventList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            backToTop.setVisibility(View.GONE);
                        }
                    }, 2000);
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            backToTop.setVisibility(View.VISIBLE);
                        }
                    },500);
                } else if (dy < 0){
                    backToTop.setVisibility(View.VISIBLE);
                }
            }
        });
    }

        public static class EventViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public EventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setEventID(String ID){
            TextView post_eventID = (TextView) mView.findViewById(R.id.eventID);
            post_eventID.setText(ID);
        }
        public void setEventName(String eventName) {
            TextView post_eventName = (TextView) mView.findViewById(R.id.eventTitle);
            post_eventName.setText(eventName);
        }

        public void setDesc(String desc) {
            //TextView post_desc = (TextView) mView.findViewById(R.id.eventDesc);
            //post_desc.setText(desc);
        }

        public void setTimeString(String timeString){
            TextView post_eventTime = (TextView) mView.findViewById(R.id.eventTime);
            post_eventTime.setText(timeString);
        }

        public void setEventInterest(String location) {
            TextView post_eventInterest = (TextView) mView.findViewById(R.id.eventInterest);
            post_eventInterest.setText(location);
        }

        public void setDayOfMonth(Integer dayOfMonth) {
            TextView post_eventDayOfMonth = (TextView) mView.findViewById(R.id.monthDay);
            post_eventDayOfMonth.setText(String.valueOf(dayOfMonth));
        }

        public void setMonthAbr(String monthAbr) {
            TextView post_monthAbr = (TextView) mView.findViewById(R.id.monthAbr);
            post_monthAbr.setText(monthAbr);
        }

        public void setImage(Context ctx, String image)
        {
            StorageReference reference;
            reference = FirebaseStorage.getInstance().getReference().child("eventPictures/");
            ImageView post_Image = (ImageView) mView.findViewById(R.id.eventImage);
            reference = reference.child(image);
            Task<Uri> imageUrl = reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                  // Success
                    Glide.with(ctx).load(uri.toString())
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(post_Image);

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