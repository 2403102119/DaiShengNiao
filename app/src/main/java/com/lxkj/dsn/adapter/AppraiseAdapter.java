package com.lxkj.dsn.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hys.utils.AppUtils;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.EvaluateListBean;
import com.lxkj.dsn.bean.OrdertailListBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created ：李迪迦
 * on:2019/12/3 0003.
 * Describe :
 */

public class AppraiseAdapter extends  RecyclerView.Adapter<AppraiseAdapter.MyHolder>  {

    private Context context;
    private List<OrdertailListBean> list;

    private ArrayList<String> refundimage = new ArrayList<>();



    public AppraiseAdapter(Context context, List<OrdertailListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public AppraiseAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appraise, parent, false);
        return new AppraiseAdapter.MyHolder(view);
    }
    @Override
    public void onBindViewHolder(AppraiseAdapter.MyHolder holder, final int position) {
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.logo)
                .placeholder(R.mipmap.logo))
                .load(list.get(position).gimage)
                .into(holder.riIcon);

        holder.tvTitle.setText(list.get(position).gname);

        PostArticleImgAdapter postArticleImgAdapter = new PostArticleImgAdapter(context,list.get(position).mBannerSelectPath);
        holder.ryimage.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        holder.ryimage.setAdapter(postArticleImgAdapter);
        postArticleImgAdapter.setOnItemClickListener(new PostArticleImgAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int positiom) {
                list.get(position).mBannerSelectPath.remove(positiom);
                notifyDataSetChanged();
            }

            @Override
            public void Onbig(int positiom) {
                onItemClickListener.OnItemClickListener(position);
            }

        });



        holder.mr_score.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                onItemClickListener.OnRatingListener(position,rating+"");
            }
        });

        //内容
        holder.editFeed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                onItemClickListener.OnEditListener(position,editable.toString());
            }
        });


    }
    @Override
    public long getItemId(int i) {
        return i;
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
        private RoundedImageView riIcon;
        TextView tvTitle;
        EditText editFeed;
        RecyclerView ryimage;
        MaterialRatingBar mr_score;
        public MyHolder(View itemView) {
            super(itemView);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ryimage = itemView.findViewById(R.id.ryimage);
            mr_score = itemView.findViewById(R.id.mr_score);
            editFeed = itemView.findViewById(R.id.editFeed);
        }
    }



    private AppraiseAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AppraiseAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void OnEditListener(int firstPosition,String edit);
        void OnRatingListener(int firstPosition,String star);
    }
}
