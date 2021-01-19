package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
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
public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public RuleAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public RuleAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rule, parent, false);
        return new RuleAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RuleAdapter.MyHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {

//        if (list == null) {
//            return 0;
//        } else {
//            return list.size();
//        }
        return 10;
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(View itemView) {
            super(itemView);

        }
    }
    private RuleAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RuleAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}
