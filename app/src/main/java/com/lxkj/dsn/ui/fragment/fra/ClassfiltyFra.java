package com.lxkj.dsn.ui.fragment.fra;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.ClassAdapter;
import com.lxkj.dsn.adapter.ProductAdapter;
import com.lxkj.dsn.adapter.StairAdapter;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/2/2
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:分类
 */
public class ClassfiltyFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.et_seek)
    EditText etSeek;
    @BindView(R.id.tv_seek)
    TextView tvSeek;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.imMessage)
    ImageView imMessage;
    @BindView(R.id.tvOrderCount)
    TextView tvOrderCount;
    @BindView(R.id.page_top)
    LinearLayout pageTop;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llArea)
    LinearLayout llArea;
    @BindView(R.id.imXiaoliang)
    ImageView imXiaoliang;
    @BindView(R.id.llClass)
    LinearLayout llClass;
    @BindView(R.id.imJuli)
    ImageView imJuli;
    @BindView(R.id.llSpecification)
    LinearLayout llSpecification;
    @BindView(R.id.llScreen)
    LinearLayout llScreen;
    @BindView(R.id.RecyclerViewLeft)
    RecyclerView RecyclerViewLeft;
    @BindView(R.id.ll_wu)
    LinearLayout llWu;
    @BindView(R.id.RecyclerViewRight)
    RecyclerView RecyclerViewRight;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    private ArrayList<DataListBean> listBeansOne = new ArrayList<>();
    private ClassAdapter messageAdapter;
    private List<DataListBean> stairlist = new ArrayList<>();
    private List<DataListBean> secondlist = new ArrayList<>();
    private StairAdapter stairAdapter;
    private ProductAdapter productAdapter;
    private int page = 1, totalPage = 1;
    private String fid,sid,sort = "0",position,type, xiaoliang = "1", jiage = "4";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_classfilty, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }

    public void initView() {

        position = getArguments().getString("position");
        type = getArguments().getString("type");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        messageAdapter = new ClassAdapter(getContext(), listBeansOne);//首页一级分类
        if (type.equals("1")){
            messageAdapter.checked = true;
            messageAdapter.select = Integer.parseInt(position);
        }else {
            messageAdapter.checked = true;
            messageAdapter.select = 0;
        }
        recyclerView.setAdapter(messageAdapter);
        messageAdapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                stairAdapter.selectPosition = 0;
                fid = listBeansOne.get(firstPosition).fid;
                getclassify2list();
            }
        });


        layoutManager = new LinearLayoutManager(getActivity());
        RecyclerViewLeft.setLayoutManager(layoutManager);
        stairAdapter = new StairAdapter(getActivity(), stairlist);
        if (type.equals("2")){
            stairAdapter.selectPosition = Integer.parseInt(position);
        }
        RecyclerViewLeft.setAdapter(stairAdapter);
        stairAdapter.setOnItemClickListener(new StairAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {
                sid = stairlist.get(position).sid;
                getclassify2goodslist(sid);
            }

        });



        RecyclerViewRight.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        productAdapter = new ProductAdapter(getContext(), secondlist);
        RecyclerViewRight.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {//商品详情
                Bundle bundle = new Bundle();
                bundle.putString("gid", secondlist.get(firstPosition).gid);
                ActivitySwitcher.startFragment(getActivity(), DetailFra.class, bundle);
            }
        });

        etSeek.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                    getclassify2goodslist(sid);

                    return true;
                }
                return false;
            }
        });

        getclassify1list();

        llArea.setOnClickListener(this);
        llClass.setOnClickListener(this);
        llSpecification.setOnClickListener(this);
        imMessage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.llArea://综合排序
              sort = "0";
              xiaoliang = "1";
              imXiaoliang.setImageResource(R.mipmap.xiaoliangdi);
              jiage = "4";
              imXiaoliang.setImageResource(R.mipmap.xiaoliang);
              getclassify2goodslist(sid);
              break;
          case R.id.llClass://销量
              if (xiaoliang.equals("1")) {
                  xiaoliang = "2";
                  sort = "2";
                  imXiaoliang.setImageResource(R.mipmap.xiaoliang);
              } else {
                  xiaoliang = "1";
                  sort = "1";
                  imXiaoliang.setImageResource(R.mipmap.xiaoliangdi);
              }
              getclassify2goodslist(sid);
              break;
          case R.id.llSpecification://价格
              if (jiage.equals("4")) {
                  jiage = "3";
                  sort = "3";
                  imXiaoliang.setImageResource(R.mipmap.xiaoliangdi);
              } else {
                  jiage = "4";
                  sort = "4";
                  imXiaoliang.setImageResource(R.mipmap.xiaoliang);
              }
              getclassify2goodslist(sid);
              break;
          case R.id.imMessage://系统消息
              ActivitySwitcher.startFragment(getActivity(), MessageFra.class);
              break;
      }
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
                if (type.equals("1")){

                    fid = listBeansOne.get(Integer.parseInt(position)).fid;
                    getclassify2list();
                }else {
                    fid = listBeansOne.get(0).fid;
                    getclassify2list();
                }


            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //二级分类
    private void getclassify2list() {
        Map<String, Object> params = new HashMap<>();
        params.put("fid",fid);
        mOkHttpHelper.post_json(getContext(), Url.getclassify2list, params, new BaseCallback<ResultBean>() {
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
                stairlist.clear();
                stairlist.addAll(resultBean.dataList);
                stairAdapter.notifyDataSetChanged();
                if (type.equals("2")){
                    stairAdapter.selectPosition = Integer.parseInt(position);
                    sid = resultBean.dataList.get(Integer.parseInt(position)).sid;
                    getclassify2goodslist(resultBean.dataList.get(Integer.parseInt(position)).sid);
                }else {
                    sid = resultBean.dataList.get(0).sid;
                    getclassify2goodslist(resultBean.dataList.get(0).sid);
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    //二级分类下的商品
    private void getclassify2goodslist(String sid) {
        Map<String, Object> params = new HashMap<>();
        params.put("sid", sid);
        params.put("content", etSeek.getText().toString());
        params.put("nowPage", page);
        params.put("pageCount", "10");
        params.put("sort", sort);
        mOkHttpHelper.post_json(getContext(), Url.getclassify2goodslist, params, new BaseCallback<ResultBean>() {
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
                    secondlist.clear();
                    productAdapter.notifyDataSetChanged();
                }
                if (null != resultBean.dataList)
                    secondlist.addAll(resultBean.dataList);

                if (resultBean.dataList.size()==0){
                    llWu.setVisibility(View.VISIBLE);
                }else {
                    llWu.setVisibility(View.GONE);
                }

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
                if (StringUtil.isEmpty(resultBean.datastr)||resultBean.datastr.equals("0")){
                    tvOrderCount.setVisibility(View.GONE);
                }else {
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
        getmessnum();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
