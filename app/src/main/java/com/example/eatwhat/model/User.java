package com.example.eatwhat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    public String username;
    public String email_addr;
    public String image_url;
    public List<String> preference;


    public User(String username, String email_addr, String image_url){
        this.username = username;
        this.email_addr = email_addr;
        this.image_url = image_url;
        preference = new ArrayList<>();
    }
}