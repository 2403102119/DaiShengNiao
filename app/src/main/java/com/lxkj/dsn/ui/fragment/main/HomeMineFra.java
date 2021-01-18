package com.lxkj.dsn.ui.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.biz.EventCenter;
import com.lxkj.dsn.ui.fragment.CachableFrg;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.lxkj.dsn.utils.ToastUtil;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**

 *Time:2020/10/28

 *Author:李迪迦

 *Description:我的

 */
public class HomeMineFra extends CachableFrg implements View.OnClickListener {

    Unbinder unbinder;


    @Override
    protected int rootLayout() {
        return R.layout.fra_mine;
    }

    @Override
    protected void initView() {



//        info();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @PermissionGrant(AppConsts.PMS_LOCATION)
    public void pmsLocationSuccess() {
        //权限授权成功
//        platformPhone();

    }

    @PermissionDenied(AppConsts.PMS_LOCATION)
    public void pmsLocationError() {
        ToastUtil.show("请设置电话权限");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onEvent(EventCenter.HcbEvent e) {
        super.onEvent(e);
        switch (e.type) {
            case EVT_EDITINFO:
                userId = SharePrefUtil.getString(getContext(), AppConsts.UID, "");
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
