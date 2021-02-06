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

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.ProductAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.CachableFrg;
import com.lxkj.dsn.ui.fragment.fra.DetailFra;
import com.lxkj.dsn.ui.fragment.fra.MessageFra;
import com.lxkj.dsn.utils.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2020/8/24
 * <p>
 * Author:李迪迦
 * <p>
 * Description:影音书
 */
public class AudioFra extends CachableFrg implements View.OnClickListener {


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
    @BindView(R.id.llArea)
    LinearLayout llArea;
    @BindView(R.id.imXiaoliang)
    ImageView imXiaoliang;
    @BindView(R.id.llClass)
    LinearLayout llClass;
    @BindView(R.id.imJuli)
    ImageView imJuli;
    @BindView(R.id.llSpecification)
    LinearLayout llSpecification;
    @BindView(R.id.llScreen)
    LinearLayout llScreen;
    @BindView(R.id.ryProduct)
    RecyclerView ryProduct;
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.llNodata)
    LinearLayout llNodata;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    private ArrayList<DataListBean> listBeans = new ArrayList<>();

    private ProductAdapter productAdapter;
    private TagAdapter<String> adapter;
    private List<String> hot_list = new ArrayList<>();
    private List<String> sidlist = new ArrayList<>();
    private int page = 1, totalPage = 1;
    private String sid, sort = "0", xiaoliang = "1", jiage = "4";

    @Override
    protected int rootLayout() {
        return R.layout.fra_nearby;
    }

    @Override
    protected void initView() {


        adapter = new TagAdapter<String>(hot_list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_choose, parent, false);
                view.setText(s);
                return view;
            }
        };
        adapter.setSelectedList(0);//默认选中第一个
        TagFlow.setAdapter(adapter);

        TagFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                sid = sidlist.get(position);
                getclassify2goodslist(sid);
                return true;
            }
        });


        ryProduct.setHasFixedSize(true);
        ryProduct.setNestedScrollingEnabled(false);
        ryProduct.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        productAdapter = new ProductAdapter(getContext(), listBeans);
        ryProduct.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                Bundle bundle = new Bundle();
                bundle.putString("gid", listBeans.get(firstPosition).gid);
                ActivitySwitcher.startFragment(getActivity(), DetailFra.class, bundle);
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
                getclassify2goodslist(sid);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getclassify2goodslist(sid);
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
                    getclassify2goodslist(sid);

                    return true;
                }
                return false;
            }
        });


        getclassify2list();

        llArea.setOnClickListener(this);
        llClass.setOnClickListener(this);
        llSpecification.setOnClickListener(this);
        imMessage.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llArea://综合排序
                sort = "0";
                xiaoliang = "1";
                imXiaoliang.setImageResource(R.mipmap.xiaoliangdi);
                jiage = "4";
                imXiaoliang.setImageResource(R.mipmap.xiaoliang);
                getclassify2goodslist(sid);
                break;
            case R.id.llClass://销量
                if (xiaoliang.equals("1")) {
                    xiaoliang = "2";
                    sort = "2";
                    imXiaoliang.setImageResource(R.mipmap.xiaoliang);
                } else {
                    xiaoliang = "1";
                    sort = "1";
                    imXiaoliang.setImageResource(R.mipmap.xiaoliangdi);
                }
                getclassify2goodslist(sid);
                break;
            case R.id.llSpecification://价格
                if (jiage.equals("4")) {
                    jiage = "3";
                    sort = "3";
                    imXiaoliang.setImageResource(R.mipmap.xiaoliangdi);
                } else {
                    jiage = "4";
                    sort = "4";
                    imXiaoliang.setImageResource(R.mipmap.xiaoliang);
                }
                getclassify2goodslist(sid);
                break;
            case R.id.imMessage://系统消息
                ActivitySwitcher.startFragment(getActivity(), MessageFra.class);
                break;
        }
    }


    //二级分类
    private void getclassify2list() {
        Map<String, Object> params = new HashMap<>();
        params.put("fid", "5");
        mOkHttpHelper.post_json(getContext(), Url.getclassify2list, params, new BaseCallback<ResultBean>() {
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
                sidlist.clear();
                for (int i = 0; i < resultBean.dataList.size(); i++) {
                    hot_list.add(resultBean.dataList.get(i).name);
                    sidlist.add(resultBean.dataList.get(i).sid);
                }
                adapter.notifyDataChanged();
                sid = resultBean.dataList.get(0).sid;
                getclassify2goodslist(resultBean.dataList.get(0).sid);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //二级分类下的商品
    private void getclassify2goodslist(String sid) {
        Map<String, Object> params = new HashMap<>();
        params.put("sid", sid);
        params.put("content", etSeek.getText().toString());
        params.put("nowPage", page);
        params.put("pageCount", "10");
        params.put("sort", sort);
        mOkHttpHelper.post_json(getContext(), Url.getclassify2goodslist, params, new BaseCallback<ResultBean>() {
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
                    productAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans.addAll(resultBean.dataList);

                if (resultBean.dataList.size() == 0) {
                    llNodata.setVisibility(View.VISIBLE);
                } else {
                    llNodata.setVisibility(View.GONE);
                }

                productAdapter.notifyDataSetChanged();

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

    @Override
    public void onResume() {
        super.onResume();
        getmessnum();
    }
}
