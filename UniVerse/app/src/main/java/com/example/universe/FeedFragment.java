package com.example.universe;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FeedFragment extends Fragment {

    public RecyclerView mEventList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mref;
    private Context thisContext;

    public FeedFragment() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        thisContext = container.getContext();

        mDatabase = FirebaseDatabase.getInstance();
        mref = mDatabase.getReference("Events");
        mEventList = rootView.findViewById(R.id.recycleView);
        mEventList.setHasFixedSize(true);
        mEventList.setLayoutManager(new LinearLayoutManager(thisContext));

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisContext, CreateEvent.class);
                startActivity(intent);
            }
        });


        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>().setQuery(mref,Event.class).build();
        FirebaseRecyclerAdapter<Event, Feed.EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, Feed.EventViewHolder>
                (options) {
            @Override
            protected void onBindViewHolder(@NonNull Feed.EventViewHolder viewHolder, int position, @NonNull Event model) {
                viewHolder.setEventName(model.getEventName());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setTimeString(model.getTimeString());
                viewHolder.setDayOfMonth(model.getDayOfMonth());
                viewHolder.setMonthAbr(model.getMonthAbr());
                viewHolder.setImage(thisContext, model.getPhoto());
            }

            @NonNull
            @Override
            public Feed.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);

                Button info = view.findViewById(R.id.info_button);
                info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(thisContext, EventInfo.class));
                    }
                });

                return new Feed.EventViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        mEventList.setAdapter(firebaseRecyclerAdapter);


        // Inflate the layout for this fragment
        return rootView;
    }
}