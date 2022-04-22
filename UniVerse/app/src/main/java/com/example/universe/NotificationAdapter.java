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
import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Event>{

    public NotificationAdapter(@NonNull Context context, ArrayList<Event> notificationList) {
        super(context, 0, (List<Event>) notificationList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.schedule_list_items, parent, false);
        }

        Event event = getItem(position);

        TextView eventName = listItemView.findViewById(R.id.event_name_noti);
        TextView eventTime = listItemView.findViewById(R.id.event_time_noti);
        TextView eventDescription = listItemView.findViewById(R.id.event_description_noti);

        eventName.setText(event.getEventName());
        eventTime.setText(event.getTime());
        eventDescription.setText(event.getLocation());

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Load event info page.
            }
        });

        return listItemView;
    }
}
