package com.lxkj.dsn.ui.fragment.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.BigClassAdapter;
import com.lxkj.dsn.adapter.ClassAdapter;
import com.lxkj.dsn.adapter.ProductAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.ui.fragment.CachableFrg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;

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
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.im_xiaoxi)
    ImageView imXiaoxi;
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

    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private ClassAdapter messageAdapter;
    private BigClassAdapter bigClassAdapter;
    private ProductAdapter productAdapter;

    @Override
    protected int rootLayout() {
        return R.layout.fra_home;
    }

    @Override
    protected void initView() {

        smart.setEnableNestedScroll(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        messageAdapter = new ClassAdapter(getContext(), listBeans);
        recyclerView.setAdapter(messageAdapter);
        messageAdapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

            }
        });

        ryClass.setHasFixedSize(true);
        ryClass.setNestedScrollingEnabled(false);
        ryClass.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        bigClassAdapter = new BigClassAdapter(getContext(), listBeans);
        ryClass.setAdapter(bigClassAdapter);
        bigClassAdapter.setOnItemClickListener(new BigClassAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {


            }
        });
        ryProduct.setHasFixedSize(true);
        ryProduct.setNestedScrollingEnabled(false);
        ryProduct.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        productAdapter = new ProductAdapter(getContext(), listBeans);
        ryProduct.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {


            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (!StringUtil.isEmpty(userId))
//            home();
    }


}
