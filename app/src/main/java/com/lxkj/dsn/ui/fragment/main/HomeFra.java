package com.lxkj.dsn.ui.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.BigClassAdapter;
import com.lxkj.dsn.adapter.ClassAdapter;
import com.lxkj.dsn.adapter.ProductAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.activity.MainActivity;
import com.lxkj.dsn.ui.fragment.CachableFrg;
import com.lxkj.dsn.ui.fragment.fra.ClassfiltyFra;
import com.lxkj.dsn.ui.fragment.fra.DetailFra;
import com.lxkj.dsn.ui.fragment.fra.MessageFra;
import com.lxkj.dsn.ui.fragment.fra.SearchFra;
import com.lxkj.dsn.utils.PicassoUtil;
import com.lxkj.dsn.utils.StringUtil;
import com.lzy.ninegrid.ImageInfo;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
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
 * Description:首页
 */
public class HomeFra extends CachableFrg implements View.OnClickListener {


    @BindView(R.id.et_seek)
    TextView etSeek;
    @BindView(R.id.tv_seek)
    TextView tvSeek;
    @BindView(R.id.imMessage)
    ImageView imMessage;
    @BindView(R.id.tvOrderCount)
    TextView tvOrderCount;
    @BindView(R.id.page_top)
    LinearLayout pageTop;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ryClass)
    RecyclerView ryClass;
    @BindView(R.id.ry_product)
    RecyclerView ryProduct;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    private ArrayList<DataListBean> listBeans = new ArrayList<>();
    private ArrayList<DataListBean> listBeansOne;
    private ArrayList<DataListBean> listBeansTwo = new ArrayList<>();
    private int page = 1, totalPage = 1;
    private ClassAdapter messageAdapter;
    private BigClassAdapter bigClassAdapter;
    private ProductAdapter productAdapter;
    private List<String> BanString = new ArrayList<>();
    private ArrayList<ImageInfo> imageInfo = new ArrayList<>();

    @Override
    protected int rootLayout() {
        return R.layout.fra_home;
    }

    @Override
    protected void initView() {

        smart.setEnableNestedScroll(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        listBeansOne = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        messageAdapter = new ClassAdapter(getContext(), listBeansOne);//首页一级分类
        messageAdapter.checked = false;
        recyclerView.setAdapter(messageAdapter);
        messageAdapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                if (listBeansOne.get(firstPosition).fid.equals("5")) {
                    ((MainActivity) getActivity()).mTabHost.setCurrentTab(1);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("position", firstPosition + "");
                    bundle.putString("type", "1");
                    ActivitySwitcher.startFragment(getActivity(), ClassfiltyFra.class, bundle);
                }
            }
        });

        ryClass.setHasFixedSize(true);
        ryClass.setNestedScrollingEnabled(false);
        ryClass.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        bigClassAdapter = new BigClassAdapter(getContext(), listBeansTwo);//首页二级分类
        ryClass.setAdapter(bigClassAdapter);
        bigClassAdapter.setOnItemClickListener(new BigClassAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                Bundle bundle = new Bundle();
                bundle.putString("position", firstPosition + "");
                bundle.putString("type", "2");
                ActivitySwitcher.startFragment(getActivity(), ClassfiltyFra.class, bundle);

            }
        });

//        ryProduct.setHasFixedSize(true);
        ryProduct.setNestedScrollingEnabled(false);
        ryProduct.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        productAdapter = new ProductAdapter(getContext(), listBeans);//首页推荐商品
        ryProduct.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {//商品详情
                Bundle bundle = new Bundle();
                bundle.putString("gid", listBeans.get(firstPosition).gid);
                ActivitySwitcher.startFragment(getActivity(), DetailFra.class, bundle);
            }
        });

        smart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page >= totalPage) {
                    refreshLayout.setNoMoreData(true);
                    return;
                }
                page++;
                gettuigoodslist();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                gettuigoodslist();
                refreshLayout.setNoMoreData(false);
            }
        });

        phoneRegister();
        getclassify1list();
        gettuiclassify2list();
        gettuigoodslist();

        imMessage.setOnClickListener(this);
        llSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imMessage://系统消息
                ActivitySwitcher.startFragment(getActivity(), MessageFra.class);
                break;
            case R.id.llSearch://搜索
                ActivitySwitcher.startFragment(getActivity(), SearchFra.class);
                break;
        }
    }


    //轮播图
    private void phoneRegister() {
        Map<String, Object> params = new HashMap<>();
        mOkHttpHelper.post_json(getContext(), Url.getbannerlist, params, new BaseCallback<ResultBean>() {
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
                if (null != resultBean.dataList) {
                    for (int i = 0; i < resultBean.dataList.size(); i++) {
                        BanString.add(resultBean.dataList.get(i).image);
                        ImageInfo info = new ImageInfo();
                        imageInfo.add(info);
                    }
                }
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
                                Bundle bundle = new Bundle();
                                bundle.putString("gid", listBeans.get(position).gid);
                                ActivitySwitcher.startFragment(getActivity(), DetailFra.class, bundle);
                            }
                        })
                        //填充数据
                        .execute(BanString);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //一级分类
    private void getclassify1list() {
        Map<String, Object> params = new HashMap<>();
        mOkHttpHelper.post_json(getContext(), Url.getclassify1list, params, new BaseCallback<ResultBean>() {
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
                listBeansOne.clear();
                listBeansOne.addAll(resultBean.dataList);
                messageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //首页推荐二级分类
    private void gettuiclassify2list() {
        Map<String, Object> params = new HashMap<>();
        mOkHttpHelper.post_json(getContext(), Url.gettuiclassify2list, params, new BaseCallback<ResultBean>() {
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
                listBeansTwo.clear();
                listBeansTwo.addAll(resultBean.dataList);
                bigClassAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //首页推荐商品
    private void gettuigoodslist() {
        Map<String, Object> params = new HashMap<>();
        params.put("nowPage", page);
        params.put("pageCount", "10");
        mOkHttpHelper.post_json(getContext(), Url.gettuigoodslist, params, new BaseCallback<ResultBean>() {
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
                if (!StringUtil.isEmpty(resultBean.totalPage))
                    totalPage = Integer.parseInt(resultBean.totalPage);
                smart.finishLoadMore();
                smart.finishRefresh();
                if (page == 1) {
                    listBeans.clear();
                    productAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    listBeans.addAll(resultBean.dataList);

                productAdapter.notifyDataSetChanged();

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
//        if (!StringUtil.isEmpty(userId))
//            home();
        getmessnum();
    }


}
