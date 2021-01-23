package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.IntegralAdapter;
import com.lxkj.dsn.adapter.MessageListAdapter;
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/21
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:系统消息
 */
public class MessageFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private MessageListAdapter messageListAdapter;
    @Override
    public String getTitleName() {
        return "消息中心";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_message, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        listBeans = new ArrayList<DataListBean>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messageListAdapter = new MessageListAdapter(getContext(), listBeans);
        recyclerView.setAdapter(messageListAdapter);
        messageListAdapter.setOnItemClickListener(new MessageListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

            }

            @Override
            public void OnDelateClickListener(int firstPosition) {

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
