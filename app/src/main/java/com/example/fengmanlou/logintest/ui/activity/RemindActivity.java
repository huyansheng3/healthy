package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.AlarmAdapter;
import com.example.fengmanlou.logintest.db.AlarmHelper;
import com.example.fengmanlou.logintest.entity.Alarm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengmanlou on 2015/5/1.
 */
public class RemindActivity extends Activity{
    private Button add_alarm;
    private ListView listView;
    private List<Alarm> alarmList;
    public static final int DELETE_ID = 1;
    private AlarmAdapter adapter;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);
        setTitle("就医提醒");
        add_alarm = (Button) findViewById(R.id.add_alarm);
        listView = (ListView) findViewById(android.R.id.list);
        alarmList = getAllAlarms(RemindActivity.this);
        adapter = new AlarmAdapter(RemindActivity.this,alarmList);
        registerForContextMenu(listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(RemindActivity.this,"不好意思，程序员太笨无法提供修改功能",Toast.LENGTH_SHORT).show();
            }
        });

        add_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemindActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });

    }

    private ArrayList<Alarm> getAllAlarms(Context context) {
        AlarmHelper helper = new AlarmHelper(context);
        return helper.getAlarms();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, "删除闹钟");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                // Delete the remote object
                final Alarm alarm = alarmList.get(info.position);
                alarm.deleteFromDB();
                refresh();

        }
        return super.onContextItemSelected(item);
    }

    public void refresh(){  //删除后刷新页面
        alarmList = getAllAlarms(RemindActivity.this);
        adapter = new AlarmAdapter(RemindActivity.this,alarmList);
        listView.setAdapter(adapter);
    }

}
