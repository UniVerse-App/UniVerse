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
import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Event>{
    private String eventId;

    public NotificationAdapter(@NonNull Context context, ArrayList<Event> notificationList, String eventId) {
        super(context, 0, (List<Event>) notificationList);
        this.eventId = eventId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.row_notification, parent, false);
        }

        Event event = getItem(position);

        TextView eventName = listItemView.findViewById(R.id.event_name_noti);
        TextView eventDate = listItemView.findViewById(R.id.event_time_noti);
        TextView eventDescription = listItemView.findViewById(R.id.event_description_noti);
        TextView eventId  = listItemView.findViewById(R.id.notificationItem_eventID);

        eventName.setText(event.getEventName());
        eventDate.setText(event.getDateString());
        eventDescription.setText(event.getLocation());
        String id = this.eventId;

        eventId.setText(event.key);

        Context thisContext = this.getContext();

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Load event info page.
                Intent intent = new Intent(thisContext, EventInfo.class);
                intent.putExtra("Event_ID", event.key);
                thisContext.startActivity(intent);
                //finish();
            }
        });

        return listItemView;
    }
}
