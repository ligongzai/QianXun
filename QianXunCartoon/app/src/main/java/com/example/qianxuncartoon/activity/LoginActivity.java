package com.example.qianxuncartoon.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
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
import com.example.qianxuncartoon.Constant;
import com.example.qianxuncartoon.HttpResponeCallBack;
import com.example.qianxuncartoon.QianXunApplication;
import com.example.qianxuncartoon.QianXunLibApplication;
import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.RequestApiData;
import com.example.qianxuncartoon.UrlConstance;
import com.example.qianxuncartoon.bean.UserBaseInfo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements HttpResponeCallBack {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
    }

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
                    RequestApiData.getInstance().getLoginData(username, password, UserBaseInfo.class, LoginActivity.this);
                } else {
                    Toast.makeText(LoginActivity.this, "账号或者密码有误", Toast.LENGTH_SHORT).show();
                }

                Intent i2 = new Intent(this, LoginSuccessActivity.class);
                startActivity(i2, oc2.toBundle());
                break;
        }
    }

    @Override
    public void onResponeStart(String apiName) {
        if (UrlConstance.KEY_LOGIN_INFO.equals(apiName)) {
            Toast.makeText(LoginActivity.this, "正在加载数据中", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onLoading(String apiName, long count, long current) {
        Toast.makeText(LoginActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String apiName, Object object) {
        if (UrlConstance.KEY_LOGIN_INFO.equals(apiName)) {
            //Toast.makeText(LoginActivity.this, object.toString(), Toast.LENGTH_LONG).show();

            if (object != null && object instanceof JSONObject) {

                String jsonString = ((JSONObject) object).getString(Constant.SUC_MSG);
                if (jsonString != null) {
                    UserBaseInfo user = JSON.parseObject(jsonString, UserBaseInfo.class);
                    Toast.makeText(LoginActivity.this, user.toString(), Toast.LENGTH_LONG).show();

                    //登陆成功，保存登录信息
                    QianXunApplication.getInstance().setBaseUser(user);

                    //保存在SP中


                } else {
                    Toast.makeText(LoginActivity.this, ((JSONObject) object).getString(Constant.FAIL_MSG), Toast.LENGTH_LONG).show();
                }


            }
        }
    }

    @Override
    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
        Toast.makeText(LoginActivity.this, strMsg, Toast.LENGTH_LONG).show();
    }
}