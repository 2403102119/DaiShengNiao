package com.lxkj.dsn.ui.fragment.fra;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.OrderListAdapter;
import com.lxkj.dsn.adapter.ShoppingAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.StringUtil;
import com.lxkj.dsn.view.NormalDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.beecloud.BCPay;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2020/9/9
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class OrderListFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    private int pageNoIndex = 1;
    private int totalPage = 1;
//    private ShoppingAdapter adapter;

    private ArrayList<DataListBean> listBeans;
    private int page = 1;
    private String state,keywords = "",orderType = "1",beginTime = "",endTime = "";
    private PopupWindow popupWindow,popupWindow1;// 声明PopupWindow
    private LinearLayout ll_ll;
    private OrderListAdapter adapter;
    private LinearLayout ll_all_item;
    private RelativeLayout ll_all;
    private String pay_type="1",payMoney;
    private ProgressDialog loadingDialog;
    private String money = "0", payMethod,optionId;
    private String id,realAmount,ShequOrZiti;
    private List<DataListBean> spinnerList = new ArrayList<>();
    private boolean ryVis =false;
    @Override
    public String getTitleName() {
        return "全部订单";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_orderlist, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        state = getArguments().getString("state");

        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OrderListAdapter(getContext(), listBeans);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OrderListAdapter.OnItemClickListener() {
            @Override
            public void OnItem(int position) {//详情
                if (listBeans.get(position).state.equals("5")||listBeans.get(position).state.equals("6")){
                    Bundle bundle = new Bundle();
                    bundle.putString("ordernum",listBeans.get(position).ordernum);
                    ActivitySwitcher.startFragment(getActivity(), RefundFra.class,bundle);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("ordernum",listBeans.get(position).ordernum);
                    ActivitySwitcher.startFragment(getActivity(), OrderDetailsFra.class,bundle);
                }
            }

            @Override
            public void OnPay(int position, String state) {
                Bundle bundle = new Bundle();
                switch (state){
                    case "去付款":

                        bundle.putString("ordernum",listBeans.get(position).ordernum);
                        bundle.putString("money",listBeans.get(position).goodsprice);
                        ActivitySwitcher.startFragment(getActivity(), PayFra.class,bundle);
                        break;
                    case "提醒发货":
                        Toast toast = Toast.makeText(getContext(), "已提醒商家尽快发货", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0, 0);
                        toast.show();
                        break;
                    case "确认收货":
                        NormalDialog dialog = new NormalDialog(getContext(), "确认确认收货？", "取消", "确定", true);
                        dialog.show();
                        dialog.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                            @Override
                            public void OnRightClick() {
                                orderconfirm(listBeans.get(position).ordernum);
                            }

                            @Override
                            public void OnLeftClick() {

                            }
                        });
                        break;
                    case "去评价":
                        bundle.putString("ordernum", listBeans.get(position).ordernum);
                        ActivitySwitcher.startFragment(getActivity(), AppraiseFra.class, bundle);
                        break;

                }
            }


            @Override
            public void OnQuxiao(int position) {//取消订单
                Bundle bundle = new Bundle();
                bundle.putString("ordernum",listBeans.get(position).ordernum);
                ActivitySwitcher.startFragment(getActivity(), QuxiaoOrderFra.class,bundle);
            }
        });
        smart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page >= totalPage) {
                    refreshLayout.setNoMoreData(true);
                    return;
                }
                page++;
                myorderlist();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                refreshLayout.setNoMoreData(false);
                myorderlist();
            }
        });

    }


    /**
     * 我的商城订单
     */
    private void myorderlist() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("nowPage", page + "");
        params.put("type", state);
        mOkHttpHelper.post_json(getContext(), Url.myorderlist, params, new BaseCallback<ResultBean>() {
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

                if (!StringUtil.isEmpty(resultBean.totalPage))
                    totalPage = Integer.parseInt(resultBean.totalPage);
                smart.finishLoadMore();
                smart.finishRefresh();
                if (page == 1) {
                    listBeans.clear();
                    adapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans.addAll(resultBean.dataList);

                if (resultBean.dataList.size() == 0){
                    llNoData.setVisibility(View.VISIBLE);
                }else {
                    llNoData.setVisibility(View.GONE);
                }

                adapter.notifyDataSetChanged();

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

                myorderlist();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        myorderlist();
    }



    /**
     * 设置手机屏幕亮度显示正常
     */
    private void lighton() {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 1f;
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
