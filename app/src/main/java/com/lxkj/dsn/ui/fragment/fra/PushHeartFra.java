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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.hys.utils.AppUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.adapter.PostArticleAdapter;
import com.lxkj.dsn.adapter.PostArticleImgAdapter;
import com.lxkj.dsn.bean.ResultBean;
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
 * Time:2021/2/2
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:发布心得
 */
public class PushHeartFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.spcheliangleixing)
    Spinner spcheliangleixing;
    @BindView(R.id.tvPush)
    TextView tvPush;
    private  List<String> list = new ArrayList<>();
    private  List<String> fidlist = new ArrayList<>();
    private ArrayAdapter mAdapter;
    private List<String> mBannerSelectPath = new ArrayList<>();
    private PostArticleAdapter postArticleImgAdapter;
    private int select_number = 6;
    private String plusPath;
    private ArrayList<String> imagelist = new ArrayList<>();
    private List<LocalMedia> selectList = new ArrayList<>();
    private String carousel,fid;
    private List<String> upould = new ArrayList<>();
    public static final int IMAGE_SIZE = 6;//可添加图片最大数
    @Override
    public String getTitleName() {
        return "发布心得";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_pushheart, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        mAdapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_item, list);
        spcheliangleixing.setAdapter(mAdapter);
        spcheliangleixing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//车辆类型
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fid = fidlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        postArticleImgAdapter = new PostArticleAdapter(mContext, mBannerSelectPath);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(postArticleImgAdapter);
        postArticleImgAdapter.setOnItemClickListener(new PostArticleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int positiom) {
                mBannerSelectPath.remove(positiom);
                upould.remove(positiom);
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

        getclassify1list();

        tvPush.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvPush://发布
                if (StringUtil.isEmpty(etContent.getText().toString())){
                    ToastUtil.show("请写入您的心得");
                    return;
                }
                addmydynamic();
                break;
        }
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
                    select_number = 6 - (mBannerSelectPath.size() - 1);
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
//                    carousel += resultBean.dataarr.get(i) + "|";
                    upould.add(resultBean.dataarr.get(i));
                }
//                carousel = carousel.replaceAll("null", "");
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
                list.clear();
                fidlist.clear();
                for (int i = 0; i <resultBean.dataList.size() ; i++) {
                    list.add(resultBean.dataList.get(i).name);
                    fidlist.add(resultBean.dataList.get(i).fid);
                }
                mAdapter.notifyDataSetChanged();
                fid = resultBean.dataList.get(0).fid;
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //发布动态
    private void addmydynamic() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid",userId);
        params.put("fid",fid);
        params.put("content",etContent.getText().toString());
        params.put("images",upould);
        mOkHttpHelper.post_json(getContext(), Url.addmydynamic, params, new BaseCallback<ResultBean>() {
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
                    ToastUtil.show("发布成功，正在审核...");
                    act.finishSelf();

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
