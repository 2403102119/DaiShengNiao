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
public class ConsumeItemAdapter extends RecyclerView.Adapter<ConsumeItemAdapter.MyHolder> {
    private Context context;
    private List<DataListBean.OrdertailList> list;
    public ConsumeItemAdapter(Context context, List<DataListBean.OrdertailList> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public ConsumeItemAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_consumeitem, parent, false);
        return new ConsumeItemAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ConsumeItemAdapter.MyHolder holder, final int position) {
        holder.tvName.setText(list.get(position).gname);
        holder.tvNumber.setText("¥"+list.get(position).gnum);
        holder.tvPrice.setText(list.get(position).gprice);


        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).gimage)
                .into(holder.riIcon);

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
        TextView tvNumber;
        TextView tvPrice;
        RoundedImageView riIcon;
        public MyHolder(View itemView) {
            super(itemView);

            riIcon = itemView.findViewById(R.id.riIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
    private ConsumeItemAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ConsumeItemAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

