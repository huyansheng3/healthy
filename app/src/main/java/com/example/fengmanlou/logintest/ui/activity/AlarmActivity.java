package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.entity.Alarm;
import com.example.fengmanlou.logintest.util.Logger;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by fengmanlou on 2015/5/2.
 */
public class AlarmActivity extends Activity {
    private Spinner ring_spinner;
    private String[] ringTones;
    private Uri[] uris;
    public GregorianCalendar QCalendar;
    private ArrayAdapter<String> spinnerAdapter;
    public int setMode = MODE_ADD_ALARM;// 默认为添加闹钟
    public static final int MODE_ADD_ALARM = 0;
    public static final int MODE_EDIT_ALARM = 1;
    public Alarm alarm;
    private EditText alarm_tag;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button add_alarm_success,delete_alarm_success;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        setTitle("设置闹钟");
        ring_spinner = (Spinner) findViewById(R.id.ring_spinner);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        add_alarm_success = (Button) findViewById(R.id.add_alarm_success);
        delete_alarm_success = (Button) findViewById(R.id.delete_alarm_success);
        alarm_tag = (EditText) findViewById(R.id.alarm_tag);
        alarm = new Alarm(getApplicationContext(), getIntent().getBundleExtra(
                "alarm"));
        setRingSpinner();



        //final Calendar calendar = Calendar.getInstance(Locale.CHINA);  此方法和上面的格力高日历本质是一样的
        if (setMode == MODE_ADD_ALARM){
            QCalendar = getCalendarAfter30Mins();

        }else {
            QCalendar = alarm.getAudreyCalendar();
        }

        final int mYear = QCalendar.get(Calendar.YEAR);
        final int mMonth = QCalendar.get(Calendar.MONTH);
        int mDay = QCalendar.get(Calendar.DAY_OF_MONTH);
        int mHour = QCalendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = QCalendar.get(Calendar.MINUTE);

        datePicker.init(mYear,mMonth,mDay,new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                QCalendar.set(year,monthOfYear,dayOfMonth);
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                QCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                QCalendar.set(Calendar.MINUTE, minute);
            }
        });




        add_alarm_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAlarm();
                done();
            }
        });


        delete_alarm_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AlarmActivity.this,"删除闹钟成功",Toast.LENGTH_SHORT).show();
            }
        });

    }


    public GregorianCalendar getCalendarAfter30Mins() {
        GregorianCalendar calendar = new GregorianCalendar();
        if (calendar.get(Calendar.MINUTE) >= 30) {
            calendar.add(Calendar.MINUTE, 60 - calendar.get(Calendar.MINUTE));
        } else {
            calendar.add(Calendar.MINUTE, 30 - calendar.get(Calendar.MINUTE));
        }

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }


    public void saveAlarm(){
        Bundle bundle = new Bundle();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        bundle.putString(Alarm.ALARM_ID, id);
        bundle.putBoolean(Alarm.ALARM_CANCELABLE,true);
        String alarmTag = alarm_tag.getText().toString().trim();
        if (alarmTag.equals("")){
            alarmTag = "闹钟";
        }
        bundle.putString(Alarm.ALARM_TAG,alarmTag);
        bundle.putSerializable(Alarm.ALARM_CALENDAR, QCalendar);
        bundle.putBoolean(Alarm.ALARM_AVAILABLE, true);
        if (ringTones != null && ringTones.length > 0){
            bundle.putString(Alarm.ALARM_RINGTONE,ringTones[ring_spinner.getSelectedItemPosition()]);
        }
        Alarm alarm = new Alarm(getApplicationContext(),bundle);
        Logger.d("bundle is null ? : "+bundle.toString());
        alarm.storeInDB();
        alarm.activate();

    }

    public void editAlarm(){
        String alarmTag = alarm_tag.getText().toString().trim();
        if (alarmTag.equals("")){
            alarmTag = "闹钟";
        }
        alarm.setAudreyCalendar(QCalendar);
        alarm.setTag(alarmTag);
        alarm.edit();
        alarm.activate();
    }


    public void setRingSpinner(){
        RingtoneManager ringtoneManager = new RingtoneManager(AlarmActivity.this);
        ringtoneManager.setType(RingtoneManager.TITLE_COLUMN_INDEX);
        Cursor cursor = ringtoneManager.getCursor();
        int num = cursor.getCount();
        if (num > 0){
            ringTones = new String[num];
            uris = new Uri[num];
            int idx = 0;
            if (setMode == MODE_ADD_ALARM) {
                if (cursor.moveToFirst()) {
                    do {
                        // TODO
                        ringTones[cursor.getPosition()] = cursor
                                .getString(RingtoneManager.TITLE_COLUMN_INDEX);
                    } while (cursor.moveToNext());
                }
            } else {
                // TODO
                if (cursor.moveToFirst()) {
                    do {
                        // TODO
                        int i=cursor.getPosition();
                        ringTones[i] = cursor
                                .getString(RingtoneManager.TITLE_COLUMN_INDEX);
                        if (alarm.getRingtoneId().equals(ringTones[i])) {
                            idx = i;
                        }
                    } while (cursor.moveToNext());
                }
            }
            spinnerAdapter = new ArrayAdapter<String>(AlarmActivity.this,
                    android.R.layout.simple_spinner_item, ringTones);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ring_spinner.setAdapter(spinnerAdapter);
            ring_spinner.setSelection(idx);
        }else {
            Toast.makeText(getApplicationContext(), "No Ringtones Found !",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void done() {
        Intent intent = new Intent(this, RemindActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
