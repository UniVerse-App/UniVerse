package com.example.universe;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

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
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        mEventList = rootView.findViewById(R.id.recycleView);
        mEventList.setHasFixedSize(true);
        mEventList.setLayoutManager(new LinearLayoutManager(thisContext));
        mEventList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    fab.setVisibility(View.INVISIBLE);
                }

                else {
                    if (fab.getVisibility() == View.INVISIBLE) {
                        fab.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisContext, CreateEvent.class);
                startActivity(intent);
            }
        });

        Query query = mref.orderByChild("timestamp").startAfter(Calendar.getInstance().getTimeInMillis());
        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>().setQuery(query,Event.class).build();
        FirebaseRecyclerAdapter<Event, Feed.EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, Feed.EventViewHolder>
                (options) {
            @Override
            protected void onBindViewHolder(@NonNull Feed.EventViewHolder viewHolder, int position, @NonNull Event model) {
                DatabaseReference eventReference = getRef(position);
                viewHolder.setEventName(model.getEventName());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setTimeString(model.getTimeString());
                viewHolder.setDayOfMonth(model.getDayOfMonth());
                viewHolder.setMonthAbr(model.getMonthAbr());
                viewHolder.setEventID(eventReference.getKey());
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
                        Intent intent = new Intent(thisContext, EventInfo.class);
                        ViewGroup parent = (ViewGroup) view.getParent();
                        TextView idView = (TextView) parent.findViewById(R.id.eventID);
                        intent.putExtra("Event_ID",idView.getText().toString());
                        startActivity(intent);

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