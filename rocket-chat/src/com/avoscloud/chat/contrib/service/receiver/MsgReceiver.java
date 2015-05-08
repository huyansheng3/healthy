package com.avoscloud.chat.contrib.service.receiver;

import android.content.Context;
import com.avos.avoscloud.AVMessage;
import com.avos.avoscloud.AVMessageReceiver;
import com.avos.avoscloud.Session;
import com.avoscloud.chat.contrib.service.ChatService;
import com.avoscloud.chat.contrib.service.listener.MsgListener;
import com.avoscloud.chat.contrib.util.AVOSUtils;
import com.avoscloud.chat.contrib.util.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lzw on 14-8-7.
 */
public class MsgReceiver extends AVMessageReceiver {
  public static Set<MsgListener> msgListeners = new HashSet<MsgListener>();
  private static boolean sessionPaused = true;

  public static boolean isSessionPaused() {
    return sessionPaused;
  }

  public static void setSessionPaused(boolean sessionPaused) {
    MsgReceiver.sessionPaused = sessionPaused;
  }

  @Override
  public void onSessionOpen(Context context, Session session) {
    Logger.d("onSessionOpen");
    sessionPaused = false;
  }

  @Override
  public void onSessionPaused(Context context, Session session) {
    Logger.d("onSessionPaused");
    sessionPaused = true;
  }

  @Override
  public void onSessionResumed(Context context, Session session) {
    Logger.d("onSessionResumed");
    sessionPaused = false;
  }

  @Override
  public void onPeersWatched(Context context, Session session, List<String> peerIds) {
    if (peerIds.size() != 1) {
      throw new IllegalStateException("the size of watched peers isn't 1");
    }
    Logger.d("watched " + peerIds);
  }

  @Override
  public void onPeersUnwatched(Context context, Session session, List<String> peerIds) {
    Logger.d("unwatch " + peerIds);
  }

  @Override
  public void onMessage(final Context context, Session session, AVMessage avMsg) {
    Logger.d("onMessage");
    AVOSUtils.logAVMessage(avMsg);
    ChatService.onMessage(context, avMsg, msgListeners);
  }

  @Override
  public void onMessageSent(Context context, Session session, AVMessage avMsg) {
    Logger.d("onMessageSent");
    AVOSUtils.logAVMessage(avMsg);
    ChatService.onMessageSent(avMsg, msgListeners);
  }

  @Override
  public void onMessageDelivered(Context context, Session session, AVMessage msg) {
    Logger.d("onMessageDelivered");
    AVOSUtils.logAVMessage(msg);
    ChatService.onMessageDelivered(msg, msgListeners);
  }

  @Override
  public void onMessageFailure(Context context, Session session, AVMessage avMsg) {
    Logger.d("onMessageFailure");
    AVOSUtils.logAVMessage(avMsg);
    ChatService.onMessageFailure(avMsg, msgListeners);
  }

  @Override
  public void onStatusOnline(Context context, Session session, List<String> peerIds) {

  }

  @Override
  public void onStatusOffline(Context context, Session session, List<String> peerIds) {

  }

  @Override
  public void onError(Context context, Session session, Throwable throwable) {
    //Utils.toast(context, throwable.getMessage());
    throwable.printStackTrace();
  }

    @Override
    public void onSessionClose(Context context, Session session) {

    }

    public static void addMsgListener(MsgListener listener) {
    msgListeners.add(listener);
  }

  public static void removeMsgListener(MsgListener listener) {
    msgListeners.remove(listener);
  }

}
