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
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/2/7
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class ConsumeAdapter extends RecyclerView.Adapter<ConsumeAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    private ConsumeItemAdapter consumeItemAdapter;
    public ConsumeAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public ConsumeAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_consume, parent, false);
        return new ConsumeAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ConsumeAdapter.MyHolder holder, final int position) {
        holder.tvName.setText(list.get(position).ordernum);
        holder.tvOrderStatus.setText(list.get(position).adtime);
        holder.tvHeji.setText(list.get(position).goodsprice);
        holder.tvShouyi.setText(list.get(position).profitmoney);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(layoutManager);
        consumeItemAdapter = new ConsumeItemAdapter(context, list.get(position).ordertailList);
        holder.recyclerView.setAdapter(consumeItemAdapter);
        consumeItemAdapter.setOnItemClickListener(new ConsumeItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

            }
        });

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
        TextView tvName;
        TextView tvOrderStatus;
        TextView tvHeji;
        TextView tvShouyi;
        RecyclerView recyclerView;
        public MyHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvHeji = itemView.findViewById(R.id.tvHeji);
            tvShouyi = itemView.findViewById(R.id.tvShouyi);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }
    private ConsumeAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ConsumeAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}
