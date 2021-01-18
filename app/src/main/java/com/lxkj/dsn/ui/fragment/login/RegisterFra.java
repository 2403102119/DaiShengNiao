package com.lxkj.dsn.ui.fragment.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.http.SpotsCallBack;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.Md5;
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
import okhttp3.Response;

/**
 * Created by kxn on 2020/1/10 0010.
 */
public class RegisterFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;



    @Override
    public String getTitleName() {
        return "注册";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fra_register, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {

    }

//    /**
//     * 检测手机号是否注册
//     */
//    private void checkPhone() {
//        String user_phone_number = etAccount.getText().toString().trim();
//        //验证电话号码不能为空
//        if (TextUtils.isEmpty(user_phone_number)) {
//            ToastUtil.show("请输入手机号");
//            return;
//        }
//        //验证手机号是否正确
//        if (!StringUtils.isMobile(user_phone_number)) {
//            ToastUtil.show("输入的手机号格式不正确");
//            return;
//        }
//
//        Map<String, Object> param = new HashMap<>();
//        param.put("mobile", user_phone_number);
//        mOkHttpHelper.post_json(getContext(), Url.shopExist, param, new SpotsCallBack<ResultBean>(getContext()) {
//            @Override
//            public void onSuccess(Response response, ResultBean resultBean) {
//                if (resultBean.status.equals("0")) {
//                    ToastUtil.show("手机号未注册");
//                } else{
//                    getCode();
//                }
//
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//                ToastUtil.show(getString(R.string.httperror));
//            }
//        });
//    }
//
//    /**
//     * 获取验证码
//     */
//    private void getCode() {
//        String user_phone_number = etAccount.getText().toString().trim();
//        //验证电话号码不能为空
//        if (TextUtils.isEmpty(user_phone_number)) {
//            ToastUtil.show("请输入手机号");
//            return;
//        }
//        //验证手机号是否正确
//        if (!StringUtils.isMobile(user_phone_number)) {
//            ToastUtil.show("输入的手机号格式不正确");
//            return;
//        }
//        Map<String, Object> param = new HashMap<>();
//        param.put("mobile", user_phone_number);
//        mOkHttpHelper.post_json(getContext(), Url.getAuthCode, param, new SpotsCallBack<ResultBean>(getContext()) {
//            @Override
//            public void onSuccess(Response response, ResultBean resultBean) {
//                if (resultBean.getResult().equals("0")) {
//                    TimerUtil mTimerUtil = new TimerUtil(tvGetCode);
//                    mTimerUtil.timers();
//                    ToastUtil.show("验证码已发送，其注意查收");
//                } else
//                    ToastUtil.show(resultBean.getResultNote());
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//                ToastUtil.show(getString(R.string.httperror));
//            }
//        });
//    }
//
//    private void userRegister() {
//        if (TextUtils.isEmpty(etAccount.getText())) {
//            ToastUtil.show("请输入手机号码");
//            return;
//        }
//        if (TextUtils.isEmpty(etCode.getText())) {
//            ToastUtil.show("请输入验证码");
//            return;
//        }
//        if (TextUtils.isEmpty(etPsw.getText())) {
//            ToastUtil.show("请输入密码");
//            return;
//        }
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("mobile", etAccount.getText().toString());
//        params.put("authCode", etCode.getText().toString());
//        params.put("password", Md5.encode(etPsw.getText().toString()));
//        mOkHttpHelper.post_json(mContext, Url.retrievePassword, params, new SpotsCallBack<ResultBean>(mContext) {
//            @Override
//            public void onSuccess(Response response, ResultBean resultBean) {
//                act.finish();
//                ToastUtil.show("请重新登录");
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//
//            }
//
//        });
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
//            case R.id.tvGetCode:
//                checkPhone();
//                break;
//            case R.id.tvConfirm:
//                String inputCode = etCode.getText().toString().trim();
//                if (StringUtil.isEmpty(inputCode)) {
//                    ToastUtil.show("请获取验证码");
//                    return;
//                }
//                if (StringUtil.isEmpty(inputCode)) {
//                    ToastUtil.show("请输入验证码");
//                    return;
//                }
////                if (TextUtils.isEmpty(etInviteCode.getText())) {
////                    ToastUtil.show("请输入邀请码");
////                    return;
////                }
//                userRegister();
//                break;
        }
    }
}
