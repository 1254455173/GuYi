package com.example.guyi.gson;

/**
 * Created by 陈 on 2020/7/8.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 总的实例类类引用更改创建的各个实体类
 */

public class Weather {
    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
