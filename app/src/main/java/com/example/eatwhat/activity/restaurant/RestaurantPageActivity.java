package com.example.eatwhat.activity.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.eatwhat.R;
import com.example.eatwhat.activity.user.ReviewHistoryActivity;
import com.example.eatwhat.adapter.ReviewAdapter;
import com.example.eatwhat.adapter.ReviewHistoryAdapter;
import com.example.eatwhat.cardview.RestaurantCard;
import com.example.eatwhat.cardview.ReviewCard;
import com.example.eatwhat.cardview.ReviewHistoryCard;
import com.example.eatwhat.databinding.ActivityScrollingBinding;
import com.example.eatwhat.model.User;
import com.example.eatwhat.service.BusinessesPojo.Category;
import com.example.eatwhat.service.BusinessesPojo.DetailedBusiness;
import com.example.eatwhat.service.RestaurantPojo.Business;
import com.example.eatwhat.service.RestaurantPojo.Restaurant;
import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;
import com.example.eatwhat.service.ReviewsPojo.Reviews;
import com.example.eatwhat.service.ReviewsPojo.SingleReview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantPageActivity extends AppCompatActivity {
    private TextView nameTv, restaurant_address, price_level, ratingText, phoneText;
    private ImageView resImage, address_icon, call_icon;
    private FloatingActionButton floatingActionButton;

    private DetailedBusiness business;

    private final static String TAG = "Restaurant Page Activity";
    private static final String TOKEN = "RO1Oxxrhr0ZE2nvxEvJ0ViejBTWKcLLhPQ7wg6GGPlGiHvjwaLPU2eWlt4myH3BC1CP4RSzIQ7UCFjZ-FBaF_4ToUYHfs6FF6FwipyMuz47xVvlpEr6gDv-2YRQUYnYx";

    private ActivityScrollingBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private boolean isCollected;

    private ArrayList<ReviewCard> ReviewCardArrayList = new ArrayList<>();
    private String businessID = null;

    private LinearLayout address_direction;
    private LinearLayout call_phone_liearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scrolling);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FloatingActionButton fab = binding.fab;

        String uid = FirebaseAuth.getInstance().getUid();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("user").document(uid);
        checkIsCollected(fab, docRef);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                User user = document.toObject(User.class);
                                List<String> collectedList = user.getCollected_restaurant();

                                Log.d(TAG, "current collectedList" + collectedList.size());

                                    Intent intent = getIntent();
                                    String id = intent.getStringExtra("id");
                                    if(!collectedList.contains(id)){
                                        collectedList.add(id);
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("collected_restaurant", collectedList);
                                        fab.setImageResource (R.drawable.collected_36);
                                    }else {
                                        collectedList.remove(id);
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("collected_restaurant", collectedList);
                                        fab.setImageResource (R.drawable.not_collect_36);
                                    }


                                docRef
                                        .update("collected_restaurant", collectedList)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully updated!");

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error updating document", e);
                                            }
                                        });



                            }
                        }
                    }
                });

            }
        });

        nameTv = (TextView) findViewById(R.id.restaurant_detail_name);
        resImage = (ImageView) findViewById(R.id.restaurant_detail_image);
        price_level = (TextView) findViewById(R.id.price_level);
        restaurant_address = (TextView) findViewById(R.id.restaurant_address);
        ratingText = (TextView) findViewById(R.id.rating);
        phoneText = (TextView) findViewById(R.id.restaurant_phone);
        address_icon = (ImageView)findViewById(R.id.address_icon);
        call_icon = (ImageView)findViewById(R.id.call_icon);
        address_direction = (LinearLayout)findViewById(R.id.address_direction_layout);
        call_phone_liearlayout = (LinearLayout)findViewById(R.id.call_phone_layout);


        restaurant_address.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(restaurant_address.getText().toString());
                Toast.makeText(getApplicationContext(), "Address is been copied!", Toast.LENGTH_SHORT).show();
                return true;
            };
        });

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                            .addHeader("Authorization", " Bearer " + TOKEN)
                            .build());
                    Glide.with(this)
                            .load(glideUrl)
                            .into(resImage);

        getRestaurantData();
        getReviewData();
    }


    public void getRestaurantData() {
        RetrofitClient retrofitClient = new RetrofitClient();
        RestaurantService methods = retrofitClient.getRetrofit().create(RestaurantService.class);
        Intent intent = getIntent();
        businessID = intent.getStringExtra("id");
        Call<DetailedBusiness> call = methods.getRestaurantById(businessID);
        call.enqueue(new Callback<DetailedBusiness>() {
            @Override
            public void onResponse(Call<DetailedBusiness> call, Response<DetailedBusiness> response) {
                Log.e("onResponse", response.code() + " ");
                if (response.code() == 200){
                    business = response.body();
                    String name = business.getName();
                    String phone = business.getDisplayPhone();
                    String address = business.getLocation().getAddress1() + " "
                            + business.getLocation().getCity() + ", "
                            + business.getLocation().getState() + " "
                            + business.getLocation().getZipCode() + " ";
                    String priceLevel = business.getPrice();
                    String rating = String.valueOf(business.getRating());

                    Double latitude = business.getCoordinates().getLatitude();
                    Double longitude = business.getCoordinates().getLongitude();

                    nameTv.setText(name);
                    price_level.setText(priceLevel);
                    restaurant_address.setText(address);
                    ratingText.setText(rating);
                    phoneText.setText(phone);



                    address_direction.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            // Create a Uri from an intent string. Use the result to create an Intent.
                            Uri gmmIntentUri = Uri.parse("geo:0,0" + "?q=" + address);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    });
                    call_phone_liearlayout.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            callPhone(phone);
                        }
                    });
                    Log.e("getAlias(): ", response.body().getAlias());
                }
            }

            @Override
            public void onFailure(Call<DetailedBusiness> call, Throwable t) {
                Log.e("e res" ,t.toString());
            }
        });
    }

    public void checkIsCollected(FloatingActionButton fab, DocumentReference docRef) {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        User user = document.toObject(User.class);
                        List<String> collectedList = user.getCollected_restaurant();

                        Intent intent = getIntent();
                        String id = intent.getStringExtra("id");

                            if(collectedList.contains(id)) {
                                isCollected = true;
                                fab.setImageResource (R.drawable.collected_36);
                            }else {
                                fab.setImageResource (R.drawable.not_collect_36);
                            }

                        }
                    }
                }
            });
    }

    private void setRecyclerViewForReviews() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.review_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RestaurantPageActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ReviewAdapter myAdapter = new ReviewAdapter(RestaurantPageActivity.this, ReviewCardArrayList);
        recyclerView.setAdapter(myAdapter);
    }

    private void getReviewData() {
        RetrofitClient retrofitClient = new RetrofitClient();
        RestaurantService methods = retrofitClient.getRetrofit().create(RestaurantService.class);
        System.out.println(businessID);
        Call<Reviews> call = methods.queryReviewByBusinessID(businessID);
        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                if (response.code() == 200) {
                    Log.d(TAG, " response 200");

                    for (int i = 0; i < 50 && i < response.body().getReviews().size(); i++) {
                        SingleReview review = response.body().getReviews().get(i);
                        ReviewCard reviewCard = new ReviewCard(review.getText(), review.getReviewUser().getName(), review.getTimeCreated());
                        ReviewCardArrayList.add(reviewCard);
                    }
                    setRecyclerViewForReviews();
                    Log.d(TAG, " load review finished");
                }
                else {
                    Log.d(TAG, " response not 200");
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                Log.e("e res" ,t.toString());
            }
        });
    }

    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}