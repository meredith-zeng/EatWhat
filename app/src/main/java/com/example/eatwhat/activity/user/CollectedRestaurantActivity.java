package com.example.eatwhat.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_restaurant);

        restaurantService = retrofitClient.getRetrofit().create(RestaurantService.class);
        retrofitClient = new RetrofitClient();
        userCollectedRestaurantList = new ArrayList<>();

        getCollectedIdFromFirebase();
        getRestaurantByIdFromYelp();


    }

    private void getCollectedIdFromFirebase(){
        // get current user's collected list
        String uid = FirebaseAuth.getInstance().getUid();
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

                    }
                }
            }
        });
    }

    private void getRestaurantByIdFromYelp(){
        final int[] dataSize = {userCollectedRestaurantList.size()};
        if (dataSize[0] == 0){
            getDataFlag = true;
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
                        dataSize[0]--;
                    }
                }

                @Override
                public void onFailure(Call<DetailedBusiness> call, Throwable t) {
                    Log.e("e res" ,t.toString());
                }
            });
        }
        if (dataSize[0] == 0){
            getDataFlag = true;
        }
        initRecyclerView();
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (restaurantCardsList == null || restaurantCardsList.size() == 0){
            return;
        }

        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(this, restaurantCardsList);
        restaurantAdapter.setRecyclerViewOnItemClickListener(new RestaurantAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(CollectedRestaurantActivity.this, RestaurantPageActivity.class);
                intent.putExtra("title", restaurantCardArrayList.get(position).getTitle());
                intent.putExtra("content", restaurantCardArrayList.get(position).getContent());
                intent.putExtra("imageUrl", restaurantCardArrayList.get(position).getRestaurantImageUrl());
                intent.putExtra("id", restaurantCardArrayList.get(position).getId());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(restaurantAdapter);
    }
}