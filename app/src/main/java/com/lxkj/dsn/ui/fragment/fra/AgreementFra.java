package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.AgreementAdapter;
import com.lxkj.dsn.adapter.MessageListAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.ui.fragment.system.WebFra;
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
 * Time:2021/1/29
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:协议中心
 */
public class AgreementFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.llNodata)
    LinearLayout llNodata;
    @BindView(R.id.ryList)
    RecyclerView ryList;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    private ArrayList<DataListBean> listBeans = new ArrayList<>();
    private int page = 1, totalPage = 1;
    private AgreementAdapter agreementAdapter;
    @Override
    public String getTitleName() {
        return "协议中心";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_recycleview, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        DataListBean dataListBean = new DataListBean();
        dataListBean.url = "http://8.140.109.101/daishengniao/display/agreement?id=1";
        dataListBean.title = "《注册协议》";
        listBeans.add(dataListBean);

        DataListBean dataListBean1 = new DataListBean();
        dataListBean1.url = "http://8.140.109.101/daishengniao/display/agreement?id=2";
        dataListBean1.title = "《隐私政策》";
        listBeans.add(dataListBean1);

        DataListBean dataListBean2 = new DataListBean();
        dataListBean2.url = "http://8.140.109.101/daishengniao/display/agreement?id=3";
        dataListBean2.title = "《关于我们》";
        listBeans.add(dataListBean2);

        DataListBean dataListBean3 = new DataListBean();
        dataListBean3.url = "http://8.140.109.101/daishengniao/display/agreement?id=4";
        dataListBean3.title = "《会员中心》";
        listBeans.add(dataListBean3);

        DataListBean dataListBean4 = new DataListBean();
        dataListBean4.url = "http://8.140.109.101/daishengniao/display/agreement?id=5";
        dataListBean4.title = "《积分规则说明》";
        listBeans.add(dataListBean4);

        DataListBean dataListBean5 = new DataListBean();
        dataListBean5.url = "http://8.140.109.101/daishengniao/display/agreement?id=6";
        dataListBean5.title = "《积分使用规则》";
        listBeans.add(dataListBean5);

        DataListBean dataListBean6 = new DataListBean();
        dataListBean6.url = "http://8.140.109.101/daishengniao/display/agreement?id=7";
        dataListBean6.title = "《邀请规则》";
        listBeans.add(dataListBean6);

        ryList.setLayoutManager(new LinearLayoutManager(getContext()));
        agreementAdapter = new AgreementAdapter(getContext(), listBeans);
        ryList.setAdapter(agreementAdapter);
        agreementAdapter.setOnItemClickListener(new AgreementAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                Bundle bundle = new Bundle();
                bundle.putString("title", listBeans.get(firstPosition).title);
                bundle.putString("url",listBeans.get(firstPosition).url);
                ActivitySwitcher.startFragment(getContext(), WebFra.class, bundle);
            }
        });
        smart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smart.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smart.finishRefresh();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
