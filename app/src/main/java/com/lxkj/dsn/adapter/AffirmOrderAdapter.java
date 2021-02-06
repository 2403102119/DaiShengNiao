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
import com.lxkj.dsn.utils.AmountView2;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.lxkj.dsn.utils.StringUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Time:2021/2/3
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class AffirmOrderAdapter extends RecyclerView.Adapter<AffirmOrderAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public AffirmOrderAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public AffirmOrderAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_affirmorder, parent, false);
        return new AffirmOrderAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(AffirmOrderAdapter.MyHolder holder, final int position) {
        if (StringUtil.isEmpty(list.get(position).image)){
            Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                    .error(R.mipmap.logo)
                    .placeholder(R.mipmap.logo))
                    .load(list.get(position).gimage)
                    .into(holder.riIcon);
        }else {
            Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                    .error(R.mipmap.logo)
                    .placeholder(R.mipmap.logo))
                    .load(list.get(position).image)
                    .into(holder.riIcon);
        }

        if (StringUtil.isEmpty(list.get(position).name)){
            holder.tvName.setText(list.get(position).gname);
        }else {
            holder.tvName.setText(list.get(position).name);
        }


        if (SharePrefUtil.getString(context, AppConsts.ismember,null).equals("0")){
            holder.tvPrice.setText("¥"+list.get(position).oldprice);
        }else {
            holder.tvPrice.setText("¥"+list.get(position).newprice);
        }

        holder.tvNumber.setText("x"+list.get(position).numbers);
        holder.AmountView.setGoodsNubber(list.get(position).numbers);

        if (StringUtil.isEmpty(list.get(position).type)){
            holder.tvNumber.setVisibility(View.VISIBLE);
            holder.AmountView.setVisibility(View.GONE);
        }else {
            if (list.get(position).type.equals("1")){
                holder.AmountView.setVisibility(View.VISIBLE);
            }else {
                holder.AmountView.setVisibility(View.GONE);
            }
            holder.tvNumber.setVisibility(View.GONE);
        }


        holder.AmountView.setOnAmountChangeListener(new AmountView2.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                onItemClickListener.Onselect(position,amount+"");
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
         TextView tvSku;
         TextView tvPrice;
         TextView tvNumber;
         AmountView2 AmountView;
        public MyHolder(View itemView) {
            super(itemView);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvSku = itemView.findViewById(R.id.tvSku);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            AmountView = itemView.findViewById(R.id.AmountView);
        }
    }
    private AffirmOrderAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AffirmOrderAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void Onselect(int firstPosition,String amount);
    }
}
