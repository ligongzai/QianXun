package com.example.qianxuncartoon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qianxuncartoon.Constant;
import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.UrlConstance;
import com.example.qianxuncartoon.UserPreference;
import com.example.qianxuncartoon.bean.UserBaseInfo;
import com.example.qianxuncartoon.http.MyOkhttp;

public class SplashActivity extends Activity {
    private RelativeLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        //旋转动画
//        rlRoot = (RelativeLayout) findViewById(R.id.activity_splash);
//        RotateAnimation animationRotate = new RotateAnimation(0, 360,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
//        animationRotate.setDuration(1000);
//        animationRotate.setFillAfter(true);

        //缩放动画
//        rlRoot = (RelativeLayout) findViewById(R.id.activity_splash);
//        ScaleAnimation animationScale=new ScaleAnimation(0, 1, 0, 1,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
//        animationScale.setDuration(500);
//        animationScale.setFillAfter(true);

        rlRoot = (RelativeLayout) findViewById(R.id.activity_splash);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
        alphaAnimation.setDuration(2000);

        //动画集合
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(alphaAnimation);
//      set.addAnimation(animationRotate);

        //启动
        rlRoot.startAnimation(set);


        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //自动登陆
//                String username = UserPreference.read(Constant.IS_USER_NAME, null);
//                String password = UserPreference.read(Constant.IS_USER_PASSWORD, null);
//                if (TextUtils.isEmpty(username)){//没有保存登录信息
//                    Intent intent;
//                    intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else {
//                    new SplashActivity.login().execute(UrlConstance.APP_URL + UrlConstance.KEY_LOGIN_INFO + "?username=" + username + "&password=" + password + "&usertype=0");
//                }


                //动画结束
                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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
                    Toast.makeText(SplashActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                    //登陆成功，保存登录信息到Application中
                    //QianXunApplication.getInstance().setBaseUser(user);

                    //保存在SP中
                    UserPreference.save(Constant.IS_USER_ID, String.valueOf(user.getUserid()));
                    UserPreference.save(Constant.IS_USER_NAME, user.getUsername());
                    UserPreference.save(Constant.IS_USER_PASSWORD, user.getUserpwd());
                    UserPreference.save(Constant.IS_USER_TYPE, String.valueOf(user.getUsertype()));
                    UserPreference.save(Constant.IS_USER_IMAGE,user.getUserimage());
                    //otto框架的页面信息传递
                    //AppBus.getInstance().post(new BusEventData("somebody alive"));

                    //eventbus发送语句
                    //EventBus.getDefault().post(new LogMsg("FirstEvent btn clicked"));
                    Intent intent;
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SplashActivity.this, JSON.parseObject(result).getString(Constant.FAIL_MSG), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
