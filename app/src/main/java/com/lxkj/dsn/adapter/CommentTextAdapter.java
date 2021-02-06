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
import com.lxkj.dsn.bean.DataListBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/2/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class CommentTextAdapter extends RecyclerView.Adapter<CommentTextAdapter.MyHolder> {
    private Context context;
    private List<DataListBean.SecondList> list;
    public CommentTextAdapter(Context context, List<DataListBean.SecondList> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public CommentTextAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_commenttext, parent, false);
        return new CommentTextAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentTextAdapter.MyHolder holder, final int position) {
         holder.tvContent.setText(list.get(position).content);
         holder.tvYonghu.setText(list.get(position).tanickname);
         if (list.get(position).usertype.equals("0")){
             holder.tvZuozhe.setText(list.get(position).usernickname);
         }else {
             holder.tvZuozhe.setText("作者");
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
        TextView tvZuozhe;
        TextView tvYonghu;
        TextView tvContent;
        public MyHolder(View itemView) {
            super(itemView);
            tvZuozhe = itemView.findViewById(R.id.tvZuozhe);
            tvYonghu = itemView.findViewById(R.id.tvYonghu);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
    private CommentTextAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(CommentTextAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

