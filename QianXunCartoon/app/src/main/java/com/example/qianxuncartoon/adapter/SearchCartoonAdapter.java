package com.example.qianxuncartoon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qianxuncartoon.R;
import com.example.qianxuncartoon.model.TbComic;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 咸鱼 on 2017/2/27.
 */

public class SearchCartoonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;
    private List<TbComic> datas;

    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        mOnItemClickListener = listener;
    };

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null){
             mOnItemClickListener.onItemClick(v);
        }
    }

    //适配器初始化
    public SearchCartoonAdapter(Context context,List<TbComic> datas){
        this.context = context;
        this.datas = datas;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item,parent,false);
        SearchHolder mHolder = new SearchHolder(view);
        view.setOnClickListener(this);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((SearchHolder)holder).title.setText(datas.get(position).getComicname());
        ((SearchHolder)holder).context.setText(datas.get(position).getComicdptn());
        ((SearchHolder)holder).idTag.setText(String.valueOf(datas.get(position).getComicid()));
        Picasso.with(context).load(datas.get(position).getComiccover()).into(((SearchHolder)holder).showImage);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private class SearchHolder extends RecyclerView.ViewHolder{

        private ImageView showImage;
        private TextView title;
        private TextView context;
        private TextView idTag;

        public SearchHolder(View itemView) {
            super(itemView);
            showImage = (ImageView) itemView.findViewById(R.id.showImage);
            title = (TextView) itemView.findViewById(R.id.title);
            context = (TextView) itemView.findViewById(R.id.content);
            idTag = (TextView) itemView.findViewById(R.id.idTag);
        }
    }
}
