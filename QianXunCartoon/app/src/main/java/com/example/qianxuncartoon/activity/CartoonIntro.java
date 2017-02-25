package com.example.qianxuncartoon.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.adapter.Recyclerchapter;
import com.example.qianxuncartoon.algorithm.Fastblur;
import com.example.qianxuncartoon.http.MyOkhttp;
import com.example.qianxuncartoon.model.TbComic;
import com.example.qianxuncartoon.model.TbEpisode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.example.qianxuncartoon.adapter.Recyclerchapter.*;


public class CartoonIntro extends AppCompatActivity implements View.OnClickListener {
    
    private ImageView cartoon_intro_img_blur;
    private ImageView cartoon_intro_img_original;
    private TextView cartoon_intro_itsname;
    private TextView cartoon_intro_authorname;
    private TextView cartoon_intro_itsdescript;
    private TextView cartoon_intro_itstype;
    private TextView ic_text_download;
    private TextView ic_text_getsource;
    private Button btn_cartooninfo_collect;
    private Button btn_cartooninfo_beginread;
    private TbComic mTbComic;
    private List<TbEpisode> mTbEpisode;
    private RecyclerView recycler_singlecartoon;
    private Recyclerchapter madapter = null;
    private GridLayoutManager mgridLayoutManager;

    private int site = 1; //站点id

    private final String URL_SINGLECOMIC = "/singleComic?comicId=";
    private final String URL_EPISODES = "/source?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoon_intro);
        initWidget();
        getData();

    }

    private void initWidget() {
        cartoon_intro_img_blur = (ImageView) findViewById(R.id.cartoon_intro_img_blur);
        cartoon_intro_img_original = (ImageView) findViewById(R.id.cartoon_intro_img_original);
        cartoon_intro_itsname = (TextView) findViewById(R.id.cartoon_intro_itsname);
        cartoon_intro_authorname = (TextView) findViewById(R.id.cartoon_intro_authorname);
        cartoon_intro_itstype = (TextView) findViewById(R.id.cartoon_intro_itstype);
        cartoon_intro_itsdescript = (TextView) findViewById(R.id.cartoon_intro_itsdescript);
        btn_cartooninfo_collect = (Button) findViewById(R.id.btn_cartooninfo_collect);
        btn_cartooninfo_beginread = (Button) findViewById(R.id.btn_cartooninfo_beginread);
        ic_text_download = (TextView) findViewById(R.id.ic_text_download);
        ic_text_getsource = (TextView) findViewById(R.id.ic_text_getsource);
        recycler_singlecartoon = (RecyclerView) findViewById(R.id.recycler_singlecartoon);
        mgridLayoutManager = new GridLayoutManager(getApplicationContext(), 4 ,GridLayoutManager.VERTICAL,false);
        recycler_singlecartoon.setLayoutManager(mgridLayoutManager);

        //设置图标字体
        Typeface iconfont = Typeface.createFromAsset(getAssets(),"font_w27n7ly31ae5ewmi/iconfont.ttf");
        ic_text_download.setTypeface(iconfont);
        ic_text_download.setText("\ue62a");
        ic_text_getsource.setTypeface(iconfont);
        ic_text_getsource.setText("\ue690");
    }

    private void getData() {
        //开线程获取数据
        new GetData().execute(MainActivity.URL_PREFIX + URL_SINGLECOMIC+getIntent().getStringExtra("url"));

    }

    //主线程更新UI
    private void upDateUI() {
        Picasso.with(getApplicationContext()).load(mTbComic.getComiccover()).into(cartoon_intro_img_blur);
        Picasso.with(getApplicationContext()).load(mTbComic.getComiccover()).into(cartoon_intro_img_original);
        //图片做模糊处理
        applyBlur();
        cartoon_intro_itsname.setText(mTbComic.getComicname());
        cartoon_intro_authorname.setText(mTbComic.getComicauth());
        cartoon_intro_itstype.setText(mTbComic.getClassname());
        cartoon_intro_itsdescript.setText(mTbComic.getComicdptn());

        btn_cartooninfo_collect.setOnClickListener(this);
        btn_cartooninfo_beginread.setOnClickListener(this);
        ic_text_download.setOnClickListener(this);
        ic_text_getsource.setOnClickListener(this);

        //开线程获取集数相关信息
        new GetEpisode().execute(MainActivity.URL_PREFIX + URL_EPISODES + "siteId=" + site + "&comicId=" +mTbComic.getComicid());

    }

    //主线程更新集数页面
    private void upDateEpisode() {
        if (madapter == null){
            madapter = new Recyclerchapter(getApplicationContext(),mTbEpisode);
            recycler_singlecartoon.setAdapter(madapter);

            madapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view) {

                    Intent intent = new Intent(getApplicationContext(),ReadCartoonActivity.class);
                    String i = (String) ((TextView)view.findViewById(R.id.gridv_chapter_epiid)).getText();
                    //此处要加入观看者信息
                    intent.putExtra("UserId","1");
                    intent.putExtra("EpisodeId",i);
                    startActivity(intent);
                }
            });
        }
    }

    private void applyBlur() {
        //当一个视图树将要绘制时，所要调用的回调函数的接口类
        cartoon_intro_img_blur.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                cartoon_intro_img_blur.getViewTreeObserver().removeOnPreDrawListener(this);
                cartoon_intro_img_blur.buildDrawingCache();

                Bitmap bmp = cartoon_intro_img_blur.getDrawingCache();
                cartoon_intro_img_blur.setImageBitmap(blur(bmp ,cartoon_intro_img_blur));
                return true;
            }
        });
    }

    private Bitmap blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float radius = 20;

        Bitmap overlay = Bitmap.createBitmap((int)(view.getMeasuredWidth()), (int)(view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);
        return Fastblur.doBlur(overlay, (int)radius, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cartooninfo_collect:
                Toast.makeText(getApplicationContext(),"你点击了收藏",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cartooninfo_beginread:
                Toast.makeText(getApplicationContext(),"你点击了开始阅读",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ic_text_download:
                Toast.makeText(getApplicationContext(),"你点击了下载",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ic_text_getsource:
                Toast.makeText(getApplicationContext(),"你点击了选择源",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //线程池，工作者线程
    private class GetData extends AsyncTask<String ,Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            return  MyOkhttp.get(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           if (!TextUtils.isEmpty(result)){
               JSONObject jsonObject;
               String jsonData = null;
               Gson gson = new Gson();
               try {
                   jsonObject = new JSONObject(result);
                   jsonData = jsonObject.getString("comic");
                   mTbComic = gson.fromJson(jsonData,TbComic.class);
                   mTbComic.setClassname(jsonObject.getString("classname"));
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
           upDateUI();
        }

    }

    private class GetEpisode extends AsyncTask<String , Integer,String>{

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
                Gson gson = new Gson();
                try {
                    jsonObject = new JSONObject(result);
                    jsonData = jsonObject.getString("success");
                    mTbEpisode = gson.fromJson(jsonData, new TypeToken<List<TbEpisode>>(){}.getType());
                    upDateEpisode();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }


}
