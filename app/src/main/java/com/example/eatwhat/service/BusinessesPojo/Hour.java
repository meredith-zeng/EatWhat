
package com.example.eatwhat.service.BusinessesPojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hour {

    @SerializedName("open")
    @Expose
    private List<Open> open = null;
    @SerializedName("hours_type")
    @Expose
    private String hoursType;
    @SerializedName("is_open_now")
    @Expose
    private Boolean isOpenNow;

    public List<Open> getOpen() {
        return open;
    }

    public void setOpen(List<Open> open) {
        this.open = open;
    }

    public String getHoursType() {
        return hoursType;
    }

    public void setHoursType(String hoursType) {
        this.hoursType = hoursType;
    }

    public Boolean getIsOpenNow() {
        return isOpenNow;
    }

    public void setIsOpenNow(Boolean isOpenNow) {
        this.isOpenNow = isOpenNow;
    }

}
