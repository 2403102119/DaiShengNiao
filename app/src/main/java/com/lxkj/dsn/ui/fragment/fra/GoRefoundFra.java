package com.lxkj.dsn.ui.fragment.fra;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hys.utils.AppUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.OrderDetailAdapter;
import com.lxkj.dsn.adapter.PostArticleImgAdapter;
import com.lxkj.dsn.bean.OrdertailListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.SpotsCallBack;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.imageloader.GlideEngine;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.ListUtil;
import com.lxkj.dsn.utils.StringUtil;
import com.lxkj.dsn.utils.ToastUtil;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import top.zibin.luban.Luban;

import static android.app.Activity.RESULT_OK;
import static com.luck.picture.lib.config.PictureConfig.CHOOSE_REQUEST;

/**
 * Time:2021/2/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:申请退款
 */
public class GoRefoundFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.etReason)
    EditText etReason;
    @BindView(R.id.riimage)
    RecyclerView riimage;
    @BindView(R.id.tvIssue)
    TextView tvIssue;
    @BindView(R.id.tvYuanyin)
    EditText tvYuanyin;

    private OrderDetailAdapter orderDetailAdapter;
    private List<OrdertailListBean> bean = new ArrayList();
    private List<String> mBannerSelectPath = new ArrayList<>();
    private PostArticleImgAdapter postArticleImgAdapter;
    private int select_number = 6;
    private String plusPath;
    private ArrayList<String> imagelist = new ArrayList<>();
    private ArrayList<String> refundimage = new ArrayList<>();
    private ArrayList<String> odidlist = new ArrayList<>();
    private List<LocalMedia> selectList = new ArrayList<>();
    private String carousel, fid;
    private List<String> upould = new ArrayList<>();
    public static final int IMAGE_SIZE = 6;//可添加图片最大数
    private Double zongjine = 0.00;
    private int zongshuliang = 0;
    private String ordernum;

    @Override
    public String getTitleName() {
        return "退款/售后";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_gorefound, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        ordernum = getArguments().getString("ordernum");

        if (null != getArguments().getSerializable("bean")) {
            bean.clear();
            bean.addAll((List<OrdertailListBean>) getArguments().getSerializable("bean"));
        }
        odidlist.clear();
        for (int i = 0; i < bean.size(); i++) {
            odidlist.add(bean.get(i).odid);
            BigDecimal price = new BigDecimal(bean.get(i).gprice);
            BigDecimal count = new BigDecimal(bean.get(i).gnum);
            BigDecimal jine = count.multiply(price);
            zongjine += jine.doubleValue();
            zongshuliang += Integer.parseInt(bean.get(i).gnum);
        }
        tvMoney.setText("¥" + zongjine);
        tvNumber.setText(zongshuliang + "");



        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        orderDetailAdapter = new OrderDetailAdapter(getContext(), bean);
        recyclerView.setAdapter(orderDetailAdapter);
        orderDetailAdapter.setOnItemClickListener(new OrderDetailAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {

            }

            @Override
            public void Onselect(int firstPosition, String amount) {

            }
        });

        postArticleImgAdapter = new PostArticleImgAdapter(mContext, mBannerSelectPath);
        riimage.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        riimage.setAdapter(postArticleImgAdapter);
        postArticleImgAdapter.setOnItemClickListener(new PostArticleImgAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int positiom) {
                mBannerSelectPath.remove(positiom);
                refundimage.remove(positiom);
                postArticleImgAdapter.notifyDataSetChanged();
                select_number = 6 - (mBannerSelectPath.size() - 1);
            }

            @Override
            public void Onbig(int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPmsExternalStorageDeatil();
                } else {
                    pmsExternalStorageSuccess();
                }
            }

        });

        //添加按钮图片资源
        plusPath = getString(R.string.glide_plus_icon_string) + AppUtils.getPackageInfo(mContext).packageName + "/mipmap/" + R.mipmap.tianjiatupian;
        mBannerSelectPath.add(plusPath);//添加按键，超过9张时在adapter中隐藏

        tvIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(tvYuanyin.getText().toString())){
                    ToastUtil.show("请填写退款原因");
                    return;
                }
                orderrefund();
            }
        });

    }

    /**
     * 申请退款
     */
    private void orderrefund() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("ordernum", ordernum);
        params.put("odidlist", odidlist);
        params.put("refundreason", etReason.getText().toString());
        params.put("refunddesc", tvYuanyin.getText().toString());
        params.put("refundimage", refundimage);
        mOkHttpHelper.post_json(getContext(), Url.orderrefund, params, new BaseCallback<ResultBean>() {
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
                ActivitySwitcher.startFragment(getActivity(), ConversionOkFra.class);
                act.finishSelf();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    private void checkPmsExternalStorageDeatil() {
        MPermissions.requestPermissions(this, AppConsts.PMS_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        );
    }

    @PermissionGrant(AppConsts.PMS_LOCATION)
    public void pmsExternalStorageSuccess() {
        //权限授权成功
        PictureSelector.create(act)//在Fragment中使用则是 fragment.this
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .loadImageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(select_number)
                .minSelectNum(1)
                .imageSpanCount(3)// 每行显示个数 int
                .isCamera(true)// 是否显示拍照按钮 true or false
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .compress(true)// 是否压缩 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .enableCrop(false)//是否裁剪
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .showCropFrame(true)
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .forResult(CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_REQUEST && resultCode == RESULT_OK) {
            ArrayList<String> photoLists = new ArrayList<>();
            selectList = PictureSelector.obtainMultipleResult(data);
            for (int i = 0; i < selectList.size(); i++) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    photoLists.add(selectList.get(i).getAndroidQToPath());
                } else {
                    if (StringUtil.isEmpty(selectList.get(i).getPath())) {
                        photoLists.add(selectList.get(i).getRealPath());
                    } else {
                        photoLists.add(selectList.get(i).getPath());
                    }
                }
            }
            if (photoLists != null && !photoLists.isEmpty()) {
                File file = new File(photoLists.get(0));//获取第一张图片
                if (file.exists()) {
                    mBannerSelectPath.remove(mBannerSelectPath.size() - 1);
                    mBannerSelectPath.addAll(photoLists);
                    uploadImage(mBannerSelectPath);
                    mBannerSelectPath.add(plusPath);//添加按键，超过9张时在adapter中隐藏
                    Log.i("TAG", "onSuccess:------- " + mBannerSelectPath);
                    select_number = 5 - (mBannerSelectPath.size() - 1);
                    postArticleImgAdapter.notifyDataSetChanged();
                }
            }
        }

    }


    /**
     * 上传图片
     */
    private void uploadImage(List<String> path) {
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).contains(getString(R.string.glide_plus_icon_string))) {
                path.remove(i);
            }
        }

        List<String> reason = new ArrayList<>();
        List<File> reasonFile = new ArrayList<>();
        reason.addAll(path);
        try {
            reasonFile.addAll(Luban.with(getContext()).load(reason).get());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, List<File>> files = new HashMap<>();
        if (!ListUtil.isEmpty(reasonFile))
            files.put("file", reasonFile);
        mOkHttpHelper.post_file(mContext, Url.uploadmanyFile, files, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                upould.clear();
                for (int i = 0; i < resultBean.dataarr.size(); i++) {
                    refundimage.add(resultBean.dataarr.get(i));
                }

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
}
