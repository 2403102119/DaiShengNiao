package com.lxkj.dsn.ui.fragment.fra;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.SpotsCallBack;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.lxkj.dsn.utils.StringUtil;
import com.lxkj.dsn.utils.StringUtils;
import com.lxkj.dsn.utils.TimerUtil;
import com.lxkj.dsn.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
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
 * Interface:
 */
public class BindingFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.tvOldPhone)
    TextView tvOldPhone;
    @BindView(R.id.etOldCode)
    EditText etOldCode;
    @BindView(R.id.tvGetOldCode)
    TextView tvGetOldCode;

    @Override
    public String getTitleName() {
        return "绑定手机号";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_binding, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        tvOldPhone.setText(SharePrefUtil.getString(getContext(), AppConsts.PHONE, ""));

        tvGetCode.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvGetOldCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGetCode://获取验证码
                getValidateCode();
                break;
            case R.id.tvConfirm://绑定
                if (StringUtil.isEmpty(etOldCode.getText().toString())){
                    ToastUtil.show("原手机号验证码不能为空");
                    return;
                }
                if (StringUtil.isEmpty(etAccount.getText().toString())){
                    ToastUtil.show("换绑手机号不能为空");
                    return;
                }
                if (StringUtil.isEmpty(etCode.getText().toString())){
                    ToastUtil.show("换绑手机号验证码不能为空");
                    return;
                }
                bindingphone();
                break;
            case R.id.tvGetOldCode://老手机号的验证码
                getValidateOldCode();
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void getValidateCode() {
        String user_phone_number = etAccount.getText().toString().trim();
        //验证电话号码不能为空
        if (TextUtils.isEmpty(user_phone_number)) {
            ToastUtil.show("请输入手机号");
            return;
        }
        //验证手机号是否正确
        if (!StringUtils.isMobile(user_phone_number)) {
            ToastUtil.show("输入的手机号格式不正确");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("phone", user_phone_number);
        mOkHttpHelper.post_json(getContext(), Url.getValidateCode, param, new SpotsCallBack<ResultBean>(getContext()) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                if (resultBean.getResult().equals("0")) {
                    TimerUtil mTimerUtil = new TimerUtil(tvGetCode);
                    mTimerUtil.timers();
                    ToastUtil.show("验证码已发送，其注意查收");
                } else
                    ToastUtil.show(resultBean.getResultNote());
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtil.show(getString(R.string.httperror));
            }
        });
    }

    //换绑手机
    private void bindingphone() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("phone", etAccount.getText().toString());
        params.put("code1", etOldCode.getText().toString());
        params.put("code2", etCode.getText().toString());
        mOkHttpHelper.post_json(getContext(), Url.editmemberInfo, params, new BaseCallback<ResultBean>() {
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
                ToastUtil.show(resultBean.resultNote);
                Intent intent = new Intent();
                intent.putExtra("et",etAccount.getText().toString());
                act.setResult(111, intent);
                act.finishSelf();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 获取验证码
     */
    private void getValidateOldCode() {
        Map<String, Object> param = new HashMap<>();
        param.put("phone", tvOldPhone.getText().toString());
        mOkHttpHelper.post_json(getContext(), Url.getValidateCode, param, new SpotsCallBack<ResultBean>(getContext()) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                if (resultBean.getResult().equals("0")) {
                    TimerUtil mTimerUtil = new TimerUtil(tvGetOldCode);
                    mTimerUtil.timers();
                    ToastUtil.show("验证码已发送，其注意查收");
                } else
                    ToastUtil.show(resultBean.getResultNote());
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtil.show(getString(R.string.httperror));
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
