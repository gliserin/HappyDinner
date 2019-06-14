package model;

import com.google.gson.annotations.SerializedName;

public class DimiBob {

    @Override
    public String toString() {
        return "아침 : " + breakfast + "\n점심 : " + lunch + "\n저녁 : " + dinner +
                "\n간식 : " + snack + "\n날짜 : " + date;
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
