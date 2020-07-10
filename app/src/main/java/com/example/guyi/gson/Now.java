package com.example.guyi.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 陈 on 2020/7/8.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }
}
