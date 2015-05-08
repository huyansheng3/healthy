package com.example.fengmanlou.logintest.db;

import android.provider.BaseColumns;

/**
 * Created by fengmanlou on 2015/5/3.
 */
public class AlarmColumn implements BaseColumns {
    public static final String ALARM_ID = "ALARM_ID";
    public static final String ALARM_CALENDAR = "ALARM_CALENDAR";
    public static final String ALARM_CANCELABLE = "ALARM_CANCELABLE";
    public static final String ALARM_TAG = "ALARM_TAG";
    public static final String ALARM_AVAILABLE = "ALARM_AVAILABLE";
    public static final String ALARM_RINGTONE = "ALARM_RINGTONE";

    public static final int ALARM_ID_COLUMN = 1;
    public static final int ALARM_CALENDAR_COLUMN = 2;
    public static final int ALARM_CANCELABLE_COLUMN = 3;
    public static final int ALARM_TAG_COLUMN = 4;
    public static final int ALARM_AVAILABLE_COLUMN = 5;
    public static final int ALARM_RINGTONE_COLUMN = 6;

    // 查询结果集
    public static final String[] PROJECTION = { _ID, ALARM_ID,
            ALARM_CALENDAR, ALARM_CANCELABLE, ALARM_TAG,
            ALARM_AVAILABLE, ALARM_RINGTONE };

}
