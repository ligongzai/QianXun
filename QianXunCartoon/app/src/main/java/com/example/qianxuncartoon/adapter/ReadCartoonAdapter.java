package com.example.qianxuncartoon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.model.TbPicture;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 咸鱼 on 2017/2/24.
 */

public class ReadCartoonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<TbPicture> datas;

    //适配器初始化
    public ReadCartoonAdapter(Context context,List<TbPicture> datas){
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_readcartoon,parent,false);
        ReadHolder mHolder = new ReadHolder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String i = datas.get(position).getPictureurl();
        Picasso.with(context).load(datas.get(position).getPictureurl()).into(((ReadHolder)holder).image);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private class ReadHolder extends RecyclerView.ViewHolder{

        private ImageView image;

        public ReadHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.recycler_item_readcartoon_img);
        }
    }
}
