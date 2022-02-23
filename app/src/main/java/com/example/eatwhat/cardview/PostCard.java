package com.example.eatwhat.cardview;

public class PostCard{


    private String post_title;
    private String post_content;
    private int number_of_likes;
    private String post_image_url;

    public PostCard(String post_title, String post_content, int number_of_likes, String post_image_url) {
        this.post_title = post_title;
        this.post_content = post_content;
        this.number_of_likes = number_of_likes;
        this.post_image_url = post_image_url;
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

    public String getPost_image_url() {
        return post_image_url;
    }

    public void setPost_image_url(String post_image_url) {
        this.post_image_url = post_image_url;
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
