package com.example.zz_xa.firecontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.zz_xa.firecontrol.Chat.ChatGroup;
import com.example.zz_xa.firecontrol.Erwei.ErweiPlan;
import com.example.zz_xa.firecontrol.Plotting.MarkerPlotting;
import com.example.zz_xa.firecontrol.VideoLive.VideoLive;
import com.example.zz_xa.firecontrol.VideoMonitoring.VideoMonitoring;

/**
 * Created by ZZ-XA of wxb on 2018/4/11.
 * Fix by:
 */

public class TabActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private TabLayout tabLayout = null;

    private ViewPager viewPager;

    private Fragment[] mFragmentArrays = new Fragment[6];

    private String[] mTabTitles = new String[6];
    private static MainActivity sMainActivity = null;

    public static void setMainActivity(MainActivity activity) {
        sMainActivity = activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab_layout);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        initView();
    }

    private void initView() {
        mTabTitles[0] = "地图信息";
        mTabTitles[1] = "信息交互";
     //   mTabTitles[2] = "通讯录";
        mTabTitles[2] = "视频直播";
        mTabTitles[3] = "物联标绘";
        mTabTitles[4] = "二维预案";
        mTabTitles[5] = "视频监控";
        //tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置tablayout距离上下左右的距离
        //tab_title.setPadding(20,20,20,20);
        //mFragmentArrays[0] = TabFragment.newInstance();
        //mFragmentArrays[0] = (Fragment)findViewById(R.layout.w_webview);
        mFragmentArrays[0] = new VideoMonitoring();
        mFragmentArrays[1] = new ChatGroup();
    //    mFragmentArrays[2] = new ChatAddressList();
        mFragmentArrays[2] = new VideoLive();
        mFragmentArrays[3] = new MarkerPlotting();
        mFragmentArrays[4] = new ErweiPlan();
        mFragmentArrays[5] = new VideoMonitoring();
        PagerAdapter pagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);
        //将ViewPager和TabLayout绑定
        tabLayout.setupWithViewPager(viewPager);

        int iType = sMainActivity.getiType();

        tabLayout.getTabAt(iType).select();
    }

    //以下三个为翻页监听
    @Override
    public void onPageScrollStateChanged(int arg0){
        int a = arg0;
    }
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    // TODO Auto-generated method stub  
        int a = arg0;
        float b = arg1;
        int c = arg2;

    }
    @Override
    public void onPageSelected(int arg0){
        int a=arg0;
        if(arg0 == 0){
            finish();
        }
    }

    final class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentArrays[position];
        }


        @Override
        public int getCount() {
            return mFragmentArrays.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];

        }
    }
}
