package com.example.qianxuncartoon.fragment;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pools;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.qianxuncartoon.Constant;
import com.example.qianxuncartoon.HttpResponeCallBack;
import com.example.qianxuncartoon.QianXunApplication;
import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.RequestApiData;
import com.example.qianxuncartoon.UrlConstance;
import com.example.qianxuncartoon.activity.LoginActivity;
import com.example.qianxuncartoon.adapter.RecyclerViewAdapter;
import com.example.qianxuncartoon.bean.FavorPrivateInfo;
import com.example.qianxuncartoon.http.MyOkhttp;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 单个漫画展示的Fragment
 */
public class ContentFragment extends Fragment {
    // 标志位，标志已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
    private boolean isPrepared;
    private boolean isVisible;//标志当前页面是否可见
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private List mDataList = new ArrayList<>();
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private String mTitle;
    private Handler handler;
    private Runnable runnable;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = this.getArguments().getString("title");
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setRefreshing(true);
        //设置并启动适配器
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), mDataList);
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        //设置下拉和上滑刷新的监听
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mDataList.clear(); //清除list里面的全部内容
                setList();
            }
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        isPrepared = true;
        lazyLoad();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //懒加载
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {
        if (!isVisible || !isPrepared) {
            return;
        }
        setList();
    }

    protected void onInvisible() {
    }

    protected void loadMore() {
        page++;
        setList();
    }


    //请求网络并向List里插入标题和描述信息
    private void setList() {
        new getFavorData().execute(UrlConstance.APP_URL + UrlConstance.KEY_FOVAR_PRIVATE + "?userId=1" + "&page=" + page);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacks(runnable);
    }

//    @Override
//    public void onResponeStart(String apiName) {
//
//    }
//
//    @Override
//    public void onLoading(String apiName, long count, long current) {
//
//    }
//
//    @Override
//    public void onSuccess(String apiName, Object object) {
//        if (UrlConstance.KEY_FOVAR_PRIVATE.equals(apiName)) {
//            if (object != null && object instanceof JSONObject) {
//                String jsonString = ((JSONObject) object).getString(Constant.SUC_MSG);
//                if (jsonString != null) {
//                    favorList = JSON.parseArray(jsonString, FavorPrivateInfo.class);
//                } else {
//                    Toast.makeText(QianXunApplication.getInstance(), ((JSONObject) object).getString(Constant.FAIL_MSG), Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
//
//    }

    //异步访问网络
    class getFavorData extends AsyncTask<String, Integer, String> {

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
                    mDataList = JSON.parseArray(jsonString, FavorPrivateInfo.class);

                    mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), mDataList);
                    mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);

                    mRecyclerViewAdapter.notifyDataSetChanged();
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                } else {
                    //Toast.makeText(QianXunApplication.getInstance(), JSON.parseObject(result).getString(Constant.FAIL_MSG), Toast.LENGTH_LONG).show();
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    return;
                }
            }
        }
    }
}

