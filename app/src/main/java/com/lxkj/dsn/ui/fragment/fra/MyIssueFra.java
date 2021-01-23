package com.lxkj.dsn.ui.fragment.fra;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lxkj.dsn.R;
import com.lxkj.dsn.ui.fragment.TitleFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/22
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class MyIssueFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.navi_left)
    ImageView naviLeft;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private int type = 0;
    private List<Fragment> fragments = new ArrayList<>();


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> list;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_myissue, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }

    public void initView() {
        naviLeft.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        setState();


        NGCFra driverFra = new NGCFra();
        RepalyFra ownerFra = new RepalyFra();
        fragments.add(driverFra);
        fragments.add(ownerFra);



        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                type = i;
                setState();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navi_left:
                act.finishSelf();
                break;
            case R.id.btn1:
                //我的心得
                if (type != 1) {
                    type = 1;
                    setState();
                    viewPager.setCurrentItem(type);
                }
                break;
            case R.id.btn2:
                //我的回复
                if (type != 0) {
                    type = 0;
                    setState();
                    viewPager.setCurrentItem(type);
                }
                break;
        }
    }

    private void setState() {
        switch (type) {
            case 0:
                btn1.setSelected(false);
                btn1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                btn2.setSelected(true);
                btn2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case 1:
                btn1.setSelected(true);
                btn1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                btn2.setSelected(false);
                btn2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
