package com.example.eatwhat.activity.post;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.eatwhat.R;
import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;
import com.example.eatwhat.service.pojo.Business;
import com.example.eatwhat.service.pojo.Restaurant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostCreationActivity extends AppCompatActivity {

    private ImageButton mCancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_creation);
        cancel_button_init();
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