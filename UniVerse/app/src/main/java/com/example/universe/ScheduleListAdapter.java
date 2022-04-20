package com.example.universe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ScheduleListAdapter extends ArrayAdapter<Event> {

    //
    public ScheduleListAdapter(@NonNull Context context, ArrayList<Event> eventsList) {
        super(context, 0, eventsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.schedule_list_items, parent, false);
        }

        Event event = getItem(position);

        TextView eventName = listItemView.findViewById(R.id.eventName);
        TextView eventTime = listItemView.findViewById(R.id.eventTime);
        TextView eventLocation = listItemView.findViewById(R.id.eventLocation);

        eventName.setText(event.getEventName());
        eventTime.setText(event.getTime());
        eventLocation.setText(event.getLocation());

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Load event info page.
            }
        });

        return listItemView;
    }
}
