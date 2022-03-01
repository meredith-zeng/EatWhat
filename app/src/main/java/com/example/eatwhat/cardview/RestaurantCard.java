package com.example.eatwhat.cardview;


public class RestaurantCard {
    private String restaurantImageUrl;
    private String title;
    private String content;
    private boolean isCollect;
    private String id;

    public RestaurantCard(String restaurantImageUrl, String title, String content, boolean isCollect, String id) {
        this.restaurantImageUrl = restaurantImageUrl;
        this.title = title;
        this.content = content;
        this.isCollect = isCollect;
        this.id = id;
    }

    public String getRestaurantImageUrl() {
        return restaurantImageUrl;
    }

    public void setRestaurantImageUrl(String restaurantImageUrl) {
        this.restaurantImageUrl = restaurantImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
