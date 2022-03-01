package com.example.eatwhat.activity.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.eatwhat.R;
import com.example.eatwhat.databinding.ActivityScrollingBinding;
import com.example.eatwhat.service.BusinessesPojo.Category;
import com.example.eatwhat.service.BusinessesPojo.DetailedBusiness;
import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantPageActivity extends AppCompatActivity {
    private TextView nameTv, categoryTv, restaurant_address, price_level, ratingText, phoneText;
    private ImageView resImage;

    private DetailedBusiness business;

    private final static String TAG = "Restaurant Page Activity";
    private static final String TOKEN = "RO1Oxxrhr0ZE2nvxEvJ0ViejBTWKcLLhPQ7wg6GGPlGiHvjwaLPU2eWlt4myH3BC1CP4RSzIQ7UCFjZ-FBaF_4ToUYHfs6FF6FwipyMuz47xVvlpEr6gDv-2YRQUYnYx";

    private ActivityScrollingBinding binding;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean isCollected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scrolling);

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Firebase: collect this restaurant", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        nameTv = (TextView) findViewById(R.id.restaurant_detail_name);
        categoryTv = (TextView) findViewById(R.id.restaurant_category);
        resImage = (ImageView) findViewById(R.id.restaurant_detail_image);
        price_level = (TextView) findViewById(R.id.price_level);
        restaurant_address = (TextView) findViewById(R.id.restaurant_address);
        ratingText = (TextView) findViewById(R.id.rating);
        phoneText = (TextView) findViewById(R.id.restaurant_phone);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                            .addHeader("Authorization", " Bearer " + TOKEN)
                            .build());
                    Glide.with(this)
                            .load(glideUrl)
                            .into(resImage);

        getRestaurantData();


    }


    public void getRestaurantData() {
        RetrofitClient retrofitClient = new RetrofitClient();
        RestaurantService methods = retrofitClient.getRetrofit().create(RestaurantService.class);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Call<DetailedBusiness> call = methods.getRestaurantById(id);
//        business = call.execute().body();
//        Log.e("getAlias(): ", business.getAlias());
        call.enqueue(new Callback<DetailedBusiness>() {
            @Override
            public void onResponse(Call<DetailedBusiness> call, Response<DetailedBusiness> response) {
                Log.e("onResponse", response.code() + " ");
                if (response.code() == 200){
                    business = response.body();
                    String name = business.getName();
                    String phone = business.getDisplayPhone();
                    List<Category> categoryList = business.getCategories();
                    String address = business.getLocation().getAddress1() + " "
                            + business.getLocation().getCity() + ", "
                            + business.getLocation().getState() + " "
                            + business.getLocation().getZipCode() + " ";
                    String priceLevel = business.getPrice();
                    String rating = String.valueOf(business.getRating());



                    nameTv.setText(name);
                    categoryTv.setText(categoryList.get(0).getTitle());
                    price_level.setText(priceLevel);
                    restaurant_address.setText(address);
                    ratingText.setText(rating);
                    phoneText.setText(phone);

                    Log.e("getAlias(): ", response.body().getAlias());
                }
            }

            @Override
            public void onFailure(Call<DetailedBusiness> call, Throwable t) {
                Log.e("e res" ,t.toString());
            }
        });
    }



}