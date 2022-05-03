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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ScheduleFragment extends Fragment {

    private User user;
    private EditText todaysDateField;
    private Context thisContext;
    private Calendar calendar;
    private Calendar todaysDate;

    private ListView eventListView;
    private ArrayList<Event> eventList;

    private ImageView leftButton, rightButton;

    public ScheduleFragment() {
        // require a empty public constructor
    }

    private void fetchEvents() {
        eventList.clear();
        eventListView.removeAllViewsInLayout();

        long startDateTimestamp = calendar.getTimeInMillis();

        DatabaseReference eventsRecord = FirebaseDatabase.getInstance().getReference("Events");
        if (user != null) {
            HashMap<String, String> eventsAttending = user.getEventsAttending();
            // End at a day after start stamp, 86400000 is num ms in a day.
            Query query = eventsRecord.orderByChild("timestamp").startAt(startDateTimestamp).endAt((startDateTimestamp + 86400000));
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Event event = snapshot.getValue(Event.class);

                            if (eventsAttending != null && eventsAttending.containsValue(snapshot.getKey())) {
                                eventList.add(event);
                                ScheduleListAdapter adapter = new ScheduleListAdapter(thisContext, eventList, snapshot.getKey());
                                eventListView.setAdapter(adapter);
                            } else {
                                ScheduleListAdapter adapter = new ScheduleListAdapter(thisContext, eventList, "");
                                eventListView.setAdapter(adapter);
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
        }
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
                calendar.set(year, month, day, 0, 0, 0);
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

        DatabaseReference userRecord = FirebaseDatabase.getInstance().getReference("Users");

        userRecord.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String key = userSnapshot.getKey().toString();
                    if (key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        user = userSnapshot.getValue(User.class);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
