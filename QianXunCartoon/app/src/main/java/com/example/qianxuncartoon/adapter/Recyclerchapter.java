package com.example.qianxuncartoon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.model.TbEpisode;

import java.util.List;

/**
 * Created by 咸鱼 on 2017/2/24.
 */

public class Recyclerchapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<TbEpisode> datas;

    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public Recyclerchapter(Context context,List<TbEpisode> datas) {
        mContext=context;
        this.datas=datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gridv_chapter,parent,false);
        ChapterViewHolder mHolder = new ChapterViewHolder(view);
        view.setOnClickListener(this);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Integer i = datas.get(position).getEpisodeid();
        String la = String.valueOf(i);
        ((ChapterViewHolder)holder).episodeid.setText(String.valueOf(datas.get(position).getEpisodeid()));
        ((ChapterViewHolder)holder).episodenum.setText("第" + datas.get(position).getEpisodenum() + "话");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v);
        }
    }

    //自定义ViewHolder
    class ChapterViewHolder extends RecyclerView.ViewHolder{

        private TextView episodeid;
        private TextView episodenum;
        public ChapterViewHolder(View itemView) {
            super(itemView);
            episodeid = (TextView) itemView.findViewById(R.id.gridv_chapter_epiid);
            episodenum = (TextView) itemView.findViewById(R.id.gridv_chapter_epinum);
        }
    }
}
