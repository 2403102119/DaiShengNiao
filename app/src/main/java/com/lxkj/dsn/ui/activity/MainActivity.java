package com.lxkj.dsn.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gyf.immersionbar.ImmersionBar;
import com.lxkj.dsn.AppConsts;
import com.lxkj.dsn.GlobalBeans;
import com.lxkj.dsn.R;
import com.lxkj.dsn.bean.ResultBean;
import com.lxkj.dsn.bean.SendmessageBean;
import com.lxkj.dsn.biz.EventCenter;
import com.lxkj.dsn.http.BaseCallback;
import com.lxkj.dsn.http.OkHttpHelper;
import com.lxkj.dsn.http.Url;
import com.lxkj.dsn.socket.WsManager;
import com.lxkj.dsn.ui.fragment.main.HomeFra;
import com.lxkj.dsn.ui.fragment.main.HomeMineFra;
import com.lxkj.dsn.ui.fragment.main.FaceFra;
import com.lxkj.dsn.ui.fragment.main.ShoppCar;
import com.lxkj.dsn.ui.fragment.main.AudioFra;
import com.lxkj.dsn.utils.SharePrefUtil;
import com.lxkj.dsn.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseFragAct
        implements TabHost.OnTabChangeListener, EventCenter.EventListener {
    @BindView(R.id.tabhost)
    public FragmentTabHost mTabHost;
    private int curTab = 0, tabIdx = 0;
    private WsManager wsManager;
    public  TextView tvUnreadCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (GlobalBeans.getSelf() == null) {
            GlobalBeans.initForMainUI(getApplication());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        AppConsts.userId = SharePrefUtil.getString(this, AppConsts.UID, "");
        ButterKnife.bind(this);
        initTabHost();
        setTabSelected(curTab, true);
        eventCenter.registEvent(this, EventCenter.EventType.EVT_TOHOME);
        eventCenter.registEvent(this, EventCenter.EventType.EVT_LOGIN);
        eventCenter.registEvent(this, EventCenter.EventType.EVT_LOGOUT);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar();
        mImmersionBar.init();

        if (!EventBus.getDefault().isRegistered(this)) {//判断是否已经注册了（避免崩溃）
            EventBus.getDefault().register(this); //向EventBus注册该对象，使之成为订阅者
        }



    }



    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void senmessage(SendmessageBean mMessageEvent) {
        if (mMessageEvent.type.equals("load")){
            Map<String,String> map = new HashMap<>();
            map.put("cmd","19");
            map.put("fromUserId","");
            map.put("userId",SharePrefUtil.getString(this, AppConsts.UID, ""));
            map.put("pageNo",mMessageEvent.pageNo);
            map.put("pageSize","10");
            map.put("type","1");
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String requestData = gson.toJson(map);
            wsManager.sendMessage(requestData);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mTabHost.setCurrentTab(tabIdx);

        memberinfo();
    }

    //用户个人信息
    private void memberinfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", SharePrefUtil.getString(MainActivity.this,AppConsts.UID,null));
        OkHttpHelper.getInstance().post_json(MainActivity.this, Url.memberinfo, params, new BaseCallback<ResultBean>() {
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

                SharePrefUtil.saveString(MainActivity.this, AppConsts.ismember, resultBean.dataobject.ismember);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    private void initTabHost() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (int i = 0; i < tabs.size(); i++) {
            final TabDesc td = tabs.get(i);
            final View vTab = makeTabView();
            TextView tab = ((TextView) vTab.findViewById(R.id.tab_label));
            if (i==1)
                tvUnreadCount = ((TextView) vTab.findViewById(R.id.tvUnreadCount));

            tab.setText(td.name);
            refreshTab(vTab, td, false);
            mTabHost.addTab(mTabHost.newTabSpec(td.tag).setIndicator(vTab), td.frgClass, null);
        }
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(this);
    }

    private void setTabSelected(int tabIdx, boolean selected) {
        refreshTab(mTabHost.getTabWidget().getChildAt(tabIdx),
                tabs.get(tabIdx), selected);
    }

    private View makeTabView() {
        return this.getLayoutInflater().inflate(
                R.layout.maintab, mTabHost.getTabWidget(), false);
    }

    private void refreshTab(View vTab, TabDesc td, boolean selected) {
        final ImageView iv = (ImageView) vTab.findViewById(R.id.tab_image);
        iv.setImageResource(selected ? td.icSelect : td.icNormal);
    }
    private final List<TabDesc> tabs = new ArrayList<TabDesc>() {
        {
            add(TabDesc.make("home", R.string.home,//首页
                    R.drawable.shouye, R.drawable.shouye_xuanzhong, HomeFra.class));
            add(TabDesc.make("shop", R.string.shop,//影音书
                    R.drawable.yingyinshu, R.drawable.yingyinshu_xuanzhong, AudioFra.class));
            add(TabDesc.make("add", R.string.add,//面对面
                    R.drawable.face, R.drawable.face_xuanzhong, FaceFra.class));
            add(TabDesc.make("tg", R.string.car,//购物车
                    R.drawable.gouwuche, R.drawable.gouwuche_xuanzhong, ShoppCar.class));
            add(TabDesc.make("mine", R.string.mine,//我的
                    R.drawable.wode, R.drawable.wode_xuanzhong, HomeMineFra.class));
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getSupportFragmentManager().findFragmentByTag("home").onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 在onStop时释放掉播放器
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventCenter.unregistEvent(this, EventCenter.EventType.EVT_TOHOME);
        eventCenter.unregistEvent(this, EventCenter.EventType.EVT_LOGIN);
    }

    @Override
    public void onEvent(EventCenter.HcbEvent e) {
        switch (e.type) {
            case EVT_TOHOME:
                tabIdx = 0;
                break;
            case EVT_LOGOUT:
                onExit();
                break;
        }
    }


    private static class TabDesc {
        String tag;
        int name;
        int icNormal;
        int icSelect;
        Class<? extends Fragment> frgClass;

        static TabDesc make(String tag, int name, int icNormal, int icSelect,
                            Class<? extends Fragment> frgClass) {
            TabDesc td = new TabDesc();
            td.tag = tag;
            td.name = name;
            td.icNormal = icNormal;
            td.icSelect = icSelect;
            td.frgClass = frgClass;
            return td;
        }

    }


    @Override
    public void onTabChanged(String s) {
        tabIdx = mTabHost.getCurrentTab();
        if (tabIdx == curTab) {
            return;
        }
        setTabSelected(curTab, false);
        curTab = tabIdx;
        setTabSelected(curTab, true);
    }


    private long backPressTime = 0;
    private static final int SECOND = 1000;

    @Override
    public void onBackPressed() {
        // 在全屏或者小窗口时按返回键要先退出全屏或小窗口，
        final long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - backPressTime > 2 * SECOND) {
            backPressTime = uptimeMillis;
            ToastUtil.show(getString(R.string.press_again_to_leave));
        } else {
            ToastUtil.cancel();
            onExit();
        }
    }

    private void onExit() {

        finish();

        EventBus.getDefault().unregister(this);
        if (wsManager != null) {
            wsManager.stopConnect();
            wsManager = null;
        }

        if (null != beans) {
            beans.onTerminate();
        }
    }

}