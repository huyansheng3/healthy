package com.example.fengmanlou.logintest.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.ui.activity.MainActivity;
import com.example.fengmanlou.logintest.util.Logger;

import java.util.Date;

/**
 * Created by fengmanlou on 2015/5/1.
 */
public class AlarmService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
