package com.example.fengmanlou.logintest.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.NewsAdapter;
import com.example.fengmanlou.logintest.adapter.SystemImageAdapter;
import com.example.fengmanlou.logintest.service.NewsService;
import com.example.fengmanlou.logintest.ui.activity.ChatRoomActivity;
import com.example.fengmanlou.logintest.ui.activity.TestActivity;
import com.example.fengmanlou.logintest.util.Logger;

import java.util.List;

/**
 * Created by fengmanlou on 2015/4/4.
 */
public class SystemFragment extends Fragment {

    private ListView listView;
    private SystemImageAdapter adapter;
    private List<AVUser> avUserList;
    private String otherId;
    private String selfId;
    private Dialog progressDialog;

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
        View view = inflater.inflate(R.layout.system_fragment, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        new RemoteDataTask().execute();
        InitListener();
        return view;
    }


    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        @Override
        protected Void doInBackground(Void... params) {
            AVQuery<AVUser> query = AVQuery.getQuery(AVUser.class);
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
            adapter = new SystemImageAdapter(getActivity(),avUserList);
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

