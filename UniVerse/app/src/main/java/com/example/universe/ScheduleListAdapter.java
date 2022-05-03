package com.example.universe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ScheduleListAdapter extends ArrayAdapter<Event> {
    private String eventId;
    //
    public ScheduleListAdapter(@NonNull Context context, ArrayList<Event> eventsList, String eventId) {
        super(context, 0, eventsList);
        this.eventId = eventId;
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
        TextView eventId = listItemView.findViewById(R.id.scheduleItem_eventID);

        eventName.setText(event.getEventName());
        eventTime.setText(event.getTimeString());
        eventLocation.setText(event.getLocation());
        String id = this.eventId;
        Context thisContext = this.getContext();
        eventId.setText(id);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisContext, EventInfo.class);
                intent.putExtra("Event_ID", id);
                thisContext.startActivity(intent);
            }
        });

        return listItemView;
    }
}
