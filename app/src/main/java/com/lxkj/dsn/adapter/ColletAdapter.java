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
 * Time:2021/1/23
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class ColletAdapter extends RecyclerView.Adapter<ColletAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public ColletAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public ColletAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_collet, parent, false);
        return new ColletAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ColletAdapter.MyHolder holder, final int position) {


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
    private ColletAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ColletAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

