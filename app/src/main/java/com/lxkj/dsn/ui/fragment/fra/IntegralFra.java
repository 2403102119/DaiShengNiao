package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.actlink.NaviRightListener;
import com.lxkj.dsn.adapter.IntegralAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/19
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:积分商城
 */
public class IntegralFra extends TitleFragment implements View.OnClickListener, NaviRightListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.tvRule)
    TextView tvRule;
    @BindView(R.id.tvDetail)
    TextView tvDetail;
    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private IntegralAdapter integralAdapter;

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
        listBeans = new ArrayList<DataListBean>();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        integralAdapter = new IntegralAdapter(getContext(), listBeans);
        recyclerView.setAdapter(integralAdapter);
        integralAdapter.setOnItemClickListener(new IntegralAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

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

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;

                refreshLayout.setNoMoreData(false);
            }
        });

        tvRule.setOnClickListener(this);
        tvDetail.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRule://积分规则
                ActivitySwitcher.startFragment(getActivity(), RuleFra.class);
                break;
            case R.id.tvDetail://积分明细
                ActivitySwitcher.startFragment(getActivity(), IntegralDetailFra.class);
                break;
        }
    }

    @Override
    public String rightText() {
        return "兑换记录";
    }

    @Override
    public void onRightClicked(View v) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
