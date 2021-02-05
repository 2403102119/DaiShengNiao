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
import com.lxkj.dsn.utils.Md5;
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
 * Interface:意见反馈
 */
public class IdeaFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.etIdea)
    EditText etIdea;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.tvIssue)
    TextView tvIssue;

    @Override
    public String getTitleName() {
        return "意见反馈";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_idea, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        tvIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(etIdea.getText().toString())){
                    ToastUtil.show("请留下您宝贵的意见！");
                    return;
                }
                if (StringUtil.isEmpty(etPhone.getText().toString())){
                    ToastUtil.show("请留下您的联系方式");
                    return;
                }
                addfeedback();
            }
        });
    }

    //意见反馈
    private void addfeedback() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("content",etIdea.getText().toString());
        params.put("phone",etPhone.getText().toString());
        mOkHttpHelper.post_json(getContext(), Url.addfeedback, params, new BaseCallback<ResultBean>() {
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
                ToastUtil.show("意见反馈成功");
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
