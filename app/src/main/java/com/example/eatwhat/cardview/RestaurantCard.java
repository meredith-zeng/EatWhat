package com.example.eatwhat.cardview;


public class RestaurantCard {
    private String restaurantImageUrl;
    private String title;
    private String content;
    private boolean isCollect;


    public String getRestaurantImageUrl() {
        return restaurantImageUrl;
    }

    public RestaurantCard(String restaurantImageUrl, String title, String content, boolean isCollect) {
        this.restaurantImageUrl = restaurantImageUrl;
        this.title = title;
        this.content = content;
        this.isCollect = isCollect;
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

    @Override
    public String toString() {
        return "RestaurantCard{" +
                "restaurantImageUrl='" + restaurantImageUrl + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", isCollect=" + isCollect +
                '}';
    }
}
