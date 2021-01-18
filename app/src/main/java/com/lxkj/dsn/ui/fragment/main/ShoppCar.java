package com.lxkj.dsn.ui.fragment.main;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.ShoppingAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.ui.fragment.CachableFrg;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Time:2020/10/28
 * <p>
 * Author:李迪迦
 * <p>
 * Description:购物车
 */
public class ShoppCar extends CachableFrg implements View.OnClickListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<DataListBean> listBeans = new ArrayList<>();
    private ShoppingAdapter shoppingAdapter;

    @Override
    protected int rootLayout() {
        return R.layout.fra_messagelist;
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        shoppingAdapter = new ShoppingAdapter(getActivity(),listBeans);
        recyclerView.setAdapter(shoppingAdapter);
        shoppingAdapter.setOnItemClickListener(new ShoppingAdapter.OnItemClickListener() {
            @Override
            public void OnItem(int position) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
