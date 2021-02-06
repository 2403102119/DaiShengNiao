package com.lxkj.dsn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    private CommentTextAdapter commentTextAdapter;
    public CommentAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public CommentAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.MyHolder holder, final int position) {

        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).usericon)
                .into(holder.ri_icon);
        holder.tv_name.setText(list.get(position).usernickname);
        holder.tv_content.setText(list.get(position).content);
        holder.tv_time.setText(list.get(position).adtime);
        holder.tvDianzan.setText(list.get(position).zannum);
        if (list.get(position).iszan.equals("0")){
            holder.imDianzan.setImageResource(R.mipmap.weidianzan);
        }else {
            holder.imDianzan.setImageResource(R.mipmap.yidianzan);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(layoutManager);
        commentTextAdapter = new CommentTextAdapter(context, list.get(position).secondList);
        holder.recyclerView.setAdapter(commentTextAdapter);
        commentTextAdapter.setOnItemClickListener(new CommentTextAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
            }
        });
        holder.imDianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnDianzanClickListener(position);
            }
        });
        holder.imgengduo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnJubaoClickListener(position);
            }
        });

//        List<String> spinnerlist = new ArrayList<>();
//        if (list.get(position).usertype.equals("0")){
//            spinnerlist.add("举报用户");
//            if (list.get(position).userid.equals(SharePrefUtil.getString(context, AppConsts.UID,null))){
//                spinnerlist.add("删除");
//            }
//        }else {
//            spinnerlist.add("举报用户");
//            spinnerlist.add("删除");
//        }
//        ArrayAdapter mAdapter = new ArrayAdapter<String>(context, R.layout.commet_spinner, spinnerlist);
//        holder.spinner.setAdapter(mAdapter);
//        holder.spinner.setSelection(0,false);
//        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int selectposition, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

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
        RoundedImageView ri_icon;
        TextView tv_name;
        TextView tv_content;
        TextView tvDianzan;
        TextView tv_time;
        ImageView imDianzan;
        RecyclerView recyclerView;
        Spinner spinner;
        ImageView imgengduo;
        public MyHolder(View itemView) {
            super(itemView);
            ri_icon = itemView.findViewById(R.id.ri_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_time = itemView.findViewById(R.id.tv_time);
            tvDianzan = itemView.findViewById(R.id.tvDianzan);
            imDianzan = itemView.findViewById(R.id.imDianzan);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            spinner = itemView.findViewById(R.id.spinner);
            imgengduo = itemView.findViewById(R.id.imgengduo);
        }
    }
    private CommentAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(CommentAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void OnDianzanClickListener(int firstPosition);
        void OnJubaoClickListener(int position);
    }
}

