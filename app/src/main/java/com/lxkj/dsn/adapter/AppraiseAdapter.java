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
    private PostArticleImgAdapter postArticleImgAdapter;
    private String plusPath;
    private ArrayList<String> refundimage = new ArrayList<>();
    private List<String> mBannerSelectPath = new ArrayList<>();
    private static EvaluateListBean evaluateListBean = new EvaluateListBean();

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

        evaluateListBean.odid = list.get(position).odid;


        postArticleImgAdapter = new PostArticleImgAdapter(context,mBannerSelectPath);
        holder.ryimage.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        holder.ryimage.setAdapter(postArticleImgAdapter);
        postArticleImgAdapter.setOnItemClickListener(new PostArticleImgAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int positiom) {
                evaluateListBean.images.remove(positiom);
                mBannerSelectPath.remove(positiom);
                postArticleImgAdapter.notifyDataSetChanged();
            }

            @Override
            public void Onbig(int position) {
               onItemClickListener.OnItemClickListener(position,mBannerSelectPath);
            }

        });

        //添加按钮图片资源
        plusPath = context.getString(R.string.glide_plus_icon_string) + AppUtils.getPackageInfo(context).packageName + "/mipmap/" + R.mipmap.tianjiatupian;
        mBannerSelectPath.add(plusPath);//添加按键，超过9张时在adapter中隐藏

        holder.mr_score.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                evaluateListBean.star = rating+"";
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
                evaluateListBean.content =  editable.toString();
            }
        });


    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setdata(List<String> bean,List<String> mBannerSelectPath1){
        evaluateListBean.images = bean;
        mBannerSelectPath = mBannerSelectPath1;
        postArticleImgAdapter.notifyDataSetChanged();
    }
    public EvaluateListBean getBean(){

        return evaluateListBean;
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
        void OnItemClickListener(int firstPosition,List<String> mBannerSelectPath);
    }
}
