package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.avos.avoscloud.AVUser;
import com.example.fengmanlou.logintest.R;

import java.util.Random;

/**
 * Created by fengmanlou on 2015/4/22.
 */
public class SplashActivity extends Activity{
    private ImageView background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        background = (ImageView) findViewById(R.id.splash_background);
        Random random = new Random();
        int num = random.nextInt(4);
        int drawable = R.drawable.background01;
        switch (num){
            case 0:
            break;
            case 1:
                drawable = R.drawable.background02;
                break;
            case 2:
                drawable = R.drawable.background03;
                break;
            case 3:
                drawable = R.drawable.background04;
                break;
        }
        background.setImageResource(drawable);
        Animation animation = new AnimationUtils().loadAnimation(this,R.anim.image_welcome);
        background.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AVUser currentUser = AVUser.getCurrentUser();   //获得当前用户
                if (currentUser != null) {
                    //如果当前用户存在，则直接跳转到主页面
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);

                }else {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }
                animation.setFillAfter(true);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void finish(){
        background.destroyDrawingCache();
        super.finish();
    }
}
