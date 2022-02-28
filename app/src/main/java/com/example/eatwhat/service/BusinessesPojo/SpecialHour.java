
package com.example.eatwhat.service.BusinessesPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpecialHour {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("is_closed")
    @Expose
    private Object isClosed;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("is_overnight")
    @Expose
    private Boolean isOvernight;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Object getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Object isClosed) {
        this.isClosed = isClosed;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Boolean getIsOvernight() {
        return isOvernight;
    }

    public void setIsOvernight(Boolean isOvernight) {
        this.isOvernight = isOvernight;
    }

}
