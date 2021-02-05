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
import com.lxkj.dsn.bean.OrdertailListBean;
import com.lxkj.dsn.utils.AmountView2;
import com.lxkj.dsn.utils.StringUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/2/4
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyHolder> {
    private Context context;
    private List<OrdertailListBean> list;
    public OrderDetailAdapter(Context context, List<OrdertailListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public OrderDetailAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_affirmorder, parent, false);
        return new OrderDetailAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderDetailAdapter.MyHolder holder, final int position) {

        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.logo)
                .placeholder(R.mipmap.logo))
                .load(list.get(position).gimage)
                .into(holder.riIcon);

        holder.tvName.setText(list.get(position).gname);
        if (list.get(position).type.equals("2")){//积分订单
            holder.tvPrice.setText(list.get(position).gprice+"积分");
        }else {//商品订单
            holder.tvPrice.setText("¥"+list.get(position).gprice);
        }

        holder.tvNumber.setText("x"+list.get(position).gnum);
        holder.AmountView.setGoodsNubber(list.get(position).gnum);


        holder.tvNumber.setVisibility(View.VISIBLE);
        holder.AmountView.setVisibility(View.GONE);


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
    private OrderDetailAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OrderDetailAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void Onselect(int firstPosition,String amount);
    }
}
