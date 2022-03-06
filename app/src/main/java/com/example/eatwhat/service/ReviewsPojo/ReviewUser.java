package com.example.eatwhat.service.ReviewsPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewUser {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("profile_url")
    @Expose
    private String profileURL;
    @SerializedName("image_url")
    @Expose
    private String imageURL;
    @SerializedName("name")
    @Expose
    private String name;

    public ReviewUser(String id, String profileURL, String imageURL, String name) {
        this.id = id;
        this.profileURL = profileURL;
        this.imageURL = imageURL;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
