package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.lxkj.dsn.R;
import com.lxkj.dsn.actlink.NaviRightListener;
import com.lxkj.dsn.adapter.ColletAdapter;
import com.lxkj.dsn.adapter.InviteAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.ui.fragment.TitleFragment;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/25
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:我的邀请
 */
public class InviteFra extends TitleFragment implements NaviRightListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private InviteAdapter inviteAdapter;
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

    }
}
