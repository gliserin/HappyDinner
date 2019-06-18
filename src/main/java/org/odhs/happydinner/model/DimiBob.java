package org.odhs.happydinner.model;

import com.google.gson.annotations.SerializedName;

public class DimiBob {

    public DimiBob() {}

    public DimiBob(String breakfast, String lunch, String dinner, String snack, String date) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.snack = snack;
        this.date = date;
    }

    @Override
    public String toString() {
        return "아침 : " + breakfast + "\n점심 : " + lunch + "\n저녁 : " + dinner + "\n날짜 : " + date + "\n";
    }

    @SerializedName("breakfast")
    public String breakfast;

    @SerializedName("lunch")
    public String lunch;

    @SerializedName("dinner")
    public String dinner;

    @SerializedName("snack")
    public String snack;

    @SerializedName("date")
    public String date;

}
