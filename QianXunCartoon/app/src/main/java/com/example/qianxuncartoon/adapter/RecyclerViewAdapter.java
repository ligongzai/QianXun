package com.example.qianxuncartoon.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qianxuncartoon.R;

import java.util.List;

/**
 * 漫画展示小组件的适配器
 *
 *
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<String> dataList;
    private Activity mActivity;

    public RecyclerViewAdapter(Activity activity, List<String> dataList) {
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
        holder.title.setText(dataList.get(position).split(",")[0]);
        holder.content.setText(dataList.get(position).split(",")[1]);
        if (position % 2 == 0) {
            holder.showImage.setBackgroundResource(R.mipmap.show_img1);
        } else {
            holder.showImage.setBackgroundResource(R.mipmap.show_img2);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        ImageView showImage;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
            showImage = (ImageView) itemView.findViewById(R.id.showImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast;
                    toast = Toast.makeText(mActivity, "你点了一下哦", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }
}
