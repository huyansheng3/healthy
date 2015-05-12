package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.fengmanlou.logintest.R;

/**
 * Created by fengmanlou on 2015/4/22.
 */
public class AboutActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("关于");
        setContentView(R.layout.activity_about);

        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
