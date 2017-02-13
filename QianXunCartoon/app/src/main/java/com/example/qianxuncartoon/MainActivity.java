package com.example.qianxuncartoon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager mViewPager;

    //适配器
    private FragmentPagerAdapter mAdapter;

    //装载fragment的集合
    private List<Fragment> mFragments;

    //三个Tab对应布局
    private LinearLayout mFraHomePager;
    private LinearLayout mFraTrace;
    private LinearLayout mFraAccount;

    //对应tab总体
    private LinearLayout tab_homePage;
    private LinearLayout tab_trace;
    private LinearLayout tab_account;

    //对应的ImageButton
    private ImageButton btn_homepage;
    private ImageButton btn_trace;
    private ImageButton btn_account;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();//初始化控件
        initEvents();//初始化事件
        initDatas();//初始化数据
    }

    private void initDatas() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomePagerFragment());
        mFragments.add(new TraceFragment());
        mFragments.add(new AccountFragment());

        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                //从集合中获取对应位置的Fragment
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                //获取集合中Fragment的总数
                return mFragments.size();
            }
        };

        mViewPager.setAdapter(mAdapter);

        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                resetImgs();
                selectTab(position);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //根据点击改变此时按钮状态
    private void selectTab(int position) {
        switch (position){
            case 0:
                tab_homePage.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                tab_trace.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                tab_account.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
        mViewPager.setCurrentItem(position);
    }

    private void resetImgs() {
        tab_homePage.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        tab_trace.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        tab_account.setBackgroundColor(getResources().getColor(R.color.colorGrey));
    }

    private void initEvents() {
        btn_homepage.setOnClickListener(this);
        btn_trace.setOnClickListener(this);
        btn_account.setOnClickListener(this);
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mFraHomePager = (LinearLayout) findViewById(R.id.id_fragment_homepage);
        mFraTrace = (LinearLayout) findViewById(R.id.id_fragment_trace);
        mFraAccount = (LinearLayout) findViewById(R.id.id_fragment_account);

        tab_homePage = (LinearLayout) findViewById(R.id.id_tab_homepage);
        tab_trace = (LinearLayout) findViewById(R.id.id_tab_trace);
        tab_account = (LinearLayout) findViewById(R.id.id_tab_account);

        btn_homepage = (ImageButton) findViewById(R.id.id_tab_homepage_img);
        btn_trace = (ImageButton) findViewById(R.id.id_tab_trace_img);
        btn_account = (ImageButton) findViewById(R.id.id_tab_account_img);

    }

    @Override
    public void onClick(View v) {

        resetImgs();

        switch (v.getId()){
            case R.id.id_tab_homepage_img:
                selectTab(0);
                Toast.makeText(getApplicationContext(), "点击是有反应的",Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_tab_trace_img:
                selectTab(1);
                break;
            case R.id.id_tab_account_img:
                selectTab(2);
                break;
        }
    }
}
