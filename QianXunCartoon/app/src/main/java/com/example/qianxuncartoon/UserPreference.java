package com.example.qianxuncartoon;

import android.content.SharedPreferences;

/**
 * Created by Alex on 2017/2/24.
 * 用户登陆返回的信息存储类
 */

public class UserPreference {
    private static SharedPreferences mUserPreferences;
    private static final String USER_PREFERENCE = "user_preference";

    public static SharedPreferences ensureIntializePreference() {
        if (mUserPreferences == null) {
            mUserPreferences = QianXunLibApplication.getInstance().getSharedPreferences(USER_PREFERENCE, 0);
        }
        return mUserPreferences;
    }
    //存储信息
    public static void save(String key, String value) {
        SharedPreferences.Editor editor = ensureIntializePreference().edit();
        editor.putString(key, value);
        editor.commit();
    }
    //读取信息
    public static String read(String key, String defaultvalue) {
        return ensureIntializePreference().getString(key, defaultvalue);
    }
    //清空信息
    public static void delete(){
        SharedPreferences.Editor editor = ensureIntializePreference().edit();
        editor.clear();
        editor.commit();
    }

    public static Boolean isLogin(){
        if (mUserPreferences == null){
            return false;
        }
        return true;
    }
}

