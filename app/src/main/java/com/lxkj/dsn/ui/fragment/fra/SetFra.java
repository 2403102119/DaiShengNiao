package com.lxkj.dsn.ui.fragment.fra;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.SpotsCallBack;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.ui.fragment.login.LoginFra;
import com.lxkj.dsn.utils.DataCleanManager;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.lxkj.dsn.utils.StringUtil;
import com.lxkj.dsn.utils.ToastUtil;
import com.lxkj.dsn.view.NormalDialog;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/1/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:设置
 */
public class SetFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.llAgreement)
    LinearLayout llAgreement;
    @BindView(R.id.llAmend)
    LinearLayout llAmend;
    @BindView(R.id.llIdea)
    LinearLayout llIdea;
    @BindView(R.id.llUpdate)
    LinearLayout llUpdate;
    @BindView(R.id.tvCacheData)
    TextView tvCacheData;
    @BindView(R.id.llClear)
    LinearLayout llClear;
    @BindView(R.id.llLought)
    LinearLayout llLought;
    @BindView(R.id.tvLogout)
    TextView tvLogout;

    private PopupWindow popupWindow;// 声明PopupWindow
    private LinearLayout ll_ll;

    @Override
    public String getTitleName() {
        return "设置";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_set, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        llAgreement.setOnClickListener(this);
        llAmend.setOnClickListener(this);
        llIdea.setOnClickListener(this);
        llUpdate.setOnClickListener(this);
        llClear.setOnClickListener(this);
        llLought.setOnClickListener(this);
        tvLogout.setOnClickListener(this);

        try {
            tvCacheData.setText(DataCleanManager.getTotalCacheSize(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAgreement://协议中心
                ActivitySwitcher.startFragment(getActivity(), AgreementFra.class);
                break;
            case R.id.llAmend://修改密码
                ActivitySwitcher.startFragment(getActivity(), AmendFra.class);
                break;
            case R.id.llIdea://意见反馈
                ActivitySwitcher.startFragment(getActivity(), IdeaFra.class);
                break;
            case R.id.llUpdate://版本更新
                ActivitySwitcher.startFragment(getActivity(), UpdateFra.class);
                break;
            case R.id.llClear://清理缓存
                NormalDialog dialog1 = new NormalDialog(mContext, "您确定要清理缓存吗？", "取消", "确定", true);
                dialog1.show();
                dialog1.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                    @Override
                    public void OnRightClick() {
                        ToastUtil.show("清空缓存成功！");
                        DataCleanManager.clearAllCache(mContext);
                        tvCacheData.setText("0.0KB");
                    }

                    @Override
                    public void OnLeftClick() {
                    }
                });

                break;
            case R.id.llLought://注销账号
                NormalDialog dialog = new NormalDialog(mContext, "您确定要注销账号吗？", "取消", "确定", true);
                dialog.show();
                dialog.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                    @Override
                    public void OnRightClick() {
                        getValidateCode();
                    }

                    @Override
                    public void OnLeftClick() {

                    }
                });
                break;
            case R.id.tvLogout://退出登录
                NormalDialog dialog3 = new NormalDialog(mContext, "退出登录", "取消", "确定", true);
                dialog3.show();
                dialog3.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                    @Override
                    public void OnRightClick() {
                        outlogin();
                    }

                    @Override
                    public void OnLeftClick() {

                    }
                });
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void getValidateCode() {
        Map<String, Object> param = new HashMap<>();
        param.put("phone", SharePrefUtil.getString(getContext(), AppConsts.PHONE, ""));
        mOkHttpHelper.post_json(getContext(), Url.getValidateCode, param, new SpotsCallBack<ResultBean>(getContext()) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                if (resultBean.getResult().equals("0")) {
                    ToastUtil.show("验证码已发送，其注意查收");
                    state();
                    ll_ll.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.activity_translate_in));
                    popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
                } else
                    ToastUtil.show(resultBean.getResultNote());
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtil.show(getString(R.string.httperror));
            }
        });
    }

    //注销用户
    private void deletemember(String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("code", code);
        mOkHttpHelper.post_json(getContext(), Url.deletemember, params, new BaseCallback<ResultBean>() {
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
                ToastUtil.show("注销成功");
                SharePrefUtil.saveString(mContext, AppConsts.UID, "");
                ActivitySwitcher.startFragment(act, LoginFra.class);
                act.finish();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    //退出登录(清空token不在推送)
    private void outlogin() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        mOkHttpHelper.post_json(getContext(), Url.outlogin, params, new BaseCallback<ResultBean>() {
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
                SharePrefUtil.saveString(mContext, AppConsts.UID, "");
                ActivitySwitcher.startFragment(act, LoginFra.class);
                act.finish();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /*
     * 验证码
     * */
    public void state() {
        popupWindow = new PopupWindow(getActivity());
        View view = getLayoutInflater().inflate(R.layout.popup_code, null);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setClippingEnabled(false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        ll_ll = view.findViewById(R.id.ll_ll);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView tvEnsure = view.findViewById(R.id.tvEnsure);
        EditText etCode = view.findViewById(R.id.etCode);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                ll_ll.clearAnimation();
            }
        });
        tvEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(etCode.getText().toString())) {
                    ToastUtil.show("请填写登录账号收到的验证码");
                    return;
                }
                deletemember(etCode.getText().toString());
                popupWindow.dismiss();
                ll_ll.clearAnimation();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
