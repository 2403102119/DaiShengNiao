package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.ui.fragment.login.LoginFra;
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
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/1/29
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:修改登录密码
 */
public class AmendFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.etOldPassword)
    EditText etOldPassword;
    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.tvEnter)
    TextView tvEnter;

    @Override
    public String getTitleName() {
        return "修改登陆密码";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_amend, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        tvEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(etOldPassword.getText().toString())){
                    ToastUtil.show("请输入旧密码");
                    return;
                }
                if (StringUtil.isEmpty(etNewPassword.getText().toString())){
                    ToastUtil.show("请输入新密码");
                    return;
                }
                if (etOldPassword.getText().toString().equals(etNewPassword.getText().toString())){
                    ToastUtil.show("新密码不能与旧密码一致");
                    return;
                }
                updatePassword();
            }
        });
    }

    //修改密码
    private void updatePassword() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("oldpassword",Md5.encode( etOldPassword.getText().toString()));
        params.put("password", Md5.encode(etNewPassword.getText().toString()));
        mOkHttpHelper.post_json(getContext(), Url.updatePassword, params, new BaseCallback<ResultBean>() {
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
                ToastUtil.show("密码修改成功，请重新登录");
                SharePrefUtil.saveString(mContext, AppConsts.UID, "");
                ActivitySwitcher.startFragment(act, LoginFra.class);
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
