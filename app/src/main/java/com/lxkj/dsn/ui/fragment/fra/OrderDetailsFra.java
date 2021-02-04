package com.lxkj.dsn.ui.fragment.fra;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.OrderDetailAdapter;
import com.lxkj.dsn.bean.OrdertailListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.StringUtil;
import com.lxkj.dsn.view.NormalDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * Time:2021/2/1
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:订单详情
 */
public class OrderDetailsFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvAllPrice)
    TextView tvAllPrice;
    @BindView(R.id.llfanhui)
    LinearLayout llfanhui;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.tvDetail)
    TextView tvDetail;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvSite)
    TextView tvSite;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvOrderPrice)
    TextView tvOrderPrice;
    @BindView(R.id.tvOrderNum)
    TextView tvOrderNum;
    @BindView(R.id.tvChuangjian)
    TextView tvChuangjian;
    @BindView(R.id.tvFukuan)
    TextView tvFukuan;
    @BindView(R.id.tvFahuo)
    TextView tvFahuo;
    @BindView(R.id.tvChengjiao)
    TextView tvChengjiao;
    @BindView(R.id.llCall)
    LinearLayout llCall;
    @BindView(R.id.imCopy)
    ImageView imCopy;
    @BindView(R.id.llFukuan)
    LinearLayout llFukuan;
    @BindView(R.id.llFahuo)
    LinearLayout llFahuo;
    @BindView(R.id.llChengjiao)
    LinearLayout llChengjiao;
    @BindView(R.id.llButton)
    LinearLayout llButton;
    private OrderDetailAdapter affirmOrderAdapter;
    private String ordernum;
    private List<OrdertailListBean> listBeans = new ArrayList<>();

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

    public void initView() {
        ordernum = getArguments().getString("ordernum");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        affirmOrderAdapter = new OrderDetailAdapter(getContext(), listBeans);
        recyclerView.setAdapter(affirmOrderAdapter);
        affirmOrderAdapter.setOnItemClickListener(new OrderDetailAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

            }

            @Override
            public void Onselect(int firstPosition, String amount) {

            }
        });

        myorderdetail();

        tvPay.setOnClickListener(this);
        imCopy.setOnClickListener(this);
        llfanhui.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPay:
                switch (tvPay.getText().toString()) {
                    case "去付款":
                        Bundle bundle = new Bundle();
                        bundle.putString("ordernum", ordernum);
                        bundle.putString("money", tvOrderPrice.getText().toString());
                        ActivitySwitcher.startFragment(getActivity(), PayFra.class, bundle);
                        break;
                    case "提醒发货":
                        Toast toast = Toast.makeText(getContext(), "已提醒商家尽快发货", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    case "确认收货":
                        NormalDialog dialog = new NormalDialog(getContext(), "确认确认收货？", "取消", "确定", true);
                        dialog.show();
                        dialog.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                            @Override
                            public void OnRightClick() {
                                orderconfirm(ordernum);
                            }

                            @Override
                            public void OnLeftClick() {

                            }
                        });
                        break;
                    case "去评价":
                        break;
                    case "删除订单":
                        NormalDialog dialog1 = new NormalDialog(getContext(), "确认删除订单？", "取消", "确定", true);
                        dialog1.show();
                        dialog1.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                            @Override
                            public void OnRightClick() {
                                orderdelete(ordernum);
                            }

                            @Override
                            public void OnLeftClick() {

                            }
                        });
                        break;
                }
                break;
            case R.id.imCopy:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText(null, tvOrderNum.getText().toString());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);

                Toast.makeText(getContext(), " 已复制", Toast.LENGTH_SHORT).show();
                break;
            case R.id.llfanhui:
                act.finishSelf();
                break;
            case R.id.tvCancel:
                switch (tvCancel.getText().toString()){
                    case "取消订单":
                        NormalDialog dialog1 = new NormalDialog(getContext(), "确认取消订单？", "取消", "确定", true);
                        dialog1.show();
                        dialog1.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                            @Override
                            public void OnRightClick() {
                                Bundle bundle = new Bundle();
                                bundle.putString("ordernum",ordernum);
                                ActivitySwitcher.startFragment(getActivity(), QuxiaoOrderFra.class,bundle);
                                act.finish();
                            }

                            @Override
                            public void OnLeftClick() {

                            }
                        });
                        break;
                    case "查看物流":
                        break;
                }
                break;
        }
    }

    /**
     * 订单详情
     */
    private void myorderdetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("ordernum", ordernum);
        mOkHttpHelper.post_json(getContext(), Url.myorderdetail, params, new BaseCallback<ResultBean>() {
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
                listBeans.clear();
                listBeans.addAll(resultBean.dataobject.ordertailList);
                affirmOrderAdapter.notifyDataSetChanged();

                tvSite.setText("收货地址：" + resultBean.dataobject.address);
                tvName.setText(resultBean.dataobject.name + ":" + resultBean.dataobject.phone);
                tvAllPrice.setText(resultBean.dataobject.goodsprice);
                tvOrderPrice.setText(resultBean.dataobject.goodsprice);
                tvOrderNum.setText(resultBean.dataobject.ordernum);
                tvChuangjian.setText(resultBean.dataobject.adtime);
                tvFukuan.setText(resultBean.dataobject.paytime);
                tvFahuo.setText(resultBean.dataobject.sendtime);
                tvChengjiao.setText(resultBean.dataobject.shtime);

                if (StringUtil.isEmpty(resultBean.dataobject.paytime)) {
                    llFukuan.setVisibility(View.GONE);
                } else {
                    llFukuan.setVisibility(View.GONE);
                }
                if (StringUtil.isEmpty(resultBean.dataobject.sendtime)) {
                    llFahuo.setVisibility(View.GONE);
                } else {
                    llFahuo.setVisibility(View.GONE);
                }
                if (StringUtil.isEmpty(resultBean.dataobject.shtime)) {
                    llChengjiao.setVisibility(View.GONE);
                } else {
                    llChengjiao.setVisibility(View.GONE);
                }

                switch (resultBean.dataobject.state) {
                    case "0":
                        tvState.setText("待付款");
                        tvPay.setVisibility(View.VISIBLE);
                        tvCancel.setVisibility(View.VISIBLE);
                        tvPay.setText("去付款");
                        tvCancel.setText("取消订单");
                        llButton.setVisibility(View.VISIBLE);
                        break;
                    case "1":
                        tvState.setText("待发货");
                        tvPay.setVisibility(View.VISIBLE);
                        tvCancel.setVisibility(View.GONE);
                        tvPay.setText("提醒发货");
                        llButton.setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        tvState.setText("待收货");
                        tvPay.setVisibility(View.VISIBLE);
                        tvCancel.setVisibility(View.VISIBLE);
                        tvPay.setText("确认收货");
                        tvCancel.setText("查看物流");
                        llButton.setVisibility(View.VISIBLE);
                        break;
                    case "3":
                        tvState.setText("待评价");
                        tvPay.setVisibility(View.VISIBLE);
                        tvCancel.setVisibility(View.GONE);
                        tvPay.setText("去评价");
                        llButton.setVisibility(View.VISIBLE);
                        break;
                    case "4":
                        tvState.setText("已完成");
                        tvPay.setVisibility(View.VISIBLE);
                        tvCancel.setVisibility(View.GONE);
                        tvPay.setText("删除订单");
                        llButton.setVisibility(View.VISIBLE);
                        break;
                    case "5":
                        tvState.setText("退款中");
                        tvPay.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.GONE);
                        llButton.setVisibility(View.GONE);
                        break;
                    case "6":
                        tvState.setText("已退款");
                        tvPay.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.GONE);
                        llButton.setVisibility(View.GONE);
                        break;
                    case "7":
                        tvState.setText("已取消");
                        tvPay.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.GONE);
                        llButton.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 确认收货
     */
    private void orderconfirm(String ordernum) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("ordernum", ordernum);
        mOkHttpHelper.post_json(getContext(), Url.orderconfirm, params, new BaseCallback<ResultBean>() {
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

                act.finishSelf();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 订单删除
     */
    private void orderdelete(String ordernum) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("ordernum", ordernum);
        mOkHttpHelper.post_json(getContext(), Url.orderdelete, params, new BaseCallback<ResultBean>() {
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

                act.finishSelf();
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
