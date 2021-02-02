package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.utils.AppUtil;
import com.lxkj.dsn.utils.StringUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/29
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public AuthorAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public AuthorAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_anchor, parent, false);
        return new AuthorAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(AuthorAdapter.MyHolder holder, final int position) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.riImage.getLayoutParams();
        params.width =(StringUtils.getScreenWidth(context) - AppUtil.dp2px(context,8))/3;
        params.height =  params.width;
        holder.riImage.setLayoutParams(params);

        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.logo)
                .placeholder(R.mipmap.logo))
                .load(list.get(position).image)
                .into(holder.riImage);
        holder.tvName.setText(list.get(position).name);

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
        RoundedImageView riImage;
        TextView tvName;
        public MyHolder(View itemView) {
            super(itemView);
            riImage = itemView.findViewById(R.id.riImage);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
    private AuthorAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AuthorAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

