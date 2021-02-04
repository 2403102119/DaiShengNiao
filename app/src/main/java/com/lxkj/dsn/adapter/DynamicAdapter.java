package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Time:2020/12/30
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    private List<String> images = new ArrayList<>();
    public DynamicAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public DynamicAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dynamic, parent, false);
        return new DynamicAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(DynamicAdapter.MyHolder holder, final int position) {
        holder.tvTitle.setText(list.get(position).username);
        holder.tvTime.setText(list.get(position).adtime);
        holder.tvConten.setText(list.get(position).content);
        holder.tvcommentCount.setText(list.get(position).commentnum);
        holder.tvcollectCount.setText(list.get(position).zannum);
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).usericon)
                .into(holder.riIcon);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(layoutManager);
        Recycle_one_itemAdapter recycletwoItemAdapter=new Recycle_one_itemAdapter(context,list.get(position).images);
        holder.recyclerView.setAdapter(recycletwoItemAdapter);
        recycletwoItemAdapter.setOnItemClickListener(new Recycle_one_itemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
//                onItemClickListener.Onchakandatu(firstPosition,position);
            }
        });

        recycletwoItemAdapter.notifyDataSetChanged();
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
        RecyclerView recyclerView;
        TextView tvTitle;
        TextView tvTime;
        TextView tvConten;
        TextView tvcommentCount;
        TextView tvcollectCount;
        RoundedImageView riIcon;
        public MyHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvConten = itemView.findViewById(R.id.tvConten);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvcommentCount = itemView.findViewById(R.id.tvcommentCount);
            tvcollectCount = itemView.findViewById(R.id.tvcollectCount);
        }
    }
    private DynamicAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(DynamicAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void OnDetailClickListener(int firstPosition);
        void OnFenxiangClickListener(int firstPosition);
    }
}


