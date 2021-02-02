package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.lxkj.dsn.R;
import com.lxkj.dsn.ui.fragment.TitleFragment;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/2/1
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:订单详情
 */
public class OrderDetailsFra extends TitleFragment {
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_orderdetail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }
    public void initView(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
