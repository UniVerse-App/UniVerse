package com.example.universe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class NotificationFragment extends Fragment implements View.OnClickListener{
    private Context thisContext;

    private ListView notificationListView;
    private ArrayList<Event> notificationList;

    private Calendar calendar;

    private String eventTime;

    private ImageView settingButton;

    public NotificationFragment() {
        //require an empty public constructor
    }

    private void fetchNotification() {
        notificationList.clear();

        //Using Events data to display on notification
        DatabaseReference eventsTable = FirebaseDatabase.getInstance().getReference("Events");

        long startDateTimestamp = calendar.getTimeInMillis();


        // End at a day after start stamp, 604800000 is num ms in a week.
        //Query query = eventsTable.orderByChild("timestamp").startAt(startDateTimestamp).endAt((startDateTimestamp + 604800000));
        //query.addValueEventListener(new ValueEventListener() {
        eventsTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if  (snapshot.exists()) {
                    for (DataSnapshot eventsSnapshot : snapshot.getChildren()) {
                        Event event = eventsSnapshot.getValue(Event.class);

                        notificationList.add(event);
                        NotificationAdapter adapter = new NotificationAdapter(thisContext, notificationList);
                        notificationListView.setAdapter(adapter);
                    }
                } else {
                    NotificationAdapter adapter = new NotificationAdapter(thisContext, notificationList);
                    notificationListView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("loadEvent:onCancelled", error.getMessage());
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        thisContext = container.getContext();

        settingButton = rootView.findViewById(R.id.builtin_settings_button);
        settingButton.setOnClickListener(this);

        notificationListView = rootView.findViewById(R.id.notificationList);
        notificationListView.setEmptyView(rootView.findViewById(R.id.emptyElement));
        notificationList = new ArrayList<>();
        fetchNotification();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
    }

}

