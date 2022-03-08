package com.example.eatwhat.notification;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APISERVICE {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAxzshjFw:APA91bF3Zb7nfP0VwMZv8MJqR749gOri36erQVkRRmQHVgCs-WKsrj1Y-l9lR1nJppL1FuZ0cqJAVRHusVFc6t6W-aUg-WAZfQuS0ILw4nNEy58okobYd330xvjK-jWYT6lAdy-3bPHP"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);

}
