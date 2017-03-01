package com.example.qianxuncartoon.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.example.qianxuncartoon.Constant;
import com.example.qianxuncartoon.HttpResponeCallBack;
import com.example.qianxuncartoon.QianXunApplication;
import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.RequestApiData;
import com.example.qianxuncartoon.UrlConstance;
import com.example.qianxuncartoon.UserPreference;
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


public class CartoonIntro extends AppCompatActivity implements View.OnClickListener ,HttpResponeCallBack{

  //  private CartoonDetail mCartoonDetail;
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
    private List<TbEpisode> mTbEpisode = null;
    private RecyclerView recycler_singlecartoon;
    private Recyclerchapter madapter = null;
    private GridLayoutManager mgridLayoutManager;

    private int site = 1; //站点id
    private int doubleSite = 1;
    private String userId;

    private final String URL_SINGLECOMIC = "/singleComic?comicId=";
    private final String URL_EPISODES = "/source?";
    private final String URL_READSTART = "/startReading?";

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

        if (UserPreference.isLogin()){
            userId = UserPreference.read(Constant.IS_USER_ID, null);
        }else {
            userId = "1";
        }

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

        getEpisode();

    }

    private void getEpisode(){
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
                    intent.putExtra("EpisodeId",i);
                    intent.putExtra("page","1");
                    startActivity(intent);
                }
            });
        }else {
            madapter.notifyDataSetChanged();
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
        float radius = 60;

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
                String userid = UserPreference.read(Constant.IS_USER_ID, null);
                RequestApiData.getInstance().markComic(((userid==null||userid.equals("")) ? 1 : Integer.parseInt(userid)),Integer.parseInt(getIntent().getStringExtra("url")), QianXunApplication.class,CartoonIntro.this);
                break;
            //点击开始阅读
            case R.id.btn_cartooninfo_beginread:
                new GetstartReadData().execute(MainActivity.URL_PREFIX+URL_READSTART+"comicId="+mTbComic.getComicid()
                         + "&userId=" + userId +"&siteId=" + site);
                break;
            case R.id.ic_text_download:
                beginDownLoad();
                break;
            case R.id.ic_text_getsource:
                final BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(R.layout.view_bottom_sheet);
                dialog.show();
                ((Button)dialog.findViewById(R.id.btn_bottom_sheet_1)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (site == 1){
                            Toast.makeText(getApplicationContext(),"当前来源就是非常爱漫",Toast.LENGTH_SHORT).show();
                            return;
                        }
                            doubleSite = site;
                            site = 1;
                            getEpisode();
                            dialog.dismiss();
                    }
                });
                ((Button)dialog.findViewById(R.id.btn_bottom_sheet_2)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (site == 2){
                            Toast.makeText(getApplicationContext(),"当前来源就是4399漫画",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        doubleSite = site;
                        site = 2;
                        getEpisode();
                        dialog.dismiss();

                    }
                });
                ((Button)dialog.findViewById(R.id.btn_bottom_sheet_3)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (site == 3){
                            Toast.makeText(getApplicationContext(),"当前来源就是Kuku漫画",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        doubleSite = site;
                        site = 3;
                        getEpisode();
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    private void beginDownLoad() {

        //请求权限
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            //requestPermissions执行弹出请求授权对话框
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constant.READ_EXTERNAL_STORAGE);
        }else {
            //执行有权限之后的相应操作
            downLoading();
        }
    }

    private void downLoading() {
        Toast.makeText(getApplicationContext(),"进入了下载",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constant.READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission 允许
                    downLoading();
                }else {
                    //Permission 拒绝
                    Toast.makeText(getApplicationContext(),"您没开启读取权限，无法下载",Toast.LENGTH_SHORT).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResponeStart(String apiName) {

    }

    @Override
    public void onLoading(String apiName, long count, long current) {

    }

    @Override
    public void onSuccess(String apiName, Object object) {
        if (UrlConstance.KEY_MARK.equals(apiName)){
            if (object != null && object instanceof com.alibaba.fastjson.JSONObject){
                String jsonString = ((com.alibaba.fastjson.JSONObject) object).getString(Constant.SUC_MSG);
                if (jsonString != null){
                    Toast.makeText(CartoonIntro.this, ((com.alibaba.fastjson.JSONObject) object).getString(Constant.SUC_MSG), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(CartoonIntro.this, ((com.alibaba.fastjson.JSONObject) object).getString(Constant.FAIL_MSG), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {

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
                    if (jsonObject.has("failure")){
                        //就是无这个站点漫画
                        site = doubleSite;
                        Toast.makeText(getApplicationContext(),"本漫画在此来源网站中无资源",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    jsonData = jsonObject.getString("success");
                    if (mTbEpisode == null){
                        mTbEpisode = gson.fromJson(jsonData, new TypeToken<List<TbEpisode>>(){}.getType());
                    }else {
                        mTbEpisode.clear();
                        List<TbEpisode> more = gson.fromJson(jsonData, new TypeToken<List<TbEpisode>>(){}.getType());
                        mTbEpisode.addAll(more);
                    }

                    upDateEpisode();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class GetstartReadData extends AsyncTask<String , String ,String>{

        @Override
        protected String doInBackground(String... params) {
            return MyOkhttp.get(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //就是无这个站点漫画
            if (result == null){
                Toast.makeText(getApplicationContext(),"本漫画在此来源网站中无资源",Toast.LENGTH_SHORT).show();
            }
            if (!TextUtils.isEmpty(result)){
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                    String[] jsonData = jsonObject.getString("success").split("#");

                    Intent intent = new Intent(getApplicationContext(),ReadCartoonActivity.class);
                    intent.putExtra("EpisodeId",jsonData[0]);
                    intent.putExtra("page",jsonData[2]);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
