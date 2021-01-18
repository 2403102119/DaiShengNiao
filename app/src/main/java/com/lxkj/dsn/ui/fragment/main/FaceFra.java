package com.lxkj.dsn.ui.fragment.main;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.ClassAdapter;
import com.lxkj.dsn.adapter.FaceAdapter;
import com.lxkj.dsn.adapter.ProductAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.ui.fragment.CachableFrg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Time:2020/10/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:面对面
 */
public class FaceFra extends CachableFrg implements View.OnClickListener {


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
    @BindView(R.id.TagFlow)
    TagFlowLayout TagFlow;
    @BindView(R.id.ryFace)
    RecyclerView ryFace;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    private ArrayList<DataListBean> listBeans = new ArrayList<>();

    private FaceAdapter faceAdapter;
    private TagAdapter<String> adapter;
    private List<String> hot_list = new ArrayList<>();
    @Override
    protected int rootLayout() {
        return R.layout.fra_issue;
    }

    @Override
    protected void initView() {
        hot_list.clear();
        hot_list.add("推荐");
        hot_list.add("0~3岁");
        hot_list.add("3~6岁");
        hot_list.add("6~9岁");
        hot_list.add("9~12岁");

        adapter = new TagAdapter<String>(hot_list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_select_face, parent, false);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ryFace.setLayoutManager(layoutManager);
        faceAdapter = new FaceAdapter(getContext(), listBeans);
        ryFace.setAdapter(faceAdapter);
        faceAdapter.setOnItemClickListener(new FaceAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}

