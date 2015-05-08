package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

import com.example.fengmanlou.logintest.R;

/**
 * Created by fengmanlou on 2015/5/1.
 */
public class NotificationActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }
}
