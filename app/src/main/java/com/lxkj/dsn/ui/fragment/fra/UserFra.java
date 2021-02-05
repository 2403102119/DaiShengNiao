package com.lxkj.dsn.ui.fragment.fra;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.Jsontwobean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.SpotsCallBack;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.imageloader.GlideEngine;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.GetJsonDataUtil;
import com.lxkj.dsn.utils.ListUtil;
import com.lxkj.dsn.utils.StringUtil;
import com.lxkj.dsn.utils.ToastUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;
import top.zibin.luban.Luban;

import static android.app.Activity.RESULT_OK;
import static com.luck.picture.lib.config.PictureConfig.CHOOSE_REQUEST;

/**
 * Time:2021/1/29
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:个人资料
 */
public class UserFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvWx)
    TextView tvWx;
    @BindView(R.id.tvSite)
    TextView tvSite;
    @BindView(R.id.llAdress)
    LinearLayout llAdress;
    @BindView(R.id.tvSign)
    TextView tvSign;
    @BindView(R.id.llSign)
    LinearLayout llSign;
    private List<LocalMedia> selectList = new ArrayList<>();
    private ArrayList<String> sexList = new ArrayList<>();
    private ArrayList<String> ageList = new ArrayList<>();
    private String carousel, nickname, content, phone, lon, lat, province_city_town, address;

    private static boolean isLoaded = false;
    private List<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private String tx;
    private static final String TAG = "SeeMoreFra";
    private String province;
    private String city;
    private String twon;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        //Toast.makeText(RegisterCui1Activity.this, "开始准备数据", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
//                                initJsonData();
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;
                case MSG_LOAD_SUCCESS:
                    //Toast.makeText(SelectAddActivity.this, "解析成功", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;
                case MSG_LOAD_FAILED:
                    //Toast.makeText(SelectAddActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public String getTitleName() {
        return "设置";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_user, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        llAdress.setOnClickListener(this);
        riIcon.setOnClickListener(this);
        tvName.setOnClickListener(this);
        tvSex.setOnClickListener(this);
        tvAge.setOnClickListener(this);
        tvPhone.setOnClickListener(this);
        tvWx.setOnClickListener(this);
        tvSite.setOnClickListener(this);
        tvSign.setOnClickListener(this);
        llSign.setOnClickListener(this);
        tvPhone.setOnClickListener(this);

        mHandler.sendEmptyMessage(MSG_LOAD_DATA);

        for (int i = 18; i < 70; i++) {
            ageList.add(i + "");
        }
        sexList.add("男");
        sexList.add("女");

        memberinfo();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.llAdress://收货地址
                bundle.putString("type", "0");
                ActivitySwitcher.startFragment(getActivity(), AddressListFra.class, bundle);
                break;
            case R.id.riIcon://头像
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPmsExternalStorageDeatil();
                } else {
                    pmsExternalStorageSuccess();
                }
                break;
            case R.id.tvName://昵称
                AppConsts.title = "名字";
                bundle.putString("title", "名字");
                ActivitySwitcher.startFrgForResultBun(act, NameFra.class, 111, bundle);
                break;
            case R.id.tvSex://性别
                OptionsPickerView pvOptions1 = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        tvSex.setText(sexList.get(options1));
                    }
                })
                        .setTitleText("性别")
                        .setDividerColor(getResources().getColor(R.color.main_color))
                        .setSubmitColor(getResources().getColor(R.color.main_color))//确定按钮文字颜色
                        .setCancelColor(getResources().getColor(R.color.txt_66))//取消按钮文字颜色
                        .setTextColorCenter(getResources().getColor(R.color.main_color)) //设置选中项文字颜色
                        .setContentTextSize(20)
                        .build();

                pvOptions1.setPicker(sexList);//一级选择器
                pvOptions1.show();
                break;
            case R.id.tvAge://年龄
                OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        tvAge.setText(ageList.get(options1));
                    }
                })
                        .setTitleText("性别")
                        .setDividerColor(getResources().getColor(R.color.main_color))
                        .setSubmitColor(getResources().getColor(R.color.main_color))//确定按钮文字颜色
                        .setCancelColor(getResources().getColor(R.color.txt_66))//取消按钮文字颜色
                        .setTextColorCenter(getResources().getColor(R.color.main_color)) //设置选中项文字颜色
                        .setContentTextSize(20)
                        .build();

                pvOptions.setPicker(ageList);//一级选择器
                pvOptions.show();
                break;
            case R.id.tvPhone://手机号
                ActivitySwitcher.startFrgForResultBun(act, BindingFra.class, 333, bundle);
                break;
            case R.id.tvSite://地区
                hideInput(getContext(), v);
                if (isLoaded) {
                    showPickerView();
                }
                break;
            case R.id.tvSign://签名

                break;
            case R.id.llSign:
                AppConsts.title = "签名";
                bundle.putString("title", "签名");
                ActivitySwitcher.startFrgForResultBun(act, NameFra.class, 222, bundle);
                break;

        }
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
            if (photoLists != null && photoLists.size() != 0) {
                File file = new File(photoLists.get(0));//获取第一张图片
                if (file.exists()) {
                    uploadImage(photoLists);
                }
            }
        }
        if (resultCode == 103) {
            ToastUtil.show("请检查相机权限~");
        }
        if (requestCode == 111 && resultCode == 111) {//名字
            if (null != data) {
                nickname = data.getStringExtra("et");
                tvName.setText(nickname);
                editmemberInfo();
            }
        }
        if (requestCode == 222 && resultCode == 111) {//签名
            if (null != data) {
                content = data.getStringExtra("et");
                tvSign.setText(content);
                editmemberInfo();
            }
        }
        if (requestCode == 333 && resultCode == 111) {//手机号
            if (null != data) {
                phone = data.getStringExtra("et");
                tvPhone.setText(phone);
                editmemberInfo();
            }
        }
        if (requestCode == 444 && resultCode == 111) {//地址
            if (null != data) {
                lon = data.getStringExtra("lng");
                lat = data.getStringExtra("lat");
                province_city_town = data.getStringExtra("province_city_town");
                address = data.getStringExtra("poiName");
                tvSite.setText(address);
                editmemberInfo();
            }
        }
    }

    /**
     * 强制隐藏输入法键盘
     */
    private void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                carousel = "";
                for (int i = 0; i < resultBean.dataarr.size(); i++) {
                    carousel += resultBean.dataarr.get(i) + "|";
                }
                carousel = carousel.replaceAll("null", "");
                carousel = carousel.substring(0, carousel.length() - 1);

                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.touxiang)
                        .placeholder(R.mipmap.touxiang))
                        .load(carousel)
                        .into(riIcon);

                editmemberInfo();
            }


            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    //编辑用户信息
    private void editmemberInfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("nickname", nickname);
        params.put("icon", carousel);
        params.put("sex", tvSex.getText().toString());
        params.put("age", tvAge.getText().toString());
        params.put("autograph", tvSign.getText().toString());
        params.put("province", province);
        params.put("city", city);
        params.put("area", twon);
        mOkHttpHelper.post_json(getContext(), Url.editmemberInfo, params, new BaseCallback<ResultBean>() {
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


            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //用户个人信息
    private void memberinfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        mOkHttpHelper.post_json(getContext(), Url.memberinfo, params, new BaseCallback<ResultBean>() {
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

                Glide.with(getContext()).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.touxiang)
                        .placeholder(R.mipmap.touxiang))
                        .load(resultBean.dataobject.icon)
                        .into(riIcon);
                tvName.setText(resultBean.dataobject.nickname);
                tvSign.setText(resultBean.dataobject.autograph);
                tvPhone.setText(resultBean.dataobject.phone);
                tvAge.setText(resultBean.dataobject.age);
                tvSex.setText(resultBean.dataobject.sex);
                tvName.setText(resultBean.dataobject.nickname);
                tvSite.setText(resultBean.dataobject.province + resultBean.dataobject.city + resultBean.dataobject.area);
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
                .maxSelectNum(1)
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

    private void initJsonData() {//解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(getContext(), "city.min.json");//获取assets目录下的json文件数据
        ArrayList<Jsontwobean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            options1Items.add(jsonBean.get(i).getName());
            for (int c = 0; c < jsonBean.get(i).getC().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getC().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                if (null != jsonBean.get(i).getC().get(c).getD()) {
                    for (int j = 0; j < jsonBean.get(i).getC().get(c).getD().size(); j++) {
                        city_AreaList.add(jsonBean.get(i).getC().get(c).getD().get(j).getName());
                    }
                }
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    /*三级联动---选择省市县*/
    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1) : "";
                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";
                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";
                tx = opt1tx + opt2tx + opt3tx;
                //Toast.makeText(RegisterCui1Activity.this, tx, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onOptionsSelect: 选择的是" + tx + "---" + opt1tx + "---" + opt2tx + "---" + opt3tx);
                province = opt1tx;
                city = opt2tx;
                twon = opt3tx;
                tvSite.setText(tx);

                editmemberInfo();
            }
        })
                .setTitleText("请选择地区")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    public ArrayList<Jsontwobean> parseData(String result) {//Gson 解析
        ArrayList<Jsontwobean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                Jsontwobean entity = gson.fromJson(data.optJSONObject(i).toString(), Jsontwobean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
