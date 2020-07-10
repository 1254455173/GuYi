package com.example.guyi.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 陈 on 2020/7/7.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){  // 异步
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
