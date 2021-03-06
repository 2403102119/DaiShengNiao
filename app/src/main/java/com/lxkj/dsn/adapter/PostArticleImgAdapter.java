package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.R;
import com.lxkj.dsn.ui.fragment.fra.PushHeartFra;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 图片显示Adapter
 * Created by kuyue on 2017/6/19 下午3:59.
 * 邮箱:595327086@qq.com
 */

public class PostArticleImgAdapter extends RecyclerView.Adapter<PostArticleImgAdapter.MyViewHolder> {

    private List<String> mDatas;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public PostArticleImgAdapter(Context context, List<String> datas) {
        this.mDatas = datas;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.item_post_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (position >= PushHeartFra.IMAGE_SIZE) {//图片已选完时，隐藏添加按钮
//            holder.imageView.setVisibility(View.GONE);
//        } else {
//            holder.imageView.setVisibility(View.VISIBLE);
//        }

        if (position == mDatas.size())
            holder.imageView.setImageResource(R.mipmap.tianjiatupian);
        else
            Glide.with(mContext).applyDefaultRequestOptions(new RequestOptions()
                    .error(R.mipmap.imageerror)
                    .placeholder(R.mipmap.imageerror))
                    .load(mDatas.get(position))
                    .into(holder.imageView);


        if (position == mDatas.size()) {
            holder.del.setVisibility(View.GONE);
        }else {
            holder.del.setVisibility(View.VISIBLE);
        }

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == mDatas.size())
                    onItemClickListener.Onbig(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return  mDatas.size()+1;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView,del;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sdv);
            del = itemView.findViewById(R.id.del);
        }
    }

    private PostArticleImgAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(PostArticleImgAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int position);
        void Onbig(int position);

    }
}
