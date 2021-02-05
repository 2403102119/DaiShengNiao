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
import android.widget.TextView;

import com.hys.utils.AppUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.AppraiseAdapter;
import com.lxkj.dsn.adapter.PostArticleImgAdapter;
import com.lxkj.dsn.bean.OrdertailListBean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.bean.EvaluateListBean;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
 * Interface:
 */
public class AppraiseFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.okID)
    TextView okID;
    private AppraiseAdapter appraiseAdapter;
    private List<OrdertailListBean> listBeans = new ArrayList<>();
    private String ordernum;
    private int select_number = 6;
    private String plusPath;
    private ArrayList<String> refundimage = new ArrayList<>();
    private List<LocalMedia> selectList = new ArrayList<>();
    private String carousel, fid;
    private List<String> upould = new ArrayList<>();
    private List<EvaluateListBean> evaluateList = new ArrayList<>();
    private List<String> mBannerSelectPath = new ArrayList<>();

    @Override
    public String getTitleName() {
        return "评价";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_appraise, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        ordernum = getArguments().getString("ordernum");


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycle.setLayoutManager(layoutManager);
        appraiseAdapter = new AppraiseAdapter(getContext(), listBeans);
        recycle.setAdapter(appraiseAdapter);
        appraiseAdapter.setOnItemClickListener(new AppraiseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition, List<String> mBannerSelectPath1) {
                mBannerSelectPath = mBannerSelectPath1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPmsExternalStorageDeatil();
                } else {
                    pmsExternalStorageSuccess();
                }
            }

        });

        myorderdetail();

        okID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i <listBeans.size() ; i++) {
                    evaluateList.add(appraiseAdapter.getBean());
                }
                orderevaluate();

            }
        });

        plusPath = getString(R.string.glide_plus_icon_string) + AppUtils.getPackageInfo(getContext()).packageName + "/mipmap/" + R.mipmap.tianjiatupian;
    }

    /**
     * 订单评价
     */
    private void orderevaluate() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("ordernum", ordernum);
        params.put("evaluateList", evaluateList);
        mOkHttpHelper.post_json(getContext(), Url.orderevaluate, params, new BaseCallback<ResultBean>() {
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
                act.finishSelf();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
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
                for (int i = 0; i <resultBean.dataobject.ordertailList.size() ; i++) {
                    resultBean.dataobject.ordertailList.get(i).type = resultBean.dataobject.type;
                }
                listBeans.addAll(resultBean.dataobject.ordertailList);

                appraiseAdapter.notifyDataSetChanged();


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

                appraiseAdapter.setdata(resultBean.dataarr,mBannerSelectPath);

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
