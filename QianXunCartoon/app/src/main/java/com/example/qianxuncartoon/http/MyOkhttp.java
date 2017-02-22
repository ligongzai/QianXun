package com.example.qianxuncartoon.http;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Created by 咸鱼 on 2017/2/16.
 */

public class MyOkhttp {

    public static OkHttpClient client = new OkHttpClient();

    public static String get(String url){

        try {
            client.newBuilder().connectTimeout(100000, TimeUnit.MILLISECONDS);

            Request request = new Request.Builder().url(url).build();

           Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }


}
