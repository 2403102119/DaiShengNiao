package com.lxkj.dsn.ui.fragment.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
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
 * Time:2021/2/4
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:取消订单
 */
public class QuxiaoOrderFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.tvQueding)
    TextView tvQueding;
    @BindView(R.id.etReason)
    EditText etReason;
    private String ordernum;

    @Override
    public String getTitleName() {
        return "取消原因";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_quxiao, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        ordernum = getArguments().getString("ordernum");

        tvQueding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (StringUtil.isEmpty(etReason.getText().toString())){
                ToastUtil.show("请输入取消原因");
                return;
            }
                ordercancel();
            }
        });
    }

    /**
     * 取消订单
     */
    private void ordercancel() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("ordernum",ordernum);
        params.put("reason", etReason.getText().toString());
        mOkHttpHelper.post_json(getContext(), Url.ordercancel, params, new BaseCallback<ResultBean>() {
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

                  act.finishSelf();
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
