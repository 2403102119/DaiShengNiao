package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.flyco.tablayout.SlidingTabLayout;
import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.MFragmentStatePagerAdapter;
import com.lxkj.dsn.ui.fragment.TitleFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/22
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:资金账户
 */
public class FoundFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private String CurrentItem = "1";
    @Override
    public String getTitleName() {
        return "我的余额";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_found, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        String[] titles = new String[3];
        titles[0] = "全部";
        titles[1] = "收入";
        titles[2] = "支出";

        FoundFollowFra followFra = new FoundFollowFra();
        Bundle dfk = new Bundle();
        dfk.putString("attention", "1");
        dfk.putString("type", "0");
        followFra.setArguments(dfk);

        FoundFollowFra followFra1 = new FoundFollowFra();
        Bundle dfk1 = new Bundle();
        dfk1.putString("attention", "0");
        dfk1.putString("type", "2");

        followFra1.setArguments(dfk1);

        FoundFollowFra followFra2 = new FoundFollowFra();

        Bundle dfk2 = new Bundle();
        dfk2.putString("attention", "0");
        dfk2.putString("type", "3");
        followFra2.setArguments(dfk2);


        fragments.add(followFra);
        fragments.add(followFra1);
        fragments.add(followFra2);

        viewPager.setAdapter(new MFragmentStatePagerAdapter(act.getSupportFragmentManager(), fragments, titles));
        tabLayout.setViewPager(viewPager);
        viewPager.setCurrentItem(Integer.parseInt(CurrentItem));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
