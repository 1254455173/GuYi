package com.example.guyi.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 陈 on 2020/7/8.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public UpDate update;

    public class UpDate{

        @SerializedName("loc")
        public String updateTime;
    }
}
