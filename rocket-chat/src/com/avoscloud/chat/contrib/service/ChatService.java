package com.avoscloud.chat.contrib.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;
import com.alibaba.fastjson.JSONException;
import com.avos.avoscloud.*;
import com.avoscloud.chat.contrib.db.DBHelper;
import com.avoscloud.chat.contrib.db.DBMsg;
import com.avoscloud.chat.contrib.entity.Conversation;
import com.avoscloud.chat.contrib.entity.Msg;
import com.avoscloud.chat.contrib.service.listener.MsgListener;
import com.avoscloud.chat.contrib.service.receiver.MsgReceiver;
import com.avoscloud.chat.contrib.ui.activity.ChatActivity;
import com.avoscloud.chat.contrib.util.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.avoscloud.chat.contrib.R;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by lzw on 14-7-9.
 */
public class ChatService {
  public static final String DB_NAME = "chat_contrib.db3";
  public static final int DB_VER = 1;
  private static final int REPLY_NOTIFY_ID = 1;
  private static String selfId;
  private static boolean debug = true;
  public static Context ctx;
  private static UserHelper userHelper;
  public static ImageLoader imageLoader = ImageLoader.getInstance();
  private static boolean showIconAtChatRoom = false;
  private static boolean useSignature = false;
  private static final long NOTIFY_PEROID = 1000;
  static long lastNotifyTime = 0;
  static int baseColorResId = R.color.common_blue;
  static int rightBubbleResId = R.drawable.chat_right_qp;
  static int leftVoiceResId = R.drawable.voice_left;

  public static String getSelfId() {
    return selfId;
  }

  public static boolean isDebug() {
    return debug;
  }

  public static void setDebug(boolean debug) {
    ChatService.debug = debug;
  }

  public static boolean isShowIconAtChatRoom() {
    return showIconAtChatRoom;
  }

  public static void setShowIconAtChatRoom(boolean showIconAtChatRoom) {
    ChatService.showIconAtChatRoom = showIconAtChatRoom;
  }

  public static int getRightBubbleResId() {
    return rightBubbleResId;
  }

  public static void setRightBubbleResId(int rightBubbleResId) {
    ChatService.rightBubbleResId = rightBubbleResId;
  }

  public static int getBaseColorResId() {
    return baseColorResId;
  }

  public static int getBaseColor() {
    return ctx.getResources().getColor(baseColorResId);
  }

  public static void setLeftVoiceResId(int leftVoiceResId) {
    ChatService.leftVoiceResId = leftVoiceResId;
  }

  public static int getLeftVoiceResId() {
    return leftVoiceResId;
  }

  public static void setBaseColorResId(int baseColorResId) {
    ChatService.baseColorResId = baseColorResId;
  }

  public static boolean isUseSignature() {
    return useSignature;
  }

  public static void setUseSignature(boolean useSignature) {
    ChatService.useSignature = useSignature;
  }

  public static UserHelper getUserHelper() {
    return userHelper;
  }

  public static void setUserHelper(UserHelper userHelper) {
    ChatService.userHelper = userHelper;
  }

  public static void withUsersToWatch(List<String> userIds, boolean watch) {
    String selfId = getSelfId();
    Session session = SessionManager.getInstance(selfId);
    if (watch) {
      session.watchPeers(userIds);
    } else {
      session.unwatchPeers(userIds);
    }
  }

  public static void watchWithUserId(String userId, boolean watch) {
    List<String> users = new ArrayList<String>();
    users.add(userId);
    withUsersToWatch(users, watch);
  }

  public static Session getSession() {
    return SessionManager.getInstance(ChatService.getSelfId());
  }

  public static Msg sendAudioMsg(String toString, String path, String msgId) throws IOException, AVException {
    return sendFileMsg(toString, msgId, Msg.Type.Audio, path);
  }

  public static Msg sendImageMsg(String chatUser, String filePath, String msgId) throws IOException, AVException {
    return sendFileMsg(chatUser, msgId, Msg.Type.Image, filePath);
  }

  public static Msg sendFileMsg(String toString, String objectId, Msg.Type type, String filePath) throws IOException, AVException {
    AVFile file = AVFile.withAbsoluteLocalPath(objectId, filePath);
    file.save();
    String url = file.getUrl();
    Msg msg = createAndSendMsgWithId(toString, type, url, objectId);
    DBMsg.insertMsg(msg);
    return msg;
  }

  public static Msg sendTextMsg(String toString, String content) {
    Msg.Type type = Msg.Type.Text;
    Msg msg = sendMsgAndInsertDB(toString, type, content);
    return msg;
  }

  public static Msg createAndSendMsg(String toPeer, Msg.Type type, String content) {
    return createAndSendMsgWithId(toPeer, type, content, Utils.uuid());
  }

  public static Msg sendMsgAndInsertDB(String toPeer, Msg.Type type, String content) {
    Msg msg = createAndSendMsg(toPeer, type, content);
    DBMsg.insertMsg(msg);
    return msg;
  }

  public static Msg createAndSendMsgWithId(String toPeerId, Msg.Type type, String content, String objectId) {
    Msg msg;
    msg = new Msg();
    msg.setStatus(Msg.Status.SendStart);
    msg.setContent(content);
    msg.setTimestamp(System.currentTimeMillis());
    msg.setReadStatus(Msg.ReadStatus.HaveRead);
    msg.setFromPeerId(getSelfId());
    String convid;
    msg.setToPeerId(toPeerId);
    convid = AVOSUtils.convid(ChatService.getSelfId(), toPeerId);
    msg.setRequestReceipt(true);
    msg.setObjectId(objectId);
    msg.setConvid(convid);
    msg.setType(type);
    return sendMessage(msg);
  }

  public static Msg sendMessage(Msg msg) {
    AVMessage avMsg = msg.toAVMessage();
    Session session = getSession();
    session.sendMessage(avMsg);
    return msg;
  }

  public static void openSession(String selfId) {
    ChatService.selfId = selfId;
    ChatService.setSelfId(selfId);
    Session session = getSession();
    if (useSignature) {
      session.setSignatureFactory(new SignatureFactory());
    }
    if (session.isOpen() == false) {
      session.open(new LinkedList<String>());
    }
  }

  private static void setSelfId(String selfId) {
    ChatService.selfId = selfId;
  }

  public static List<Conversation> getConversations() throws AVException {
    List<Msg> msgs = DBMsg.getRecentMsgs(ChatService.getSelfId());
    ArrayList<Conversation> conversations = new ArrayList<Conversation>();
    DBHelper dbHelper = ChatService.getDBHelper();
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    for (Msg msg : msgs) {
      Conversation conversation = new Conversation();
      String chatUserId = msg.getOtherId();
      conversation.setToUserId(chatUserId);
      conversation.setMsg(msg);
      conversation.setUnreadCount(DBMsg.getUnreadCount(db, msg.getConvid()));
      conversations.add(conversation);
    }
    db.close();
    return conversations;
  }

  private static DBHelper getDBHelper() {
    return new DBHelper(ctx, DB_NAME, DB_VER);
  }


  public static void closeSession() {
    Session session = ChatService.getSession();
    if (session.isOpen()) {
      session.close();
    }
  }

  public static void notifyMsg(Context context, Msg msg) throws JSONException {
    if (System.currentTimeMillis() - lastNotifyTime < NOTIFY_PEROID) {
      return;
    } else {
      lastNotifyTime = System.currentTimeMillis();
    }
    int icon = context.getApplicationInfo().icon;
    Intent intent;
    intent = getUserChatIntent(context, msg.getFromPeerId());
    //why Random().nextInt()
    //http://stackoverflow.com/questions/13838313/android-onnewintent-always-receives-same-intent
    PendingIntent pend = PendingIntent.getActivity(context, new Random().nextInt(),
        intent, 0);
    Notification.Builder builder = new Notification.Builder(context);
    CharSequence notifyContent = msg.getNotifyContent();
    CharSequence username = msg.getFromName();
    builder.setContentIntent(pend)
        .setSmallIcon(icon)
        .setWhen(System.currentTimeMillis())
        .setTicker(username + "\n" + notifyContent)
        .setContentTitle(username)
        .setContentText(notifyContent)
        .setAutoCancel(true);
    NotificationManager man = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    Notification notification = builder.getNotification();
    PreferenceMap preferenceMap = PreferenceMap.getCurUserPrefDao(context);
    if (preferenceMap.isVoiceNotify()) {
      notification.defaults |= Notification.DEFAULT_SOUND;
    }
    if (preferenceMap.isVibrateNotify()) {
      notification.defaults |= Notification.DEFAULT_VIBRATE;
    }
    man.notify(REPLY_NOTIFY_ID, notification);
  }

  public static void onMessage(Context context, AVMessage avMsg, Set<MsgListener> listeners) {
    final Msg msg = Msg.fromAVMessage(avMsg);
    String convid;
    String selfId = getSelfId();
    msg.setToPeerId(selfId);
    convid = AVOSUtils.convid(selfId, msg.getFromPeerId());
    msg.setStatus(Msg.Status.SendReceived);
    msg.setConvid(convid);
    msg.setReadStatus(Msg.ReadStatus.Unread);
    handleReceivedMsg(context, msg, listeners);
  }

  public static void handleReceivedMsg(final Context context, final Msg msg, final Set<MsgListener> listeners) {
    new NetAsyncTask(context, false) {
      @Override
      protected void doInBack() throws Exception {
        if (msg.getType() == Msg.Type.Audio) {
          File file = new File(msg.getAudioPath());
          String url = msg.getContent();
          Utils.downloadFileIfNotExists(url, file);
        }
      }

      @Override
      protected void onPost(Exception e) {
        if (e != null) {
          Utils.toast(context, com.avoscloud.chat.contrib.R.string.badNetwork);
        } else {
          DBMsg.insertMsg(msg);
          String otherId = msg.getFromPeerId();
          boolean done = false;
          for (MsgListener listener : listeners) {
            if (listener.onMessageUpdate(otherId)) {
              done = true;
              break;
            }
          }
          if (!done) {
            if (ChatService.getSelfId() != null) {
              PreferenceMap preferenceMap = PreferenceMap.getCurUserPrefDao(context);
              if (preferenceMap.isNotifyWhenNews()) {
                notifyMsg(context, msg);
              }
            }
          }
        }
      }
    }.execute();
  }

  public static void onMessageSent(AVMessage avMsg, Set<MsgListener> listeners) {
    Msg msg = Msg.fromAVMessage(avMsg);
    DBMsg.updateStatusAndTimestamp(msg.getObjectId(), Msg.Status.SendSucceed, msg.getTimestamp());
    String otherId = msg.getToPeerId();
    for (MsgListener msgListener : listeners) {
      if (msgListener.onMessageUpdate(otherId)) {
        break;
      }
    }
  }

  public static void onMessageFailure(AVMessage avMsg, Set<MsgListener> msgListeners) {
    Msg msg = Msg.fromAVMessage(avMsg);
    DBMsg.updateStatus(msg.getObjectId(), Msg.Status.SendFailed);
    String otherId = msg.getToPeerId();
    for (MsgListener msgListener : msgListeners) {
      if (msgListener.onMessageUpdate(otherId)) {
        break;
      }
    }
  }

  public static Msg resendMsg(Msg msg) {
    msg.setRequestReceipt(true);
    sendMessage(msg);
    DBMsg.updateStatus(msg.getObjectId(), Msg.Status.SendStart);
    return msg;
  }

  public static void cancelNotification(Context ctx) {
    Utils.cancelNotification(ctx, REPLY_NOTIFY_ID);
  }

  public static void onMessageDelivered(AVMessage avMsg, Set<MsgListener> listeners) {
    Msg msg = Msg.fromAVMessage(avMsg);
    DBMsg.updateStatus(msg.getObjectId(), Msg.Status.SendReceived);
    String otherId = msg.getToPeerId();
    for (MsgListener listener : listeners) {
      if (listener.onMessageUpdate(otherId)) {
        break;
      }
    }
  }

  public static void displayAvatar(String imageUrl, ImageView avatarView) {
    imageLoader.displayImage(imageUrl, avatarView, PhotoUtil.avatarImageOptions);
  }

  public static void init(Context ctx) {
    ChatService.ctx = ctx;
    AVOSCloud.setDebugLogEnabled(debug);
    if (debug) {
      Logger.level = Logger.VERBOSE;
    } else {
      Logger.level = Logger.NONE;
    }
    initImageLoader(ctx);
  }

  public static void initImageLoader(Context context) {
    File cacheDir = StorageUtils.getOwnCacheDirectory(context,
        PathUtils.getPackageName() + "/Cache");
    ImageLoaderConfiguration config = PhotoUtil.getImageLoaderConfig(context, cacheDir);
    ImageLoader.getInstance().init(config);
  }

  public static void goUserChat(Activity ctx, String userId) {
    Intent intent = getUserChatIntent(ctx, userId);
    ctx.startActivity(intent);
  }

  public static Intent getUserChatIntent(Context ctx, String userId) {
    Intent intent = new Intent(ctx, ChatActivity.class);
    intent.putExtra(ChatActivity.CHAT_USER_ID, userId);
    return intent;
  }

  public static boolean isSessionPaused() {
    return MsgReceiver.isSessionPaused();
  }


}
