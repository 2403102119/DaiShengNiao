package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.utils.AppUtil;
import com.lxkj.dsn.utils.StringUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/18
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public ProductAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public ProductAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.MyHolder holder, final int position) {
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
        return 20;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
         RoundedImageView riImage;
        public MyHolder(View itemView) {
            super(itemView);
            riImage = itemView.findViewById(R.id.riImage);
        }
    }
    private ProductAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ProductAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

