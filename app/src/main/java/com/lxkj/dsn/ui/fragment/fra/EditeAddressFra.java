package com.lxkj.dsn.ui.fragment.fra;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.DataListBean;
import com.lxkj.dsn.bean.Jsontwobean;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.http.SpotsCallBack;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.ui.fragment.TitleFragment;
import com.lxkj.dsn.utils.GetJsonDataUtil;
import com.lxkj.dsn.utils.StringUtil;
import com.lxkj.dsn.utils.StringUtils;
import com.lxkj.dsn.utils.ToastUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Response;

/**
 * Created by kxn on 2020/1/18 0018.
 */
public class EditeAddressFra extends TitleFragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.etAddressDetail)
    EditText etAddressDetail;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    private List<DataListBean> provinceList = new ArrayList<>();

    private String type;
    private String addressId;
    private DataListBean dataListBean;
    private String isdefault;

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
        return "编辑收货地址";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fra_edite_address, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        dataListBean = ((DataListBean) getArguments().getSerializable("data"));
        if (null != dataListBean) {
            act.titleTv.setText("编辑地址");
            etName.setText(dataListBean.name);
            tvAddress.setText(dataListBean.address);
            etPhone.setText(dataListBean.phone);
            etAddressDetail.setText(dataListBean.addressdetail);
            addressId = dataListBean.addressid;
            type = "0";
        } else {
            act.titleTv.setText("添加新地址");
            type = "1";
        }
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        tvConfirm.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvConfirm://提交
                addAddress();
                break;
            case R.id.tvAddress://选择地址
                hideInput(getContext(),view);
                if (isLoaded) {
                    showPickerView();
                }
                break;
        }
    }
    /**
     * 强制隐藏输入法键盘
     */
    private void hideInput(Context context, View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }


    /**
     * 新增/修改地址
     */
    private void addAddress() {
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        String addressdetail = etAddressDetail.getText().toString();
        String address = tvAddress.getText().toString();

        if (StringUtil.isEmpty(name)) {
            ToastUtil.show("请输入收货人");
            return;
        }
        if (StringUtil.isEmpty(phone)) {
            ToastUtil.show("请输入联系电话");
            return;
        }
        //验证手机号是否正确
        if (!StringUtils.isMobile(phone)) {
            ToastUtil.show("输入的手机号格式不正确");
            return;
        }
        if (StringUtil.isEmpty(address)) {
            ToastUtil.show("请选择收货地址");
            return;
        }
        if (StringUtil.isEmpty(addressdetail)) {
            ToastUtil.show("请输入详细地址");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("name", name);
        params.put("addressid", addressId);
        params.put("phone", phone);
        params.put("address", tvAddress.getText().toString());
        params.put("addressdetail", addressdetail);
        params.put("isdefault", "0");
        mOkHttpHelper.post_json(getContext(), Url.addAddress, params, new SpotsCallBack<ResultBean>(getContext()) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                ToastUtil.show(resultBean.getResultNote());
                act.finishSelf();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
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
                tvAddress.setText(tx);
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
