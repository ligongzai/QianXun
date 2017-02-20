package com.example.qianxuncartoon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.activity.LoginActivity;

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
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }

        });

        return view;
    }



}
