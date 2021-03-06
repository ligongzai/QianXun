package com.example.qianxuncartoon.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.qianxuncartoon.fragment.AccountFragment;
import com.example.qianxuncartoon.fragment.HomePagerFragment;
import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.fragment.TraceFragment;
import com.example.qianxuncartoon.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity //implements View.OnClickListener
{

    /**  总体常量  **/
    public static final String URL_PREFIX = "http://192.168.191.1:8080/qxComic";

    private NoScrollViewPager mViewPager;

    //适配器
    private FragmentPagerAdapter mAdapter;

    //装载fragment的集合
    private List<Fragment> mFragments;

    //三个Tab对应布局
    private LinearLayout mFraHomePager;
    private LinearLayout mFraAccount;
    private LinearLayout mFraTrace;

    //底部按键布局
    private BottomNavigationView bottomNavigationView;

    private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        initViews();//初始化控件
        initDatas();//初始化数据
    }

    @Override
    protected void onRestart() {
        super.onRestart();

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

        //设置底栏标签的切换监听
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        mViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.item2:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.item3:
                        mViewPager.setCurrentItem(2, false);
                        break;
                }
                return false;
            }
        });


    }


    private void initViews() {
        mViewPager = (NoScrollViewPager) findViewById(R.id.id_viewpager);

        mFraHomePager = (LinearLayout) findViewById(R.id.id_fragment_homepage);
        mFraTrace = (LinearLayout) findViewById(R.id.id_fragment_trace);
        mFraAccount = (LinearLayout) findViewById(R.id.id_fragment_account);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

}
