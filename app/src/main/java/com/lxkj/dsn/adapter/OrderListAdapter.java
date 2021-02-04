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
 * Time:2021/2/1
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;;

    public OrderListAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public OrderListAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_orderlist, parent, false);
        return new OrderListAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderListAdapter.MyHolder holder, final int position) {

       holder.tvorderNo.setText(list.get(position).ordernum);
       holder.tvRealAmount.setText(list.get(position).goodsprice);
       holder.tvTime.setText("日期："+list.get(position).adtime);
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).ordertailList.get(0).gimage)
                .into(holder.riIcon);

       if (list.get(position).state.equals("0")){
           holder.tvOrderStatus.setText("待付款");
           holder.tvPay.setVisibility(View.VISIBLE);
           holder.tvCancel.setVisibility(View.VISIBLE);
           holder.tvPay.setText("去付款");
           holder.tvCancel.setText("取消订单");
       }else if (list.get(position).state.equals("1")){
           holder.tvOrderStatus.setText("待发货");
           holder.tvPay.setVisibility(View.VISIBLE);
           holder.tvCancel.setVisibility(View.GONE);
           holder.tvPay.setText("提醒发货");
       }else if (list.get(position).state.equals("2")){
           holder.tvOrderStatus.setText("待收货");
           holder.tvPay.setVisibility(View.VISIBLE);
           holder.tvCancel.setVisibility(View.GONE);
           holder.tvPay.setText("确认收货");
       }else if (list.get(position).state.equals("3")){
           holder.tvOrderStatus.setText("待评价");
           holder.tvPay.setVisibility(View.VISIBLE);
           holder.tvCancel.setVisibility(View.GONE);
           holder.tvPay.setText("去评价");
       }else if (list.get(position).state.equals("4")){
           holder.tvOrderStatus.setText("已完成");
           holder.tvPay.setVisibility(View.GONE);
           holder.tvCancel.setVisibility(View.GONE);
       }else if (list.get(position).state.equals("5")){
           holder.tvOrderStatus.setText("退款中");
           holder.tvPay.setVisibility(View.GONE);
           holder.tvCancel.setVisibility(View.GONE);
       }else if (list.get(position).state.equals("6")){
           holder.tvOrderStatus.setText("已退款");
           holder.tvPay.setVisibility(View.GONE);
           holder.tvCancel.setVisibility(View.GONE);
       }else if (list.get(position).state.equals("7")){
           holder.tvOrderStatus.setText("已取消");
           holder.tvPay.setVisibility(View.GONE);
           holder.tvCancel.setVisibility(View.GONE);
       }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItem(position);
            }
        });
       holder.tvPay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onItemClickListener.OnPay(position,holder.tvPay.getText().toString());
           }
       });
       holder.tvCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onItemClickListener.OnQuxiao(position);
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
         TextView tvorderNo;
         TextView tvTime;
         TextView tvRealAmount;
         TextView tvOrderStatus;
         TextView tvPay;
         TextView tvCancel;
         RoundedImageView riIcon;
        public MyHolder(View itemView) {
            super(itemView);
            tvorderNo = itemView.findViewById(R.id.tvorderNo);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvRealAmount = itemView.findViewById(R.id.tvRealAmount);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvPay = itemView.findViewById(R.id.tvPay);
            tvCancel = itemView.findViewById(R.id.tvCancel);
            riIcon = itemView.findViewById(R.id.riIcon);
        }
    }
    private OrderListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OrderListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void OnItem(int position);
        void OnPay(int position,String state);
        void OnQuxiao(int position);

    }


}
