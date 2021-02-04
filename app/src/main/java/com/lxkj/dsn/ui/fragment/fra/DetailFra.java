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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.ProductAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.activity.NaviActivity;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.PicassoUtil;
import com.lxkj.dsn.utils.ToastUtil;
import com.lzy.ninegrid.ImageInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
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
 * Time:2021/1/29
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:商品详情
 */
public class DetailFra extends TitleFragment implements NaviActivity.NaviRigthImageListener, View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llService)
    LinearLayout llService;
    @BindView(R.id.imEnshrine)
    ImageView imEnshrine;
    @BindView(R.id.tvEnshrine)
    TextView tvEnshrine;
    @BindView(R.id.llEnshrine)
    LinearLayout llEnshrine;
    @BindView(R.id.tvAddCar)
    TextView tvAddCar;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.llAuthor)
    LinearLayout llAuthor;
    @BindView(R.id.llComment)
    LinearLayout llComment;
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
    @BindView(R.id.tvCommnum)
    TextView tvCommnum;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.riAimage)
    RoundedImageView riAimage;
    @BindView(R.id.tvAiNme)
    TextView tvAiNme;
    @BindView(R.id.tvAbirthday)
    TextView tvAbirthday;
    @BindView(R.id.tvAschool)
    TextView tvAschool;

    private ProductAdapter productAdapter;
    private List<DataListBean> listBeans = new ArrayList<>();
    private List<DataListBean> listDataBeans = new ArrayList<>();
    private String gid,aid,iscoll;
    private List<String> BanString = new ArrayList<>();
    private DataListBean dataListBean = new DataListBean();


    @Override
    public String getTitleName() {
        return "商品详情";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        gid = getArguments().getString("gid");

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        productAdapter = new ProductAdapter(getContext(), listBeans);
        recyclerView.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {//商品详情
                Bundle bundle = new Bundle();
                bundle.putString("gid", listBeans.get(firstPosition).gid);
                ActivitySwitcher.startFragment(getActivity(), DetailFra.class, bundle);
            }
        });

        llAuthor.setOnClickListener(this);
        llComment.setOnClickListener(this);
        tvBuy.setOnClickListener(this);
        llEnshrine.setOnClickListener(this);
        tvAddCar.setOnClickListener(this);

        getgoodsdetail();
    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.llAuthor://作者详情
                bundle.putString("aid",aid);
                ActivitySwitcher.startFragment(getActivity(), AuthorFra.class,bundle);
                break;
            case R.id.llComment://评论列表
                bundle.putString("gid",gid);
                ActivitySwitcher.startFragment(getActivity(), EvaluateDetalFra.class,bundle);
                break;
            case R.id.tvBuy://购买
                listDataBeans.clear();
                listDataBeans.add(dataListBean);
                bundle.putSerializable("bean", (Serializable) listDataBeans);
                bundle.putString("type", "0");
                ActivitySwitcher.startFragment(getActivity(), AffirmOrderFra.class,bundle);
                break;
            case R.id.llEnshrine://收藏
                addgoodscoll();
                break;
            case R.id.tvAddCar://添加购物车
                addgoodscar();
                break;
        }
    }


    //商品详情
    private void getgoodsdetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("gid", gid);
        mOkHttpHelper.post_json(getContext(), Url.getgoodsdetail, params, new BaseCallback<ResultBean>() {
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
                                View view = LayoutInflater.from(context).inflate(R.layout.custom_banner_page, null);
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
                dataListBean.numbers = "1";
                dataListBean.type = "1";

                aid = resultBean.dataobject.aid;
                tvName.setText(resultBean.dataobject.name);
                tvPrice.setText(resultBean.dataobject.newprice);
                tvOldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tvOldPrice.setText("原价:¥ "+resultBean.dataobject.oldprice);
                tvSalenum.setText("已售："+resultBean.dataobject.salenum);
                tvSkunum.setText("库存量："+resultBean.dataobject.skunum);
                tvCommnum.setText(resultBean.dataobject.commnum+"条");
                webSetting(resultBean.dataobject.url);
                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.logo)
                        .placeholder(R.mipmap.logo))
                        .load(resultBean.dataobject.aimage)
                        .into(riAimage);
                tvAiNme.setText(resultBean.dataobject.aname);
                tvAbirthday.setText("出生年月："+resultBean.dataobject.abirthday);
                tvAschool.setText("毕业院校："+resultBean.dataobject.aschool);
                listBeans.clear();
                listBeans.addAll(resultBean.dataList);
                productAdapter.notifyDataSetChanged();
                iscoll = resultBean.dataobject.iscoll;
                if (resultBean.dataobject.iscoll.equals("1")){
                    imEnshrine.setImageResource(R.mipmap.yishoucang);
                    tvEnshrine.setText("已收藏");
                }else {
                    imEnshrine.setImageResource(R.mipmap.shoucang);
                    tvEnshrine.setText("收藏");
                }

                if (resultBean.dataobject.type.equals("0")){//普通商品
                    tvAddCar.setVisibility(View.VISIBLE);
                }else {
                    tvAddCar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //商品收藏
    private void addgoodscoll() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("gid", gid);
        mOkHttpHelper.post_json(getContext(), Url.addgoodscoll, params, new BaseCallback<ResultBean>() {
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

                if (iscoll.equals("0")){
                    iscoll = "1";
                    imEnshrine.setImageResource(R.mipmap.yishoucang);
                    tvEnshrine.setText("已收藏");
                }else {
                    iscoll = "0";
                    imEnshrine.setImageResource(R.mipmap.shoucang);
                    tvEnshrine.setText("收藏");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    //添加购物车
    private void addgoodscar() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("gid", gid);
        params.put("numbers", "1");
        mOkHttpHelper.post_json(getContext(), Url.addgoodscar, params, new BaseCallback<ResultBean>() {
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

                ToastUtil.show(resultBean.resultNote);
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

    @Override
    public int rightImg() {
        return R.mipmap.fenxiang;
    }

    @Override
    public void onRightClicked(View v) {

    }
}
