package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.example.fengmanlou.logintest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by fengmanlou on 2015/4/22.
 */
public class UserInforActivity extends Activity{
    private ImageView user_avatar;
    private TextView nickname_textView,account_textView,email_textView,sex_textView;

    public void findView(){
        user_avatar = (ImageView) findViewById(R.id.user_avatar);
        nickname_textView = (TextView) findViewById(R.id.nickname_textView);
        account_textView = (TextView) findViewById(R.id.account_textView);
        email_textView = (TextView) findViewById(R.id.email_textView);
        sex_textView = (TextView) findViewById(R.id.sex_textView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfor);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("个人中心");
        findView();
        AVUser user = AVUser.getCurrentUser();
        account_textView.setText(user.getUsername());

        if (user.getString("nickname") != null) {
            nickname_textView.setText(user.getString("nickname"));
        }
        if (user.getEmail() != null) {
            email_textView.setText(user.getEmail());
        }
        AVFile avFile = user.getAVFile("avatar");
        if (avFile != null) {
            String url = avFile.getUrl();
            //显示图片的配置
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnFail(R.drawable.healthy_icon01)
                    .showImageForEmptyUri(R.drawable.healthy_icon01)
                    .showImageOnLoading(R.drawable.healthy_icon01)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .displayer(new SimpleBitmapDisplayer())
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            ImageLoader.getInstance().displayImage(url, user_avatar,options);
        }
        if (user.get("sex") != null) {
            int sexNum = (int) user.get("sex");

            if (sexNum == 2131361933) {
                sex_textView.setText("男");
            }
            if (sexNum == 2131361934) {
                sex_textView.setText("女");
            }
        }


    }
}
