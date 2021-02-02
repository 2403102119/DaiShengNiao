package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/18
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public int select = 0;
    public boolean checked = false;
    public ClassAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public ClassAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class, parent, false);
        return new ClassAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassAdapter.MyHolder holder, final int position) {
        holder.tvName.setText(list.get(position).name);
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.yingyinshu)
                .placeholder(R.mipmap.yingyinshu))
                .load(list.get(position).image)
                .into(holder.imIcon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
            }
        });

         if (checked){
             if (position == select){
                 holder.tvName.setTextColor(context.getResources().getColor(R.color.main_color));
             }else {
                 holder.tvName.setTextColor(context.getResources().getColor(R.color.yijifenlei));
             }
         }


    }

    @Override
    public int getItemCount() {

        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView imIcon;
        TextView tvName;
        public MyHolder(View itemView) {
            super(itemView);
            imIcon = itemView.findViewById(R.id.imIcon);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
    private ClassAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ClassAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

