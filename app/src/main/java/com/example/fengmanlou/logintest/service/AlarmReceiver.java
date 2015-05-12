package com.example.fengmanlou.logintest.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.entity.Alarm;
import com.example.fengmanlou.logintest.ui.activity.NotificationActivity;
import com.example.fengmanlou.logintest.ui.activity.RingActivity;

/**
 * Created by fengmanlou on 2015/5/1.
 */
public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        Alarm alarm=new Alarm(context, intent.getExtras());
        Intent intent2=new Intent();
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.setClass(context, RingActivity.class);
        intent2.putExtras(Alarm.alarm2Bundle(alarm));
        context.startActivity(intent2);



    }
}
