package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.example.fengmanlou.logintest.R;

import junit.framework.Test;

/**
 * Created by fengmanlou on 2015/5/11.
 */
public class TestActivity extends Activity  implements View.OnClickListener{
    private EditText otherIdEditText;
    private Button chatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        otherIdEditText = (EditText) findViewById(R.id.otherIdEditText);
        chatButton = (Button) findViewById(R.id.chatButton);
        chatButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        String otherId = otherIdEditText.getText().toString();
        if (TextUtils.isEmpty(otherId) == false) {
            final ChatManager chatManager = ChatManager.getInstance();
            chatManager.fetchConversationWithUserId(otherId, new AVIMConversationCreatedCallback() {
                @Override
                public void done(AVIMConversation conversation, AVException e) {
                    if (e != null) {
                        Toast.makeText(TestActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        chatManager.registerConversation(conversation);
                        Intent intent = new Intent(TestActivity.this, ChatRoomActivity.class);
                        intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
