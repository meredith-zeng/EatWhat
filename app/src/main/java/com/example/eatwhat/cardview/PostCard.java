package com.example.eatwhat.cardview;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PostCard implements Serializable{

    private String uid;
    private String postId;
    private String post_title;
    private String post_content;
    private long number_of_likes;
    private String post_image_url;
    private String restuarant_name;
    private float star;
    private List<String> likedUidList;

    public PostCard(String uid, String postId, String post_title, String post_content, long number_of_likes, String post_image_url, String restuarant_name, float star) {
        this.uid = uid;
        this.postId = postId;
        this.post_title = post_title;
        this.post_content = post_content;
        this.number_of_likes = number_of_likes;
        this.post_image_url = post_image_url;
        this.restuarant_name = restuarant_name;
        this.star = star;
        this.likedUidList = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public long getNumber_of_likes() {
        return number_of_likes;
    }

    public void setNumber_of_likes(long number_of_likes) {
        this.number_of_likes = number_of_likes;
    }

    public String getPost_image_url() {
        return post_image_url;
    }

    public void setPost_image_url(String post_image_url) {
        this.post_image_url = post_image_url;
    }

    public String getRestuarant_name() {
        return restuarant_name;
    }

    public void setRestuarant_name(String restuarant_name) {
        this.restuarant_name = restuarant_name;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public List<String> getLikedUidList() {
        return likedUidList;
    }

    public void setLikedUidList(List<String> likedUidList) {
        this.likedUidList = likedUidList;
    }

    public PostCard() {
    }


}
