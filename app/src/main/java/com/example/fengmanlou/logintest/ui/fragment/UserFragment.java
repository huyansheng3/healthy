package com.example.fengmanlou.logintest.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.UserImageAdapter;
import com.example.fengmanlou.logintest.avobject.Hang;
import com.example.fengmanlou.logintest.ui.activity.ChatRoomActivity;
import com.example.fengmanlou.logintest.util.Logger;

import java.util.List;

/**
 * Created by fengmanlou on 2015/4/4.
 */
public class UserFragment extends Fragment {

    private ListView listView;
    private UserImageAdapter adapter;
    private List<AVUser> avUserList;
    private String otherId;
    private String selfId;
    private Dialog progressDialog;
    private static final int ADD_HOME =2;

    @Override
    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("SystemFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("SystemFragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        new RemoteDataTask().execute();
        registerForContextMenu(listView);
        InitListener();
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, ADD_HOME, 0, "添加群组");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ADD_HOME:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                // 把这个用户添加到家庭群组中
                final AVUser user = avUserList.get(info.position); //获得被操作的User
                AVUser curUser = AVUser.getCurrentUser();

                curUser.followInBackground(user.getObjectId(),new FollowCallback() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (e == null){
                            Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    protected void internalDone0(Object o, AVException e) {
                        Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT).show();
                    }
                });

        }
        return super.onContextItemSelected(item);
    }



    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        @Override
        protected Void doInBackground(Void... params) {
            AVQuery<AVUser> query = AVQuery.getQuery(AVUser.class);
            query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
            query.whereNotEqualTo("objectId",AVUser.getCurrentUser().getObjectId());
            query.findInBackground(new FindCallback<AVUser>() {
                @Override
                public void done(List<AVUser> avUsers, AVException e) {
                    if (e == null){
                        avUserList = avUsers;
                    }else {
                        Logger.d("avUsers are null");
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // 展现ListView
            adapter = new UserImageAdapter(getActivity(),avUserList);
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
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    } else {
                                        chatManager.registerConversation(conversation);
                                        Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                                        intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                                        startActivity(intent);
                                    }
                                }
                            });
            }
        });
    }

    public void goActivity(Context context, Class<? extends android.app.Activity> anotherclass){
        Intent intent = new Intent(context,anotherclass);
        context.startActivity(intent);
    }

}

