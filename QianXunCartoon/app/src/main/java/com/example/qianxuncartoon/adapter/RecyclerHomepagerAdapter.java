package com.example.qianxuncartoon.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qianxuncartoon.model.CartoonCover;

import java.util.List;
import com.example.qianxuncartoon.R;
import com.squareup.picasso.Picasso;

/**
 * Created by 咸鱼 on 2017/2/16.
 *
 * 首页列表的recyclerAdapter
 * 用到多个RecylerAdapter，后续可以做抽象
 */

public class RecyclerHomepagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<CartoonCover> datas;

    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void clear(){
        datas.clear();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public RecyclerHomepagerAdapter(Context context,List<CartoonCover> datas) {
        mContext=context;
        this.datas=datas;
    }

    @Override
    public int getItemViewType(int position) {
        //判断item类别，是图还是显示页数（图片有URL）
        if (!TextUtils.isEmpty(datas.get(position).getComiccover())) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据item类别加载不同ViewHolder
        if(viewType==0){
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_small, parent, false);
            //这个布局就是一个imageview用来显示图片
            MyViewHolder holder = new MyViewHolder(view);
            //给布局设置点击和长点击监听
            view.setOnClickListener(this);
            return holder;
        }else{
            MyViewHolder2 holder2=new MyViewHolder2(LayoutInflater.from(mContext).inflate(R.layout.recycler_test, parent,
                    false));
            //这个布局就是一个textview用来显示页数
            return holder2;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //将数据与item视图进行绑定，如果是MyViewHolder就加载网络图片，如果是MyViewHolder2就显示页数
        if(holder instanceof MyViewHolder){
            Picasso.with(mContext).load(datas.get(position).getComiccover()).into(((MyViewHolder) holder).item_img);//加载网络图片
           //此处可以优化
            ((MyViewHolder) holder).item_tex.setText(datas.get(position).getComicname());
       //     ((MyViewHolder) holder).item_id.setText(datas.get(position).getComicid());

        }else if(holder instanceof MyViewHolder2){
            ((MyViewHolder2) holder).item_img.setText(datas.get(position)+"页");
        }
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    //点击事件的回调
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v);
        }
    }

    //自定义ViewHolder，用于加载图片
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView item_img;
        private TextView item_tex;
        private TextView item_id;
        public MyViewHolder(View view)
        {
            super(view);
            item_img = (ImageView) view.findViewById(R.id.recycler_item_small_img);
            item_tex = (TextView) view.findViewById(R.id.recycler_item_small_txt);
            item_id = (TextView) view.findViewById(R.id.recycler_item_comic_id);
        }
    }

    //自定义ViewHolder，用于显示页数
    class MyViewHolder2 extends RecyclerView.ViewHolder
    {
        private TextView item_img;
        private TextView item_tex;

        public MyViewHolder2(View view)
        {
            super(view);
            item_img = (TextView) view.findViewById(R.id.recycler_test_test1);
            item_tex = (TextView) view.findViewById(R.id.recycler_test_test2);
            item_tex.setText("测试之用");
        }
    }
}
