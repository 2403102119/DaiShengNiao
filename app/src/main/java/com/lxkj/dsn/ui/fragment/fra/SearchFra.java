package com.lxkj.dsn.ui.fragment.fra;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.lxkj.dsn.utils.ToastUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2020/9/26
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:搜索界面
 */
public class SearchFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.imfish)
    ImageView imfish;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.flowHistory)
    TagFlowLayout flowHistory;
    @BindView(R.id.tvClear)
    TextView tvClear;
    TagAdapter<String> adapter, hoistAdapter;
    List<String> hot_list = new ArrayList<>();
    private List<String> HistoryList = new ArrayList<>();
    private List<String> list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fra_search, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }

    public void initView() {

        adapter = new TagAdapter<String>(hot_list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag, parent, false);
                view.setText(s);
                return view;
            }

        };

        setData();

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    return true;
                }

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    if (!TextUtils.isEmpty(etSearch.getText().toString())) {
                        list.add(etSearch.getText().toString());
                        SharePrefUtil.addSessionMap(AppConsts.History, list);
                        setData();
                        String edStr = etSearch.getText().toString().trim();
                        Bundle bundle = new Bundle();
                        bundle.putString("search", edStr);
                        ActivitySwitcher.startFragment(getActivity(), SearchResultFra.class, bundle);
//                        //隐藏软键盘
//                      hideInputMethod();
                    } else {
                        ToastUtil.show("关键字不能为空");
                    }
                    return true;
                }
                return false;
            }
        });


        imfish.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

    }

    public void setData() {
        list.addAll(SharePrefUtil.getDataList(AppConsts.History));

        hoistAdapter = new TagAdapter<String>(removeDuplicate((list))) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag, parent, false);
                view.setText(s);
                return view;
            }

        };
        flowHistory.setAdapter(hoistAdapter);
        flowHistory.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {//历史搜索
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Bundle bundle = new Bundle();
                bundle.putString("search", SharePrefUtil.getDataList(AppConsts.History).get(position));
                ActivitySwitcher.startFragment(getActivity(), SearchResultFra.class, bundle);
                return true;
            }
        });
    }

    public List removeDuplicate(List list) {
        HashSet h = new LinkedHashSet(list);
        list.clear();
        list.addAll(h);
        Collections.reverse(list);
        return list;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imfish:
                act.finishSelf();
                break;
            case R.id.tvClear://清空历史记录
                list.clear();
                SharePrefUtil.addSessionMap(AppConsts.History, list);
                setData();
                break;
            case R.id.tvSearch:
                if (!TextUtils.isEmpty(etSearch.getText().toString())) {
                    list.add(etSearch.getText().toString());
                    SharePrefUtil.addSessionMap(AppConsts.History, list);
                    setData();
                    String edStr = etSearch.getText().toString().trim();
                    Bundle bundle = new Bundle();
                    bundle.putString("search", edStr);
                    ActivitySwitcher.startFragment(getActivity(), SearchResultFra.class, bundle);
//                        //隐藏软键盘
//                      hideInputMethod();
                } else {
                    ToastUtil.show("关键字不能为空");
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
