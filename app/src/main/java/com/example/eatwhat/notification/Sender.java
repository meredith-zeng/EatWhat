package com.example.eatwhat.notification;

import com.google.gson.annotations.SerializedName;

public class Sender {
    @SerializedName("notification")
    NotificationData data;
    @SerializedName("to")
    String to;

    public Sender(String to, NotificationData data) {
        this.to = to;
        this.data = data;

    }
}
