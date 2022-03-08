package com.example.eatwhat.notification;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.eatwhat.R;
import com.example.eatwhat.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {


    private static final String TAG = "PushNotificationService";



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Message Received");

        String sented = remoteMessage.getNotification().getTitle();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            if(sented.equals(firebaseUser.getUid())){
                showNotification(sented, remoteMessage.getNotification().getBody());
                //sendNotification(remoteMessage);
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendNotification(RemoteMessage remoteMessage){
        String title = "EatWhat";
        String body = remoteMessage.getData().get("sent_name");


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.hoticon)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);

        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(1, builder.build());

    }


    public void showNotification(String title,
                                 String message) {
        Log.d(TAG, "show Notification");

        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, MainActivity.class);
        // Assign channel ID
        String channel_id = "notification_channel";
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.drawable.hoticon)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);



        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel
                = new NotificationChannel(
                channel_id, "web_app",
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(
                notificationChannel);

        notificationManager.notify(0, builder.build());
    }

}
