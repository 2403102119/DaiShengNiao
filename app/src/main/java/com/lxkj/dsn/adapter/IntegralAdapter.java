package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.utils.AppUtil;
import com.lxkj.dsn.utils.StringUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/19
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class IntegralAdapter extends RecyclerView.Adapter<IntegralAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public IntegralAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public IntegralAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_integral, parent, false);
        return new IntegralAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(IntegralAdapter.MyHolder holder, final int position) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.riImage.getLayoutParams();
        params.width =(StringUtils.getScreenWidth(context) - AppUtil.dp2px(context,8))/2;
        params.height =  params.width;
        holder.riImage.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
//
//        if (list == null) {
//            return 0;
//        } else {
//            return list.size();
//        }
        return 5;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        RoundedImageView riImage;
        public MyHolder(View itemView) {
            super(itemView);
            riImage = itemView.findViewById(R.id.riImage);
        }
    }
    private IntegralAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(IntegralAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

