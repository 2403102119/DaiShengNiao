package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.flyco.tablayout.SlidingTabLayout;
import com.lxkj.dsn.R;
import com.lxkj.dsn.actlink.NaviRightListener;
import com.lxkj.dsn.adapter.MFragmentStatePagerAdapter;
import com.lxkj.dsn.ui.fragment.TitleFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2020/11/9
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:我的订单
 */
public class OrderFra extends TitleFragment implements NaviRightListener {
    Unbinder unbinder;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private String state;
    @Override
    public String getTitleName() {
        return "全部订单";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_order, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        state = getArguments().getString("state");

        String[] titles = new String[6];
        titles[0] = "全部";
        titles[1] = "待付款";
        titles[2] = "待发货";
        titles[3] = "待收货";
        titles[4] = "待评价";
        titles[5] = "退款/售后";
        //空-全部,1.待支付，3.待发货，6.进行中，7.待从社区自提，8.已完成，0.退款售后订单
        OrderListFra allOrderListFra = new OrderListFra();
        Bundle all = new Bundle();
        all.putString("state", "");
        allOrderListFra.setArguments(all);

        OrderListFra dfkOrderListFra = new OrderListFra();
        Bundle dfk = new Bundle();
        dfk.putString("state", "0");
        dfkOrderListFra.setArguments(dfk);

        OrderListFra dfhOrderListFra = new OrderListFra();
        Bundle dfh = new Bundle();
        dfh.putString("state", "1");
        dfhOrderListFra.setArguments(dfh);

        OrderListFra dshOrderListFra = new OrderListFra();
        Bundle dsh = new Bundle();
        dsh.putString("state", "2");
        dshOrderListFra.setArguments(dsh);

        OrderListFra dpjOrderListFra = new OrderListFra();
        Bundle dpj = new Bundle();
        dpj.putString("state", "3");
        dpjOrderListFra.setArguments(dpj);

        OrderListFra dpjOrderListFra1 = new OrderListFra();
        Bundle dpj1 = new Bundle();
        dpj1.putString("state", "4");
        dpjOrderListFra1.setArguments(dpj1);


        fragments.add(allOrderListFra);
        fragments.add(dfkOrderListFra);
        fragments.add(dfhOrderListFra);
        fragments.add(dshOrderListFra);
        fragments.add(dpjOrderListFra);
        fragments.add(dpjOrderListFra1);

        viewPager.setAdapter(new MFragmentStatePagerAdapter(getChildFragmentManager(), fragments, titles));
        tabLayout.setViewPager(viewPager);
        viewPager.setCurrentItem(Integer.parseInt(state));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public String rightText() {
        return "";
    }

    @Override
    public void onRightClicked(View v) {

    }
}
