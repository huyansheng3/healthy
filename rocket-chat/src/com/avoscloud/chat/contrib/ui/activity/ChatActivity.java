package com.avoscloud.chat.contrib.ui.activity;

import android.accounts.NetworkErrorException;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.avoscloud.chat.contrib.R;
import com.avoscloud.chat.contrib.adapter.ChatMsgAdapter;
import com.avoscloud.chat.contrib.adapter.EmotionGridAdapter;
import com.avoscloud.chat.contrib.adapter.EmotionPagerAdapter;
import com.avoscloud.chat.contrib.db.DBHelper;
import com.avoscloud.chat.contrib.db.DBMsg;
import com.avoscloud.chat.contrib.entity.Msg;
import com.avoscloud.chat.contrib.service.ChatService;
import com.avoscloud.chat.contrib.service.UserHelper;
import com.avoscloud.chat.contrib.service.listener.MsgListener;
import com.avoscloud.chat.contrib.service.receiver.MsgReceiver;
import com.avoscloud.chat.contrib.ui.view.EmotionEditText;
import com.avoscloud.chat.contrib.ui.view.RecordButton;
import com.avoscloud.chat.contrib.ui.view.xlist.XListView;
import com.avoscloud.chat.contrib.util.*;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity implements OnClickListener, MsgListener,
    XListView.IXListViewListener {
  private static final int IMAGE_REQUEST = 0;
  private static final int TAKE_CAMERA_REQUEST = 2;
  public static final int PAGE_SIZE = 20;

  private ChatMsgAdapter adapter;
  private List<Msg> msgs = new ArrayList<Msg>();
  DBHelper dbHelper;
  public static ChatActivity ctx;

  View chatTextLayout, chatAudioLayout, chatAddLayout, chatEmotionLayout;
  View turnToTextBtn, turnToAudioBtn, sendBtn, addImageBtn, showAddBtn, showEmotionBtn;
  LinearLayout chatBottomLayout;
  ViewPager emotionPager;
  private EmotionEditText contentEdit;
  private XListView xListView;
  RecordButton recordBtn;
  List<String> emotions = EmotionUtils.emotionTexts;
  private String localCameraPath = PathUtils.getTmpPath();
  private View addCameraBtn;
  int msgSize;

  public static final String CHAT_USER_ID = "chatUserId";
  String chatUserId;
  String audioId;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ctx = this;
    setContentView(R.layout.chat_layout);
    findView();
    initByIntent(getIntent());
  }

  private void initByIntent(Intent intent) {
    initData(intent);
    initActionBar();
    initEmotionPager();
    initRecordBtn();
    setEditTextChangeListener();

    initListView();
    setSoftInputMode();
    loadMsgsFromDB(true);
    ChatService.cancelNotification(ctx);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    initByIntent(intent);
  }

  private void initListView() {
    adapter = new ChatMsgAdapter(ctx, msgs);
    adapter.setDatas(msgs);
    xListView.setAdapter(adapter);
    xListView.setPullRefreshEnable(true);
    xListView.setPullLoadEnable(false);
    xListView.setXListViewListener(this);
    xListView.setOnScrollListener(
        new PauseOnScrollListener(ChatService.imageLoader, true, true));
  }

  private void initEmotionPager() {
    List<View> views = new ArrayList<View>();
    for (int i = 0; i < 2; i++) {
      views.add(getEmotionGridView(i));
    }
    EmotionPagerAdapter pagerAdapter = new EmotionPagerAdapter(views);
    emotionPager.setAdapter(pagerAdapter);
  }

  private View getEmotionGridView(int pos) {
    LayoutInflater inflater = LayoutInflater.from(ctx);
    View emotionView = inflater.inflate(R.layout.chat_emotion_gridview, null);
    GridView gridView = (GridView) emotionView.findViewById(R.id.gridview);
    EmotionGridAdapter emotionGridAdapter = new EmotionGridAdapter(ctx);
    List<String> pageEmotions;
    if (pos == 0) {
      pageEmotions = emotions.subList(0, 20);
    } else {
      pageEmotions = emotions.subList(20, emotions.size());
    }
    emotionGridAdapter.setDatas(pageEmotions);
    gridView.setAdapter(emotionGridAdapter);
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String emotionText = (String) parent.getAdapter().getItem(position);
        int start = contentEdit.getSelectionStart();
        CharSequence content = contentEdit.getText().insert(start, emotionText);
        contentEdit.setText(content);
        CharSequence info = contentEdit.getText();
        if (info instanceof Spannable) {
          Spannable spannable = (Spannable) info;
          Selection.setSelection(spannable, start + emotionText.length());
        }
      }
    });
    return gridView;
  }

  public void initRecordBtn() {
    setNewRecordPath();
    recordBtn.setOnFinishedRecordListener(new RecordButton.RecordEventListener() {
      @Override
      public void onFinishedRecord(final String audioPath, int secs) {
        final String objectId = audioId;
        new SendMsgTask(ctx) {
          @Override
          Msg sendMsg() throws Exception {
            return ChatService.sendAudioMsg(chatUserId, audioPath, objectId);
          }
        }.execute();
        setNewRecordPath();
      }

      @Override
      public void onStartRecord() {
      }
    });
  }

  public void setNewRecordPath() {
    audioId = Utils.uuid();
    String audioPath = PathUtils.getChatFile(audioId);
    recordBtn.setSavePath(audioPath);
  }

  public void setEditTextChangeListener() {
    contentEdit.addTextChangedListener(new SimpleTextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          showSendBtn();
        } else {
          showTurnToRecordBtn();
        }
      }
    });
  }

  private void showTurnToRecordBtn() {
    sendBtn.setVisibility(View.GONE);
    turnToAudioBtn.setVisibility(View.VISIBLE);
  }

  private void showSendBtn() {
    sendBtn.setVisibility(View.VISIBLE);
    turnToAudioBtn.setVisibility(View.GONE);
  }

  void initActionBar() {
    UserHelper userHelper = ChatService.getUserHelper();
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    View header = LayoutInflater.from(this).inflate(R.layout.chat_header, null);
    header.setBackgroundColor(ChatService.getBaseColor());
    TextView titleView = (TextView) header.findViewById(R.id.title);
    titleView.setText(userHelper.getDisplayName(chatUserId));
    actionBar.setCustomView(header, new ActionBar.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));
  }

  private void findView() {
    xListView = (XListView) findViewById(R.id.listview);
    addImageBtn = findViewById(R.id.addImageBtn);

    contentEdit = (EmotionEditText) findViewById(R.id.textEdit);
    chatTextLayout = findViewById(R.id.chatTextLayout);
    chatAudioLayout = findViewById(R.id.chatRecordLayout);
    chatBottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
    turnToAudioBtn = findViewById(R.id.turnToAudioBtn);
    turnToTextBtn = findViewById(R.id.turnToTextBtn);
    recordBtn = (RecordButton) findViewById(R.id.recordBtn);
    chatTextLayout = findViewById(R.id.chatTextLayout);
    chatAddLayout = findViewById(R.id.chatAddLayout);
    chatEmotionLayout = findViewById(R.id.chatEmotionLayout);
    showAddBtn = findViewById(R.id.showAddBtn);
    showEmotionBtn = findViewById(R.id.showEmotionBtn);
    sendBtn = findViewById(R.id.sendBtn);
    emotionPager = (ViewPager) findViewById(R.id.emotionPager);
    addCameraBtn = findViewById(R.id.addCameraBtn);

    sendBtn.setOnClickListener(this);
    contentEdit.setOnClickListener(this);
    addImageBtn.setOnClickListener(this);
    turnToAudioBtn.setOnClickListener(this);
    turnToTextBtn.setOnClickListener(this);
    showAddBtn.setOnClickListener(this);
    showEmotionBtn.setOnClickListener(this);
    addCameraBtn.setOnClickListener(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    MsgReceiver.addMsgListener(this);
  }

  @Override
  public void onPause() {
    super.onPause();
    MsgReceiver.removeMsgListener(this);
  }

  public void initData(Intent intent) {
    chatUserId = ChatService.getSelfId();
    dbHelper = new DBHelper(ctx, ChatService.DB_NAME, ChatService.DB_VER);
    msgSize = PAGE_SIZE;
    chatUserId = intent.getStringExtra(CHAT_USER_ID);
    ChatService.watchWithUserId(chatUserId, true);
  }

  public void loadMsgsFromDB(boolean showDialog) {
    new GetDataTask(ctx, showDialog, true).execute();
  }

  @Override
  public void onRefresh() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        msgSize += PAGE_SIZE;
        new GetDataTask(ctx, false, false).execute();
      }
    }, 1000);
  }

  @Override
  public void onLoadMore() {
  }

  @Override
  public boolean onMessageUpdate(String otherId) {
    if (otherId.equals(chatUserId)) {
      loadMsgsFromDB(false);
      return true;
    }
    return false;
  }

  public void resendMsg(final Msg resendMsg) {
    new SendMsgTask(ctx) {
      @Override
      Msg sendMsg() throws Exception {
        return ChatService.resendMsg(resendMsg);
      }
    }.execute();
  }

  class GetDataTask extends NetAsyncTask {
    List<Msg> msgs;
    boolean scrollToLast = true;

    GetDataTask(Context cxt, boolean openDialog, boolean scrollToLast) {
      super(cxt, openDialog);
      this.scrollToLast = scrollToLast;
    }

    @Override
    protected void doInBack() throws Exception {
      String convid = AVOSUtils.convid(ChatService.getSelfId(), chatUserId);
      msgs = DBMsg.getMsgs(dbHelper, convid, msgSize);
      DBMsg.markMsgsAsHaveRead(msgs);
    }

    @Override
    protected void onPost(Exception e) {
      if (e == null) {
        stopRefresh(xListView);
        addMsgsAndRefresh(msgs, scrollToLast);
      } else {
        Utils.toast(R.string.failedToGetData);
      }
    }
  }

  public static void stopRefresh(XListView xListView) {
    if (xListView.getPullRefreshing()) {
      xListView.stopRefresh();
    }
  }

  public void addMsgsAndRefresh(List<Msg> msgs, boolean scrollToLast) {
    int lastN = adapter.getCount();
    int newN = msgs.size();
    this.msgs = msgs;
    adapter.setDatas(this.msgs);
    adapter.notifyDataSetChanged();
    if (scrollToLast) {
      scrollToLast();
    } else {
      xListView.setSelection(newN - lastN - 1);
      if (lastN == newN) {
        Utils.toast(R.string.loadMessagesFinish);
      }
    }
    if (newN < PAGE_SIZE) {
      xListView.setPullRefreshEnable(false);
    } else {
      xListView.setPullRefreshEnable(true);
    }
  }

  @Override
  public void onClick(View v) {
    // TODO Auto-generated method stub
    int viewId = v.getId();
    if (viewId == R.id.sendBtn) {
      sendText();
    } else if (viewId == R.id.addImageBtn) {
      selectImageFromLocal();
    } else if (viewId == R.id.turnToAudioBtn) {
      showAudioLayout();
    } else if (viewId == R.id.turnToTextBtn) {
      showTextLayout();
    } else if (viewId == R.id.showAddBtn) {
      toggleBottomAddLayout();
    } else if (viewId == R.id.showEmotionBtn) {
      toggleEmotionLayout();
    } else if (viewId == R.id.textEdit) {
      hideBottomLayoutAndScrollToLast();
    } else if (viewId == R.id.addCameraBtn) {
      selectImageFromCamera();
    }
  }

  private void hideBottomLayoutAndScrollToLast() {
    hideBottomLayout();
    scrollToLast();
  }

  private void hideBottomLayout() {
    hideAddLayout();
    chatEmotionLayout.setVisibility(View.GONE);
  }

  private void toggleEmotionLayout() {
    if (chatEmotionLayout.getVisibility() == View.VISIBLE) {
      chatEmotionLayout.setVisibility(View.GONE);
    } else {
      chatEmotionLayout.setVisibility(View.VISIBLE);
      hideAddLayout();
      showTextLayout();
      hideSoftInputView();
    }
  }

  private void toggleBottomAddLayout() {
    if (chatAddLayout.getVisibility() == View.VISIBLE) {
      hideAddLayout();
    } else {
      chatEmotionLayout.setVisibility(View.GONE);
      hideSoftInputView();
      showAddLayout();
    }
  }

  private void hideAddLayout() {
    chatAddLayout.setVisibility(View.GONE);
  }

  private void showAddLayout() {
    chatAddLayout.setVisibility(View.VISIBLE);
  }

  private void showTextLayout() {
    chatTextLayout.setVisibility(View.VISIBLE);
    chatAudioLayout.setVisibility(View.GONE);
  }

  private void showAudioLayout() {
    chatTextLayout.setVisibility(View.GONE);
    chatAudioLayout.setVisibility(View.VISIBLE);
    chatEmotionLayout.setVisibility(View.GONE);
    hideSoftInputView();
  }

  public void selectImageFromLocal() {
    Intent intent;
    intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    startActivityForResult(intent, IMAGE_REQUEST);
  }

  public void selectImageFromCamera() {
    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    Uri imageUri = Uri.fromFile(new File(localCameraPath));
    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    startActivityForResult(openCameraIntent,
        TAKE_CAMERA_REQUEST);
  }

  private void sendText() {
    final String contString = contentEdit.getText().toString();
    if (TextUtils.isEmpty(contString) == false) {
      new SendMsgTask(ctx) {
        @Override
        Msg sendMsg() throws Exception {
          return ChatService.sendTextMsg(chatUserId, contString);
        }


        @Override
        protected void onPost(Exception e) {
          super.onPost(e);
          if (e == null) {
            contentEdit.setText("");
          }
        }
      }.execute();
    }
  }

  private String parsePathByReturnData(Intent data) {
    if (data == null) {
      return null;
    }
    String localSelectPath = null;
    Uri selectedImage = data.getData();
    if (selectedImage != null) {
      Cursor cursor = getContentResolver().query(
          selectedImage, null, null, null, null);
      cursor.moveToFirst();
      int columnIndex = cursor.getColumnIndex("_data");
      localSelectPath = cursor.getString(columnIndex);
      cursor.close();
    }
    return localSelectPath;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case IMAGE_REQUEST:
          String localSelectPath = parsePathByReturnData(data);
          sendImageByPath(localSelectPath);
          break;
        case TAKE_CAMERA_REQUEST:
          sendImageByPath(localCameraPath);
          break;
      }
    }
    hideBottomLayout();
    super.onActivityResult(requestCode, resultCode, data);
  }


  public abstract class SendMsgTask extends NetAsyncTask {
    Msg msg;

    protected SendMsgTask(Context cxt) {
      super(cxt, false);
    }

    @Override
    protected void doInBack() throws Exception {
      if (Connectivity.isConnected(ctx) == false) {
        throw new NetworkErrorException(ChatService.ctx.getString(R.string.pleaseCheckNetwork));
      } else if (ChatService.isSessionPaused()) {
        throw new NetworkErrorException(ChatService.ctx.getString(R.string.sessionPausedTips));
      } else {
        msg = sendMsg();
      }
    }

    @Override
    protected void onPost(Exception e) {
      if (e != null) {
        e.printStackTrace();
        Utils.toast(e.getMessage());
      } else {
        loadMsgsFromDB(false);
      }
    }

    abstract Msg sendMsg() throws Exception;
  }

  private void sendImageByPath(String localSelectPath) {
    final String objectId = Utils.uuid();
    final String newPath = PathUtils.getChatFile(objectId);
    //PhotoUtil.simpleCompressImage(localSelectPath,newPath);
    PhotoUtil.compressImage(localSelectPath, newPath);
    new SendMsgTask(ctx) {
      @Override
      Msg sendMsg() throws Exception {
        return ChatService.sendImageMsg(chatUserId, newPath, objectId);
      }
    }.execute();
  }

  public void scrollToLast() {
    xListView.setSelection(xListView.getCount() - 1);
  }

  @Override
  protected void onDestroy() {
    ChatService.watchWithUserId(chatUserId, false);
    ctx = null;
    super.onDestroy();
  }

}
