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
import com.lxkj.dsn.utils.AmountView2;
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
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.logo)
                .placeholder(R.mipmap.logo))
                .load(list.get(position).gimage)
                .into(holder.imIcon);
        holder.tvName.setText(list.get(position).gname);
        holder.tvPrice.setText(list.get(position).newprice);
        holder.AmountView.setGoodsNubber(list.get(position).numbers);

        if (list.get(position).isCheck==true){
            holder.imageSel.setImageResource(R.drawable.xuanzhong);
        }else {
            holder.imageSel.setImageResource(R.drawable.weixuanzhong);
        }
        holder.imageSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).isCheck==false){
                    list.get(position).isCheck = true;
                    holder.imageSel.setImageResource(R.drawable.xuanzhong);
                }else {
                    list.get(position).isCheck = false;
                    holder.imageSel.setImageResource(R.drawable.weixuanzhong);
                }
                onItemClickListener.OnItem(position);
            }
        });
        holder.AmountView.setOnAmountChangeListener(new AmountView2.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                onItemClickListener.Onselect(position,amount+"");
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
           ImageView imIcon;
           ImageView imageSel;
           TextView tvName;
           TextView tvPrice;
           AmountView2 AmountView;
        public MyHolder(View itemView) {
            super(itemView);
            imIcon = itemView.findViewById(R.id.imIcon);
            imageSel = itemView.findViewById(R.id.imageSel);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            AmountView = itemView.findViewById(R.id.AmountView);
        }
    }
    private ShoppingAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ShoppingAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void OnItem(int position);
        void Onselect(int position,String mount);
    }


}
