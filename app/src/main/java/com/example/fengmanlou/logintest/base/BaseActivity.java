package com.example.fengmanlou.logintest.base;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by fengmanlou on 2015/4/7.
 */
public class BaseActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }



}
