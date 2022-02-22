package com.example.eatwhat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;
import com.example.eatwhat.service.pojo.Business;
import com.example.eatwhat.service.pojo.Restaurant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostCreationActivity extends AppCompatActivity {

    private ImageButton mCancelBtn;


    private Button getData;
    private final static String TAG = "http get test";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_creation);
        cancel_button_init();


        getData = findViewById(R.id.getData);
        getData.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                RestaurantService methods = RetrofitClient.getRetrofitInstance().create(RestaurantService.class);

                String location = "Santa Clara University";
                String restaurantId = "WavvLdfdP6g8aZTtbBQHTw";
//                Call<Restaurant> call = methods.getRestaurantByLocation(location, null, null, null);
                Call<Business> call = methods.getRestaurantById(restaurantId);
                call.enqueue(new Callback<Business>() {
                    @Override
                    public void onResponse(Call<Business> call, Response<Business> response) {
                        Log.e(TAG, "onResponse: code" + response.code());
//                        for(Business res: response.body().getBusinesses() ){
//                            Log.e(TAG, res.getName());
//                        }
                        Log.e(TAG, response.body().getName());
                    }

                    @Override
                    public void onFailure(Call<Business> call, Throwable t) {

                    }
                });

            }
        });

    }

    private void cancel_button_init() {
        mCancelBtn = findViewById(R.id.post_creation_cancel_btn);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}