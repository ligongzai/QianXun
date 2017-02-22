package com.example.qianxuncartoon.activity;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.algorithm.Fastblur;
import com.example.qianxuncartoon.http.MyOkhttp;
import com.example.qianxuncartoon.model.CartoonCover;
import com.example.qianxuncartoon.model.CartoonDetail;
import com.example.qianxuncartoon.model.TbClass;
import com.example.qianxuncartoon.model.TbComic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CartoonIntro extends AppCompatActivity {

    private TextView cartoonName;
  //  private CartoonDetail mCartoonDetail;
    private ImageView imageView_title;
    private TbComic mTbComic;

    private final String URL_SINGLECOMIC = "singleComic?comicId=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoon_intro);
        initWidget();
        getData();

    }

    private void initWidget() {
        cartoonName = (TextView) findViewById(R.id.carton_info_url);
        imageView_title = (ImageView) findViewById(R.id.cartoon_intro_img_blur);
//        imageView_title.setImageDrawable();
    }


    private void getData() {

        new GetData().execute(MainActivity.URL_PREFIX + URL_SINGLECOMIC+getIntent().getStringExtra("url"));




        //开线程获取数据

        //向后台传输数据
//        float radius = 20;//模糊程度
//        float scaleFactor = 8;//图片缩放比例；
//        Bitmap overlay = Bitmap.createBitmap(
//                (int) (imageView_title.getMeasuredWidth() / scaleFactor),
//                (int) (imageView_title.getMeasuredHeight() / scaleFactor),
//                Bitmap.Config.ARGB_8888);
//        overlay = Fastblur.doBlur(overlay, (int) radius, true);
//        imageView_title.setBackgroundDrawable(new BitmapDrawable(getResources(), overlay));

    }

    private void applyBlur() {

        //当一个视图树将要绘制时，所要调用的回调函数的接口类
        imageView_title.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView_title.getViewTreeObserver().removeOnPreDrawListener(this);
                imageView_title.buildDrawingCache();

                Bitmap bmp = imageView_title.getDrawingCache();
                imageView_title.setImageBitmap(blur(bmp ,imageView_title));
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Bitmap blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float radius = 20;

        Bitmap overlay = Bitmap.createBitmap((int)(view.getMeasuredWidth()), (int)(view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);
        return Fastblur.doBlur(overlay, (int)radius, true);
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
               Gson gson = new Gson();
               String jsonData = result;
               mTbComic = gson.fromJson(jsonData,new TypeToken<List<TbComic>>() {}.getType());
           }
           Picasso.with(getApplicationContext()).load(mTbComic.getComiccover()).into(imageView_title);
            //图片做模糊处理
            applyBlur();
        }
    }
}
