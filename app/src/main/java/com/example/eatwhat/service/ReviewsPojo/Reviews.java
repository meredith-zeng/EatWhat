package com.example.eatwhat.service.ReviewsPojo;

import com.example.eatwhat.service.RestaurantPojo.Business;
import com.example.eatwhat.service.RestaurantPojo.Region;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reviews {
    @SerializedName("reviews")
    @Expose
    private List<SingleReview> reviews = null;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("possible_languages")
    @Expose
    private List<String> languages;

    public Reviews(List<SingleReview> reviews, int total, List<String> languages) {
        this.reviews = reviews;
        this.total = total;
        this.languages = languages;
    }

    public List<SingleReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<SingleReview> reviews) {
        this.reviews = reviews;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
