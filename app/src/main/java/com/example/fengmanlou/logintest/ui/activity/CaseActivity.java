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

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.CaseAdapter;
import com.example.fengmanlou.logintest.adapter.DeviceAdapter;
import com.example.fengmanlou.logintest.avobject.Case;
import com.example.fengmanlou.logintest.service.CaseService;
import com.example.fengmanlou.logintest.service.DeviceService;
import com.example.fengmanlou.logintest.util.DateSyncUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by fengmanlou on 2015/5/14.
 */
public class CaseActivity extends Activity{  //病历夹
    private ListView listView;
    private Dialog progressDialog;
    private List<Case> caseList;

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        @Override
        protected Void doInBackground(Void... params) {
            caseList = CaseService.findCaseList();
            return null;
        }

        @Override
        protected void onPreExecute() {
            CaseActivity.this.progressDialog =
                    ProgressDialog.show(CaseActivity.this, "", "Loading...", true);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // 展现ListView
            CaseAdapter adapter = new CaseAdapter(CaseActivity.this,caseList);
            listView.setAdapter(adapter);
            CaseActivity.this.progressDialog.dismiss();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);
        setTitle("病历夹");
        listView = (ListView) findViewById(android.R.id.list);
        new RemoteDataTask().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CaseActivity.this,CaseDetailActivity.class);
                intent.putExtra("name",caseList.get(position).getName());
                intent.putExtra("age",caseList.get(position).getAge());
                intent.putExtra("hospital",caseList.get(position).getHospital());
                intent.putExtra("describe",caseList.get(position).getDescribe());
                Date date = caseList.get(position).getCreatedAt();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = simpleDateFormat.format(date);
                intent.putExtra("time",dateString);
                startActivity(intent);
            }
        });
    }
}
