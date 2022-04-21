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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

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


//        mDatabase = FirebaseDatabase.getInstance();
//        mref = mDatabase.getReference("Event");
//        mEventList = (RecyclerView)findViewById(R.id.recycleView);
//        mEventList.setHasFixedSize(true);
//        mEventList.setLayoutManager(new LinearLayoutManager(this));

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //startActivity(new Intent(this,));
//            }
//        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>().setQuery(mref,Event.class).build();
//        FirebaseRecyclerAdapter<Event,EventViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>
//                (options) {
//            @Override
//            protected void onBindViewHolder(@NonNull EventViewHolder viewHolder, int position, @NonNull Event model) {
//                viewHolder.setEventName(model.getEventName());
//                viewHolder.setDesc(model.getDescription());
//                viewHolder.setImage(getApplicationContext(),model.getPhoto());
//            }
//
//            @NonNull
//            @Override
//            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);
//
//                return new EventViewHolder(view);
//            }
//        };
//        firebaseRecyclerAdapter.startListening();
//        mEventList.setAdapter(firebaseRecyclerAdapter);
//    }
//
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
            //TextView post_desc = (TextView) mView.findViewById(R.id.eventDesc);
            //post_desc.setText(desc);
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