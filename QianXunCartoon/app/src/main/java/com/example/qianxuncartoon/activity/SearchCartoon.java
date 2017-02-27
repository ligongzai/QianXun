package com.example.qianxuncartoon.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.text.TextUtils;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.adapter.RecyclerViewAdapter;
import com.example.qianxuncartoon.http.MyOkhttp;
import com.example.qianxuncartoon.model.TbComic;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchCartoon extends AppCompatActivity {

    private SearchView mSearchView;
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private List mDataList = new ArrayList<>();
    private Button mButton;
    private RecyclerViewAdapter mAdapter;

    private final String URL_SEARCH = "/search?word=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cartoon);
        initWidget();
    }

    private void initWidget() {
        mSearchView = (SearchView) findViewById(R.id.searchview);
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.recycle_showSearch);
        mButton = (Button) findViewById(R.id.btn_searcb_cartoon);

        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setRefreshing(true);
        //设置并启动适配器
        mAdapter = new RecyclerViewAdapter(SearchCartoon.this, mDataList);
        mPullLoadMoreRecyclerView.setAdapter(mAdapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                String i = query;
                new GetData().execute(MainActivity.URL_PREFIX+URL_SEARCH +query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetData().execute(MainActivity.URL_PREFIX+URL_SEARCH +mSearchView.getQuery());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    class GetData extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            return MyOkhttp.get(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!TextUtils.isEmpty(result)){
                String jsonString = result;
                if (jsonString != null) {
                    mDataList = JSON.parseArray(jsonString, TbComic.class);
                    mAdapter.notifyDataSetChanged();
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                } else {
                    //Toast.makeText(QianXunApplication.getInstance(), JSON.parseObject(result).getString(Constant.FAIL_MSG), Toast.LENGTH_LONG).show();
                    //Toast.makeText(QianXunApplication.getInstance(), "没有更多了", Toast.LENGTH_LONG).show();
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    return;
                }
            }

        }

    }
}
