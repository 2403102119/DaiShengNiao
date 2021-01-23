package com.lxkj.dsn.ui.fragment.fra;

import android.widget.LinearLayout;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.DynamicAdapter;
import com.lxkj.dsn.adapter.RepalyAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.ui.fragment.CachableFrg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Time:2021/1/22
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:我的回复
 */
public class RepalyFra extends CachableFrg {
    @BindView(R.id.llNodata)
    LinearLayout llNodata;
    @BindView(R.id.ryList)
    RecyclerView ryList;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private RepalyAdapter repalyAdapter;
    @Override
    protected int rootLayout() {
        return R.layout.fra_recycleview;
    }

    @Override
    protected void initView() {
        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ryList.setLayoutManager(layoutManager);
        repalyAdapter = new RepalyAdapter(getContext(), listBeans);
        ryList.setAdapter(repalyAdapter);
        repalyAdapter.setOnItemClickListener(new RepalyAdapter.OnItemClickListener() {
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
}
