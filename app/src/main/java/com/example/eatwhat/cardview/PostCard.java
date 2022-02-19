package com.example.eatwhat.cardview;
public class PostCard {

    private String post_title;
    private String post_content;
    private int number_of_likes;
    private int post_image;

    public PostCard(String post_title, String post_content, int number_of_likes, int course_image) {
        this.post_title = post_title;
        this.post_content = post_content;
        this.number_of_likes = number_of_likes;
        this.post_image = post_image;
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

    public int getPost_image() {
        return post_image;
    }

    public void setPost_image(int course_image) {
        this.post_image = post_image;
    }
}
