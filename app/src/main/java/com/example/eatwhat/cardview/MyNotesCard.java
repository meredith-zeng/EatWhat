package com.example.eatwhat.cardview;
import java.io.Serializable;
public class MyNotesCard implements Serializable{


    private String myNoteTitle;
    private String myNoteContent;
    private int myNoteNumberOfLikes;
    private int myNoteImageUrl;
    private double star;

    public MyNotesCard(String myNoteTitle, String myNoteContent, int myNoteNumberOfLikes, int myNoteImageUrl, double star) {
        this.myNoteTitle = myNoteTitle;
        this.myNoteContent = myNoteContent;
        this.myNoteNumberOfLikes = myNoteNumberOfLikes;
        this.myNoteImageUrl = myNoteImageUrl;
        this.star = star;
    }

    public String getMyNoteTitle() {
        return myNoteTitle;
    }

    public void setMyNoteTitle(String myNoteTitle) {
        this.myNoteTitle = myNoteTitle;
    }

    public String getMyNoteContent() {
        return myNoteContent;
    }

    public void setMyNoteContent(String myNoteContent) {
        this.myNoteContent = myNoteContent;
    }

    public int getMyNoteNumberOfLikes() {
        return myNoteNumberOfLikes;
    }

    public void setMyNoteNumberOfLikes(int myNoteNumberOfLikes) {
        this.myNoteNumberOfLikes = myNoteNumberOfLikes;
    }

    public int getMyNoteImageUrl() {
        return myNoteImageUrl;
    }

    public void setMyNoteImageUrl(int myNoteImageUrl) {
        this.myNoteImageUrl = myNoteImageUrl;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    @Override
    public String toString() {
        return "MyNotesCard{" +
                "myNoteTitle='" + myNoteTitle + '\'' +
                ", myNoteContent='" + myNoteContent + '\'' +
                ", myNoteNumberOfLikes=" + myNoteNumberOfLikes +
                ", myNoteImageUrl=" + myNoteImageUrl +
                ", star=" + star +
                '}';
    }
}
