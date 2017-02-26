package com.example.qianxuncartoon;

import android.graphics.Bitmap;
import android.renderscript.RenderScript;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qianxuncartoon.utils.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 2017/2/19.
 */
public class RequestManager {
    private static RequestQueue mRequestQueue;

    public static <T> void post(final String app_url, final String tag_url, final HashMap<String, String> parameter, Class<T> clazz,
                                final HttpResponeCallBack callback) {
        post(app_url, tag_url, parameter, clazz, callback, RenderScript.Priority.NORMAL);
    }

    private static <T> void post(final String app_url, final String url, final HashMap<String, String> parameter, final Class<T> clazz, final HttpResponeCallBack callback, RenderScript.Priority normal) {
        if (callback == null) {
            callback.onResponeStart(url); //回调请求开始
        }
        initRequestQueue();

        //将公共的接口前缀和接口名称拼接
        StringBuilder builder = new StringBuilder(app_url);
        builder.append(url);

        {// 检查当前网络是否可用
            final NetworkUtils networkUtils = new NetworkUtils(QianXunLibApplication.getInstance());

            if (!networkUtils.isNetworkConnected() && android.os.Build.VERSION.SDK_INT > 10) {
                if (callback != null) {
                    callback.onFailure(url, null, 0, "网络出错");//回调请求失败
                    return;
                }
            }
        }

        /**
         * 正式使用volley框架请求服务器
         * Method.POST：请求方式为post
         * builder.toString()：请求的链接
         * Listener<String>：监听
         */
        StringRequest request = new StringRequest(Request.Method.POST, builder.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response != null && callback != null) {
//                        Gson gson = new Gson();
//                         Object obj =  gson.fromJson(response, clazz);
                        JSONObject newMap = JSON.parseObject(response);
                        callback.onSuccess(url, newMap);
//                        String jsonString=(String) newMap.getString("success");
//                        if (jsonString!=null) {
//                            UserBaseInfo user = JSON.parseObject(jsonString, UserBaseInfo.class);
//                            //回调请求成功
//                            callback.onSuccess(url, user);//gson.fromJson(response, clazz)
//                        }else {
//
//                        }
                    }
                } catch (Exception e) {
                    if (callback != null) {
                        //回调请求失败--解析异常
                        callback.onFailure(url, e, 0, "解析异常");
                        return;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (callback != null) {
                    if (volleyError != null) {
                        callback.onFailure(url, volleyError.getCause(), 0, volleyError.getMessage());
                    } else {
                        callback.onFailure(url, null, 0, "服务器好像有点小问题哦");
                    }
                }
            }
        }) {
            //post请求的参数信息
            protected Map<String, String> getParams() {
                return getPostApiParmes(parameter);
            }
        };

        addRequest(request, url);
    }

    private static ApiParams getPostApiParmes(HashMap<String, String> parameter) {
        ApiParams api = new ApiParams();
        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            api.with(entry.getKey(), entry.getValue());
        }
        return api;
    }

    private static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    private synchronized static void initRequestQueue() {
        if (mRequestQueue == null) {
            //创建一个请求队列
            mRequestQueue = Volley.newRequestQueue(QianXunLibApplication.getInstance());  //QianXunLibApplication.getInstance()
        }
    }

    public static void getImage(String imgUrl, ImageView view) {
        initRequestQueue();
        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, R.mipmap.bitbucket, R.mipmap.email);
        imageLoader.get(imgUrl,listener);
    }

    public static void getUserPhoto(String imgUrl, ImageView view) {
        initRequestQueue();
        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, R.mipmap.profile_picture, R.mipmap.profile_picture);
        imageLoader.get(imgUrl,listener);
    }
}
