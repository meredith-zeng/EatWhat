package com.example.eatwhat.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.eatwhat.R;
import com.example.eatwhat.activity.restaurant.RestaurantPageActivity;
import com.example.eatwhat.adapter.RestaurantAdapter;
import com.example.eatwhat.cardview.PostCard;
import com.example.eatwhat.cardview.RestaurantCard;
import com.example.eatwhat.cardview.ReviewCard;
import com.example.eatwhat.model.User;
import com.example.eatwhat.service.BusinessesPojo.DetailedBusiness;
import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;
import com.example.eatwhat.service.ReviewsPojo.Reviews;
import com.example.eatwhat.service.ReviewsPojo.SingleReview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectedRestaurantActivity extends AppCompatActivity {
    private ArrayList<RestaurantCard> restaurantCardArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final String TAG = "CollectedRestaurantActivity";
    private List<String> userCollectedRestaurantList;
    private ArrayList<RestaurantCard> restaurantCardsList;
    private RetrofitClient retrofitClient;
    private RestaurantService restaurantService;
    private boolean getDataFlag = false;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_restaurant);

        restaurantService = retrofitClient.getRetrofit().create(RestaurantService.class);
        retrofitClient = new RetrofitClient();
        recyclerView = (RecyclerView) findViewById(R.id.collected_restaurant_recyclerview);
        userCollectedRestaurantList = new ArrayList<>();
        restaurantCardsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getCollectedIdFromFirebase();
        pullDownRefresh();

    }

    private void getCollectedIdFromFirebase(){
        // get current user's collected list
        String uid = mAuth.getUid();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("user").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        User user = document.toObject(User.class);
                        List<String> collectedList = user.getCollected_restaurant();

                        userCollectedRestaurantList =  collectedList;
                        getRestaurantByIdFromYelp();
                    }
                }
            }
        });
    }

    private void getRestaurantByIdFromYelp(){
        int dataSize = userCollectedRestaurantList.size();
        if (dataSize == 0){
            getDataFlag = true;
            initRecyclerView();
            return;
        }
        for(String businessID: userCollectedRestaurantList){
            Call<DetailedBusiness> call = restaurantService.getRestaurantById(businessID);
            call.enqueue(new Callback<DetailedBusiness>() {
                @Override
                public void onResponse(Call<DetailedBusiness> call, Response<DetailedBusiness> response) {
                    if (response.code() == 200) {
                        DetailedBusiness business = response.body();
                        Float rating = ((Double) business.getRating()).floatValue();
                        RestaurantCard restaurantCard = new RestaurantCard(business.getImageUrl(), business.getName(), business.getCategories().get(0).getTitle(), rating, business.getId());
                        restaurantCardsList.add(restaurantCard);
                        if(restaurantCardsList.size() == dataSize){
                            initRecyclerView();
                        }
                    }
                }

                @Override
                public void onFailure(Call<DetailedBusiness> call, Throwable t) {
                    Log.e("e res" ,t.toString());
                }
            });
        }

    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(this, restaurantCardsList);
        restaurantAdapter.setRecyclerViewOnItemClickListener(new RestaurantAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(CollectedRestaurantActivity.this, RestaurantPageActivity.class);
                intent.putExtra("title", restaurantCardsList.get(position).getTitle());
                intent.putExtra("content", restaurantCardsList.get(position).getContent());
                intent.putExtra("imageUrl", restaurantCardsList.get(position).getRestaurantImageUrl());
                intent.putExtra("id", restaurantCardsList.get(position).getId());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(restaurantAdapter);
    }


    private void pullDownRefresh() {
        SwipeRefreshLayout swipe = findViewById(R.id.swiperefresh_collect_restaurant);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                restaurantCardsList.clear();
                getCollectedIdFromFirebase();
                swipe.setRefreshing(false);
            }
        });
    }


}