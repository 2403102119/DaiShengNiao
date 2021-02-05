package com.lxkj.dsn.ui.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.biz.EventCenter;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.SpotsCallBack;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.activity.MainActivity;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.ui.fragment.system.WebFra;
import com.lxkj.dsn.utils.Md5;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.lxkj.dsn.utils.StringUtil;
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
 * Created by kxn on 2020/1/9 0009.
 */
public class LoginFra extends TitleFragment implements View.OnClickListener, EventCenter.EventListener {

    Unbinder unbinder;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.imEys)
    ImageView imEys;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.tvRetrieve)
    TextView tvRetrieve;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvYonghu)
    TextView tvYonghu;
    @BindView(R.id.imWeChat)
    ImageView imWeChat;
    @BindView(R.id.imQQ)
    ImageView imQQ;
    @BindView(R.id.ckXieyi)
    CheckBox ckXieyi;
    private boolean eyes = false;

    @Override
    public String getTitleName() {
        return "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_login, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }

    public void initView() {
        act.hindNaviBar();
        eventCenter.registEvent(this, EventCenter.EventType.EVT_BINDPHONE);
        eventCenter.registEvent(this, EventCenter.EventType.EVT_LOGIN);
        eventCenter.registEvent(this, EventCenter.EventType.EVT_REGISTER);




        tvRegister.setOnClickListener(this);
        tvRetrieve.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvYonghu.setOnClickListener(this);
        imEys.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        etPhone.setText(SharePrefUtil.getString(getContext(), AppConsts.PHONE, ""));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRegister://注册
                ActivitySwitcher.startFragment(act, RegisterFra.class);
                break;
            case R.id.tvRetrieve://忘记密码
                ActivitySwitcher.startFragment(act, RestFra.class);
                break;
            case R.id.tvLogin://登录
                if (StringUtil.isEmpty(etPhone.getText().toString())) {
                    ToastUtil.show("请输入手机号");
                    return;
                }
                if (StringUtil.isEmpty(etPassword.getText().toString())) {
                    ToastUtil.show("请输入密码");
                    return;
                }
                if (!ckXieyi.isChecked()){
                    ToastUtil.show("请阅读并同意《用户协议》");
                    return;
                }

                phoneRegister();
                break;
            case R.id.tvYonghu://用户协议
                Bundle bundle = new Bundle();
                bundle.putString("title", "用户协议");
                bundle.putString("url","http://8.140.109.101/daishengniao/display/agreement?id=1");
                ActivitySwitcher.startFragment(getContext(), WebFra.class, bundle);
                break;
            case R.id.imEys:
                if (eyes){
                    eyes= false;
                    imEys.setImageResource(R.mipmap.biyan);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    eyes= true;
                    imEys.setImageResource(R.mipmap.zhengyan);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
        }
    }

    //密码登录
    private void userLogin() {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", etPhone.getText().toString());
        params.put("password", Md5.encode(etPassword.getText().toString()));
        params.put("token", JPushInterface.getRegistrationID(mContext));//推送标识
        mOkHttpHelper.post_json(mContext, Url.userLogin, params, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                SharePrefUtil.saveString(mContext, AppConsts.UID, resultBean.uid);
                AppConsts.userId = resultBean.uid;
                SharePrefUtil.saveString(mContext, AppConsts.PHONE, etPhone.getText().toString());
                ActivitySwitcher.start(act, MainActivity.class);
                act.finishSelf();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    //验证手机号是否注册
    private void phoneRegister() {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", etPhone.getText().toString());
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
                if (resultBean.isregister.equals("0")){
                    ToastUtil.show("手机号未注册");
                    return;
                }
                userLogin();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        eventCenter.unregistEvent(this, EventCenter.EventType.EVT_BINDPHONE);
    }

    @Override
    public void onEvent(EventCenter.HcbEvent e) {
        switch (e.type) {
            case EVT_LOGIN:
                act.finishSelf();
                break;
        }
    }
}
