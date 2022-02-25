package com.example.eatwhat.activity.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.eatwhat.R;
import com.google.android.material.snackbar.Snackbar;

public class RestaurantPageActivity extends AppCompatActivity {
    private TextView nameTv, categoryTv, restaurant_address, price_level;
    private ImageView restaurantImage;
    private final static String TAG = "Restaurant Page Activity";
    private static final String TOKEN = "RO1Oxxrhr0ZE2nvxEvJ0ViejBTWKcLLhPQ7wg6GGPlGiHvjwaLPU2eWlt4myH3BC1CP4RSzIQ7UCFjZ-FBaF_4ToUYHfs6FF6FwipyMuz47xVvlpEr6gDv-2YRQUYnYx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);

        Intent intent = getIntent();

        TextView nameTv = (TextView) findViewById(R.id.restaurant_detail_name);
        TextView categoryTv = (TextView) findViewById(R.id.restaurant_category);
        ImageView resImage = (ImageView) findViewById(R.id.restaurant_detail_image);

        String name = intent.getStringExtra("title");
        String imageUrl = intent.getStringExtra("imageUrl");
        String category = intent.getStringExtra("content");
//        Log.e(TAG, name);

        GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("Authorization", " Bearer " + TOKEN)
                .build());
        Glide.with(this)
                .load(glideUrl)
                .into(resImage);

        nameTv.setText(name);
        categoryTv.setText(category);



    }
}