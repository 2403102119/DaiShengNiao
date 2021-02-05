package com.lxkj.dsn.ui.fragment.fra;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.PicassoUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ymex.widget.banner.Banner;
import cn.ymex.widget.banner.callback.BindViewCallBack;
import cn.ymex.widget.banner.callback.CreateViewCallBack;
import cn.ymex.widget.banner.callback.OnClickBannerListener;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/2/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class IntegralDeatilFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvOldPrice)
    TextView tvOldPrice;
    @BindView(R.id.tvSalenum)
    TextView tvSalenum;
    @BindView(R.id.tvSkunum)
    TextView tvSkunum;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.ll)
    LinearLayout ll;
    private String gid;
    private List<String> BanString = new ArrayList<>();
    private List<DataListBean> listDataBeans = new ArrayList<>();
    private DataListBean dataListBean = new DataListBean();
    @Override
    public String getTitleName() {
        return "商品详情";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_integralproductdetail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        gid = getArguments().getString("gid");

        tvBuy.setOnClickListener(this);


        getintegralgoodsdetail();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.tvBuy://立即兑换
                bundle.putSerializable("bean", (Serializable) dataListBean);
                ActivitySwitcher.startFragment(getActivity(), ConversionFra.class,bundle);
                break;

        }
    }

    //积分商品详情
    private void getintegralgoodsdetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("gid", gid);
        mOkHttpHelper.post_json(getContext(), Url.getintegralgoodsdetail, params, new BaseCallback<ResultBean>() {
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

                BanString.clear();
                BanString.addAll(resultBean.dataobject.images);
                banner  //创建布局
                        .createView(new CreateViewCallBack() {
                            @Override
                            public View createView(Context context, ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(context).inflate(R.layout.deatil_banner_page, null);
                                return view;
                            }
                        })
                        //布局处理
                        .bindView(new BindViewCallBack<View, String>() {
                            @Override
                            public void bindView(View view, String data, int position) {
                                RoundedImageView imageView = view.findViewById(R.id.iv_pic);
                                PicassoUtil.setImag(getContext(), data, imageView);
                            }
                        })
                        //点击事件
                        .setOnClickBannerListener(new OnClickBannerListener<View, String>() {
                            @Override
                            public void onClickBanner(View view, String data, int position) {

                            }
                        })
                        //填充数据
                        .execute(BanString);

                dataListBean.image = BanString.get(0);
                dataListBean.gid = resultBean.dataobject.gid;
                dataListBean.name = resultBean.dataobject.name;
                dataListBean.oldprice = resultBean.dataobject.oldprice;
                dataListBean.newprice = resultBean.dataobject.newprice;


                tvName.setText(resultBean.dataobject.name);
                tvPrice.setText(resultBean.dataobject.newprice);
                tvOldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tvOldPrice.setText("原价:¥ "+resultBean.dataobject.oldprice);
                tvSalenum.setText("已售："+resultBean.dataobject.salenum);
                tvSkunum.setText("库存量："+resultBean.dataobject.skunum);

                webSetting(resultBean.dataobject.url);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void webSetting(String url) {
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(false);// 支持缩放
//        webSettings.setTextSize(WebSettings.TextSize.LARGEST);//显示字体大小

        DisplayMetrics metrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }


/**
 * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
 * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
 */
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
