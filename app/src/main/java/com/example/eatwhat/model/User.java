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

    public User(String uid, String username, String email_addr, String image_url) {
        this.uid = uid;
        this.username = username;
        this.email_addr = email_addr;
        this.image_url = image_url;
        preference = new ArrayList<>();
    }

    public User(){}


    public User(String username, String email_addr, String image_url){
        this.username = username;
        this.email_addr = email_addr;
        this.image_url = image_url;
        preference = new ArrayList<>();
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


}