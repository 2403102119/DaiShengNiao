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
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.activity.MainActivity;
import com.lxkj.dsn.ui.fragment.CachableFrg;
import com.lxkj.dsn.ui.fragment.fra.ColletFra;
import com.lxkj.dsn.ui.fragment.fra.FoundFra;
import com.lxkj.dsn.ui.fragment.fra.IntegralFra;
import com.lxkj.dsn.ui.fragment.fra.InviteFra;
import com.lxkj.dsn.ui.fragment.fra.MessageFra;
import com.lxkj.dsn.ui.fragment.fra.MyIssueFra;
import com.lxkj.dsn.ui.fragment.fra.OrderFra;
import com.lxkj.dsn.ui.fragment.fra.SetFra;
import com.lxkj.dsn.ui.fragment.fra.UserFra;
import com.lxkj.dsn.ui.fragment.system.WebFra;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.lxkj.dsn.utils.StringUtil;
import com.lxkj.dsn.utils.ToastUtil;
import com.lzy.ninegrid.ImageInfo;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.Unbinder;
import cn.ymex.widget.banner.Banner;
import cn.ymex.widget.banner.callback.BindViewCallBack;
import cn.ymex.widget.banner.callback.CreateViewCallBack;
import cn.ymex.widget.banner.callback.OnClickBannerListener;
import okhttp3.Request;
import okhttp3.Response;

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
    @BindView(R.id.llInvite)
    LinearLayout llInvite;
    @BindView(R.id.imSet)
    ImageView imSet;
    @BindView(R.id.llUser)
    LinearLayout llUser;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvOrderCount)
    TextView tvOrderCount;
    @BindView(R.id.llDaifukuan)
    LinearLayout llDaifukuan;
    @BindView(R.id.llDaifahuo)
    LinearLayout llDaifahuo;
    @BindView(R.id.llDaishouhuo)
    LinearLayout llDaishouhuo;
    @BindView(R.id.llDaipingjia)
    LinearLayout llDaipingjia;
    @BindView(R.id.llTuikuan)
    LinearLayout llTuikuan;
    @BindView(R.id.llVip)
    LinearLayout llVip;
    @BindView(R.id.llKefu)
    LinearLayout llKefu;
    private List<DataListBean> BanString = new ArrayList<>();
    private ArrayList<ImageInfo> imageInfo = new ArrayList<>();
    private String balance, integral, invitationcode;

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
        llInvite.setOnClickListener(this);
        imSet.setOnClickListener(this);
        llUser.setOnClickListener(this);
        tvAll.setOnClickListener(this);
        llDaifukuan.setOnClickListener(this);
        llDaifahuo.setOnClickListener(this);
        llDaishouhuo.setOnClickListener(this);
        llDaipingjia.setOnClickListener(this);
        llTuikuan.setOnClickListener(this);
        llVip.setOnClickListener(this);
        llKefu.setOnClickListener(this);

        DataListBean dataListBean = new DataListBean();
        BanString.clear();
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
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.llIntegral://积分商城
                bundle.putString("balance", integral);
                ActivitySwitcher.startFragment(getActivity(), IntegralFra.class, bundle);
                break;
            case R.id.imMessage://系统消息
                ActivitySwitcher.startFragment(getActivity(), MessageFra.class);
                break;
            case R.id.llFound://资金账户
                bundle.putString("integral", balance);
                ActivitySwitcher.startFragment(getActivity(), FoundFra.class, bundle);
                break;
            case R.id.llIssue://我的发布
                ActivitySwitcher.startFragment(getActivity(), MyIssueFra.class);
                break;
            case R.id.llCollet://我的收藏
                ActivitySwitcher.startFragment(getActivity(), ColletFra.class);
                break;
            case R.id.llInvite://我的邀请
                bundle.putString("invitationcode", invitationcode);
                ActivitySwitcher.startFragment(getActivity(), InviteFra.class, bundle);
                break;
            case R.id.imSet://设置
                ActivitySwitcher.startFragment(getActivity(), SetFra.class);
                break;
            case R.id.llUser://个人中心
                ActivitySwitcher.startFragment(getActivity(), UserFra.class);
                break;
            case R.id.tv_all://全部订单
                bundle.putString("state", "0");
                ActivitySwitcher.startFragment(getActivity(), OrderFra.class, bundle);
                break;
            case R.id.llDaifukuan://待付款
                bundle.putString("state", "1");
                ActivitySwitcher.startFragment(getActivity(), OrderFra.class, bundle);
                break;
            case R.id.llDaifahuo://待发货
                bundle.putString("state", "2");
                ActivitySwitcher.startFragment(getActivity(), OrderFra.class, bundle);
                break;
            case R.id.llDaishouhuo://待收货
                bundle.putString("state", "3");
                ActivitySwitcher.startFragment(getActivity(), OrderFra.class, bundle);
                break;
            case R.id.llDaipingjia://待评价
                bundle.putString("state", "4");
                ActivitySwitcher.startFragment(getActivity(), OrderFra.class, bundle);
                break;
            case R.id.llTuikuan://退款
                bundle.putString("state", "5");
                ActivitySwitcher.startFragment(getActivity(), OrderFra.class, bundle);
                break;
            case R.id.llVip:
                bundle.putString("title", "会员中心");
                bundle.putString("url", "http://8.140.109.101/daishengniao/display/agreement?id=4");
                ActivitySwitcher.startFragment(getContext(), WebFra.class, bundle);
                break;
            case R.id.llKefu://客服热线
                bundle.putString("title", "客服热线");
                bundle.putString("url", "http://w10.ttkefu.com/k/linkurl/?t=4C2CFJI5");
                ActivitySwitcher.startFragment(getContext(), WebFra.class, bundle);
                break;
        }
    }


    //用户个人信息
    private void memberinfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        mOkHttpHelper.post_json(getContext(), Url.memberinfo, params, new BaseCallback<ResultBean>() {
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

                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.touxiang)
                        .placeholder(R.mipmap.touxiang))
                        .load(resultBean.dataobject.icon)
                        .into(riIcon);
                tvName.setText(resultBean.dataobject.nickname);
                tvMotto.setText("个性签名：" + resultBean.dataobject.autograph);
                tvBalance.setText("¥" + resultBean.dataobject.balance);
                integral = resultBean.dataobject.integral;
                balance = resultBean.dataobject.balance;
                invitationcode = resultBean.dataobject.invitationcode;
                SharePrefUtil.saveString(getContext(), AppConsts.ismember, resultBean.dataobject.ismember);
                SharePrefUtil.saveString(getContext(), AppConsts.invitationcode, resultBean.dataobject.invitationcode);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //获取未读消息数量
    private void getmessnum() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        mOkHttpHelper.post_json(getContext(), Url.getmessnum, params, new BaseCallback<ResultBean>() {
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
                if (StringUtil.isEmpty(resultBean.datastr) || resultBean.datastr.equals("0")) {
                    tvOrderCount.setVisibility(View.GONE);
                } else {
                    tvOrderCount.setVisibility(View.VISIBLE);
                    tvOrderCount.setText(resultBean.datastr);
                }


            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        memberinfo();
        getmessnum();
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
}
