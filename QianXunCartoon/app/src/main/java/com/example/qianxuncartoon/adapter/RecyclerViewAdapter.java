package com.example.qianxuncartoon.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.toolbox.ImageLoader;
import com.example.qianxuncartoon.Constant;
import com.example.qianxuncartoon.HttpResponeCallBack;
import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.RequestApiData;
import com.example.qianxuncartoon.UrlConstance;
import com.example.qianxuncartoon.activity.CartoonIntro;
import com.example.qianxuncartoon.bean.FavorPrivateInfo;

import java.util.List;
import java.util.logging.StreamHandler;

/**
 * 漫画展示小组件的适配器
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<FavorPrivateInfo> dataList;
    private Activity mActivity;

    public RecyclerViewAdapter(Activity activity, List dataList) {
        this.dataList = dataList;
        this.mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getComicname());
        holder.content.setText(dataList.get(position).getComicdptn());
        holder.idTag.setText(String.valueOf(dataList.get(position).getComicid()));
        RequestApiData.getInstance().getFavorPrivateImage(dataList.get(position).getComiccover(), holder.showImage);


//        holder.title.setText(dataList.get(position).split(",")[0]);
//        holder.content.setText(dataList.get(position).split(",")[1]);
//        if (position % 2 == 0) {
//            holder.showImage.setBackgroundResource(R.mipmap.show_img1);
//        } else {
//            holder.showImage.setBackgroundResource(R.mipmap.show_img2);
//        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, idTag;
        ImageView showImage;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
            showImage = (ImageView) itemView.findViewById(R.id.showImage);
            idTag = (TextView) itemView.findViewById(R.id.idTag);itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
////                    Toast toast;
////                    toast = Toast.makeText(mActivity, "你点了一下哦", Toast.LENGTH_SHORT);
////                    toast.show();
//
                    Intent intent = new Intent(mActivity, CartoonIntro.class);
                    intent.putExtra("url", (String) idTag.getText());
                    mActivity.startActivity(intent);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
