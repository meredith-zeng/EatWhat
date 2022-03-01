package com.example.eatwhat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    public String uid;
    public String username;
    public String email_addr;
    public String image_url;
    public List<String> preference;
    public List<String> collected_restaurant;
    public List<String> liked_post;

    public User(String uid, String username, String email_addr, String image_url, List<String> preference, List<String> collected_restaurant, List<String> liked_post) {
        this.uid = uid;
        this.username = username;
        this.email_addr = email_addr;
        this.image_url = image_url;
        this.preference = preference;
        this.collected_restaurant = collected_restaurant;
        this.liked_post = liked_post;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail_addr() {
        return email_addr;
    }

    public void setEmail_addr(String email_addr) {
        this.email_addr = email_addr;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<String> getPreference() {
        return preference;
    }

    public void setPreference(List<String> preference) {
        this.preference = preference;
    }

    public List<String> getCollected_restaurant() {
        return collected_restaurant;
    }

    public void setCollected_restaurant(List<String> collected_restaurant) {
        this.collected_restaurant = collected_restaurant;
    }

    public List<String> getLiked_post() {
        return liked_post;
    }

    public void setLiked_post(List<String> liked_post) {
        this.liked_post = liked_post;
    }
}