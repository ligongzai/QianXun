package com.example.qianxuncartoon.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.qianxuncartoon.AppBus;
import com.example.qianxuncartoon.BusEventData;
import com.example.qianxuncartoon.Constant;
import com.example.qianxuncartoon.HttpResponeCallBack;

import com.example.qianxuncartoon.LogMsg;
import com.example.qianxuncartoon.QianXunApplication;
import com.example.qianxuncartoon.QianXunLibApplication;
import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.RequestApiData;
import com.example.qianxuncartoon.TokenKeeper;
import com.example.qianxuncartoon.UrlConstance;
import com.example.qianxuncartoon.UserPreference;
import com.example.qianxuncartoon.bean.UserBaseInfo;
import com.example.qianxuncartoon.http.MyOkhttp;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.widget.LoginButton;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity  {
    private AuthInfo mAuthInfo;
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 登陆认证对应的listener
     */
    private AuthListener mLoginListener = new AuthListener();


    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_go)
    Button btGo;
    @InjectView(R.id.cv)
    CardView cv;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.login_button_default)
    LoginButton mLoginBtnDefault;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        mAuthInfo = new AuthInfo(this, Constant.APP_KEY, Constant.REDIRECT_URL, Constant.SCOPE);
        mLoginBtnDefault.setWeiboAuthInfo(mAuthInfo, mLoginListener); // 为按钮设置授权认证信息
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }


    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_go:
                Explode explode = new Explode();
                explode.setDuration(500);

                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);

                String username = etUsername.getText().toString();//用户名
                String password = etPassword.getText().toString();//密码

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    //RequestApiData.getInstance().getLoginData(username, password, UserBaseInfo.class, LoginActivity.this);
                    //AppBus.getInstance().post(new BusEventData("somebody alive"));
                    new login().execute(UrlConstance.APP_URL + UrlConstance.KEY_LOGIN_INFO + "?username=" + username + "&password=" + password + "&usertype=0");

                } else {
                    Toast.makeText(LoginActivity.this, "账号或者密码有误", Toast.LENGTH_SHORT).show();
                }


//                overridePendingTransition(android.R.anim.slide_in_left,
//                        android.R.anim.slide_out_right);
//                Intent i2 = new Intent(this, LoginSuccessActivity.class);
//                startActivity(i2, oc2.toBundle());
                break;
        }
    }


    class login extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String i = MyOkhttp.get(params[0]);
            return i;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)) {
                String jsonString = JSON.parseObject(result).getString(Constant.SUC_MSG);
                if (jsonString != null) {
                    //将JSON字符串对象化
                    UserBaseInfo user = JSON.parseObject(jsonString, UserBaseInfo.class);
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                    //登陆成功，保存登录信息到Application中
                    //QianXunApplication.getInstance().setBaseUser(user);

                    //保存在SP中
                    UserPreference.save(Constant.IS_USER_ID, String.valueOf(user.getUserid()));
                    UserPreference.save(Constant.IS_USER_NAME, user.getUsername());
                    UserPreference.save(Constant.IS_USER_PASSWORD, user.getUserpwd());
                    UserPreference.save(Constant.IS_USER_TYPE, String.valueOf(user.getUsertype()));
                    //otto框架的页面信息传递
                    //AppBus.getInstance().post(new BusEventData("somebody alive"));

                    //eventbus发送语句
                    //EventBus.getDefault().postSticky(new LogMsg("FirstEvent btn clicked"));

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    //finish();
                } else {
                    Toast.makeText(LoginActivity.this, JSON.parseObject(result).getString(Constant.FAIL_MSG), Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    //微博登陆按钮监听器
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                TokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                Toast.makeText(LoginActivity.this,
                        "授权成功" + mAccessToken.getToken(), Toast.LENGTH_SHORT).show();
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,
                    "取消授权", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


//    @Override
//    public void onResponeStart(String apiName) {
//        if (UrlConstance.KEY_LOGIN_INFO.equals(apiName)) {
//            Toast.makeText(LoginActivity.this, "正在加载数据中", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }
//
//    @Override
//    public void onLoading(String apiName, long count, long current) {
//        Toast.makeText(LoginActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
//    }
//
//    //延时函数
//    private void delay(int ms) {
//        try {
//            Thread.currentThread();
//            Thread.sleep(ms);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //登陆成功之后的回调
//    @Override
//    public void onSuccess(String apiName, Object object) {
//        if (UrlConstance.KEY_LOGIN_INFO.equals(apiName)) {
//            //Toast.makeText(LoginActivity.this, object.toString(), Toast.LENGTH_LONG).show();
//            if (object != null && object instanceof JSONObject) {
//                String jsonString = ((JSONObject) object).getString(Constant.SUC_MSG);
//                if (jsonString != null) {
//                    //将JSON字符串对象化
//                    UserBaseInfo user = JSON.parseObject(jsonString, UserBaseInfo.class);
//                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
//                    //登陆成功，保存登录信息到Application中
//                    //QianXunApplication.getInstance().setBaseUser(user);
//
//                    //保存在SP中
//                    UserPreference.save(Constant.IS_USER_ID, String.valueOf(user.getUserid()));
//                    UserPreference.save(Constant.IS_USER_NAME, user.getUsername());
//                    UserPreference.save(Constant.IS_USER_PASSWORD, user.getUserpwd());
//                    UserPreference.save(Constant.IS_USER_TYPE, String.valueOf(user.getUsertype()));
//                    //跳转到个人信息页面
//
//                    //AppBus.getInstance().post(new BusEventData("somebody alive"));
//
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    //finish();
//
//
//                } else {
//                    Toast.makeText(LoginActivity.this, ((JSONObject) object).getString(Constant.FAIL_MSG), Toast.LENGTH_LONG).show();
//                }
//
//
//            }
//        }
//    }
//
//    @Override
//    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
//        Toast.makeText(LoginActivity.this, strMsg, Toast.LENGTH_LONG).show();
//    }

    //微博登陆成功之后的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mLoginBtnDefault != null) {
            mLoginBtnDefault.onActivityResult(requestCode, resultCode, data);
        }
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }

}