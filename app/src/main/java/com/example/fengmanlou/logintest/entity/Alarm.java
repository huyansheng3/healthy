package com.example.fengmanlou.logintest.entity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fengmanlou.logintest.db.AlarmHelper;
import com.example.fengmanlou.logintest.service.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by fengmanlou on 2015/5/2.
 */
public class Alarm {
    private String id = "";
    private GregorianCalendar AudreyCalendar; // 响铃日期，格里高利日历，奥黛丽日历
    private String tag = "";
    private boolean cancelable = true; //如果为true，则不可取消直到响铃结束
    private boolean available = true; //是否关闭
    private String ringtoneId = "";

    private Context mContext;
    private int numInSplitter = 0;

    private Boolean isSplitter = false;

    private Intent intent;

    public static final String ALARM_ID = "ALARM_ID";
    public static final String ALARM_CALENDAR = "ALARM_CALENDAR";
    public static final String ALARM_TAG = "ALARM_TAG";
    public static final String ALARM_CANCELABLE = "ALARM_CANCELABLE";
    public static final String ALARM_AVAILABLE = "ALARM_AVAILABLE";
    public static final String ALARM_RINGTONE = "ALARM_IMAGE";

    public static final String ALARM_ACTION = "com.example.fengmanlou.logintest.alarm";

    public Alarm(Context context, Bundle bundle) {
        mContext = context;
        if (bundle != null) {
            setCancelable(bundle.getBoolean(ALARM_CANCELABLE));
            setTag(bundle.getString(ALARM_TAG));
            setId(bundle.getString(ALARM_ID));
            setAudreyCalendar((GregorianCalendar) bundle
                    .getSerializable(ALARM_CALENDAR));
            setAvailable(bundle.getBoolean(ALARM_AVAILABLE));
            setRingtoneId(bundle.getString(ALARM_RINGTONE));
        }

        // 初始化不会打开闹铃
        // activate();
    }



    /**
     * 如果对闹铃进行了初始化或者修改，那么就一定要调用activate()重新设置一次闹铃 不进行数据库操作，数据库的修改操作在外部进行。
     * 当然只是设置，能不能打开还要看available
     */
    public void activate() {
        if (available) {
            setIntent();
            if (AudreyCalendar != null) {
                setOneTimeAlarm();
            }
        }
        else {
            setIntent();
            PendingIntent pendingIntent;
            AlarmManager manager = (AlarmManager) mContext
                    .getSystemService(Context.ALARM_SERVICE);
            pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            manager.cancel(pendingIntent);
        }
    }

    /**
     * 设置闹铃AlarmManager所使用的intent
     */
    public void setIntent() {
        intent = new Intent(getmContext(), AlarmReceiver.class);
        intent.putExtra(ALARM_CANCELABLE, cancelable);
        intent.putExtra(ALARM_TAG, tag);
        intent.putExtra(ALARM_ID, id);
        intent.putExtra(ALARM_RINGTONE, ringtoneId);
        intent.setData(Uri.fromParts("alarm", "id", id));
    }


    /**
     * 保存对闹钟的修改，并运行activate
     */
    public void edit() {
        if (editAlarmInDB()) {
            activate();
        } else {
            // 存储失败，要趁着intent没有修改，从intent中恢复……
        }
    }

    /**
     * 定时唤起检查闹铃的任务
     */
    public static void startAlarmRestore(Context context) {
        Intent intent = new Intent();
        intent.setAction(Alarm.ALARM_ACTION);   //设置闹钟
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        // manager.cancel(pendingIntent);//到底要不要先取消，按理说不要
        // 应该是每天00：00检查一次
        manager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), 86400000, pendingIntent);  //每隔一天设置一下闹钟
    }

    public static Bundle alarm2Bundle(Alarm alarm) {
        Bundle bundle = new Bundle();
        // ID
        bundle.putString(Alarm.ALARM_ID, alarm.getId());

        // 可取消
        bundle.putBoolean(Alarm.ALARM_CANCELABLE, alarm.isCancelable());

        // 标签
        bundle.putString(Alarm.ALARM_TAG, alarm.getTag());

        // 日期
        bundle.putSerializable(Alarm.ALARM_CALENDAR, alarm.getAudreyCalendar());

        // 可用
        bundle.putBoolean(Alarm.ALARM_AVAILABLE, alarm.isAvailable());

        // 铃声
        bundle.putString(Alarm.ALARM_RINGTONE, alarm.getRingtoneId());

        return bundle;
    }

    public static String Calendar2String(GregorianCalendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(calendar.getTime());
    }
    public static GregorianCalendar String2Calendar(String string) {
        // 日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        GregorianCalendar calendar = null;
        try {
            calendar = new GregorianCalendar();
            Date date = format.parse(string);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            calendar = null;
        }
        return calendar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GregorianCalendar getAudreyCalendar() {
        return AudreyCalendar;
    }

    public void setAudreyCalendar(GregorianCalendar audreyCalendar) {
        AudreyCalendar = audreyCalendar;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getRingtoneId() {
        return ringtoneId;
    }

    public void setRingtoneId(String ringtoneId) {
        this.ringtoneId = ringtoneId;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public Boolean getIsSplitter() {
        return isSplitter;
    }

    public void setIsSplitter(Boolean isSplitter) {
        this.isSplitter = isSplitter;
    }

    public int getNumInSplitter() {
        return numInSplitter;
    }

    public void setNumInSplitter(int numInSplitter) {
        this.numInSplitter = numInSplitter;
    }

    /**
     * 存储到数据库，只在用户创建时调用一次
     */
    public void storeInDB() {
        AlarmHelper helper = new AlarmHelper(this.getmContext());
        helper.insert(this);
    }

    /**
     * 从数据库删除
     */
    public void deleteFromDB() {
        AlarmHelper helper = new AlarmHelper(this.getmContext());
        helper.delete(this);
    }

    /**
     * 在数据库中修改
     */
    public boolean editAlarmInDB() {
        AlarmHelper helper = new AlarmHelper(this.getmContext());
        return helper.edit(this);
    }

    /**
     * 设置一次性闹钟
     */
    private void setOneTimeAlarm() {
        if (AudreyCalendar.getTimeInMillis() - System.currentTimeMillis() > 0) {
            // 最后一个参数必须是PendingIntent.FLAG_UPDATE_CURRENT，否则BroadcastReceiver将收不到参数。
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager manager = (AlarmManager) mContext
                    .getSystemService(Context.ALARM_SERVICE);
            manager.set(AlarmManager.RTC_WAKEUP,
                    AudreyCalendar.getTimeInMillis(), pendingIntent);
        } else {
            Toast.makeText(mContext, "小伙子，设置太早闹钟是不会执行滴！",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
