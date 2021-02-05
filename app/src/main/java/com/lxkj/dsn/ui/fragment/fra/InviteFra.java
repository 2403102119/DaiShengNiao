package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.actlink.NaviRightListener;
import com.lxkj.dsn.adapter.InviteAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.ui.fragment.system.WebFra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/1/25
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:我的邀请
 */
public class InviteFra extends TitleFragment implements NaviRightListener, View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvShouyi)
    TextView tvShouyi;
    @BindView(R.id.tvLeijishouji)
    TextView tvLeijishouji;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvYaoqingma)
    TextView tvYaoqingma;
    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private InviteAdapter inviteAdapter;
    private String invitationcode;

    @Override
    public String getTitleName() {
        return "我的邀请";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_invite, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        invitationcode = getArguments().getString("invitationcode");

        tvYaoqingma.setText("邀请码："+invitationcode);

        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        inviteAdapter = new InviteAdapter(getContext(), listBeans);
        recyclerView.setAdapter(inviteAdapter);
        inviteAdapter.setOnItemClickListener(new InviteAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

            }
        });

        tvAll.setOnClickListener(this);

        myfirstinvitationlist();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAll://查看全部
                ActivitySwitcher.startFragment(getActivity(), AllFriendFra.class);
                break;
        }
    }

    //我的邀请
    private void myfirstinvitationlist() {
        Map<String, Object> params = new HashMap<>();
        params.put("nowPage", page);
        params.put("uid", userId);
        params.put("pageCount", "10");
        mOkHttpHelper.post_json(getContext(), Url.myfirstinvitationlist, params, new BaseCallback<ResultBean>() {
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
                tvShouyi.setText("今日收益：" + resultBean.daymoney);
                tvLeijishouji.setText("累计收益：" + resultBean.allmoney);
                tvNumber.setText("我的好友（" + resultBean.allnum + "）");
                listBeans.clear();
                for (int i = 0; i < 3; i++) {
                    listBeans.add(resultBean.dataList.get(i));
                }
                inviteAdapter.notifyDataSetChanged();

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
        return "邀请规则";
    }

    @Override
    public void onRightClicked(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("title", "邀请规则");
        bundle.putString("url","http://8.140.109.101/daishengniao/display/agreement?id=7");
        ActivitySwitcher.startFragment(getContext(), WebFra.class, bundle);
    }
}
