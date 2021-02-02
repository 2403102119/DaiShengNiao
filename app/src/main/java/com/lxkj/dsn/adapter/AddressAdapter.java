package com.lxkj.dsn.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.ui.fragment.fra.EditeAddressFra;
import com.lxkj.dsn.view.NormalDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by kxn on 2020/1/18 0018.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private Context context;
    private List<DataListBean> list;
    private OnItemClickListener onItemClickListener;

    public AddressAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);

        void onCheckListener(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        holder.tvName.setText(list.get(position).name);
//        holder.tvPhone.setText(list.get(position).phone);
//        holder.tvAddress.setText(list.get(position).address);

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
//                bundle.putSerializable("data", list.get(position));
                ActivitySwitcher.startFragment(context, EditeAddressFra.class, bundle);
            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NormalDialog dialog = new NormalDialog(context, "确定删除该收货地址？", "取消", "确定", true);
                dialog.show();
                dialog.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                    @Override
                    public void OnRightClick() {
//                        deleteAddress(position);
                    }

                    @Override
                    public void OnLeftClick() {
                    }
                });
            }
        });


        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClickListener(position);
            }
        });
    }

//    /**
//     * 删除地址
//     *
//     * @param position
//     */
//    private void deleteAddress(final int position) {
//        final Map<String, Object> params = new HashMap<>();
//        params.put("id", list.get(position).id);
//        params.put("uid", SharePrefUtil.getString(context, AppConsts.UID,""));
//        OkHttpHelper.getInstance().post_json(context, Url.deleteAddress, params, new SpotsCallBack<ResultBean>(context) {
//            @Override
//            public void onSuccess(Response response, ResultBean resultBean) {
//                list.remove(position);
//                notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//            }
//        });
//    }

    @Override
    public int getItemCount() {
//        return list.size();
        return 2;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvPhone)
        TextView tvPhone;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.tvDelete)
        ImageView tvDelete;
        @BindView(R.id.tvEdit)
        ImageView tvEdit;
        @BindView(R.id.ll_item)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}