package com.lxkj.dsn.ui.fragment.fra;

import android.widget.LinearLayout;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.DynamicAdapter;
import com.lxkj.dsn.adapter.RepalyAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.CachableFrg;
import com.lxkj.dsn.utils.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import okhttp3.Request;
import okhttp3.Response;

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
                getmyadddynamiccommlist();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                refreshLayout.setNoMoreData(false);
                getmyadddynamiccommlist();
            }
        });

    }

    //我的评论
    private void getmyadddynamiccommlist() {
        Map<String, Object> params = new HashMap<>();
        params.put("nowPage", page);
        params.put("uid", userId);
        params.put("pageCount", "10");
        mOkHttpHelper.post_json(getContext(), Url.getmyadddynamiccommlist, params, new BaseCallback<ResultBean>() {
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
                    repalyAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans.addAll(resultBean.dataList);

                repalyAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
}
