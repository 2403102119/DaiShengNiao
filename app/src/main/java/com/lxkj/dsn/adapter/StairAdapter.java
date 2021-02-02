package com.lxkj.dsn.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created ：李迪迦
 * on:2019/11/13 0013.
 * Describe :一级分类
 */

public class StairAdapter extends RecyclerView.Adapter<StairAdapter.MyHolder> implements View.OnClickListener{
    private Context context;
    private List<DataListBean> list;
    public int selectPosition = 0;
    public StairAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public StairAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stair, parent, false);
        return new StairAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final StairAdapter.MyHolder holder, final int position) {
        holder.tv_type.setText(list.get(position).name);
        holder.ll_sell.setTag(position);
        if (position==selectPosition){
            holder.view_green.setVisibility(View.VISIBLE);
            holder.ll_sell.setBackgroundResource(R.color.white);
            holder.tv_type.setTextColor(Color.parseColor("#111111"));
        }else {
            holder.view_green.setVisibility(View.INVISIBLE);
            holder.ll_sell.setBackgroundResource(R.color.space);
            holder.tv_type.setTextColor(Color.parseColor("#666666"));
        }
//        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
//                .error(R.mipmap.logo)
//                .placeholder(R.mipmap.logo))
//                .load(list.get(position).getCategoryImage())
//                .into(holder.roundim);


    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override
    public void onClick(View v) {
        //注意这里要给某一个控件添加点击事件
        int position = (int) v.getTag();      //getTag()获取数据
        if (onItemClickListener != null) {
            switch (v.getId()) {
                case R.id.ll_sell:
                    selectPosition = position;
                    onItemClickListener.OnItemClickListener(position);
                    notifyDataSetChanged();
                    break;
                default:
                    selectPosition = position;
                    break;
            }
        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_sell;
        View view_green;
        TextView tv_type;
        RoundedImageView roundim;
        public MyHolder(View itemView) {
            super(itemView);

            ll_sell = itemView.findViewById(R.id.ll_sell);
            view_green = itemView.findViewById(R.id.view_green);
            tv_type = itemView.findViewById(R.id.tv_type);
            roundim = itemView.findViewById(R.id.roundim);
            ll_sell.setOnClickListener(StairAdapter.this);
        }
    }

    /*------------------以下为item中的button控件点击事件处理------------------*/
    //item里面有多个控件可以点击（item+item内部控件）
    public enum ViewName {
        ITEM,
        PRACTISE
    }
    private StairAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(StairAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int position);

    }
}
