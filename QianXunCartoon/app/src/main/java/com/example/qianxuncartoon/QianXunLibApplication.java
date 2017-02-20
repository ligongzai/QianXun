package com.example.qianxuncartoon;

import android.app.Application;

/**
 * Created by Alex on 2017/2/19.
 */
public class QianXunLibApplication extends Application {
    private static QianXunLibApplication instance;

    public void onCreate() {
        super.onCreate();
        setInstance(this);
    }

    private static void setInstance(QianXunLibApplication instance) {
        QianXunLibApplication.instance = instance;
    }

    public static QianXunLibApplication getInstance() {
        return instance;
    }
}
