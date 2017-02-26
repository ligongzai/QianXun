package com.example.qianxuncartoon;

import android.widget.ImageView;

import com.example.qianxuncartoon.bean.FavorPrivateInfo;
import com.example.qianxuncartoon.bean.UserBaseInfo;

import java.util.HashMap;

/**
 * Created by Alex on 2017/2/19.
 * 网路接口类
 */
public class RequestApiData {
    private static RequestApiData instance = null;
    private HttpResponeCallBack mCallBack = null;

    //创建接口对象
    public static RequestApiData getInstance() {
        if (instance == null) {
            instance = new RequestApiData();
        }
        return instance;
    }

    /**
     * @param username 用户名
     * @param password 密码
     * @param clazz    数据返回的解析对象
     * @param callback 回调
     *                 请求方式：
     */
    public void getLoginData(String username, String password,
                             Class<UserBaseInfo> clazz,
                             HttpResponeCallBack callback) {
        mCallBack = callback;
        //唯一的接口标识符
        String tagUrl = UrlConstance.KEY_LOGIN_INFO;//登陆接口
        HashMap<String,String> parameter = new HashMap<>();
        parameter.put("username",username);
        parameter.put("password",password);
        parameter.put("usertype", String.valueOf(0));

        RequestManager.post(UrlConstance.APP_URL,tagUrl,parameter,clazz,callback);


    }

    /**
     * 获取收藏漫画信息
     */
    public void getFavorPrivateData(int userId, int page, Class<FavorPrivateInfo> clazz, HttpResponeCallBack callback){
        String tagUrl= UrlConstance.KEY_FOVAR_PRIVATE; //收藏的漫画接口
        HashMap<String,String> parameter= new HashMap<>();
        parameter.put("userId", String.valueOf(userId));
        parameter.put("page", String.valueOf(page));
        RequestManager.post(UrlConstance.APP_URL,tagUrl,parameter,clazz,callback);
    }

    public void getFavorPrivateImage(String imgUrl, ImageView view){
        RequestManager.getImage(imgUrl,view);
    }

    //收藏一本漫画
    public void markComic(int userId,int comicid,Class clazz,HttpResponeCallBack callback){
        String tagUrl= UrlConstance.KEY_MARK;
        HashMap<String,String> parameter= new HashMap<>();
        parameter.put("userId", String.valueOf(userId));
        parameter.put("comicId", String.valueOf(comicid));
        RequestManager.post(UrlConstance.APP_URL,tagUrl,parameter,clazz,callback);
    }
}
