package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVUser;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.AlarmAdapter;
import com.example.fengmanlou.logintest.adapter.CommentAdapter;
import com.example.fengmanlou.logintest.adapter.HangAdapter;
import com.example.fengmanlou.logintest.avobject.Hang;
import com.example.fengmanlou.logintest.entity.Alarm;
import com.example.fengmanlou.logintest.service.HangService;
import com.example.fengmanlou.logintest.service.NewsService;
import com.example.fengmanlou.logintest.util.ListViewUtils;

import java.util.List;

/**
 * Created by fengmanlou on 2015/5/5.
 */
public class HangListActivity extends Activity{
    private List<Hang> hangList;
    private AVUser curUser;
    private Dialog progressDialog;
    private ListView listView;
    private HangAdapter adapter;
    public static final int DELETE_ID = 1;
    private static  final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hang_list);
        setTitle("自助挂号");
        curUser = AVUser.getCurrentUser();
        listView = (ListView) findViewById(android.R.id.list);
        registerForContextMenu(listView);
        new RemoteHangTask().execute();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, "删除挂号");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                // Delete the remote object
                final Hang hang = hangList.get(info.position);
                hang.deleteInBackground();
                refresh();

        }
        return super.onContextItemSelected(item);
    }

    public void refresh(){  //删除后刷新页面
        new RemoteHangTask().execute();
        adapter = new HangAdapter(HangListActivity.this,hangList);
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_hang,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_hang){
            Intent intent = new Intent(this,HangActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class RemoteHangTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        @Override
        protected Void doInBackground(Void... params) {
            hangList = HangService.findHangList(curUser.getObjectId());
            Log.i("hys", "hangList size :" + hangList.size());
            return null;
        }

        @Override
        protected void onPreExecute() {
            HangListActivity.this.progressDialog =
                    ProgressDialog.show(HangListActivity.this, "", "Loading...", true);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // 展现ListView
            adapter = new HangAdapter(HangListActivity.this,hangList);
            listView.setAdapter(adapter);
            HangListActivity.this.progressDialog.dismiss();
        }
    }
}
