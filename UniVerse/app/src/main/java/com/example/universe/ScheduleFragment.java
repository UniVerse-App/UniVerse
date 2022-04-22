package com.example.universe;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleFragment extends Fragment {

    private EditText todaysDateField;
    private Context thisContext;

    private Calendar calendar;
    private Calendar todaysDate;

    private ListView eventListView;
    private ArrayList<Event> eventList;

    private ImageView leftButton, rightButton;

    public ScheduleFragment(){
        // require a empty public constructor
    }

    private void fetchEvents() {
        eventList.clear();

        long startDateTimestamp = calendar.getTimeInMillis();
        DatabaseReference eventsTable = FirebaseDatabase.getInstance().getReference("Events");

        // End at a day after start stamp, 86400000 is num ms in a day.
        Query query = eventsTable.orderByChild("timestamp").startAt(startDateTimestamp).endAt((startDateTimestamp + 86400000));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if  (snapshot.exists()) {
                    for (DataSnapshot eventsSnapshot : snapshot.getChildren()) {
                        Event event = eventsSnapshot.getValue(Event.class);
                        eventList.add(event);
                        ScheduleListAdapter adapter = new ScheduleListAdapter(thisContext, eventList);
                        eventListView.setAdapter(adapter);
                    }
                } else {
                    ScheduleListAdapter adapter = new ScheduleListAdapter(thisContext, eventList);
                    eventListView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateCalendar() {
        String format = "MMMM d, yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        String selectedDateString = simpleDateFormat.format(calendar.getTime()).trim();

        Boolean match = simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(todaysDate.getTime()));
        // If selected date is the current day, set text to "Today" instead of date
        if (match) {
            todaysDateField.setText("Today");
        } else {
            todaysDateField.setText(selectedDateString);
        }
        fetchEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        thisContext = container.getContext();

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        todaysDate = Calendar.getInstance(); // Calendar instance that tracks todays date for comparisons.

        leftButton = rootView.findViewById(R.id.leftButton);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, -1);
                updateCalendar();
            }
        });

        rightButton = rootView.findViewById(R.id.rightButton);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, 1);
                updateCalendar();
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(year, month, day, 0,0,0);
                updateCalendar();
            }
        };

        todaysDateField = rootView.findViewById(R.id.todaysDate);
        todaysDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(thisContext, date,
                                     calendar.get(Calendar.YEAR),
                                     calendar.get(Calendar.MONTH),
                                     calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        todaysDateField.setText("Today");

        eventListView = rootView.findViewById(R.id.eventList);
        eventListView.setEmptyView(rootView.findViewById(R.id.emptyElement));
        eventList = new ArrayList<>();
        fetchEvents();

        return rootView;
    }
}