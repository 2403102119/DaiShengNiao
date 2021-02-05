package com.lxkj.dsn.ui.fragment.fra;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.PayResult;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.bean.WxPayBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.biz.EventCenter;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.SpotsCallBack;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.beecloud.BCPay;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/2/4
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:支付
 */
public class PayFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.cb_Alipay)
    CheckBox cbAlipay;
    @BindView(R.id.cb_WeChat)
    CheckBox cbWeChat;
    @BindView(R.id.cb_Yue)
    CheckBox cbYue;
    @BindView(R.id.tvPay)
    TextView tvPay;

    private String money,payMethod,ordernum;
    private ProgressDialog loadingDialog;

    @Override
    public String getTitleName() {
        return "选择付款方式";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_pay, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        money = getArguments().getString("money");
        ordernum = getArguments().getString("ordernum");
        tvMoney.setText(money);

        loadingDialog = new ProgressDialog(getContext());
        loadingDialog.setMessage("处理中，请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);

        cbAlipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {//支付宝
                if (b) {
                    payMethod = "1";
                    cbWeChat.setChecked(false);
                    cbYue.setChecked(false);
                }

            }
        });
        cbWeChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {//微信
                if (b) {
                    payMethod = "2";
                    cbAlipay.setChecked(false);
                    cbYue.setChecked(false);
                }
            }
        });
        cbYue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {//余额
                if (b) {
                    payMethod = "3";
                    cbAlipay.setChecked(false);
                    cbWeChat.setChecked(false);
                }
            }
        });

        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });

    }

    /**
     * 支付
     */
    private void pay() {
        if (null == payMethod) {
            ToastUtil.show("请选择支付方式！");
            return;
        }
        switch (payMethod) {
            case "2": //微信
                // 如果用到微信支付，在用到微信支付的Activity的onCreate函数里调用以下函数.
                // 第二个参数需要换成你自己的微信AppID.
                String initInfo = BCPay.initWechatPay(getContext(), AppConsts.WXAPPID);
                if (initInfo != null) {
                    ToastUtil.show("微信初始化失败：" + initInfo);
                }
                weChatPay();
                break;
            case "1": //支付宝支付
                zhifubaopay();
                break;
            case "3"://余额支付
                balancepay();
                break;
        }
    }


    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            Log.e("handleMessage", msg.what + "");
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.show("支付成功！");
                        eventCenter.sendType(EventCenter.EventType.EVT_PaySuccess);

                        ActivitySwitcher.startFragment(getActivity(), PayOkFra.class);
//                        act.finishSelf();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show("支付失败！");
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 支付宝支付
     */
    private void zhifubaopay() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("ordernum", ordernum);
        params.put("money", money);
        mOkHttpHelper.post_json(getContext(), Url.zhifubaopay, params, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                loadingDialog.show();
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(act);
                        Map<String, String> result = alipay.payV2(resultBean.body, true);
                        Message msg = new Message();
                        msg.obj = result;
                        msg.what = SDK_PAY_FLAG;
                        mHandler.sendMessage(msg);
                    }
                };
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 微信支付
     */
    private void weChatPay() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("ordernum", ordernum);
        params.put("money", money);
        mOkHttpHelper.post_json(getContext(), Url.weixinpay, params, new BaseCallback<WxPayBean>() {
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
            public void onSuccess(Response response, WxPayBean resultBean) {
                IWXAPI api = WXAPIFactory.createWXAPI(mContext, null);
                PayReq request = new PayReq();
                request.appId = resultBean.getBody().getAppid();
                request.partnerId = resultBean.getBody().getPartnerid();
                request.prepayId = resultBean.getBody().getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr = resultBean.getBody().getNoncestr();
                request.timeStamp = resultBean.getBody().getTimestamp();
                request.sign = resultBean.getBody().getSign();
                api.sendReq(request);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }
    /**
     * 余额支付
     */
    private void balancepay() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("ordernum", ordernum);
        params.put("money", money);
        mOkHttpHelper.post_json(getContext(), Url.balancepay, params, new BaseCallback<WxPayBean>() {
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
            public void onSuccess(Response response, WxPayBean resultBean) {
                ActivitySwitcher.startFragment(getActivity(), PayOkFra.class);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (loadingDialog.isShowing())
            loadingDialog.dismiss();

    }

    @Override
    public void onEvent(EventCenter.HcbEvent e) {
        super.onEvent(e);
        if (e.type.equals(EventCenter.EventType.EVT_WxPay)) {
            eventCenter.sendType(EventCenter.EventType.EVT_PaySuccess);
            ActivitySwitcher.startFragment(getActivity(), PayOkFra.class);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
