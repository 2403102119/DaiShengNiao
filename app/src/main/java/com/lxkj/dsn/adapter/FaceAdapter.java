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
 * Time:2021/1/18
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class FaceAdapter extends RecyclerView.Adapter<FaceAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public FaceAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public FaceAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_face, parent, false);
        return new FaceAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(FaceAdapter.MyHolder holder, final int position) {
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

        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.imageerror)
                .placeholder(R.mipmap.imageerror))
                .load(list.get(position).usericon)
                .into(holder.riIcon);
        holder.tvTitle.setText(list.get(position).username);
        holder.tvTime.setText(list.get(position).adtime);
        holder.tvConten.setText(list.get(position).content);
        holder.tvcommentCount.setText(list.get(position).commentnum);
        holder.tvcollectCount.setText(list.get(position).zannum);
        if (list.get(position).iszan.equals("0")){
            holder.imShoucang.setImageResource(R.mipmap.dianzan);
        }else {
            holder.imShoucang.setImageResource(R.mipmap.yidianzan);
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
         RecyclerView recyclerView;
         RoundedImageView riIcon;
         TextView tvTitle;
         TextView tvTime;
         TextView tvConten;
         TextView tvcommentCount;
         TextView tvcollectCount;
         ImageView imShoucang;
        public MyHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvConten = itemView.findViewById(R.id.tvConten);
            tvcommentCount = itemView.findViewById(R.id.tvcommentCount);
            tvcollectCount = itemView.findViewById(R.id.tvcollectCount);
            imShoucang = itemView.findViewById(R.id.imShoucang);
        }
    }
    private FaceAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(FaceAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}
