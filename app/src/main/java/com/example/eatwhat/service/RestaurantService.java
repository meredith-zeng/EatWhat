package com.example.eatwhat.service;

import com.example.eatwhat.service.pojo.Business;
import com.example.eatwhat.service.pojo.Restaurant;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RestaurantService {
    @GET("businesses/search")
    Call<Restaurant> queryRestaurantByLocation(@Query("location") String location,
                                             @Query("latitude") String latitude,
                                             @Query("longitude") String longitude,
                                             @Query("categories") String categories,
                                             @Query("limit") int limit,
                                             @Query("offset") int offset);

    @GET("businesses")
    Call<Business> getRestaurantById(@Query("id") String id);

    @GET
    Call<ResponseBody> getImage(@Url String imageUrl);

}
