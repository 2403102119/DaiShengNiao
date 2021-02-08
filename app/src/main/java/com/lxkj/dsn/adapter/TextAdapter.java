package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.OrdertailListBean;
import com.lxkj.dsn.utils.AmountView2;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/2/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class TextAdapter extends RecyclerView.Adapter<TextAdapter.MyHolder> {
    private Context context;
    private List<OrdertailListBean> list;
    public TextAdapter(Context context, List<OrdertailListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public TextAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_text, parent, false);
        return new TextAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(TextAdapter.MyHolder holder, final int position) {
         holder.tvName.setText(list.get(position).gname);
         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (list.get(position).check){
                     list.get(position).check = false;
                     holder.imXuanzhong.setImageResource(R.drawable.weixuanzhong);
                 }else {
                     list.get(position).check = true;
                     holder.imXuanzhong.setImageResource(R.drawable.xuanzhong);
                 }
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
        ImageView imXuanzhong;
        TextView tvName;
        public MyHolder(View itemView) {
            super(itemView);
            imXuanzhong = itemView.findViewById(R.id.imXuanzhong);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
    private TextAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(TextAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}
