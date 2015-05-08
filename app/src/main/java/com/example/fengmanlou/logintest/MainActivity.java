package com.example.fengmanlou.logintest;

import android.app.ActionBar;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.GridView;


import com.astuetz.PagerSlidingTabStrip;
import com.example.fengmanlou.logintest.util.ImageAdapter;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class MainActivity extends FragmentActivity {
        private SlidingMenu menu;
        private static final String DEBUG_TAG = "MainActivity";

         /*聊天界面的Fragment
         * */

        private HealthyFragment chatFragment;

        /**
         * 发现界面的Fragment
         */
        private PersonFragment foundFragment;

        /**
         * 通讯录界面的Fragment
         */
        private SystemFragment contactsFragment;

        /**
         * PagerSlidingTabStrip的实例
         */
        private PagerSlidingTabStrip tabs;

        /**
         * 获取当前屏幕的密度
         */
        private DisplayMetrics dm;

        private GridView gridView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            setContentView(R.layout.activity_main);
            setOverflowShowingAlways();
            ActionBar actionBar = getActionBar();
            if (actionBar == null){
                Log.i(DEBUG_TAG,"actionBar是空的");
            }

            dm = getResources().getDisplayMetrics();
            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

            Log.i(DEBUG_TAG,"gridView"+gridView);
            Log.i(DEBUG_TAG,"new ImageAdapter(getApplicationContext()"+new ImageAdapter(getApplicationContext()));
//            gridView.setAdapter(new ImageAdapter(getApplicationContext()));
//            gridView.setNumColumns(3);

            viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
            tabs.setViewPager(viewPager);
            setTabsValue();

            //初始化滑动菜单
            initSlidingMenu();
        }

        /**
         * 对PagerSlidingTabStrip的各项属性进行赋值。
         */
        private void setTabsValue() {
            // 设置Tab是自动填充满屏幕的
            tabs.setShouldExpand(true);
            // 设置Tab的分割线是透明的
            tabs.setDividerColor(Color.TRANSPARENT);
            // 设置Tab底部线的高度
            tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 1, dm));
            // 设置Tab Indicator的高度
            tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 4, dm));
            // 设置Tab标题文字的大小
            tabs.setTextSize((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, 16, dm));
            // 设置Tab Indicator的颜色
            tabs.setIndicatorColor(Color.parseColor("#45c01a"));
            // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
            tabs.setTextColor(Color.parseColor("#45c01a"));
            // 取消点击Tab时的背景色
            tabs.setTabBackground(0);
        }



    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.i(DEBUG_TAG,"fm---->"+fm);
        }

        private final String[] titles = {"健康社区", "个人应用", "系统应用"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (chatFragment == null) {
                        chatFragment = new HealthyFragment();
                    }
                    return chatFragment;
                case 1:
                    if (foundFragment == null) {
                        foundFragment = new PersonFragment();
                    }
                    return foundFragment;
                case 2:
                    if (contactsFragment == null) {
                        contactsFragment = new SystemFragment();
                    }
                    return contactsFragment;
                default:
                    return null;
            }
        }

    }

        /**
         *
         *
         * 初始化滑动菜单
         */
        private void initSlidingMenu() {
            // 设置主界面视图



            // 设置滑动菜单的属性值
            menu = new SlidingMenu(this);
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
            menu.setShadowWidthRes(R.dimen.shadow_width);
            menu.setShadowDrawable(R.drawable.shadow);
            menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
            menu.setFadeEnabled(true);
            menu.setFadeDegree(0.5f);
            menu.setBehindCanvasTransformer( new SlidingMenu.CanvasTransformer() {
                @Override
                public void transformCanvas(Canvas canvas, float percentOpen) {
                    float scale = (float) (percentOpen*0.25 + 0.75);
                    canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
                }

            });


            menu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);


            // 设置滑动菜单的视图界面
            menu.setMenu(R.layout.menu_frame);
            getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new SampleListFragment()).commit();
        }



    @Override
        public void onBackPressed() {
            //点击返回键关闭滑动菜单
            if (menu.isMenuShowing()) {
                menu.showContent();
            } else {
                super.onBackPressed();
            }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }



