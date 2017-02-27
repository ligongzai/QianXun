package com.example.qianxuncartoon.activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.qianxuncartoon.Constant;
import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.UserPreference;
import com.example.qianxuncartoon.adapter.ReadCartoonAdapter;
import com.example.qianxuncartoon.http.MyOkhttp;
import com.example.qianxuncartoon.model.CartoonCover;
import com.example.qianxuncartoon.model.TbPicture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 咸鱼 on 2017/2/24.
 */

public class ReadCartoonActivity extends BaseActivity{

    private List<TbPicture> mTbPicture;
    private ReadCartoonAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private String userId;
    private boolean dataIsExecutive = false;
    private int lastVisibleItem;//控制加载更新
    private int page = 1;
    private String episodeId;
    private String URL_READ = "/episode?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readcaroon);
        initWidget();
        initData();
        getData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void initData() {
        if (UserPreference.isLogin()){
            userId = UserPreference.read(Constant.IS_USER_ID, null);
        }else {
            userId = "1";
        }
    }

    private void initWidget() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_readcartoon);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        episodeId = getIntent().getStringExtra("EpisodeId");
        setListener(); //设置监听事件
    }

    private void getData() {
        new GetData().execute(MainActivity.URL_PREFIX + URL_READ + "episodeId=" + episodeId
              + "&userId=" + userId + "&page=" +page);
    }

    private void setListener() {
        //滑动到底部自动加载
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                // 滑动状态停止并且剩余少于两个item时，自动加载下一页
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem +1>=mLinearLayoutManager.getItemCount()) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                获取加载的最后一个可见视图在适配器的位置。
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

            }

        });
    }

    //加载更多
    private void loadMore() {

        if (dataIsExecutive){
            getNextEpisode();
            return;
        }
        ++page;
        getData();


    }

    private void getNextEpisode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("本话已阅读完毕，是否进入下一话");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new GetNextEpisodeId().execute(MainActivity.URL_PREFIX +"/next?episodeId=" + episodeId);


            }
        });
        builder.show();
    }

    private void upDateUI() {
        if (mAdapter == null){
            mAdapter = new ReadCartoonAdapter(getApplicationContext(),mTbPicture);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class GetData extends AsyncTask<String, Integer ,String>{
        @Override
        protected String doInBackground(String... params) {
            return MyOkhttp.get(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)){
                JSONObject jsonObject;
                String jsonData = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject.has("failure")){
                        dataIsExecutive = true;
                        getNextEpisode();
                        return;
                    }
                    Gson gson = new Gson();
                    jsonData = jsonObject.getString("success");
                    if(mTbPicture ==null ){
                        //根据泛型返回解析指定的类型
                        mTbPicture= gson.fromJson(jsonData, new TypeToken<List<TbPicture>>() {}.getType());
                    }else{
                        List<TbPicture> more = gson.fromJson(jsonData, new TypeToken<List<TbPicture>>() {}.getType());
                        mTbPicture.addAll(more);//在数据链表中加入一个用于显示页数的item
                    }
                    upDateUI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class GetNextEpisodeId extends AsyncTask<String, Integer ,String>{
        @Override
        protected String doInBackground(String... params) {
            return MyOkhttp.get(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)){
                String jsonData = result;
                if (result == "0"){
                    Toast.makeText(getApplicationContext(),"漫画全部阅读完毕",Toast.LENGTH_SHORT).show();
                }else {
                    episodeId = result;
                    page = 1;
                    dataIsExecutive = false;
                    mTbPicture.clear();
                    getData();
                }
            }
        }
    }
}
