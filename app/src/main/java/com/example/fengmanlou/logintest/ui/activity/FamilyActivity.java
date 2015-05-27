package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.FamilyAdapter;
import com.example.fengmanlou.logintest.adapter.UserImageAdapter;
import com.example.fengmanlou.logintest.service.FamilyService;
import com.example.fengmanlou.logintest.service.NewsService;
import com.example.fengmanlou.logintest.util.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
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
    private FamilyAdapter adapter;
    public volatile List<AVUser> avUserList;
    public List<AVUser> familyList;
    private String otherId; //表示其他用户的nickname！
    private Dialog progressDialog;
    private List<String> objectIdList;
    private String otherObjectId;
    private static final int DELETE_HOME =1;
    private TextView name_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        setTitle("家庭群组");
        listView = (ListView) findViewById(android.R.id.list);
        name_textView = (TextView) findViewById(R.id.user_items_nickname);
        registerForContextMenu(listView);
        InitListener();

        objectIdList = new ArrayList<>();
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
            for (AVUser avUser : avUserList){
                objectIdList.add(avUser.getObjectId());
            }
            Logger.d("objectIdList : "+objectIdList);

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
            adapter = new FamilyAdapter(FamilyActivity.this,objectIdList);
            listView.setAdapter(adapter);
        }
    }


    public void InitListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                otherObjectId = objectIdList.get(position); //获得其他用户的objectId

                if (otherObjectId != null){

                    GetCallback<AVObject> getCallback = new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (e == null) {
                                 otherId = avObject.getString("nickname");
                                //  Log.i("hys","nickname : "+avObject.getString("nickname"));
                            } else {
                                e.getMessage();
                            }
                        }
                    };
                    NewsService.fetchUserById(otherObjectId, getCallback);

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_HOME, 0, "删除群组");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_HOME:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                // 把这个用户添加到家庭群组中
                final AVUser user = avUserList.get(info.position); //获得被操作的User
                AVUser curUser = AVUser.getCurrentUser();

                curUser.unfollowInBackground(user.getObjectId(), new FollowCallback() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (e == null) {
                            Toast.makeText(FamilyActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    protected void internalDone0(Object o, AVException e) {
                        Toast.makeText(FamilyActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                });

        }
        return super.onContextItemSelected(item);
    }
}
