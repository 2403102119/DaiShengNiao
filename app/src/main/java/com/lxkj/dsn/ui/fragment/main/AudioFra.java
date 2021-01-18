package com.lxkj.dsn.ui.fragment.main;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.ProductAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.ui.fragment.CachableFrg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;

/**
 * Time:2020/8/24
 * <p>
 * Author:李迪迦
 * <p>
 * Description:影音书
 */
public class AudioFra extends CachableFrg implements View.OnClickListener {


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
    @BindView(R.id.llArea)
    LinearLayout llArea;
    @BindView(R.id.llClass)
    LinearLayout llClass;
    @BindView(R.id.imJuli)
    ImageView imJuli;
    @BindView(R.id.llSpecification)
    LinearLayout llSpecification;
    @BindView(R.id.llScreen)
    LinearLayout llScreen;
    @BindView(R.id.ryProduct)
    RecyclerView ryProduct;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.TagFlow)
    TagFlowLayout TagFlow;
    private ArrayList<DataListBean> listBeans = new ArrayList<>();

    private ProductAdapter productAdapter;
    private TagAdapter<String> adapter;
    private List<String> hot_list = new ArrayList<>();
    @Override
    protected int rootLayout() {
        return R.layout.fra_nearby;
    }

    @Override
    protected void initView() {
        hot_list.clear();
        hot_list.add("启蒙教育");
        hot_list.add("儿童文学");
        hot_list.add("科普百科");
        hot_list.add("教辅题材");

        adapter = new TagAdapter<String>(hot_list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_choose, parent, false);
                view.setText(s);
                return view;
            }
        };
        adapter.setSelectedList(0);//默认选中第一个
        TagFlow.setAdapter(adapter);

        TagFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                return true;
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

}
