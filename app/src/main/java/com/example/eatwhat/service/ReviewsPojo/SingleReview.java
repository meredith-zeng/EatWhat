package com.example.eatwhat.service.ReviewsPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleReview {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("user")
    @Expose
    private ReviewUser reviewUser;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("time_created")
    @Expose
    private String timeCreated;
    @SerializedName("url")
    @Expose
    private String url;

    public SingleReview(String id, float rating, ReviewUser reviewUser, String text, String timeCreated, String url) {
        this.id = id;
        this.rating = rating;
        this.reviewUser = reviewUser;
        this.text = text;
        this.timeCreated = timeCreated;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public ReviewUser getReviewUser() {
        return reviewUser;
    }

    public void setReviewUser(ReviewUser reviewUser) {
        this.reviewUser = reviewUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
