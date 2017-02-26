package com.example.qianxuncartoon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qianxuncartoon.AppBus;
import com.example.qianxuncartoon.BusEventData;
import com.example.qianxuncartoon.Constant;
import com.example.qianxuncartoon.LogMsg;
import com.example.qianxuncartoon.QianXunApplication;
import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.activity.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.R.attr.button;

/**
 * Created by 咸鱼 on 2017/2/13.
 */

public class AccountFragment extends Fragment {
    private Button loginBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        loginBtn = (Button) view.findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }

        });

        return view;

    }
    //otto总线的注册与反注册
//    @Override
//    public void onStart() {
//        super.onStart();
//        //注册到bus事件总线中
//        AppBus.getInstance().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        //反注册
//        AppBus.getInstance().unregister(this);
//    }

    //otto总线的订阅者
//    @Subscribe
//    public void setContent(BusEventData data) {
//        loginBtn.setText(data.getContent());
//    }


//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
//    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    //@Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LogMsg event) {
        String msg = event.getMsg();
        Log.d("harvic", msg);
        Toast.makeText(QianXunApplication.getInstance(), msg, Toast.LENGTH_LONG).show();
        loginBtn.setText(msg);
        //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


}
