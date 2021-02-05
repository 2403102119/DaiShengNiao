package com.lxkj.dsn.ui.fragment.fra;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.AffirmOrderAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.StringUtil;
import com.lxkj.dsn.utils.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/1/29
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:确认订单
 */
public class AffirmOrderFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.tvSite)
    TextView tvSite;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.rlLogistics)
    RecyclerView rlLogistics;
    @BindView(R.id.ns)
    NestedScrollView ns;
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.llButton)
    LinearLayout llButton;
    @BindView(R.id.llSelectAddress)
    LinearLayout llSelectAddress;
    @BindView(R.id.tvAllPrice)
    TextView tvAllPrice;
    @BindView(R.id.tvAllCount)
    TextView tvAllCount;
    @BindView(R.id.etBeizhu)
    EditText etBeizhu;
    private List<DataListBean> listBeans = new ArrayList<>();
    private List<String> gcidlist = new ArrayList<>();
    private AffirmOrderAdapter affirmOrderAdapter;
    private DataListBean bean = new DataListBean();
    private String addressid,type;
    private Double zongjine = 0.00;
    private int zongshuliang = 0;

    @Override
    public String getTitleName() {
        return "提交订单";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_affirmorder, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        if (null != getArguments().getSerializable("bean")) {
            listBeans.clear();
            listBeans.addAll((List<DataListBean>) getArguments().getSerializable("bean"));
        }
        type = getArguments().getString("type");
        gcidlist.clear();
        try {
            gcidlist.addAll((List<String>) getArguments().getSerializable("gcidlist"));
        }catch (Exception e){

        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rlLogistics.setLayoutManager(layoutManager);
        affirmOrderAdapter = new AffirmOrderAdapter(getContext(), listBeans);
        rlLogistics.setAdapter(affirmOrderAdapter);
        affirmOrderAdapter.setOnItemClickListener(new AffirmOrderAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

            }

            @Override
            public void Onselect(int firstPosition, String amount) {
                listBeans.get(firstPosition).numbers = amount;
                setData();
            }
        });

        myAddressList();

        llSelectAddress.setOnClickListener(this);
        tvPay.setOnClickListener(this);


    }

    public void setData() {
        for (int i = 0; i < listBeans.size(); i++) {
            BigDecimal price = new BigDecimal(listBeans.get(i).newprice);
            BigDecimal count = new BigDecimal(listBeans.get(i).numbers);
            BigDecimal jine = count.multiply(price);
            zongjine += jine.doubleValue();
            zongshuliang += Integer.parseInt(listBeans.get(i).numbers);
        }
        tvAllPrice.setText("¥" + zongjine);
        tvAllCount.setText("共计" + zongshuliang + "件商品,");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSelectAddress://选择地址
                ActivitySwitcher.startFrgForResult(getActivity(), AddressListFra.class, 444);
                break;
            case R.id.tvPay://提交订单
                if (StringUtil.isEmpty(addressid)){
                    ToastUtil.show("请选择收货地址");
                    return;
                }
                if (type.equals("0")){
                    immediatelypurchase();
                }else {
                    goodscarpurchase();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 444 && resultCode == 111) {
            if (null != data) {
                DataListBean dataListBean = new DataListBean();
                dataListBean = (DataListBean) data.getSerializableExtra("data");
                tvSite.setText(dataListBean.address + dataListBean.addressdetail);
                tvName.setText(dataListBean.phone + " " + dataListBean.name);
                tvName.setVisibility(View.VISIBLE);
                addressid = dataListBean.addressid;
            }
        }
    }


    //我的地址
    private void myAddressList() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        mOkHttpHelper.post_json(getContext(), Url.myAddressList, params, new BaseCallback<ResultBean>() {
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
                for (int i = 0; i < resultBean.dataList.size(); i++) {
                    if (resultBean.dataList.get(i).isdefault.equals("1")) {
                        tvSite.setText(resultBean.dataList.get(i).address + resultBean.dataList.get(i).addressdetail);
                        tvName.setText(resultBean.dataList.get(i).phone + " " + resultBean.dataList.get(i).name);
                        addressid = resultBean.dataList.get(i).addressid;
                    }
                }

                if (StringUtil.isEmpty(tvName.getText().toString())) {
                    tvName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //商品立即购买
    private void immediatelypurchase() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("gid", listBeans.get(0).gid);
        params.put("numbers", zongshuliang);
        params.put("addressid", addressid);
        params.put("remarks", etBeizhu.getText().toString());
        mOkHttpHelper.post_json(getContext(), Url.immediatelypurchase, params, new BaseCallback<ResultBean>() {
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
                Bundle bundle = new Bundle();
                bundle.putString("ordernum",resultBean.ordernum);
                bundle.putString("money",zongjine+"");
                ActivitySwitcher.startFragment(getActivity(), PayFra.class,bundle);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //提交购物车
    private void goodscarpurchase() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("gcidlist",gcidlist);
        params.put("addressid", addressid);
        params.put("remarks", etBeizhu.getText().toString());
        mOkHttpHelper.post_json(getContext(), Url.goodscarpurchase, params, new BaseCallback<ResultBean>() {
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
                Bundle bundle = new Bundle();
                bundle.putString("ordernum",resultBean.ordernum);
                bundle.putString("money",zongjine+"");
                ActivitySwitcher.startFragment(getActivity(), PayFra.class,bundle);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
