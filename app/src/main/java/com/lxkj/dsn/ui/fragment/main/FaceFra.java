package com.lxkj.dsn.ui.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.FaceAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.CachableFrg;
import com.lxkj.dsn.ui.fragment.fra.FaceDeatilFra;
import com.lxkj.dsn.ui.fragment.fra.MessageFra;
import com.lxkj.dsn.ui.fragment.fra.PushHeartFra;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.lxkj.dsn.utils.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2020/10/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:面对面
 */
public class FaceFra extends CachableFrg implements View.OnClickListener {


    @BindView(R.id.imPush)
    ImageView imPush;
    @BindView(R.id.et_seek)
    EditText etSeek;
    @BindView(R.id.tv_seek)
    TextView tvSeek;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.imMessage)
    ImageView imMessage;
    @BindView(R.id.tvOrderCount)
    TextView tvOrderCount;
    @BindView(R.id.page_top)
    LinearLayout pageTop;
    @BindView(R.id.TagFlow)
    TagFlowLayout TagFlow;
    @BindView(R.id.ryFace)
    RecyclerView ryFace;
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.llNodata)
    LinearLayout llNodata;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    private ArrayList<DataListBean> listBeans = new ArrayList<>();
    private int page = 1, totalPage = 1;

    private FaceAdapter faceAdapter;
    private TagAdapter<String> adapter;
    private List<String> hot_list = new ArrayList<>();
    private List<String> fid_list = new ArrayList<>();
    private String fid;

    @Override
    protected int rootLayout() {
        return R.layout.fra_issue;
    }

    @Override
    protected void initView() {


        adapter = new TagAdapter<String>(hot_list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_select_face, parent, false);
                view.setText(s);
                return view;
            }
        };
        adapter.setSelectedList(0);//默认选中第一个
        TagFlow.setAdapter(adapter);

        TagFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                fid = fid_list.get(position);
                getdynamiclist();
                return true;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ryFace.setLayoutManager(layoutManager);
        faceAdapter = new FaceAdapter(getContext(), listBeans);
        ryFace.setAdapter(faceAdapter);
        faceAdapter.setOnItemClickListener(new FaceAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                Bundle bundle = new Bundle();
                bundle.putString("did", listBeans.get(firstPosition).did);
                ActivitySwitcher.startFragment(getActivity(), FaceDeatilFra.class, bundle);
            }

            @Override
            public void Onchakandatu(int firstPosition, int position) {
                showImage(new ImageView(getContext()), firstPosition, listBeans.get(position).images);
            }

            @Override
            public void OnDianzan(int firstPosition) {//点赞
                dynamiczan(listBeans.get(firstPosition).did, firstPosition);
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
                getdynamiclist();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getdynamiclist();
                refreshLayout.setNoMoreData(false);
            }
        });

        etSeek.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    return true;
                }

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    getdynamiclist();

                    return true;
                }
                return false;
            }
        });


        getclassify1list();

        imPush.setOnClickListener(this);
        imMessage.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imPush://发布心得
                ActivitySwitcher.startFragment(getActivity(), PushHeartFra.class);
                break;
            case R.id.imMessage://系统消息
                ActivitySwitcher.startFragment(getActivity(), MessageFra.class);
                break;
        }
    }

    //一级分类
    private void getclassify1list() {
        Map<String, Object> params = new HashMap<>();
        mOkHttpHelper.post_json(getContext(), Url.getclassify1list, params, new BaseCallback<ResultBean>() {
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
                hot_list.clear();
                fid_list.clear();
                hot_list.add("全部");
                fid_list.add("");
                for (int i = 0; i < resultBean.dataList.size(); i++) {
                    hot_list.add(resultBean.dataList.get(i).name);
                    fid_list.add(resultBean.dataList.get(i).fid);
                }
                adapter.notifyDataChanged();

                fid = "";
                getdynamiclist();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //动态列表
    private void getdynamiclist() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", SharePrefUtil.getString(getContext(), AppConsts.UID, null));
        params.put("nowPage", page);
        params.put("fid", fid);
        params.put("content", etSeek.getText().toString());
        params.put("pageCount", "10");
        mOkHttpHelper.post_json(getContext(), Url.getdynamiclist, params, new BaseCallback<ResultBean>() {
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
                    faceAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans.addAll(resultBean.dataList);

                if (resultBean.dataList.size() == 0) {
                    llNodata.setVisibility(View.VISIBLE);
                } else {
                    llNodata.setVisibility(View.GONE);
                }

                faceAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //获取未读消息数量
    private void getmessnum() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        mOkHttpHelper.post_json(getContext(), Url.getmessnum, params, new BaseCallback<ResultBean>() {
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
                if (StringUtil.isEmpty(resultBean.datastr) || resultBean.datastr.equals("0")) {
                    tvOrderCount.setVisibility(View.GONE);
                } else {
                    tvOrderCount.setVisibility(View.VISIBLE);
                    tvOrderCount.setText(resultBean.datastr);
                }


            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 点赞/取消点赞
     */
    private void dynamiczan(String did, int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("did", did);
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
                if (listBeans.get(position).iszan.equals("0")) {
                    listBeans.get(position).iszan = "1";
                    listBeans.get(position).zannum = (Integer.parseInt(listBeans.get(position).zannum) + 1) + "";

                } else {
                    listBeans.get(position).iszan = "0";
                    listBeans.get(position).zannum = (Integer.parseInt(listBeans.get(position).zannum) - 1) + "";
                }
                faceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    private void showImage(final ImageView iv, int position, List<String> list) {
        List<Object> urls = new ArrayList<>();
        urls.clear();
        urls.addAll(list);

        new XPopup.Builder(getContext()).asImageViewer(iv, position, urls, new OnSrcViewUpdateListener() {
            @Override
            public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                popupView.updateSrcView(iv);
            }
        }, new ImageLoader())
                .show();
    }

    class ImageLoader implements XPopupImageLoader {
        @Override
        public void loadImage(int position, @NonNull Object url, @NonNull ImageView imageView) {
            //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
            Glide.with(imageView).load(url).apply(new RequestOptions().placeholder(R.mipmap.logo).override(Target.SIZE_ORIGINAL)).into(imageView);
        }

        @Override
        public File getImageFile(@NonNull Context context, @NonNull Object uri) {
            try {
                return Glide.with(context).downloadOnly().load(uri).submit().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!StringUtil.isEmpty(fid)) {
            getdynamiclist();
        }
        getmessnum();
    }
}

