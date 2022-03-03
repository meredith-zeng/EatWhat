package com.example.eatwhat.cardview;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostCard implements Serializable{

    private String uid;
    private String postId;
    private String post_title;
    private String post_content;
    private int number_of_likes;
    private int post_image_url;
    private String restuarant_name;
    private float star;
    private List<String> imageIds;

    public List<String> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<String> imageIds) {
        this.imageIds = imageIds;
    }
    public PostCard(){

    }

    public PostCard(String uid, String postId, String post_title, String post_content, int number_of_likes, String restuarant_name, float star, List<String> imageIds){
        this.uid = uid;
        this.postId = postId;
        this.post_title = post_title;
        this.post_content = post_content;
        this.number_of_likes = number_of_likes;
        this.restuarant_name = restuarant_name;
        this.star = star;
        imageIds = new ArrayList<>();
    }


    public PostCard(String uid, String postId, String post_title, String post_content, int number_of_likes,
                    int post_image_url, String restuarant_name, float star, List<String> iamgeIds) {
        this.uid = uid;
        this.postId = postId;
        this.post_title = post_title;
        this.post_content = post_content;
        this.number_of_likes = number_of_likes;
        this.post_image_url = post_image_url;
        this.restuarant_name = restuarant_name;
        this.star = star;
        this.imageIds = new ArrayList<>();
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public PostCard(String uid, String post_title, String post_content, int number_of_likes, int post_image_url, String restuarant_name, float star) {
        this.uid = uid;
        this.post_title = post_title;
        this.post_content = post_content;
        this.number_of_likes = number_of_likes;
        this.post_image_url = post_image_url;
        this.restuarant_name = restuarant_name;
        this.star = star;
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

    public int getNumber_of_likes() {
        return number_of_likes;
    }

    public void setNumber_of_likes(int number_of_likes) {
        this.number_of_likes = number_of_likes;
    }

    public int getPost_image_url() {
        return post_image_url;
    }

    public void setPost_image_url(int post_image_url) {
        this.post_image_url = post_image_url;
    }

    public String getRestuarant_name() {
        return restuarant_name;
    }

    public void setPost_image(String restuarant_name) {
        this.restuarant_name = restuarant_name;
    }

    @Override
    public String toString() {
        return "PostCard{" +
                "post_title='" + post_title + '\'' +
                ", post_content='" + post_content + '\'' +
                ", number_of_likes=" + number_of_likes +
                ", post_image_url='" + post_image_url + '\'' +
                '}';
    }

}
