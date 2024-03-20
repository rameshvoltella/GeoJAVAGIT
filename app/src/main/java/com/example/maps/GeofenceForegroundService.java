package com.example.maps;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/*public class GeofenceForegroundService extends Service {

    private static final String TAG = "GeofenceForegroundService";
    private static final int NOTIFICATION_ID = 11123;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification());
        return START_STICKY;
    }

    private Notification createNdotification() {
        // Create a notification channel if targeting Android Oreo or higher
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("geofence_channel", "Geofence Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "geofence_channel")
                .setContentTitle("Geofence Service")
                .setContentText("Running...")
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher);

        // You can add actions, set priority, etc. to the notification builder as needed

        return builder.build();
    }
    private Notification createNotification2() {
        // Create a notification channel if targeting Android Oreo or higher
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("geofence_channel", "Geofence Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Create an intent to launch your main activity
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "geofence_channel")
                .setContentTitle("Geofence Service")
                .setContentText("Running...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true) // Set ongoing to true to make the notification sticky
                .setContentIntent(pendingIntent); // Set the pending intent to launch the main activity when the notification is clicked

        // You can add actions, set priority, etc. to the notification builder as needed

        return builder.build();
    }

    private Notification createNotification() {
        // Create a notification channel if targeting Android Oreo or higher
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("geofence_channel", "Geofence Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Create an intent to launch your main activity
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        // Create an intent to stop the foreground service
        Intent stopServiceIntent = new Intent(this, GeofenceForegroundService.class);
        stopServiceIntent.setAction("STOP_FOREGROUND_SERVICE");
        PendingIntent stopServicePendingIntent = PendingIntent.getService(this, 0, stopServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "geofence_channel")
                .setContentTitle("Geofence Service")
                .setContentText("Running...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true) // Set ongoing to true to make the notification sticky
                .setContentIntent(pendingIntent) // Set the pending intent to launch the main activity when the notification is clicked
                .addAction(R.mipmap.ic_launcher_round, "Stop", stopServicePendingIntent); // Add a stop action button to the notification

        // You can add actions, set priority, etc. to the notification builder as needed

        return builder.build();
    }

    // Method to show geofence triggered notification

}*/
public class GeofenceForegroundService extends Service {

    private static final int NOTIFICATION_ID = 123;
    private static final String CHANNEL_ID = "geofence_channel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals("STOP_FOREGROUND_SERVICE")) {
            // Stop the foreground service
            stopForeground(true);
            stopSelf();
        } else {
            // Start the foreground service with notification
            startForeground(NOTIFICATION_ID, createNotification());
        }
        return START_STICKY;
    }

    private Notification createNotification() {
        // Create a notification channel if targeting Android Oreo or higher
        createNotificationChannel();

        // Create an intent to launch your main activity
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        // Create an intent to stop the foreground service
        Intent stopServiceIntent = new Intent(this, GeofenceForegroundService.class);
        stopServiceIntent.setAction("STOP_FOREGROUND_SERVICE");
        PendingIntent stopServicePendingIntent = PendingIntent.getService(this, 0, stopServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Geofence Service")
                .setContentText("Running...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true) // Set ongoing to true to make the notification sticky
                .setContentIntent(pendingIntent) // Set the pending intent to launch the main activity when the notification is clicked
                .addAction(R.mipmap.ic_launcher_round, "Stop", stopServicePendingIntent); // Add a stop action button to the notification

        return builder.build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Geofence Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
