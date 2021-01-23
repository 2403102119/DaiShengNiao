package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.IntegralDetailAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/22
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:资金明细
 */
public class FoundFollowFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.llNodata)
    LinearLayout llNodata;
    @BindView(R.id.ryList)
    RecyclerView ryList;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private IntegralDetailAdapter integralDetailAdapter;
    @Override
    public String getTitleName() {
        return "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fra_recycleview, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        listBeans = new ArrayList<DataListBean>();
        ryList.setLayoutManager(new LinearLayoutManager(getContext()));
        integralDetailAdapter = new IntegralDetailAdapter(getContext(), listBeans);
        ryList.setAdapter(integralDetailAdapter);
        integralDetailAdapter.setOnItemClickListener(new IntegralDetailAdapter.OnItemClickListener() {
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
