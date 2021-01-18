package com.lxkj.dsn.ui.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.biz.EventCenter;
import com.lxkj.dsn.ui.activity.MainActivity;
import com.lxkj.dsn.ui.fragment.TitleFragment;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

    }

    @Override
    public void onResume() {
        super.onResume();

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
                ActivitySwitcher.start(act, MainActivity.class);
                act.finishSelf();
                break;
        }
    }

//    //登录
//    private void userLogin() {
//        if (TextUtils.isEmpty(etAccount.getText())) {
//            ToastUtil.show("请输入手机号");
//            return;
//        }
//        if (login_type.equals("0")) {
//            if (TextUtils.isEmpty(etPsw.getText())) {
//                ToastUtil.show("请输入密码");
//                return;
//            }
//        } else if (login_type.equals("1")) {
//            if (TextUtils.isEmpty(etcode.getText())) {
//                ToastUtil.show("请输入验证码");
//                return;
//            }
//        }
//        Map<String, Object> params = new HashMap<>();
//        params.put("mobile", etAccount.getText().toString());
//        params.put("password", Md5.encode(etPsw.getText().toString()));
//        params.put("authCode", etcode.getText().toString());
//        params.put("rid", JPushInterface.getRegistrationID(mContext));//推送标识
//        mOkHttpHelper.post_json(mContext, Url.userLogin, params, new SpotsCallBack<ResultBean>(mContext) {
//            @Override
//            public void onSuccess(Response response, ResultBean resultBean) {
//                eventCenter.sendType(EventCenter.EventType.EVT_LOGOUT); //关闭 重新打开
//                if (resultBean.status.equals("1")){
//                    SharePrefUtil.saveString(mContext, AppConsts.UID, resultBean.sid);
//                    AppConsts.userId = resultBean.sid;
//                    SharePrefUtil.saveString(mContext, AppConsts.PHONE, etAccount.getText().toString());
//                    ActivitySwitcher.start(act, MainActivity.class);
//                    act.finishSelf();
//                }else if (resultBean.status.equals("2")){
//                    ToastUtil.show("账号审核中，请耐心等候");
//                }else if (resultBean.status.equals("3")){
//                    ToastUtil.show("账号审核失败，请重新申请");
//                }
//
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//
//            }
//        });
//    }


//    //获取验证码
//    private void getAuthCode() {
//        if (TextUtils.isEmpty(etAccount.getText())) {
//            ToastUtil.show("请输入手机号");
//            return;
//        }
//        Map<String, Object> params = new HashMap<>();
//        params.put("mobile", etAccount.getText().toString());
//        mOkHttpHelper.post_json(mContext, Url.getAuthCode, params, new SpotsCallBack<ResultBean>(mContext) {
//            @Override
//            public void onSuccess(Response response, ResultBean resultBean) {
//
//                if (resultBean.getResult().equals("0")) {
//                    TimerUtil mTimerUtil = new TimerUtil(tvgetcode);
//                    mTimerUtil.timers();
//                    ToastUtil.show("验证码已发送，其注意查收");
//                } else
//                    ToastUtil.show(resultBean.getResultNote());
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//
//            }
//        });
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventCenter.unregistEvent(this, EventCenter.EventType.EVT_BINDPHONE);
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
