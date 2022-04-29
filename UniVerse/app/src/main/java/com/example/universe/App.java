package com.example.universe;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


public class App extends Application {
    public static final String CHANNEL_1_ID = "Graduate";
    public static final String CHANNEL_2_ID = "Engineering";
    public static final String CHANNEL_3_ID = "Music";
    public static final String CHANNEL_4_ID = "Greek Life";
    public static final String CHANNEL_5_ID = "Sports";
    public static final String CHANNEL_6_ID = "School Spirit";
    public static final String CHANNEL_7_ID = "Student Council";
    public static final String CHANNEL_8_ID = "Public Speakers";
    public static final String CHANNEL_9_ID = "Nursing";
    public static final String CHANNEL_10_ID = "Business";
    public static final String CHANNEL_11_ID = "Science";
    public static final String CHANNEL_12_ID = "Education";
    public static final String CHANNEL_13_ID = "eSports";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    public void createNotificationChannels() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Graduate", NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "Engineering", NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel channel3 = new NotificationChannel(CHANNEL_3_ID, "Music", NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel channel4 = new NotificationChannel(CHANNEL_4_ID, "Greek Life", NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel channel5 = new NotificationChannel(CHANNEL_5_ID, "Sports", NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel channel6 = new NotificationChannel(CHANNEL_6_ID, "School Spirit", NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel channel7 = new NotificationChannel(CHANNEL_7_ID, "Student Council", NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel channel8 = new NotificationChannel(CHANNEL_8_ID, "Public Speakers", NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel channel9 = new NotificationChannel(CHANNEL_9_ID, "Nursing", NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel channel10 = new NotificationChannel(CHANNEL_10_ID, "Business", NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel channel11 = new NotificationChannel(CHANNEL_11_ID, "Science", NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel channel12 = new NotificationChannel(CHANNEL_12_ID, "Education", NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel channel13 = new NotificationChannel(CHANNEL_13_ID, "ESports", NotificationManager.IMPORTANCE_HIGH);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
            notificationManager.createNotificationChannel(channel3);
            notificationManager.createNotificationChannel(channel4);
            notificationManager.createNotificationChannel(channel5);
            notificationManager.createNotificationChannel(channel6);
            notificationManager.createNotificationChannel(channel7);
            notificationManager.createNotificationChannel(channel8);
            notificationManager.createNotificationChannel(channel9);
            notificationManager.createNotificationChannel(channel10);
            notificationManager.createNotificationChannel(channel11);
            notificationManager.createNotificationChannel(channel12);
            notificationManager.createNotificationChannel(channel13);
        }
    }
}
