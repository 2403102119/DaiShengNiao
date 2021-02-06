package com.lxkj.dsn.ui.fragment.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.List;
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
    private UMShareAPI mShareAPI;
    private String threeLoginType = "0", thirdUid, nickName, userIcon;
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

        mShareAPI = UMShareAPI.get(mContext);


        tvRegister.setOnClickListener(this);
        tvRetrieve.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvYonghu.setOnClickListener(this);
        imEys.setOnClickListener(this);
        imWeChat.setOnClickListener(this);
        imQQ.setOnClickListener(this);

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
            case R.id.imWeChat://微信登录
                threeLoginType = "0";
                if (!isWeixinAvilible(mContext)) {
                    ToastUtil.show("请安装微信客户端");
                    return;
                }
                ToastUtil.show("正在跳转微信登录...");
                UMShareAPI.get(mContext).doOauthVerify(getActivity(), SHARE_MEDIA.WEIXIN, umOauthListener);
                break;
            case R.id.imQQ://QQ登录
                threeLoginType = "1";
                ToastUtil.show("正在跳转QQ登录...");
                UMShareAPI.get(mContext).doOauthVerify(getActivity(), SHARE_MEDIA.QQ, umOauthListener);
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

    /**
     * 第三方登录
     *
     * @param thirdUid
     */
    private void threeLogin(String thirdUid) {
        Map<String, Object> params = new HashMap<>();
        params.put("thirdid", thirdUid);
        params.put("threetype", threeLoginType);
        params.put("icon", userIcon);
        params.put("nickname", nickName);
        params.put("token", JPushInterface.getRegistrationID(mContext));//推送标识
        mOkHttpHelper.post_json(mContext, Url.threeLogin, params, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                    if (resultBean.isnewuser.equals("0")) {//首次登录
                        Bundle bundle = new Bundle();
                        bundle.putString("type", threeLoginType);
                        bundle.putString("openId", thirdUid);
                        bundle.putString("icon", userIcon);
                        bundle.putString("nickname", nickName);
                        ActivitySwitcher.startFragment(act, ThreeLoginFra.class,bundle);
                    } else {
                        SharePrefUtil.saveString(mContext, AppConsts.UID, resultBean.uid);
                        ActivitySwitcher.start(act, MainActivity.class);
                        act.finishSelf();
                    }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }


    /**
     * 授权监听
     */
    private UMAuthListener umOauthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.i("onStart", "onStart: ");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (SHARE_MEDIA.QQ.equals(share_media))
                mShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.QQ, umAuthListener);
            else if (SHARE_MEDIA.WEIXIN.equals(share_media))
                mShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, umAuthListener);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Log.i("onError", "onError: " + "授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Log.i("onCancel", "onCancel: " + "授权取消");
        }
    };

    /**
     * 登陆监听
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.i("onStart", "onStart: ");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            nickName = map.get("name");//昵称
            userIcon = map.get("iconurl");//头像
            thirdUid = map.get("openid");//第三方平台id
            threeLogin(thirdUid);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Log.i("onError", "onError: " + "授權失敗");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Log.i("onCancel", "onCancel: " + "授權取消");
        }
    };

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
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
