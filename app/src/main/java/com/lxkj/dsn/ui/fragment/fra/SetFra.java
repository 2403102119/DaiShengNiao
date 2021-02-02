package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lxkj.dsn.R;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.ui.fragment.TitleFragment;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:设置
 */
public class SetFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.llAgreement)
    LinearLayout llAgreement;
    @BindView(R.id.llAmend)
    LinearLayout llAmend;
    @BindView(R.id.llIdea)
    LinearLayout llIdea;
    @BindView(R.id.llUpdate)
    LinearLayout llUpdate;

    @Override
    public String getTitleName() {
        return "设置";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_set, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        llAgreement.setOnClickListener(this);
        llAmend.setOnClickListener(this);
        llIdea.setOnClickListener(this);
        llUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAgreement://协议中心
                ActivitySwitcher.startFragment(getActivity(), AgreementFra.class);
                break;
            case R.id.llAmend://修改密码
                ActivitySwitcher.startFragment(getActivity(), AmendFra.class);
                break;
            case R.id.llIdea://意见反馈
                ActivitySwitcher.startFragment(getActivity(), IdeaFra.class);
                break;
            case R.id.llUpdate://版本更新
                ActivitySwitcher.startFragment(getActivity(), UpdateFra.class);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
