package com.lxkj.dsn.ui.fragment.fra;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.OrderDetailAdapter;
import com.lxkj.dsn.adapter.Recycle_one_itemAdapter;
import com.lxkj.dsn.bean.OrdertailListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.ui.fragment.main.FaceFra;
import com.lxkj.dsn.ui.fragment.system.WebFra;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
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
 * Time:2021/2/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:退款详情
 */
public class RefundFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvYuanyin)
    TextView tvYuanyin;
    @BindView(R.id.tvOrderPrice)
    TextView tvOrderPrice;
    @BindView(R.id.ryImage)
    RecyclerView ryImage;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.llfanhui)
    LinearLayout llfanhui;
    @BindView(R.id.tvDetail)
    TextView tvDetail;
    @BindView(R.id.llButton)
    LinearLayout llButton;
    @BindView(R.id.tvReason)
    TextView tvReason;
    @BindView(R.id.imfanhui)
    ImageView imfanhui;

    private OrderDetailAdapter affirmOrderAdapter;
    private String ordernum;
    private List<OrdertailListBean> listBeans = new ArrayList<>();
    private List<String> list_image = new ArrayList<>();
    private Recycle_one_itemAdapter recycle_one_itemAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_refound, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }

    public void initView() {
        llButton.setOnClickListener(this);
        imfanhui.setOnClickListener(this);

        ordernum = getArguments().getString("ordernum");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        affirmOrderAdapter = new OrderDetailAdapter(getContext(), listBeans);
        recyclerView.setAdapter(affirmOrderAdapter);
        affirmOrderAdapter.setOnItemClickListener(new OrderDetailAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

            }

            @Override
            public void Onselect(int firstPosition, String amount) {

            }
        });

        ryImage.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recycle_one_itemAdapter = new Recycle_one_itemAdapter(getContext(), list_image);
        ryImage.setAdapter(recycle_one_itemAdapter);
        recycle_one_itemAdapter.setOnItemClickListener(new Recycle_one_itemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                showImage(new ImageView(getContext()), firstPosition, list_image);
            }
        });

        myorderdetail();
    }


    /**
     * 订单详情
     */
    private void myorderdetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("ordernum", ordernum);
        mOkHttpHelper.post_json(getContext(), Url.myorderdetail, params, new BaseCallback<ResultBean>() {
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
                listBeans.clear();
                for (int i = 0; i < resultBean.dataobject.ordertailList.size(); i++) {
                    resultBean.dataobject.ordertailList.get(i).type = resultBean.dataobject.type;
                }
                listBeans.addAll(resultBean.dataobject.ordertailList);
                affirmOrderAdapter.notifyDataSetChanged();

                list_image.clear();
                list_image.addAll(resultBean.dataobject.refundimage);
                recycle_one_itemAdapter.notifyDataSetChanged();


                tvOrderPrice.setText("¥" + resultBean.dataobject.backmoney);
                tvYuanyin.setText(resultBean.dataobject.refundreason);
                tvReason.setText(resultBean.dataobject.refunddesc);
                tvTime.setText(resultBean.dataobject.sqtktime);

                switch (resultBean.dataobject.state) {
                    case "5":
                        tvState.setText("退款中");
                        break;
                    case "6":
                        tvState.setText("已退款");
                        break;
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    private void showImage(final ImageView iv, int position, List<String> list) {
        List<Object> urls = new ArrayList<>();
        urls.clear();
        urls.addAll(list);

        new XPopup.Builder(getContext()).asImageViewer(iv, position, urls, new OnSrcViewUpdateListener() {
            @Override
            public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                popupView.updateSrcView(iv);
            }
        }, new RefundFra.ImageLoader())
                .show();
    }

    class ImageLoader implements XPopupImageLoader {
        @Override
        public void loadImage(int position, @NonNull Object url, @NonNull ImageView imageView) {
            //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
            Glide.with(imageView).load(url).apply(new RequestOptions().placeholder(R.mipmap.logo).override(Target.SIZE_ORIGINAL)).into(imageView);
        }

        @Override
        public File getImageFile(@NonNull Context context, @NonNull Object uri) {
            try {
                return Glide.with(context).downloadOnly().load(uri).submit().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.llButton:
                bundle.putString("title", "客服热线");
                bundle.putString("url", "http://w10.ttkefu.com/k/linkurl/?t=4C2CFJI5");
                ActivitySwitcher.startFragment(getContext(), WebFra.class, bundle);
                break;
            case R.id.imfanhui:
                act.finishSelf();
                break;
        }
    }
}
