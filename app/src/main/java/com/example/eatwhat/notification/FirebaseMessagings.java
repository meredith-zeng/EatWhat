package com.example.eatwhat.notification;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.example.eatwhat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagings extends FirebaseMessagingService {



    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sent");
        String user = remoteMessage.getData().get("user");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            if(sented.equals(firebaseUser.getUid())){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    sendOreoNotification(remoteMessage);
                }
                else{
                    sendNormalNotification(remoteMessage);
                }
            }
        }
    }

    private void sendNormalNotification(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));

        Bundle bundle = new Bundle();
        bundle.putString("friendid", user);



        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        SharedPreferences.Editor predsefits = sharedPreferences.edit();
        predsefits.putString("friendid", user);
        predsefits.apply();

        RemoteInput remoteInput = new RemoteInput.Builder("key").setLabel("").build();
        Intent replyIntent;
        PendingIntent pIntentreply = null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            replyIntent = new Intent(this, PushNotificationService.class);
            pIntentreply = PendingIntent.getBroadcast(this, 0, replyIntent, 0);
        }




    }

    private void sendOreoNotification(RemoteMessage remoteMessage){

    }

}
