package com.example.eatwhat.service;

import com.example.eatwhat.service.pojo.Business;
import com.example.eatwhat.service.pojo.Restaurant;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestaurantService {
    @GET("businesses/search")
    Call<Restaurant> getRestaurant(@Query("location") String location,
                                             @Query("latitude") String latitude,
                                             @Query("longitude") String longitude,
                                             @Query("categories") String categories,
                                             @Query("limit") int limit,
                                             @Query("offset") int offset);

    @GET("businesses")
    Call<Business> getRestaurantById(@Query("id") String id);
}
