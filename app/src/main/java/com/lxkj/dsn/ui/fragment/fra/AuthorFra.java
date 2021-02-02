package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.AuthorAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.StringUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * Time:2021/1/29
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:作者详情
 */
public class AuthorFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.llAuthor)
    LinearLayout llAuthor;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvSchool)
    TextView tvSchool;
    @BindView(R.id.tvAbstract)
    TextView tvAbstract;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;

    private AuthorAdapter authorAdapter;
    private List<DataListBean> listBeans = new ArrayList<>();
    private String aid;
    private int page = 1, totalPage = 1;
    @Override
    public String getTitleName() {
        return "作者详情";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_autor, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        aid = getArguments().getString("aid");

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        authorAdapter = new AuthorAdapter(getContext(), listBeans);
        recyclerView.setAdapter(authorAdapter);
        authorAdapter.setOnItemClickListener(new AuthorAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {//商品详情
                Bundle bundle = new Bundle();
                bundle.putString("gid",listBeans.get(firstPosition).gid);
                ActivitySwitcher.startFragment(getActivity(), DetailFra.class,bundle);
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
                getauthorgoodslist();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getauthorgoodslist();
                refreshLayout.setNoMoreData(false);
            }
        });

        getauthordetail();
        getauthorgoodslist();

    }


    //作者详情
    private void getauthordetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("aid", aid);
        mOkHttpHelper.post_json(getContext(), Url.getauthordetail, params, new BaseCallback<ResultBean>() {
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


                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.logo)
                        .placeholder(R.mipmap.logo))
                        .load(resultBean.dataobject.aimage)
                        .into(riIcon);
                tvName.setText(resultBean.dataobject.aname);
                tvTime.setText("出生年月：" + resultBean.dataobject.abirthday);
                tvSchool.setText("毕业院校：" + resultBean.dataobject.aschool);
                tvAbstract.setText(resultBean.dataobject.adescs);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //作者代表作品
    private void getauthorgoodslist() {
        Map<String, Object> params = new HashMap<>();
        params.put("aid",aid);
        params.put("nowPage",page);
        params.put("pageCount","10");
        mOkHttpHelper.post_json(getContext(), Url.getauthorgoodslist, params, new BaseCallback<ResultBean>() {
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
                    authorAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans.addAll(resultBean.dataList);

                authorAdapter.notifyDataSetChanged();

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
}
