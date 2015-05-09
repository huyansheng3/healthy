package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.AVUtils;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.service.NewsService;
import com.example.fengmanlou.logintest.ui.fragment.HealthyFragment;
import com.example.fengmanlou.logintest.ui.fragment.PersonFragment;
import com.example.fengmanlou.logintest.ui.fragment.SampleListFragment;
import com.example.fengmanlou.logintest.ui.fragment.SystemFragment;
import com.example.fengmanlou.logintest.util.Logger;
import com.example.fengmanlou.logintest.util.PathUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class MainActivity extends FragmentActivity {
        private SlidingMenu menu;
        private static final String DEBUG_TAG = MainActivity.class.getSimpleName();
        private static final int CROP_REQUEST = 2;
        private static final int IMAGE_PICK_REQUEST = 1;
        public static ImageLoader imageLoader = ImageLoader.getInstance();
        private HealthyFragment chatFragment;
        private PersonFragment foundFragment;
        private SystemFragment contactsFragment;
        private TextView user_nick_name;
        private SearchView searchView;
        private AVUser curUser;
        /**
         * PagerSlidingTabStrip的实例
         */
        private PagerSlidingTabStrip tabs;

        /**
         * 获取当前屏幕的密度
         */
        private DisplayMetrics dm;

        private GridView gridView;

        private ImageView imageAvatar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
            setContentView(R.layout.activity_main);
            setOverflowShowingAlways();
            FeedbackAgent agent = new FeedbackAgent(MainActivity.this);  //在入口Activity注册反馈notification

            dm = getResources().getDisplayMetrics();
            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
//            gridView.setAdapter(new ImageAdapter(getApplicationContext()));
//            gridView.setNumColumns(3);
            viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
            tabs.setViewPager(viewPager);
            setTabsValue();

            //初始化滑动菜单
            initSlidingMenu();
            imageAvatar = (ImageView) findViewById(R.id.avatar);
            user_nick_name = (TextView) findViewById(R.id.user_nick_name);
            curUser = AVUser.getCurrentUser();
            user_nick_name.setText(curUser.getString("nickname"));
            AVFile avFile = curUser.getAVFile("avatar");
            if (avFile != null) {
                String url = avFile.getUrl();
                ImageLoader.getInstance().displayImage(url, imageAvatar);
            }
            imageAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, IMAGE_PICK_REQUEST);

                }
            });



        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == IMAGE_PICK_REQUEST){
                Uri uri = intent.getData(); //把图片文件的Uri传过来
                Uri tempImageUri = startImageCrop(uri, 100, 100, CROP_REQUEST);  //图片截成100*100的头像图片的uri地址
                Logger.d("tempImageUri : "+tempImageUri.toString());
                imageAvatar.setImageBitmap(BitmapFactory.decodeFile(tempImageUri.toString()));

                curUser = AVUser.getCurrentUser();


            }
        }
    }




    public Uri startImageCrop(Uri uri, int outputX, int outputY,
                              int requestCode) {
        Intent intent = null;
        intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        String outputPath = PathUtils.getAvatarTmpPath();
        File tempFile = new File(outputPath);  //之前试过给每个temp头像文件使用UUID的命名方式，不过不知道为什么读不出来，之后可以在尝试尝试
        //现在的方法算是一种折中的方法，如果之前的temp存在就删掉它，再去创建一个新的temp文件
        Uri outputUri = Uri.fromFile(new File(outputPath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false); // face detection
        startActivityForResult(intent, requestCode);
        return outputUri;
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
                    TypedValue.COMPLEX_UNIT_SP, 18, dm));
            // 设置Tab Indicator的颜色
            tabs.setIndicatorColor(Color.parseColor("#45c01a"));
            // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
            tabs.setTextColor(Color.parseColor("black"));
            // 取消点击Tab时的背景色
            tabs.setTabBackground(0);
        }



    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final String[] titles = {"健康社区", "个人应用", "所有用户"};

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


    long waitTime = 2000;
    long touchTime = 0;

    @Override
        public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
            //点击返回键关闭滑动菜单
            if (menu.isMenuShowing()) {
                menu.showContent();
            }
            else if((currentTime-touchTime)>waitTime){
                Toast.makeText(MainActivity.this,"再按一次返回键退出",Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            }else {
                finish();
            }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!AVUtils.isBlankString(query)) {
                    NewsService.searchQuery(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        return intent;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){



        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {             //通过反射机制使得Provider中的图标显示出来
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


    private void setOverflowShowingAlways() {           //通过反射机制，使得有物理按键的手机overflow统一ActionBar的显示
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



