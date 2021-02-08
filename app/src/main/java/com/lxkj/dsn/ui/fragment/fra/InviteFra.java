package com.lxkj.dsn.ui.fragment.fra;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.actlink.NaviRightListener;
import com.lxkj.dsn.adapter.InviteAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.ui.fragment.dialog.ShareFra;
import com.lxkj.dsn.ui.fragment.system.WebFra;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.mylhyl.zxing.scanner.encode.QREncode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/1/25
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:我的邀请
 */
public class InviteFra extends TitleFragment implements NaviRightListener, View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvShouyi)
    TextView tvShouyi;
    @BindView(R.id.tvLeijishouji)
    TextView tvLeijishouji;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvYaoqingma)
    TextView tvYaoqingma;
    @BindView(R.id.tvLijiyaoqing)
    TextView tvLijiyaoqing;
    @BindView(R.id.imCode)
    ImageView imCode;
    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private InviteAdapter inviteAdapter;
    private String invitationcode;

    @Override
    public String getTitleName() {
        return "我的邀请";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_invite, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        invitationcode = getArguments().getString("invitationcode");

        tvYaoqingma.setText("邀请码：" + invitationcode);

       Bitmap qrCode = new QREncode.Builder(getContext())
                .setColor(getResources().getColor(R.color.colorBlack))//二维码颜色
                .setContents("http://app.daishengbook.com/h5/#/pages/registe/index?invitationcode=" + SharePrefUtil.getString(getContext(), AppConsts.invitationcode, null) + "&type=1")//二维码内容
                .build().encodeAsBitmap();

        Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.logo)
                .placeholder(R.mipmap.logo))
                .load(qrCode)
                .into(imCode);

        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        inviteAdapter = new InviteAdapter(getContext(), listBeans);
        recyclerView.setAdapter(inviteAdapter);
        inviteAdapter.setOnItemClickListener(new InviteAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                Bundle bundle = new Bundle();
                bundle.putString("userid", listBeans.get(firstPosition).userid);
                bundle.putString("title", listBeans.get(firstPosition).username);
                ActivitySwitcher.startFragment(getActivity(), ConsumeFra.class, bundle);
            }
        });

        tvAll.setOnClickListener(this);
        tvLijiyaoqing.setOnClickListener(this);

        myfirstinvitationlist();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAll://查看全部
                ActivitySwitcher.startFragment(getActivity(), AllFriendFra.class);
                break;
            case R.id.tvLijiyaoqing://立即邀请
                AppConsts.SHAREDES = "欢迎使用戴胜鸟图书";
                AppConsts.FENGMIAN = "";
                AppConsts.miaoshu = "欢迎使用戴胜鸟图书";
                AppConsts.SHAREURL = "http://app.daishengbook.com/h5/#/pages/registe/index?invitationcode=" + SharePrefUtil.getString(getContext(), AppConsts.invitationcode, null) + "&type=1";
                new ShareFra().show(getFragmentManager(), "Menu");
                break;
        }
    }

    //我的邀请
    private void myfirstinvitationlist() {
        Map<String, Object> params = new HashMap<>();
        params.put("nowPage", page);
        params.put("uid", userId);
        params.put("pageCount", "10");
        mOkHttpHelper.post_json(getContext(), Url.myfirstinvitationlist, params, new BaseCallback<ResultBean>() {
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
                tvShouyi.setText("今日收益：" + resultBean.daymoney);
                tvLeijishouji.setText("累计收益：" + resultBean.allmoney);
                tvNumber.setText("我的好友（" + resultBean.allnum + "）");
                listBeans.clear();
                if (resultBean.dataList.size() > 3) {
                    for (int i = 0; i < 3; i++) {
                        listBeans.add(resultBean.dataList.get(i));
                    }
                } else {
                    listBeans.addAll(resultBean.dataList);
                }

                inviteAdapter.notifyDataSetChanged();

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

    @Override
    public String rightText() {
        return "邀请规则";
    }

    @Override
    public void onRightClicked(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("title", "邀请规则");
        bundle.putString("url", "http://8.140.109.101/daishengniao/display/agreement?id=7");
        ActivitySwitcher.startFragment(getContext(), WebFra.class, bundle);
    }
}
