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
 * Time:2021/1/25
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public InviteAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public InviteAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invite, parent, false);
        return new InviteAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(InviteAdapter.MyHolder holder, final int position) {


    }

    @Override
    public int getItemCount() {

//        if (list == null) {
//            return 0;
//        } else {
//            return list.size();
//        }
        return 3;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public MyHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);

        }
    }
    private InviteAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(InviteAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

