package com.lxkj.dsn.ui.fragment.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.SpotsCallBack;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.Md5;
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
import cn.jpush.android.api.JPushInterface;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2020/10/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:忘记密码
 */
public class RestFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etEnterPassword)
    EditText etEnterPassword;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    @Override
    public String getTitleName() {
        return "找回密码";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fra_rest, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        tvConfirm.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvConfirm:
                if (StringUtil.isEmpty(etAccount.getText().toString())){
                    ToastUtil.show("请输入手机号");
                    return;
                }
                if (StringUtil.isEmpty(etCode.getText().toString())){
                    ToastUtil.show("请输入验证码");
                    return;
                }
                if (StringUtil.isEmpty(etPassword.getText().toString())){
                    ToastUtil.show("请输入密码");
                    return;
                }
                if (StringUtil.isEmpty(etEnterPassword.getText().toString())){
                    ToastUtil.show("请确认密码");
                    return;
                }
                if (!etPassword.getText().toString().equals(etEnterPassword.getText().toString())){
                    ToastUtil.show("确认密码错误");
                    return;
                }
                forgetPwd();
                break;
            case R.id.tvGetCode://获取验证码
                phoneRegister();
                break;
        }
    }


    //验证手机号是否注册
    private void phoneRegister() {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", etAccount.getText().toString());
        mOkHttpHelper.post_json(mContext, Url.phoneRegister, params, new BaseCallback<ResultBean>() {
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
                if (resultBean.isregister.equals("0")) {
                    getValidateCode();
                }else {
                    ToastUtil.show("手机号已注册");
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //忘记密码
    private void forgetPwd() {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", etAccount.getText().toString());
        params.put("code", etCode.getText().toString());
        params.put("password", Md5.encode(etEnterPassword.getText().toString()));
        mOkHttpHelper.post_json(mContext, Url.forgetPwd, params, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                SharePrefUtil.saveString(mContext, AppConsts.UID, resultBean.uid);
                AppConsts.userId = resultBean.uid;
                SharePrefUtil.saveString(mContext, AppConsts.PHONE, etAccount.getText().toString());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
