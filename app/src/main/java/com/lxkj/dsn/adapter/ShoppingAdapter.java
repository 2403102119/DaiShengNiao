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

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2019/10/12 0012.
 */

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;;

    public ShoppingAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public ShoppingAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shopcar, parent, false);
        return new ShoppingAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShoppingAdapter.MyHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
//        if (list == null) {
//            return 0;
//        } else {
//            return list.size();
//        }
        return 9;
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(View itemView) {
            super(itemView);

        }
    }
    private ShoppingAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ShoppingAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void OnItem(int position);

    }


}
