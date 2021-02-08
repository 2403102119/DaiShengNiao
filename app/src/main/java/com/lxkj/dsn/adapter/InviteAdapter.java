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

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/25
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public InviteAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public InviteAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invite, parent, false);
        return new InviteAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(InviteAdapter.MyHolder holder, final int position) {

        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).usericon)
                .into(holder.riIcon);
        holder.tvName.setText(list.get(position).username);
        holder.tvTime.setText(list.get(position).adtime);
        holder.tvLeiji.setText("累计消费："+list.get(position).allsalemoney);
        holder.tvHuibao.setText("累计回报："+list.get(position).allmoney);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
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
        RoundedImageView riIcon;
        TextView tvName;
        TextView tvTime;
        TextView tvLeiji;
        TextView tvHuibao;
        public MyHolder(View itemView) {
            super(itemView);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvLeiji = itemView.findViewById(R.id.tvLeiji);
            tvHuibao = itemView.findViewById(R.id.tvHuibao);
        }
    }
    private InviteAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(InviteAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

