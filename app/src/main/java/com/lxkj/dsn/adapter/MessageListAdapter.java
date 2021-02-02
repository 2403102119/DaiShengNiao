package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.utils.StringUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2020/9/30
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:消息列表
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public MessageListAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public MessageListAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_messagelist, parent, false);
        return new MessageListAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageListAdapter.MyHolder holder, final int position) {
          if (list.get(position).state.equals("0")){
              holder.viewWeidu.setVisibility(View.VISIBLE);
          }else {
              holder.viewWeidu.setVisibility(View.GONE);
          }

          holder.tvTitle.setText(list.get(position).title);
          holder.tvContent.setText(list.get(position).content);
          holder.tvCreateDate.setText(list.get(position).adtime);
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
        View viewWeidu;
        TextView tvTitle;
        TextView tvContent;
        TextView tvCreateDate;
        public MyHolder(View itemView) {
            super(itemView);
            viewWeidu = itemView.findViewById(R.id.viewWeidu);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvCreateDate = itemView.findViewById(R.id.tvCreateDate);
        }
    }
    private MessageListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MessageListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void OnDelateClickListener(int firstPosition);
    }
}
