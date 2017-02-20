package com.example.qianxuncartoon.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.qianxuncartoon.QianXunLibApplication;

/**
 * Created by Alex on 2017/2/19.
 * 检测网站是否链接的工具类
 */
public class NetworkUtils {
    private ConnectivityManager connManager;
    private Context context;

    public NetworkUtils(Context context) {
        this.context = context;
        connManager = (ConnectivityManager) this.context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    //检查网络是否可用
    public boolean isNetworkConnected() {

        if (connManager == null) {
            connManager = (ConnectivityManager) QianXunLibApplication.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        if (connManager != null) {
            final NetworkInfo networkinfo = connManager.getActiveNetworkInfo();

            if (networkinfo != null) {
                return networkinfo.isConnected();
            }
        } else {
            return true;
        }

        return false;
    }
}
