package com.example.qianxuncartoon.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.qianxuncartoon.R;

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
        rlRoot = (RelativeLayout) findViewById(R.id.activity_splash);
        ScaleAnimation animationScale=new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animationScale.setDuration(500);
        animationScale.setFillAfter(true);

        //动画集合
        AnimationSet set= new AnimationSet(true);
//        set.addAnimation(animationRotate);
        set.addAnimation(animationScale);

        //启动
        rlRoot.startAnimation(set);


        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束
                Intent intent;
                intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
