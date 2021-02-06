package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.utils.StringUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Time:2020/8/27
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:评价列表
 */
public class EvaluateAdapter extends RecyclerView.Adapter<EvaluateAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public EvaluateAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public EvaluateAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evaluate, parent, false);
        return new EvaluateAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(EvaluateAdapter.MyHolder holder, final int position) {

        holder.tvName.setText(list.get(position).username);
        holder.tvDate.setText("评论时间："+list.get(position).adtime);
        holder.tvContent.setText(list.get(position).content);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(layoutManager);
        Recycle_one_itemAdapter recycletwoItemAdapter=new Recycle_one_itemAdapter(context,list.get(position).images);
        holder.recyclerView.setAdapter(recycletwoItemAdapter);
        recycletwoItemAdapter.setOnItemClickListener(new Recycle_one_itemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                onItemClickListener.Onchakandatu(firstPosition,position);
            }
        });

        recycletwoItemAdapter.notifyDataSetChanged();

        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).usericon)
                .into(holder.imIcon);
        if (!StringUtil.isEmpty(list.get(position).star)){
            holder.mr_score.setRating(Float.parseFloat(list.get(position).star));
        }else {
            holder.mr_score.setRating(5);
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
        private TextView tvName,tvDate,tvContent;
        private RecyclerView recyclerView;
        private RoundedImageView imIcon;
        private MaterialRatingBar mr_score;
        public MyHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvContent = itemView.findViewById(R.id.tvContent);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            imIcon = itemView.findViewById(R.id.imIcon);
            mr_score = itemView.findViewById(R.id.mr_score);
        }
    }
    private EvaluateAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(EvaluateAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void Onchakandatu(int firstPosition, int position);
    }
}
