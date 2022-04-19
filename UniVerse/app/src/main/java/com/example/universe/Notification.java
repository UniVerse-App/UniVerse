package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.os.Bundle;
import android.widget.ImageView;

public class Notification extends AppCompatActivity {

    private static final String CHANNEL_ID = "ok";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ImageView image;

        image = (ImageView) findViewById(R.id.utaLogo);
        image.setImageResource(R.drawable.uta_logo);

    }
}