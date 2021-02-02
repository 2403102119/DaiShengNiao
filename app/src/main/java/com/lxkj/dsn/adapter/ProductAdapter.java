package com.lxkj.dsn.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.utils.AppUtil;
import com.lxkj.dsn.utils.StringUtils;
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
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public ProductAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public ProductAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.MyHolder holder, final int position) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.riImage.getLayoutParams();
        params.width =(StringUtils.getScreenWidth(context) - AppUtil.dp2px(context,8))/2;
        params.height =  params.width;
        holder.riImage.setLayoutParams(params);

        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.yingyinshu)
                .placeholder(R.mipmap.yingyinshu))
                .load(list.get(position).image)
                .into(holder.riImage);
        holder.tvName.setText(list.get(position).name);
        holder.tvPrice.setText(list.get(position).newprice);
        holder.tvOldPrice.setText("¥"+list.get(position).oldprice);
        holder.tvOldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
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
         RoundedImageView riImage;
         TextView tvName;
         TextView tvOldPrice;
         TextView tvPrice;
        public MyHolder(View itemView) {
            super(itemView);
            riImage = itemView.findViewById(R.id.riImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
        }
    }
    private ProductAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ProductAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

