package com.lxkj.dsn.ui.fragment.fra;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.http.SpotsCallBack;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.APKVersionCodeUtils;
import com.lxkj.dsn.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Response;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * Time:2021/1/29
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:版本更新
 */
public class UpdateFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.tvverson)
    TextView tvverson;
    @BindView(R.id.tvJancha)
    TextView tvJancha;
    private int verCode ;
    @Override
    public String getTitleName() {
        return "检查更新";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_update, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        verCode = APKVersionCodeUtils.getVersionCode(getActivity());
        try {
            tvverson.setText("V" + getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvJancha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versionupdate();
            }
        });
    }

    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = act.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }


    /**
     * 获取版本更新
     */
    private void versionupdate() {
        Map<String, Object> params = new HashMap<>();
        mOkHttpHelper.post_json(mContext, Url.versionupdate, params, new SpotsCallBack<ResultBean>(getContext()) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                if (verCode<Integer.parseInt(resultBean.dataobject.num)){
                    AllenVersionChecker
                            .getInstance()
                            .downloadOnly(
                                    UIData.create().setDownloadUrl(resultBean.dataobject.apkurl).setTitle("提示").setContent(resultBean.dataobject.content)
                            ).setNotificationBuilder(
                            NotificationBuilder.create()
                                    .setRingtone(true)
                                    .setIcon(R.mipmap.logo)
                                    .setTicker(resultBean.dataobject.content)
                                    .setContentTitle(resultBean.dataobject.adtime)
                                    .setContentText("正在下载....")
                    ).setShowNotification(true).setShowDownloadingDialog(true).executeMission(getContext());
                }else{
                    ToastUtil.show("当前已是最新版本！");
                }

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
