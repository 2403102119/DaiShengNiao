package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.actlink.NaviRightListener;
import com.lxkj.dsn.adapter.IntegralDetailAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
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
 * Time:2021/1/19
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:积分明细
 */
public class IntegralDetailFra extends TitleFragment implements NaviRightListener, View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.tvQuanbu)
    TextView tvQuanbu;
    @BindView(R.id.tvShouru)
    TextView tvShouru;
    @BindView(R.id.tvZhichu)
    TextView tvZhichu;
    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private IntegralDetailAdapter integralDetailAdapter;
    private String type = "";

    @Override
    public String getTitleName() {
        return "积分明细";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_integraldetail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        listBeans = new ArrayList<DataListBean>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        integralDetailAdapter = new IntegralDetailAdapter(getContext(), listBeans);
        recyclerView.setAdapter(integralDetailAdapter);
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
                getmemberintegrallist1();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getmemberintegrallist1();
                refreshLayout.setNoMoreData(false);
            }
        });

        getmemberintegrallist1();

        tvQuanbu.setOnClickListener(this);
        tvShouru.setOnClickListener(this);
        tvZhichu.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvQuanbu://全部
                tvQuanbu.setBackgroundResource(R.drawable.login_20);
                tvShouru.setBackgroundResource(R.drawable.guigexuanzefalse);
                tvZhichu.setBackgroundResource(R.drawable.guigexuanzefalse);
                tvQuanbu.setTextColor(getResources().getColor(R.color.white));
                tvShouru.setTextColor(getResources().getColor(R.color.txt_66));
                tvZhichu.setTextColor(getResources().getColor(R.color.txt_66));
                type = "";
                getmemberintegrallist1();
                break;
            case R.id.tvShouru://收入
                tvQuanbu.setBackgroundResource(R.drawable.guigexuanzefalse);
                tvShouru.setBackgroundResource(R.drawable.login_20);
                tvZhichu.setBackgroundResource(R.drawable.guigexuanzefalse);
                tvQuanbu.setTextColor(getResources().getColor(R.color.txt_66));
                tvShouru.setTextColor(getResources().getColor(R.color.white));
                tvZhichu.setTextColor(getResources().getColor(R.color.txt_66));
                type = "0";
                getmemberintegrallist1();
                break;
            case R.id.tvZhichu://支出
                tvQuanbu.setBackgroundResource(R.drawable.guigexuanzefalse);
                tvShouru.setBackgroundResource(R.drawable.guigexuanzefalse);
                tvZhichu.setBackgroundResource(R.drawable.login_20);
                tvQuanbu.setTextColor(getResources().getColor(R.color.txt_66));
                tvShouru.setTextColor(getResources().getColor(R.color.txt_66));
                tvZhichu.setTextColor(getResources().getColor(R.color.white));
                type = "1";
                getmemberintegrallist1();
                break;

        }
    }

    //积分明细
    private void getmemberintegrallist1() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("type", type);
        params.put("nowPage", page);
        params.put("pageCount", "10");
        mOkHttpHelper.post_json(getContext(), Url.getmemberintegrallist1, params, new BaseCallback<ResultBean>() {
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
                    integralDetailAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans.addAll(resultBean.dataList);

                integralDetailAdapter.notifyDataSetChanged();

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

    @Override
    public String rightText() {
        return "规则说明";
    }

    @Override
    public void onRightClicked(View v) {

    }


}
