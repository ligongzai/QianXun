package com.example.qianxuncartoon.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.activity.CartoonIntro;
import com.example.qianxuncartoon.activity.MainActivity;
import com.example.qianxuncartoon.adapter.RecyclerHomepagerAdapter;
import com.example.qianxuncartoon.http.MyOkhttp;
import com.example.qianxuncartoon.model.CartoonCover;
import com.example.qianxuncartoon.model.TbComic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 咸鱼 on 2017/2/15.
 * <p>
 * 首页界面漫画展示具体，和首页tab有关
 */

public class ContentHomeFragment extends Fragment {


    private boolean isPrepared;// view已经初始化完成
    private boolean isFirstLoad = true;
    private boolean isVisible;//标志当前页面是否可见
    private boolean dataIsExecutive = false;
    private RecyclerView mrecyclerView;
    private RecyclerHomepagerAdapter mAdapter;
    private GridLayoutManager mgridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page = 1;
    private int lastVisibleItem;//控制加载更新
    private List<CartoonCover> cartooncovers;
    private int cartoon_type;   //漫画类型，用于数据请求部分


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_homepager, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartoon_type = getArguments().getInt("classid");
        //判断网络部分
        NetworkInfo netIntfo = null;
        Activity act = (Activity) getContext();
        try {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(act.CONNECTIVITY_SERVICE);
            netIntfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {
            Toast.makeText(act, "没有网络权限，请给予相关权限", Toast.LENGTH_SHORT).show();
        }
        if (netIntfo == null) {
            return;
        }
        //初始化布局
        mrecyclerView = (RecyclerView) view.findViewById(R.id.recycler_homepager);
        mgridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        mrecyclerView.setLayoutManager(mgridLayoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.grid_swipe_refresh);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mAdapter = null;
        isPrepared = true;
        isFirstLoad = true;
        lazyLoad();

        //   new GetData().execute(MainActivity.URL_PREFIX+"/hot?page="+page);
        setListener(); //设置监听事件
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    private void onInvisible() {
    }

    private void onVisible() {
        lazyLoad();//实现懒加载
    }

    private void lazyLoad() {
        if (!isVisible || !isPrepared || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        setGridList();
    }

    private void setGridList() {
        new GetData().execute(MainActivity.URL_PREFIX + "/class?classId=" + cartoon_type + "&page=" + page);
    }

    private void setListener() {
        //swipeRefreshLayout刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                page = 1;
                setGridList();
            }
        });
        //滑动到底部自动加载刷新
        mrecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                // 滑动状态停止并且剩余少于两个item时，自动加载下一页
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 5 >= mgridLayoutManager.getItemCount()) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                获取加载的最后一个可见视图在适配器的位置。
                lastVisibleItem = mgridLayoutManager.findLastVisibleItemPosition();

            }
        });
    }

    private void loadMore() {
        if (dataIsExecutive) {
            Toast.makeText(getContext(), "漫画到底部了！", Toast.LENGTH_SHORT);
            return;
        }
        ++page;
        setGridList();
    }

    //线程池,工作者线程
    class GetData extends AsyncTask<String, Integer, String> {

        //此方法会在后台任务执行前被调用，用于进行一些准备工作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //设置swipeRefreshLayout为刷新状态
            swipeRefreshLayout.setRefreshing(true);
        }

        //此方法中定义要执行的后台任务，在这个方法中可以调用publishProgress来更新任务进度
        @Override
        protected String doInBackground(String... params) {
            String i = MyOkhttp.get(params[0]);
            return i;
        }

        //后台任务执行完毕后，此方法会被调用，参数即为后台任务的返回结果
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)) {
                JSONObject jsonObject;
                Gson gson = new Gson();
                String jsonData = null;

                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject.has("failure")) {
                        dataIsExecutive = true;
                        swipeRefreshLayout.setRefreshing(false);
                        return;
                    } else if (jsonObject.has("success")) {
                        jsonData = jsonObject.getString("success");
                    } else {
                        jsonData = result;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (cartooncovers == null || cartooncovers.size() == 0) {
                    //根据泛型返回解析指定的类型
                    cartooncovers = gson.fromJson(jsonData, new TypeToken<List<CartoonCover>>() {
                    }.getType());

                    //        CartoonCover pages=new CartoonCover();
                    // pages.setPage(page);
                    //       cartooncovers.add(pages);//在数据链表中加入一个用于显示页数的item
                } else {
                    if (page == 1){
                        cartooncovers.clear();
                    }
                    List<CartoonCover> more = gson.fromJson(jsonData, new TypeToken<List<CartoonCover>>() {
                    }.getType());
//                    cartooncovers.addAll(more);

                    //          CartoonCover pages = new CartoonCover();
                    //          pages.setPage(page);
                    cartooncovers.addAll(more);//在数据链表中加入一个用于显示页数的item
                }
                if (mAdapter == null) {
                    //设置适配器
                    mAdapter = new RecyclerHomepagerAdapter(getActivity(), cartooncovers);
                    mrecyclerView.setAdapter(mAdapter);

                    //实现适配器自定义的点击监听
                    mAdapter.setOnItemClickListener(new RecyclerHomepagerAdapter.OnRecyclerViewItemClickListener() {

                        @Override
                        public void onItemClick(View view) {
                            String comcid = (String) ((TextView) view.findViewById(R.id.recycler_item_comic_id)).getText();
                            /**
                             * @desciption 测试数据
                             * **/
                            //      String position="results"+mrecyclerView.getChildAdapterPosition(view);

                            //与其它Activity进行通信
                            Intent intent = new Intent(getActivity(), CartoonIntro.class);
                            intent.putExtra("url", comcid);
                            startActivity(intent);
                        }
                    });
                } else {
                    //让适配器刷新数据
                    mAdapter.notifyDataSetChanged();
                }
            }
            //停止swipeRefreshLayout加载动画
            swipeRefreshLayout.setRefreshing(false);

        }
    }
}
