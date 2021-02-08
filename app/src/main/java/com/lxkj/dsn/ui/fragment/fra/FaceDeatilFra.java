package com.lxkj.dsn.ui.fragment.fra;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.CommentAdapter;
import com.lxkj.dsn.adapter.Recycle_one_itemAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.ui.fragment.login.LoginFra;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.lxkj.dsn.utils.StringUtil;
import com.lxkj.dsn.utils.ToastUtil;
import com.lxkj.dsn.view.NormalDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.shinichi.library.ImagePreview;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/2/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class FaceDeatilFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvConten)
    TextView tvConten;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.imDianzan)
    ImageView imDianzan;
    @BindView(R.id.tvDianzan)
    TextView tvDianzan;
    @BindView(R.id.imsubCommentList)
    TextView imsubCommentList;
    @BindView(R.id.llComment)
    LinearLayout llComment;
    @BindView(R.id.rycomment)
    RecyclerView rycomment;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.tvSent)
    TextView tvSent;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.llOne)
    LinearLayout llOne;
    @BindView(R.id.tvQuanbu)
    TextView tvQuanbu;
    @BindView(R.id.tvZhikan)
    TextView tvZhikan;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.llDianzan)
    LinearLayout llDianzan;

    private ArrayList<DataListBean> listBeans;
    private ArrayList<String> images = new ArrayList<>();
    private int page = 1, totalPage = 1;
    private CommentAdapter commentAdapter;
    private String wid, pcid, focused, toMid, commentCount;
    private int position = 0;
    private TagAdapter<String> adapter;
    private String did;
    private Recycle_one_itemAdapter recycletwoItemAdapter;
    private String type = "0", sort = "1", dcid = "",dianzanstate;
    private PopupWindow popupWindow,popupWindow1;// 声明PopupWindow
    private LinearLayout ll_ll;

    @Override
    public String getTitleName() {
        return "动态评论";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_dynamicdetail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        did = getArguments().getString("did");

        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rycomment.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(getContext(), listBeans);
        rycomment.setAdapter(commentAdapter);
        commentAdapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {//回复
                dcid = listBeans.get(firstPosition).dcid;
                etComment.setHint("请输入文字评论" + listBeans.get(firstPosition).usernickname);
            }

            @Override
            public void OnDianzanClickListener(int firstPosition) {//点赞
                dynamiczan(listBeans.get(firstPosition).dcid,firstPosition);
            }

            @Override
            public void OnJubaoClickListener(int position) {
                state_gengduo(listBeans.get(position).userid,position);
                ll_ll.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.activity_translate_in));
                popupWindow1.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
            }

        });


        StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager1);
        recycletwoItemAdapter = new Recycle_one_itemAdapter(getContext(), images);
        recyclerView.setAdapter(recycletwoItemAdapter);
        recycletwoItemAdapter.setOnItemClickListener(new Recycle_one_itemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

                ImagePreview
                        .getInstance()
                        .setContext(getContext())
                        .setIndex(firstPosition)
                        .setImageList(images)
                        .setShowDownButton(true)// 是否显示下载按钮
                        .start();// 开始跳转
            }
        });

        smart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page >= totalPage) {
                    refreshLayout.setNoMoreData(true);
                    return;
                }
                page++;
                getdynamiccommlist();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getdynamiccommlist();
                refreshLayout.setNoMoreData(false);
            }
        });


        List<String> list = new ArrayList<>();
        list.add("时间正序");
        list.add("时间倒序");
        list.add("点赞最多");
        ArrayAdapter mAdapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_item, list);
        spinner.setAdapter(mAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (list.get(position)) {
                    case "时间正序":
                        sort = "1";
                        getdynamiccommlist();
                        break;
                    case "时间倒序":
                        sort = "0";
                        getdynamiccommlist();
                        break;
                    case "点赞最多":
                        sort = "2";
                        getdynamiccommlist();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getdynamiccommlist();

        getdynamicdetail();

        tvSent.setOnClickListener(this);
        llOne.setOnClickListener(this);
        tvQuanbu.setOnClickListener(this);
        tvZhikan.setOnClickListener(this);
        llDianzan.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSent://发表评论
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))) {
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                if (StringUtil.isEmpty(etComment.getText().toString())) {
                    ToastUtil.show("请输入评论内容");
                    return;
                }
                adddynamiccomm(etComment.getText().toString());
                break;
            case R.id.llOne:
                dcid = "";
                etComment.setHint("请输入文字 评论动态");
                break;
            case R.id.tvQuanbu://全部
                type = "0";
                getdynamiccommlist();
                tvQuanbu.setTextColor(getResources().getColor(R.color.colorBlack));
                tvZhikan.setTextColor(getResources().getColor(R.color.txt_66));
                break;
            case R.id.tvZhikan://只看作者
                type = "1";
                getdynamiccommlist();
                tvQuanbu.setTextColor(getResources().getColor(R.color.txt_66));
                tvZhikan.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case R.id.llDianzan://点赞
                dynamiczan("",0);
                break;

        }
    }


    /**
     * 动态详情
     */
    private void getdynamicdetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("did", did);
        mOkHttpHelper.post_json(getContext(), Url.getdynamicdetail, params, new BaseCallback<ResultBean>() {
            @Override
            public void onBeforeRequest(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {

                Glide.with(getActivity()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.imageerror)
                        .placeholder(R.mipmap.imageerror))
                        .load(resultBean.dataobject.usericon)
                        .into(riIcon);
                tvTitle.setText(resultBean.dataobject.username);
                tvTime.setText(resultBean.dataobject.adtime);
                tvDianzan.setText(resultBean.dataobject.zannum);
                imsubCommentList.setText(resultBean.dataobject.commentnum);
                tvConten.setText(resultBean.dataobject.content);

                images.clear();
                images.addAll(resultBean.dataobject.images);
                recycletwoItemAdapter.notifyDataSetChanged();

                if (resultBean.dataobject.iszan.equals("0")) {
                    imDianzan.setImageResource(R.mipmap.weidianzan);
                } else {
                    imDianzan.setImageResource(R.mipmap.yidianzan);
                }

                dianzanstate = resultBean.dataobject.iszan;

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 发布评论
     */
    private void adddynamiccomm(String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("did", did);
        params.put("dcid", dcid);
        params.put("taid", "");
        params.put("content", content);
        mOkHttpHelper.post_json(getContext(), Url.adddynamiccomm, params, new BaseCallback<ResultBean>() {
            @Override
            public void onBeforeRequest(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                etComment.setText("");
                getdynamiccommlist();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 动态评论列表
     */
    private void getdynamiccommlist() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("did", did);
        params.put("type", type);
        params.put("sort", sort);
        params.put("nowPage", page);
        params.put("pageCount", "10");
        mOkHttpHelper.post_json(getContext(), Url.getdynamiccommlist, params, new BaseCallback<ResultBean>() {
            @Override
            public void onBeforeRequest(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                if (!StringUtil.isEmpty(resultBean.totalPage))
                    totalPage = Integer.parseInt(resultBean.totalPage);
                smart.finishLoadMore();
                smart.finishRefresh();
                if (page == 1) {
                    listBeans.clear();
                    commentAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans.addAll(resultBean.dataList);

                commentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 点赞/取消点赞
     */
    private void dynamiczan(String dcid,int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("did", did);
        params.put("dcid", dcid);
        mOkHttpHelper.post_json(getContext(), Url.dynamiczan, params, new BaseCallback<ResultBean>() {
            @Override
            public void onBeforeRequest(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                if (StringUtil.isEmpty(dcid)){
                    if (dianzanstate.equals("0")){
                        dianzanstate = "1";
                        imDianzan.setImageResource(R.mipmap.yidianzan);
                        tvDianzan.setText((Integer.parseInt(tvDianzan.getText().toString())+1)+"");
                    }else {
                        dianzanstate = "0";
                        imDianzan.setImageResource(R.mipmap.weidianzan);
                        tvDianzan.setText((Integer.parseInt(tvDianzan.getText().toString())-1)+"");
                    }
                }else {
                    if (listBeans.get(position).iszan.equals("0")){
                        listBeans.get(position).iszan = "1";
                        listBeans.get(position).zannum = (Integer.parseInt(listBeans.get(position).zannum)+1)+"";

                    }else {
                        listBeans.get(position).iszan = "0";
                        listBeans.get(position).zannum = (Integer.parseInt(listBeans.get(position).zannum)-1)+"";
                    }
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 删除评论
     */
    private void deletedynamiccomm(String dcid) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("dcid", dcid);
        mOkHttpHelper.post_json(getContext(), Url.deletedynamiccomm, params, new BaseCallback<ResultBean>() {
            @Override
            public void onBeforeRequest(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                getdynamiccommlist();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }
    /*
     * 更多
     * */
    public void state_gengduo(String taid,int position) {
        popupWindow1 = new PopupWindow(getActivity());
        View view = getLayoutInflater().inflate(R.layout.popup_gengduo, null);
        popupWindow1.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow1.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow1.setBackgroundDrawable(new BitmapDrawable());
        popupWindow1.setClippingEnabled(false);
        popupWindow1.setFocusable(true);
        popupWindow1.setOutsideTouchable(true);
        popupWindow1.setContentView(view);
        ll_ll = view.findViewById(R.id.ll_ll);
        TextView tvJubao = view.findViewById(R.id.tvJubao);
        TextView tvShanchu = view.findViewById(R.id.tvShanchu);

        if (listBeans.get(position).usertype.equals("0")){
            tvJubao.setVisibility(View.VISIBLE);
            if (listBeans.get(position).userid.equals(SharePrefUtil.getString(getContext(), AppConsts.UID,null))){
                tvShanchu.setVisibility(View.VISIBLE);
            }else {
                tvShanchu.setVisibility(View.GONE);
            }
        }else {
            if (listBeans.get(position).userid.equals(SharePrefUtil.getString(getContext(), AppConsts.UID,null))){
                tvShanchu.setVisibility(View.VISIBLE);
            }else {
                tvShanchu.setVisibility(View.GONE);
            }
            tvJubao.setVisibility(View.VISIBLE);
        }

        tvShanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NormalDialog dialog = new NormalDialog(getContext(), "确定删除该评论？", "取消", "确定", true);
                dialog.show();
                dialog.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                    @Override
                    public void OnRightClick() {
                        deletedynamiccomm(listBeans.get(position).dcid);
                    }

                    @Override
                    public void OnLeftClick() {

                    }
                });
                popupWindow1.dismiss();
                ll_ll.clearAnimation();
            }
        });
        tvJubao.setOnClickListener(new View.OnClickListener() {//举报
            @Override
            public void onClick(View v) {
                state(listBeans.get(position).userid);
                ll_ll.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.activity_translate_in));
                popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);

                popupWindow1.dismiss();
                ll_ll.clearAnimation();
            }
        });

        ll_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow1.dismiss();
                ll_ll.clearAnimation();
            }
        });
    }

    /*
     * 举报内容
     * */
    public void state(String taid) {
        popupWindow = new PopupWindow(getActivity());
        View view = getLayoutInflater().inflate(R.layout.popup_jubao, null);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setClippingEnabled(false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        ll_ll = view.findViewById(R.id.ll_ll);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView tvEnsure = view.findViewById(R.id.tvEnsure);
        EditText etCode = view.findViewById(R.id.etCode);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                ll_ll.clearAnimation();
            }
        });
        tvEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(etCode.getText().toString())) {
                    ToastUtil.show("请填写举报内容");
                    return;
                }
                addreports(taid,etCode.getText().toString());
                popupWindow.dismiss();
                ll_ll.clearAnimation();
            }
        });
    }

    /**
     * 举报用户
     */
    private void addreports(String taid,String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("taid", taid);
        params.put("content", content);
        mOkHttpHelper.post_json(getContext(), Url.addreports, params, new BaseCallback<ResultBean>() {
            @Override
            public void onBeforeRequest(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                ToastUtil.show(resultBean.resultNote);

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

