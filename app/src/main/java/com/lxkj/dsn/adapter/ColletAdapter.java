package com.lxkj.dsn.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.utils.SharePrefUtil;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/23
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class ColletAdapter extends RecyclerView.Adapter<ColletAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public ColletAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public ColletAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_collet, parent, false);
        return new ColletAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ColletAdapter.MyHolder holder, final int position) {
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).image)
                .into(holder.imIcon);
        holder.tvTitle.setText(list.get(position).name);

        if ("0".equals(SharePrefUtil.getString(context, AppConsts.ismember,null))){
            holder.tvPrice.setText(list.get(position).oldprice);
            holder.tvNumber.setVisibility(View.INVISIBLE);
        }else {
            holder.tvPrice.setText(list.get(position).newprice);
            holder.tvNumber.setVisibility(View.VISIBLE);
            holder.tvNumber.setText("¥"+list.get(position).oldprice);
            holder.tvNumber.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }



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
          ImageView imIcon;
          TextView tvTitle;
          TextView tvPrice;
          TextView tvNumber;
        public MyHolder(View itemView) {
            super(itemView);
            imIcon = itemView.findViewById(R.id.imIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvNumber = itemView.findViewById(R.id.tvNumber);
        }
    }
    private ColletAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ColletAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

