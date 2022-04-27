package com.example.universe;

import static com.example.universe.PushNotification.CHANNEL_10_ID;
import static com.example.universe.PushNotification.CHANNEL_11_ID;
import static com.example.universe.PushNotification.CHANNEL_12_ID;
import static com.example.universe.PushNotification.CHANNEL_13_ID;
import static com.example.universe.PushNotification.CHANNEL_1_ID;
import static com.example.universe.PushNotification.CHANNEL_2_ID;
import static com.example.universe.PushNotification.CHANNEL_3_ID;
import static com.example.universe.PushNotification.CHANNEL_4_ID;
import static com.example.universe.PushNotification.CHANNEL_5_ID;
import static com.example.universe.PushNotification.CHANNEL_6_ID;
import static com.example.universe.PushNotification.CHANNEL_7_ID;
import static com.example.universe.PushNotification.CHANNEL_8_ID;
import static com.example.universe.PushNotification.CHANNEL_9_ID;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
   private NotificationManagerCompat notificationManager;


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

        //Notification
        notificationManager = NotificationManagerCompat.from(getApplicationContext());

        mref = mDatabase.getReference("Events");
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.child("notify").exists()) {
                    if (snapshot.child("notify").getValue().equals("true")) {
                        String eventTitle = snapshot.child("eventName").getValue().toString();
                        String eventTime = snapshot.child("timeString").getValue().toString();
                        String eventDate = snapshot.child("dateString").getValue().toString();
                        String eventLocation = snapshot.child("location").getValue().toString();

                        sendOnChannels(eventTitle, eventTime, eventDate, eventLocation);
                        DatabaseReference notifyBool = snapshot.child("notify").getRef();
                        notifyBool.setValue("false");
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

        PushNotification notifications = new PushNotification();
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

        public void showAttendingCheck() {
            mView.findViewById(R.id.checkbox).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.checkboxBack).setVisibility(View.VISIBLE);
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

    private void sendOnChannels(String eventName, String eventTime, String eventDate, String eventLocation) {
        String channel_eventName = eventName; //eventName.getText().toString();
        String channel_location  = eventLocation; //eventLocation.getText().toString();
        String channel_eventTime = eventTime; //eventDescription.getText().toString();
        String channel_date      = eventDate; //dateButton.getText().toString();

        //Select CHANNEL
        String CHANNEL = "";
        int interest = 1; //need to add a list of interests by numbers

        switch (interest) {
            case 1:  CHANNEL = CHANNEL_1_ID;
                break;
            case 2:  CHANNEL = CHANNEL_2_ID;
                break;
            case 3:  CHANNEL = CHANNEL_3_ID;
                break;
            case 4:  CHANNEL = CHANNEL_4_ID;
                break;
            case 5:  CHANNEL = CHANNEL_5_ID;
                break;
            case 6:  CHANNEL = CHANNEL_6_ID;
                break;
            case 7:  CHANNEL = CHANNEL_7_ID;
                break;
            case 8:  CHANNEL = CHANNEL_8_ID;
                break;
            case 9:  CHANNEL = CHANNEL_9_ID;
                break;
            case 10: CHANNEL = CHANNEL_10_ID;
                break;
            case 11: CHANNEL = CHANNEL_11_ID;
                break;
            case 12: CHANNEL = CHANNEL_12_ID;
                break;
            case 13: CHANNEL = CHANNEL_13_ID;
                break;
        }
        // Create an intent to lead to Feed
        Intent intent = new Intent(this, Feed.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

//        //Set notification's visualization
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL)
                .setContentTitle("Check out this new event!")
                .setSmallIcon(R.drawable.ic_universelogo)
                .setContentText(channel_eventName)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(channel_location + " " + channel_date))
                .setContentIntent(pendingIntent);

        notificationManager.notify(interest, notification.build());

    }

}