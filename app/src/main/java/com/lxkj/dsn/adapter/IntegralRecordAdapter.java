package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/21
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class IntegralRecordAdapter extends RecyclerView.Adapter<IntegralRecordAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public IntegralRecordAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public IntegralRecordAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_integralrecord, parent, false);
        return new IntegralRecordAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(IntegralRecordAdapter.MyHolder holder, final int position) {

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
        public MyHolder(View itemView) {
            super(itemView);
        }
    }
    private IntegralRecordAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(IntegralRecordAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

