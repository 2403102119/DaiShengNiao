package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.IntegralAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.ui.fragment.system.WebFra;
import com.lxkj.dsn.utils.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/1/19
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:积分商城
 */
public class IntegralFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.tvRule)
    TextView tvRule;
    @BindView(R.id.tvDetail)
    TextView tvDetail;
    @BindView(R.id.tvJifen)
    TextView tvJifen;
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.llNodata)
    LinearLayout llNodata;
    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private IntegralAdapter integralAdapter;
    private String balance;

    @Override
    public String getTitleName() {
        return "积分商城";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_integral, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        balance = getArguments().getString("balance");
        tvJifen.setText(balance);

        listBeans = new ArrayList<DataListBean>();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        integralAdapter = new IntegralAdapter(getContext(), listBeans);
        recyclerView.setAdapter(integralAdapter);
        integralAdapter.setOnItemClickListener(new IntegralAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                Bundle bundle = new Bundle();
                bundle.putString("gid", listBeans.get(firstPosition).gid);
                ActivitySwitcher.startFragment(getActivity(), IntegralDeatilFra.class, bundle);
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
                getintegralgoodslist();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getintegralgoodslist();
                refreshLayout.setNoMoreData(false);
            }
        });
        getintegralgoodslist();

        tvRule.setOnClickListener(this);
        tvDetail.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRule://积分规则
//                ActivitySwitcher.startFragment(getActivity(), RuleFra.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "积分规则");
                bundle.putString("url", "http://8.140.109.101/daishengniao/display/agreement?id=6");
                ActivitySwitcher.startFragment(getContext(), WebFra.class, bundle);
                break;
            case R.id.tvDetail://积分明细
                ActivitySwitcher.startFragment(getActivity(), IntegralDetailFra.class);
                break;
        }
    }


    //积分商品列表
    private void getintegralgoodslist() {
        Map<String, Object> params = new HashMap<>();
        params.put("nowPage", page);
        params.put("pageCount", "10");
        mOkHttpHelper.post_json(getContext(), Url.getintegralgoodslist, params, new BaseCallback<ResultBean>() {
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
                    integralAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans.addAll(resultBean.dataList);

                if (resultBean.dataList.size()==0){
                    llNodata.setVisibility(View.VISIBLE);
                }else {
                    llNodata.setVisibility(View.GONE);
                }

                integralAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

//    @Override
//    public String rightText() {
//        return "兑换记录";
//    }
//
//    @Override
//    public void onRightClicked(View v) {
//        ActivitySwitcher.startFragment(getActivity(), IntegralRecordFra.class);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
