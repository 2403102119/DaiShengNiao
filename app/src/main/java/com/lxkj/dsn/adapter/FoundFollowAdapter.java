package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/2/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class FoundFollowAdapter extends RecyclerView.Adapter<FoundFollowAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public FoundFollowAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public FoundFollowAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_integraldetail, parent, false);
        return new FoundFollowAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(FoundFollowAdapter.MyHolder holder, final int position) {
        holder.tvTitle.setText(list.get(position).title);
        holder.tvJifen.setText(list.get(position).money);
        holder.tvTime.setText(list.get(position).adtime);
        if (list.get(position).type.equals("0")){
             holder.tvReson.setText("分销收入");
        }else if (list.get(position).type.equals("1")){
            holder.tvReson.setText("消费支出");
        }else {
            holder.tvReson.setText("退款收入");
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
        TextView tvTitle;
        TextView tvJifen;
        TextView tvTime;
        TextView tvReson;
        public MyHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvJifen = itemView.findViewById(R.id.tvJifen);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvReson = itemView.findViewById(R.id.tvReson);
        }
    }
    private FoundFollowAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(FoundFollowAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

