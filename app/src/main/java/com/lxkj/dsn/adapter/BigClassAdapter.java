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
 * Time:2021/1/18
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class BigClassAdapter extends RecyclerView.Adapter<BigClassAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public BigClassAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public BigClassAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bigclass, parent, false);
        return new BigClassAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(BigClassAdapter.MyHolder holder, final int position) {
        holder.tvTitle.setText(list.get(position).name);
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.yingyinshu)
                .placeholder(R.mipmap.yingyinshu))
                .load(list.get(position).image)
                .into(holder.riIcon);
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
        private TextView tvTitle;
        private RoundedImageView riIcon;
        public MyHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            riIcon = itemView.findViewById(R.id.riIcon);
        }
    }
    private BigClassAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(BigClassAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

