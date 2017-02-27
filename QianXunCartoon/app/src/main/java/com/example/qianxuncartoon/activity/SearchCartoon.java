package com.example.qianxuncartoon.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.adapter.SearchCartoonAdapter;
import com.example.qianxuncartoon.http.MyOkhttp;
import com.example.qianxuncartoon.model.TbComic;
import com.example.qianxuncartoon.model.TbPicture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SearchCartoon extends AppCompatActivity {

    private SearchView mSearchView;
    private RecyclerView mrecyclerView;
    private List<TbComic> mTbComic;
    private Button mButton;
    private SearchCartoonAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private final String URL_SEARCH = "/search?word=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cartoon);
        initWidget();
    }

    private void initWidget() {
        mSearchView = (SearchView) findViewById(R.id.searchview);
        mrecyclerView = (RecyclerView) findViewById(R.id.recycle_showSearch);
        mButton = (Button) findViewById(R.id.btn_searcb_cartoon);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(mLinearLayoutManager);
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
                Gson gson = new Gson();
                if (result != "") {
                    if (mTbComic == null || mTbComic.size() == 0){
                        mTbComic = gson.fromJson(result,new TypeToken<List<TbComic>>() {}.getType());
                    }else {
                        mTbComic.clear();
                        List<TbComic> more = gson.fromJson(result,new TypeToken<List<TbComic>>() {}.getType());
                        mTbComic.addAll(more);
                    }

 //                mTbComic = (List<TbComic>) gson.fromJson(result,TbComic.class);
                } else {
                    return;
                }
            }
            upDateUI();
        }

    }

    private void upDateUI() {
        if (mAdapter == null){
            mAdapter = new SearchCartoonAdapter(SearchCartoon.this,mTbComic);
            mrecyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new SearchCartoonAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CartoonIntro.class);
                    intent.putExtra("url", (String) ((TextView) view.findViewById(R.id.idTag)).getText());
                    startActivity(intent);
                }
            });
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }
}
