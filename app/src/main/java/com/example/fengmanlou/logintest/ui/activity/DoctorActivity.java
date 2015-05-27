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
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.DeviceAdapter;
import com.example.fengmanlou.logintest.adapter.DoctorAdapter;
import com.example.fengmanlou.logintest.avobject.Doctor;
import com.example.fengmanlou.logintest.service.DeviceService;
import com.example.fengmanlou.logintest.service.DoctorService;

import java.util.List;

/**
 * Created by fengmanlou on 2015/5/15.
 */
public class DoctorActivity extends Activity{
    private List<Doctor> doctorList;
    private Dialog progressDialog;
    private ListView listView;
    private String otherId;


    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        @Override
        protected Void doInBackground(Void... params) {
            doctorList = DoctorService.findDoctorList();
            return null;
        }

        @Override
        protected void onPreExecute() {
            DoctorActivity.this.progressDialog =
                    ProgressDialog.show(DoctorActivity.this, "", "Loading...", true);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // 展现ListView
            DoctorAdapter adapter = new DoctorAdapter(DoctorActivity.this,doctorList);
            listView.setAdapter(adapter);
            DoctorActivity.this.progressDialog.dismiss();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        setTitle("私人医生");
        listView = (ListView) findViewById(android.R.id.list);
        new RemoteDataTask().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                otherId = doctorList.get(position).getName();
                final ChatManager chatManager = ChatManager.getInstance();
                chatManager.fetchConversationWithUserId(otherId, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation conversation, AVException e) {
                        if (e != null) {
                            Toast.makeText(DoctorActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            chatManager.registerConversation(conversation);
                            Intent intent = new Intent(DoctorActivity.this, ChatRoomActivity.class);
                            intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                            startActivity(intent);
                        }
                    }
                });
            }
        });
       }


}
