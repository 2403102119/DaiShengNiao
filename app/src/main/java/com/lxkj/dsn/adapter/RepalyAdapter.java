package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Time:2021/1/22
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class RepalyAdapter extends RecyclerView.Adapter<RepalyAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public RepalyAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public RepalyAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_repaly, parent, false);
        return new RepalyAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RepalyAdapter.MyHolder holder, final int position) {


    }

    @Override
    public int getItemCount() {

//        if (list == null) {
//            return 0;
//        } else {
//            return list.size();
//        }
        return 8;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public MyHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);

        }
    }
    private RepalyAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RepalyAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

