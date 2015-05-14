package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.UserImageAdapter;
import com.example.fengmanlou.logintest.service.FamilyService;
import com.example.fengmanlou.logintest.util.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CountDownLatch;

/**
 * Created by fengmanlou on 2015/5/13.
 */
public class FamilyActivity extends Activity{
    private ListView listView;
    private UserImageAdapter adapter;
    public volatile List<AVUser> avUserList;
    private String otherId;
    private Dialog progressDialog;
    private static final int DELETE_HOME =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        setTitle("家庭群组");
        listView = (ListView) findViewById(android.R.id.list);
        registerForContextMenu(listView);
        InitListener();
        new RemoteDataTask().execute();
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        @Override
        protected Void doInBackground(Void... params) {
  /*          AVUser curUser = AVUser.getCurrentUser();
            AVQuery<AVUser> followeeQuery = AVUser.followeeQuery(curUser.getObjectId(),AVUser.class);
            followeeQuery.findInBackground(new FindCallback<AVUser>() {
                @Override
                public void done(List<AVUser> avUsers, AVException e) {
                    if (e == null) {
                        avUserList = avUsers;
                        Logger.d("avUserList" + avUserList);
                    }else {
                        Toast.makeText(FamilyActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });*/

            avUserList = FamilyService.findFamilies();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            FamilyActivity.this.progressDialog =
                    ProgressDialog.show(FamilyActivity.this, "", "Loading...", true);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // 展现ListView
            Logger.d("avUserList : "+avUserList);
            adapter = new UserImageAdapter(FamilyActivity.this,avUserList);
            listView.setAdapter(adapter);
        }
    }


    public void InitListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (avUserList.get(position).getString("nickname") != null) {
                    otherId = avUserList.get(position).getString("nickname");
                }
                Logger.d("otherId : " + otherId);
                final ChatManager chatManager = ChatManager.getInstance();
                chatManager.fetchConversationWithUserId(otherId, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation conversation, AVException e) {
                        if (e != null) {
                            Toast.makeText(FamilyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            chatManager.registerConversation(conversation);
                            Intent intent = new Intent(FamilyActivity.this, ChatRoomActivity.class);
                            intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

}
