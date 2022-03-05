package com.example.eatwhat.cardview;

public class ReviewCard {
    private String reviewText;
    private String reviewAuthor;
    private String reviewTime;

    public ReviewCard(String reviewText, String reviewAuthor, String reviewTime) {
        this.reviewText = reviewText;
        this.reviewAuthor = reviewAuthor;
        this.reviewTime = reviewTime;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }
}
