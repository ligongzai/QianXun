package com.example.qianxuncartoon;

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

        RequestManager.post(UrlConstance.APP_URL,tagUrl,parameter,clazz,callback);


    }
}
