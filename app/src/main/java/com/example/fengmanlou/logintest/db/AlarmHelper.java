package com.example.fengmanlou.logintest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.fengmanlou.logintest.entity.Alarm;

import java.util.ArrayList;

/**
 * Created by fengmanlou on 2015/5/3.
 */
public class AlarmHelper {
    private AlarmDBHelper dbHelper;
    private SQLiteDatabase database;
    private final Context context;

    public AlarmHelper(Context context) {
        this.context = context;
    }

    public AlarmHelper open() {
        dbHelper = new AlarmDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public long insert(Alarm alarm){
        open();
        ContentValues values = new ContentValues();
        values.put(AlarmColumn.ALARM_ID, alarm.getId());
        values.put(AlarmColumn.ALARM_CALENDAR,
                Alarm.Calendar2String(alarm.getAudreyCalendar()));
        values.put(AlarmColumn.ALARM_CANCELABLE, alarm.isCancelable() ? "true"
                : "false");
        values.put(AlarmColumn.ALARM_TAG, alarm.getTag());
        values.put(AlarmColumn.ALARM_AVAILABLE, alarm.isAvailable() ? "true"
                : "false");
        values.put(AlarmColumn.ALARM_RINGTONE, alarm.getRingtoneId());
        long res = database.insert(AlarmDBHelper.TABLE, null, values);
        close();
        return res;
    }

    public ArrayList<Alarm> getAlarms() {
        open();
        ArrayList<Alarm> list = new ArrayList<Alarm>();
        String orderBy = AlarmColumn._ID + " DESC";
        Cursor cursor = database.query(AlarmDBHelper.TABLE,
                AlarmColumn.PROJECTION, null, null, null, null, orderBy);
        if (cursor != null && cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                Bundle bundle = new Bundle();

                // ID
                bundle.putString(Alarm.ALARM_ID,
                        cursor.getString(AlarmColumn.ALARM_ID_COLUMN));


                // 可取消
                bundle.putBoolean(Alarm.ALARM_CANCELABLE, "true".equals(cursor
                        .getString(AlarmColumn.ALARM_CANCELABLE_COLUMN)) ? true
                        : false);
                // 标签
                bundle.putString(Alarm.ALARM_TAG,
                        cursor.getString(AlarmColumn.ALARM_TAG_COLUMN));
                // 日期
                bundle.putSerializable(Alarm.ALARM_CALENDAR, Alarm
                        .String2Calendar(cursor
                                .getString(AlarmColumn.ALARM_CALENDAR_COLUMN)));
                // 可用
                bundle.putBoolean(Alarm.ALARM_AVAILABLE, "true".equals(cursor
                        .getString(AlarmColumn.ALARM_AVAILABLE_COLUMN)) ? true
                        : false);

                // 铃声
                bundle.putString(Alarm.ALARM_RINGTONE,
                        cursor.getString(AlarmColumn.ALARM_RINGTONE_COLUMN));

                Alarm alarm = new Alarm(context, bundle);

                list.add(alarm);
            }
        }
        close();
        return list;
    }

    public Alarm getAlarmByID(String id) {
        open();
        String selection = AlarmColumn.ALARM_ID + "= '" + id + "'";
        Cursor cursor = database.query(AlarmDBHelper.TABLE,
                AlarmColumn.PROJECTION, selection, null, null, null, null);
        Bundle bundle = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            bundle = new Bundle();

            // ID
            bundle.putString(Alarm.ALARM_ID,
                    cursor.getString(AlarmColumn.ALARM_ID_COLUMN));

            // 可取消
            bundle.putBoolean(Alarm.ALARM_CANCELABLE, "true".equals(cursor
                    .getString(AlarmColumn.ALARM_CANCELABLE_COLUMN)) ? true
                    : false);
            // 标签
            bundle.putString(Alarm.ALARM_TAG,
                    cursor.getString(AlarmColumn.ALARM_TAG_COLUMN));
            // 日期
            bundle.putSerializable(Alarm.ALARM_CALENDAR, Alarm
                    .String2Calendar(cursor
                            .getString(AlarmColumn.ALARM_CALENDAR_COLUMN)));

            // 可用
            bundle.putBoolean(Alarm.ALARM_AVAILABLE, "true".equals(cursor
                    .getString(AlarmColumn.ALARM_AVAILABLE_COLUMN)) ? true
                    : false);

            // 铃声
            bundle.putString(Alarm.ALARM_RINGTONE,
                    cursor.getString(AlarmColumn.ALARM_RINGTONE_COLUMN));
        }
        close();
        if (bundle != null) {
            Alarm alarm = new Alarm(context, bundle);
            return alarm;
        } else {
            return null;
        }
    }

    public boolean edit(Alarm alarm) {
        open();
        ContentValues values = new ContentValues();
        values.put(AlarmColumn.ALARM_CALENDAR,
                Alarm.Calendar2String(alarm.getAudreyCalendar()));
        values.put(AlarmColumn.ALARM_CANCELABLE, alarm.isAvailable());
        values.put(AlarmColumn.ALARM_TAG, alarm.getTag());
        values.put(AlarmColumn.ALARM_AVAILABLE, alarm.isAvailable());
        values.put(AlarmColumn.ALARM_RINGTONE, alarm.getRingtoneId());
        boolean okay = database.update(AlarmDBHelper.TABLE, values,
                AlarmColumn.ALARM_ID + " = '" + alarm.getId() + "'", null) > 0;
        close();
        return okay;
    }

    public boolean delete(Alarm alarm) {
        open();
        boolean okay = database.delete(AlarmDBHelper.TABLE,
                AlarmColumn.ALARM_ID + " = '" + alarm.getId() + "'", null) > 0;
        close();
        return okay;
    }

    public boolean delete(String id) {
        open();
        boolean okay = database.delete(AlarmDBHelper.TABLE,
                AlarmColumn.ALARM_ID + " = '" + id + "'", null) > 0;
        close();
        return okay;
    }
    public boolean truncate() {
        open();
        boolean okay = database.delete(AlarmDBHelper.TABLE, null, null) > 0;
        close();
        return okay;
    }


}
