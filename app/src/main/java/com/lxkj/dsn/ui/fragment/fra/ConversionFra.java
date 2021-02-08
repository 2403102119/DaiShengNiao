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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.AmountView2;
import com.lxkj.dsn.utils.StringUtil;
import com.lxkj.dsn.utils.ToastUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/2/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:积分兑换
 */
public class ConversionFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.tvSite)
    TextView tvSite;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.llSelectAddress)
    LinearLayout llSelectAddress;
    @BindView(R.id.tvShiyongjifen)
    TextView tvShiyongjifen;
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSku)
    TextView tvSku;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.AmountView)
    AmountView2 AmountView;
    @BindView(R.id.etBeizhu)
    EditText etBeizhu;
    @BindView(R.id.ns)
    NestedScrollView ns;
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.llButton)
    LinearLayout llButton;


    private DataListBean dataListBean = new DataListBean();
    private String addressid;

    @Override
    public String getTitleName() {
        return "订单确认";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_conversion, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        if (null != getArguments().getSerializable("bean")) {
            dataListBean = (DataListBean) getArguments().getSerializable("bean");
        }

        Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.logo)
                .placeholder(R.mipmap.logo))
                .load(dataListBean.image)
                .into(riIcon);
        tvTitle.setText(dataListBean.name);
        tvShiyongjifen.setText(dataListBean.newprice);



        llSelectAddress.setOnClickListener(this);
        tvPay.setOnClickListener(this);

        myAddressList();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSelectAddress://选择地址
                ActivitySwitcher.startFrgForResult(getActivity(), AddressListFra.class, 555);
                break;
            case R.id.tvPay://提交订单
                if (StringUtil.isEmpty(addressid)) {
                    ToastUtil.show("请选择收货地址");
                    return;
                }
                exchangeintegralgoods();
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 555 && resultCode == 111) {
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

    //积分商品兑换
    private void exchangeintegralgoods() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("gid", dataListBean.gid);
        params.put("numbers", "1");
        params.put("addressid", addressid);
        params.put("remarks", etBeizhu.getText().toString());
        mOkHttpHelper.post_json(getContext(), Url.exchangeintegralgoods, params, new BaseCallback<ResultBean>() {
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
                ActivitySwitcher.startFragment(getActivity(), ConversionOkFra.class);
                act.finish();
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
