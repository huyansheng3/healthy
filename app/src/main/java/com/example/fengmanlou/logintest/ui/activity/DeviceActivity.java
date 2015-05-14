package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.DeviceAdapter;
import com.example.fengmanlou.logintest.adapter.NewsAdapter;
import com.example.fengmanlou.logintest.avobject.Device;
import com.example.fengmanlou.logintest.service.DeviceService;
import com.example.fengmanlou.logintest.service.NewsService;

import java.util.List;

/**
 * Created by fengmanlou on 2015/5/14.
 */
public class DeviceActivity extends Activity {
    private Dialog progressDialog;
    private List<Device> deviceList;
    private ListView listView;

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        @Override
        protected Void doInBackground(Void... params) {
            deviceList = DeviceService.findDeviceList();
            return null;
        }

        @Override
        protected void onPreExecute() {
            DeviceActivity.this.progressDialog =
                    ProgressDialog.show(DeviceActivity.this, "", "Loading...", true);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // 展现ListView
            DeviceAdapter adapter = new DeviceAdapter(DeviceActivity.this,deviceList);
            listView.setAdapter(adapter);
            DeviceActivity.this.progressDialog.dismiss();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        setTitle("设备租赁");
        listView = (ListView) findViewById(android.R.id.list);
        new RemoteDataTask().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(DeviceActivity.this,DeviceDetailActivity.class);
                i.putExtra("objectId",deviceList.get(position).getObjectId());
                i.putExtra("title",deviceList.get(position).getTitle());
                i.putExtra("describe",deviceList.get(position).getDescribe());
                i.putExtra("count",deviceList.get(position).getCount());
                if (deviceList.get((int)id).getAVFile("image") != null) {
                    i.putExtra("imageUrl", deviceList.get((int) id).getAVFile("image").getUrl());
                }
                startActivity(i);
            }
        });
    }
}
