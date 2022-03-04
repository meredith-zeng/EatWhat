package com.example.eatwhat.service;

import com.example.eatwhat.service.BusinessesPojo.DetailedBusiness;
import com.example.eatwhat.service.RestaurantPojo.Restaurant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RestaurantService {
    @GET("businesses/search")
    Call<Restaurant> queryRestaurantByLocation(@Query("location") String location,
//                                             @Query("latitude") String latitude,
//                                             @Query("longitude") String longitude,
//                                             @Query("categories") String categories,
                                             @Query("limit") int limit,
                                             @Query("offset") int offset);

    @GET("businesses/{id}")
    Call<DetailedBusiness> getRestaurantById(@Path("id") String id);

    @GET("businesses/search")
    Call<Restaurant> queryRestaurantByCategory(@Query("location") String location,
                                               @Query("categories") String categories,
                                               @Query("sort_by")  String sortBy,
                                               @Query("limit") int limit,
                                               @Query("offset") int offset);



}
