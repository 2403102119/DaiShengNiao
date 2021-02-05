package com.lxkj.dsn.ui.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.ShoppingAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.CachableFrg;
import com.lxkj.dsn.ui.fragment.fra.AffirmOrderFra;
import com.lxkj.dsn.utils.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import okhttp3.Request;
import okhttp3.Response;

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
    @BindView(R.id.navi_title)
    TextView naviTitle;
    @BindView(R.id.navi_right_txt)
    TextView naviRightTxt;
    @BindView(R.id.nv)
    RelativeLayout nv;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.imageSel)
    ImageView imageSel;
    @BindView(R.id.tv_sell_moeney)
    TextView tvSellMoeney;
    @BindView(R.id.tv_accounts)
    TextView tvAccounts;
    private List<DataListBean> listBeans = new ArrayList<>();
    private ShoppingAdapter shoppingAdapter;
    private int page = 1, totalPage = 1;
    private boolean imageSelcheck;
    private ArrayList<String> cartidlist = new ArrayList<>();
    private List<Float> moneylist = new ArrayList<>();
    ArrayList<DataListBean> list_intent=new ArrayList<>();
    @Override
    protected int rootLayout() {
        return R.layout.fra_messagelist;
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        shoppingAdapter = new ShoppingAdapter(getActivity(), listBeans);
        recyclerView.setAdapter(shoppingAdapter);
        shoppingAdapter.setOnItemClickListener(new ShoppingAdapter.OnItemClickListener() {
            @Override
            public void OnItem(int position) {

                if (listBeans.get(position).isCheck == true){
                    cartidlist.add(listBeans.get(position).gcid);
                    BigDecimal qian = new BigDecimal(listBeans.get(position).newprice);
                    BigDecimal shulaing = new BigDecimal(listBeans.get(position).numbers);
                    BigDecimal jine = qian.multiply(shulaing);
                    moneylist.add(jine.floatValue());
                    list_intent.add(listBeans.get(position));
                }else {
                    cartidlist.remove(listBeans.get(position).gcid);
                    BigDecimal qian = new BigDecimal(listBeans.get(position).newprice);
                    BigDecimal shulaing = new BigDecimal(listBeans.get(position).numbers);
                    BigDecimal jine = qian.multiply(shulaing);
                    moneylist.remove(jine.floatValue());
                    list_intent.remove(listBeans.get(position));
                }
                float a = 0;
                for (int i = 0; i <listBeans.size() ; i++) {
                    if (listBeans.get(i).isCheck == true){
                        a += Float.parseFloat(listBeans.get(i).newprice)*Integer.parseInt(listBeans.get(i).numbers);
                    }

                }
                BigDecimal bigDecimal = new BigDecimal(a);
                tvSellMoeney.setText(bigDecimal.setScale(2, RoundingMode.HALF_DOWN).toString());
//                zong = a+"";

                imageSelcheck = true;
                for (int i = 0; i <listBeans.size() ; i++) {
                    if (listBeans.get(i).isCheck == false){
                        imageSelcheck = false;
                    }
                }
                if (imageSelcheck){
                    imageSel.setImageResource(R.drawable.xuanzhong);
                } else{
                    imageSel.setImageResource(R.drawable.weixuanzhong);
                }

            }

            @Override
            public void Onselect(int position, String mount) {

                editgoodscar(listBeans.get(position).gcid,mount,position);
            }
        });

        smart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page >= totalPage) {
                    refreshLayout.setNoMoreData(true);
                    return;
                }
                page=1;
                getmygoodscar();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getmygoodscar();
                refreshLayout.setNoMoreData(false);
            }
        });


        imageSel.setOnClickListener(this);
        tvAccounts.setOnClickListener(this);
        naviRightTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageSel://全选
                imageSelcheck = !imageSelcheck;
                cartidlist.clear();
                list_intent.clear();
                moneylist.clear();

                if (imageSelcheck == true){
                    for (int i = 0; i <listBeans.size() ; i++) {
                        listBeans.get(i).isCheck = true;
                        BigDecimal qian = new BigDecimal(listBeans.get(i).newprice);
                        BigDecimal shulaing = new BigDecimal(listBeans.get(i).numbers);
                        BigDecimal jine = qian.multiply(shulaing);
                        moneylist.add(jine.floatValue());
                        //list_intent = list;
                        cartidlist.add(listBeans.get(i).gcid);
                    }
                    list_intent.addAll(listBeans);
                    imageSel.setImageResource(R.drawable.xuanzhong);
                }else {
                    for (int i = 0; i <listBeans.size() ; i++) {
                        listBeans.get(i).isCheck = false;
                        BigDecimal qian = new BigDecimal(listBeans.get(i).newprice);
                        BigDecimal shulaing = new BigDecimal(listBeans.get(i).numbers);
                        BigDecimal jine = qian.multiply(shulaing);
                        moneylist.remove(jine.floatValue());
                        list_intent.clear();
                    }
                    cartidlist.clear();
                    list_intent.clear();
                    moneylist.clear();
                    imageSel.setImageResource(R.drawable.weixuanzhong);
                }
                float a = 0;
                for (int i = 0; i <moneylist.size() ; i++) {
                    a += moneylist.get(i);
                }
                BigDecimal bigDecimal = new BigDecimal(a);


                tvSellMoeney.setText(bigDecimal.setScale(2, RoundingMode.HALF_DOWN).toString());
//                zong = bigDecimal.setScale(2, RoundingMode.HALF_DOWN).toString();
                shoppingAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_accounts://去结算
                if (tvAccounts.getText().toString().equals("去结算")){
                    if (cartidlist.size() == 0){
                        Toast.makeText(getContext(),"请选择商品",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", (Serializable) list_intent);
                    bundle.putSerializable("gcidlist", (Serializable) cartidlist);
                    bundle.putString("type", "1");
                    ActivitySwitcher.startFragment(getActivity(), AffirmOrderFra.class,bundle);
                }else {
                    if (cartidlist.size() == 0){
                        Toast.makeText(getContext(),"请选择商品",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    deletegoodscar();
                }

                break;
            case R.id.navi_right_txt://编辑
                 if (naviRightTxt.getText().toString().equals("编辑")){
                     naviRightTxt.setText("取消");
                     tvAccounts.setText("删除");
                 }else {
                     naviRightTxt.setText("编辑");
                     tvAccounts.setText("去结算");
                 }
                break;
        }
    }

    //我的购物车
    private void getmygoodscar() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        mOkHttpHelper.post_json(getContext(), Url.getmygoodscar, params, new BaseCallback<ResultBean>() {
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

                smart.finishLoadMore();
                smart.finishRefresh();

                listBeans.clear();
                listBeans.addAll(resultBean.dataList);
                shoppingAdapter.notifyDataSetChanged();

                cartidlist.clear();
                list_intent.clear();
                imageSel.setImageResource(R.drawable.weixuanzhong);
                imageSelcheck = false;
                tvAccounts.setText("去结算");
                for (int i = 0; i <listBeans.size() ; i++) {
                    listBeans.get(i).isCheck = false;
                }
                tvSellMoeney.setText("0.0");

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //编辑购物车
    private void editgoodscar(String gcid,String numbers,int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("gcid", gcid);
        params.put("numbers", numbers);
        mOkHttpHelper.post_json(getContext(), Url.editgoodscar, params, new BaseCallback<ResultBean>() {
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
                int a = 0;
                BigDecimal bigDecimal2 = null;

                if (listBeans.get(position).isCheck == true){
                    if (Integer.parseInt(listBeans.get(position).numbers)>Integer.parseInt(numbers)){
                        a = Integer.parseInt(listBeans.get(position).numbers) - Integer.parseInt(numbers);
                        BigDecimal bigDecimal = new BigDecimal(tvSellMoeney.getText().toString());
                        BigDecimal bigDecimal1 = new BigDecimal(a);
                        BigDecimal bigDecimal3 = new BigDecimal(listBeans.get(position).newprice);
                        BigDecimal bigDecimal4 = bigDecimal1.multiply(bigDecimal3);
                        bigDecimal2 = bigDecimal.subtract(bigDecimal4);
                        tvSellMoeney.setText(bigDecimal2.setScale(2, RoundingMode.HALF_DOWN).toString());


                    }else  if (Integer.parseInt(listBeans.get(position).numbers)<Integer.parseInt(numbers)){
                        a = Integer.parseInt(numbers)-Integer.parseInt(listBeans.get(position).numbers);
                        BigDecimal bigDecimal = new BigDecimal(tvSellMoeney.getText().toString());
                        BigDecimal bigDecimal1 = new BigDecimal(a);
                        BigDecimal bigDecimal3 = new BigDecimal(listBeans.get(position).newprice);
                        BigDecimal bigDecimal4 = bigDecimal1.multiply(bigDecimal3);
                        bigDecimal2 = bigDecimal.add(bigDecimal4);
                        tvSellMoeney.setText(bigDecimal2.setScale(2, RoundingMode.HALF_DOWN).toString());


                    }else {

                    }
                }else {

                }
                listBeans.get(position).numbers = numbers;

                shoppingAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //删除购物车
    private void deletegoodscar() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("gcidlist", cartidlist);
        mOkHttpHelper.post_json(getContext(), Url.deletegoodscar, params, new BaseCallback<ResultBean>() {
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
                getmygoodscar();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        getmygoodscar();
    }
}
