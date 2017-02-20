package com.example.qianxuncartoon;

import com.example.qianxuncartoon.bean.UserBaseInfo;

/**
 * Created by Alex on 2017/2/20.
 * 处理一些漫画app的全局标量
 */

public class QianXunApplication extends QianXunLibApplication {
    private UserBaseInfo baseUser;//用户基本信息

    private static QianXunApplication instance;

    public void onCreate() {
        super.onCreate();
        setInstance(this);


    }

    public void setInstance(QianXunApplication instance) {
        QianXunApplication.instance = instance;
    }

    public void setBaseUser(UserBaseInfo baseUser) {
        this.baseUser = baseUser;
    }

    /**
     * 获取实例
     * @return
     */
    public  static QianXunApplication getInstance(){
        return instance;
    }
}
