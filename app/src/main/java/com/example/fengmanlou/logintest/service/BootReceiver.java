package com.example.fengmanlou.logintest.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.fengmanlou.logintest.db.AlarmHelper;
import com.example.fengmanlou.logintest.entity.Alarm;

import java.util.ArrayList;

/**
 * Created by fengmanlou on 2015/5/2.
 */
public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<Alarm> alarms = getAllAlarms(context);
        for (Alarm alarm : alarms) {
            // Test
            // 当重启后，所有的都应该恢复，而如果这是定时任务，那么只要恢复月度和年度的就可以了.
            // 如果每天00：00重建闹钟的话，那么00：00时响的闹钟会不会响呢
            // 所以最后错开一点，因为闹钟没有秒数，所以设置为00：00：30秒何如。
            // 不论是什么闹钟，都会保证如果第二天有闹钟的话就会设置上的，所以不用担心00：00的闹钟不会设置上
            alarm.activate();
        }
    }

    /**
     * 从本地数据库恢复所有的闹钟
     *
     * @return
     */
    private ArrayList<Alarm> getAllAlarms(Context context) {
        AlarmHelper helper = new AlarmHelper(context);
        return helper.getAlarms();
    }
}
