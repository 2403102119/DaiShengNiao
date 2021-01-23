package com.lxkj.dsn.ui.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.biz.EventCenter;
import com.lxkj.dsn.ui.fragment.CachableFrg;
import com.lxkj.dsn.ui.fragment.fra.ColletFra;
import com.lxkj.dsn.ui.fragment.fra.FoundFra;
import com.lxkj.dsn.ui.fragment.fra.IntegralFra;
import com.lxkj.dsn.ui.fragment.fra.MessageFra;
import com.lxkj.dsn.ui.fragment.fra.MyIssueFra;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.lxkj.dsn.utils.ToastUtil;
import com.lzy.ninegrid.ImageInfo;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ymex.widget.banner.Banner;
import cn.ymex.widget.banner.callback.BindViewCallBack;
import cn.ymex.widget.banner.callback.CreateViewCallBack;
import cn.ymex.widget.banner.callback.OnClickBannerListener;

/**
 * Time:2020/10/28
 * <p>
 * Author:李迪迦
 * <p>
 * Description:我的
 */
public class HomeMineFra extends CachableFrg implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvMotto)
    TextView tvMotto;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.llIntegral)
    LinearLayout llIntegral;
    @BindView(R.id.imMessage)
    ImageView imMessage;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.llFound)
    LinearLayout llFound;
    @BindView(R.id.llIssue)
    LinearLayout llIssue;
    @BindView(R.id.llCollet)
    LinearLayout llCollet;
    private List<DataListBean> BanString = new ArrayList<>();
    private ArrayList<ImageInfo> imageInfo = new ArrayList<>();

    @Override
    protected int rootLayout() {
        return R.layout.fra_mine;
    }

    @Override
    protected void initView() {

        llIntegral.setOnClickListener(this);
        imMessage.setOnClickListener(this);
        llFound.setOnClickListener(this);
        llIssue.setOnClickListener(this);
        llCollet.setOnClickListener(this);

        DataListBean dataListBean = new DataListBean();
        BanString.clear();
//        if (null != resultBean.bannerList) {
//            for (int i = 0; i < resultBean.bannerList.size(); i++) {
//                BanString.add(resultBean.bannerList.get(i).image);
//                ImageInfo info = new ImageInfo();
//                imageInfo.add(info);
//            }
//        }
        BanString.add(dataListBean);
        BanString.add(dataListBean);
        banner  //创建布局
                .createView(new CreateViewCallBack() {
                    @Override
                    public View createView(Context context, ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(context).inflate(R.layout.logistics, null);
                        return view;
                    }
                })
                //布局处理
                .bindView(new BindViewCallBack<View, DataListBean>() {
                    @Override
                    public void bindView(View view, DataListBean data, int position) {
                        TextView tvWuliu = view.findViewById(R.id.tvWuliu);
                        TextView tvTime = view.findViewById(R.id.tvTime);
                        TextView tvTitle = view.findViewById(R.id.tvTitle);
                        TextView tvContent = view.findViewById(R.id.tvContent);
                        tvWuliu.setText("最新物流");
                        tvTime.setText("12-21");
                        tvTitle.setText("运输中");
                        tvContent.setText("【开封市】开封派件员：君临天下驿站的 电…");
                        RoundedImageView imageView = view.findViewById(R.id.imAvatar);
                        Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                                .error(R.mipmap.logo)
                                .placeholder(R.mipmap.logo))
                                .load(data.image)
                                .into(imageView);
                    }
                })
                //点击事件
                .setOnClickBannerListener(new OnClickBannerListener<View, DataListBean>() {
                    @Override
                    public void onClickBanner(View view, DataListBean data, int position) {

                    }
                })
                //填充数据
                .execute(BanString);
        Log.i("TAG", "initView: " + BanString.size());

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llIntegral://积分商城
                ActivitySwitcher.startFragment(getActivity(), IntegralFra.class);
                break;
            case R.id.imMessage://系统消息
                ActivitySwitcher.startFragment(getActivity(), MessageFra.class);
                break;
            case R.id.llFound://资金账户
                ActivitySwitcher.startFragment(getActivity(), FoundFra.class);
                break;
            case R.id.llIssue://我的发布
                ActivitySwitcher.startFragment(getActivity(), MyIssueFra.class);
                break;
            case R.id.llCollet://我的收藏
                ActivitySwitcher.startFragment(getActivity(), ColletFra.class);
                break;
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
