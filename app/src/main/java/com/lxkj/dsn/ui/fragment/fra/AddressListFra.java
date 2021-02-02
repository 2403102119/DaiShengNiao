package com.lxkj.dsn.ui.fragment.fra;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.AddressAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kxn on 2020/1/18 0018.
 */
public class AddressListFra extends TitleFragment {
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    @BindView(R.id.xRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    AddressAdapter adapter;
    List<DataListBean> listBeans;
    @BindView(R.id.tvAdd)
    TextView tvAdd;
    private int page = 1, totalPage = 1;


    @Override
    public String getTitleName() {
        return "我的地址";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fra_address_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        tvNoData.setText("还没有添加地址");
        listBeans = new ArrayList<>();
        adapter = new AddressAdapter(getContext(), listBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page >= totalPage) {
                    refreshLayout.setNoMoreData(true);
                    return;
                }
                page++;
//                getList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
//                getList();
                refreshLayout.setNoMoreData(false);
            }
        });
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
//                if (StringUtil.isEmpty(getArguments().getString("type"))){
//                    Intent intent = new Intent();
//                    intent.putExtra("data", listBeans.get(position));
//                    act.setResult(111, intent);
//                    act.finishSelf();
//                }
            }

            @Override
            public void onCheckListener(int position) {
//                setDefaultAddr(listBeans.get(position).addressId);
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySwitcher.startFragment(act, EditeAddressFra.class);
            }
        });
    }

//    /**
//     * 获取地址列表
//     */
//    private void getList() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("uid", userId);
//        mOkHttpHelper.post_json(getContext(), Url.getAddressList, params, new BaseCallback<ResultBean>() {
//            @Override
//            public void onBeforeRequest(Request request) {
//            }
//
//            @Override
//            public void onFailure(Request request, Exception e) {
//                refreshLayout.finishLoadMore();
//                refreshLayout.finishRefresh();
//            }
//
//            @Override
//            public void onResponse(Response response) {
//            }
//
//            @Override
//            public void onSuccess(Response response, ResultBean resultBean) {
//                if (!StringUtil.isEmpty(resultBean.totalPage))
//                    totalPage = Integer.parseInt(resultBean.totalPage);
//                refreshLayout.finishLoadMore();
//                refreshLayout.finishRefresh();
//                if (page == 1) {
//                    listBeans.clear();
//                    adapter.notifyDataSetChanged();
//                }
//                if (null != resultBean.dataList)
//                    listBeans.addAll(resultBean.dataList);
//
//                if (listBeans.size() == 0){
//                    recyclerView.setVisibility(View.GONE);
//                    llNoData.setVisibility(View.VISIBLE);
//                }else {
//                    recyclerView.setVisibility(View.VISIBLE);
//                    llNoData.setVisibility(View.GONE);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//                refreshLayout.finishLoadMore();
//                refreshLayout.finishRefresh();
//            }
//        });
//    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLayout.autoRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
