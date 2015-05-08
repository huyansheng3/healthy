package com.avoscloud.chat.contrib.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.avoscloud.chat.contrib.R;
import com.avoscloud.chat.contrib.entity.Msg;
import com.avoscloud.chat.contrib.service.ChatService;
import com.avoscloud.chat.contrib.service.UserHelper;
import com.avoscloud.chat.contrib.ui.activity.ChatActivity;
import com.avoscloud.chat.contrib.ui.activity.ImageBrowerActivity;
import com.avoscloud.chat.contrib.ui.view.PlayButton;
import com.avoscloud.chat.contrib.ui.view.ViewHolder;
import com.avoscloud.chat.contrib.util.EmotionUtils;
import com.avoscloud.chat.contrib.util.PathUtils;
import com.avoscloud.chat.contrib.util.PhotoUtil;
import com.avoscloud.chat.contrib.util.TimeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;

public class ChatMsgAdapter extends BaseListAdapter<Msg> {
  int msgViewTypes = 6;

  public static interface MsgViewType {
    int COME_TEXT = 0;
    int TO_TEXT = 1;
    int COME_IMAGE = 2;
    int TO_IMAGE = 3;
    int COME_AUDIO = 4;
    int TO_AUDIO = 5;
  }

  ChatActivity chatActivity;

  public ChatMsgAdapter(ChatActivity chatActivity, List<Msg> datas) {
    super(chatActivity, datas);
    this.chatActivity = chatActivity;
  }

  public int getItemPosById(String objectId) {
    for (int i = 0; i < getCount(); i++) {
      Msg itemMsg = datas.get(i);
      if (itemMsg.getObjectId().equals(objectId)) {
        return i;
      }
    }
    return -1;
  }

  public Msg getItem(String objectId) {
    for (Msg msg : datas) {
      if (msg.getObjectId().equals(objectId)) {
        return msg;
      }
    }
    return null;
  }

  public int getItemViewType(int position) {
    Msg entity = datas.get(position);
    boolean comeMsg = entity.isComeMessage();
    Msg.Type type = entity.getType();
    switch (type) {
      case Text:
        return comeMsg ? MsgViewType.COME_TEXT : MsgViewType.TO_TEXT;
      case Image:
        return comeMsg ? MsgViewType.COME_IMAGE : MsgViewType.TO_IMAGE;
      case Audio:
        return comeMsg ? MsgViewType.COME_AUDIO : MsgViewType.TO_AUDIO;
    }
    throw new IllegalStateException("position's type is wrong");
  }

  public int getViewTypeCount() {
    return msgViewTypes;
  }

  public View getView(int position, View conView, ViewGroup parent) {
    Msg msg = datas.get(position);
    int itemViewType = getItemViewType(position);
    boolean isComMsg = msg.isComeMessage();
    if (conView == null) {
      conView = createViewByType(itemViewType);
    }
    TextView sendTimeView = ViewHolder.findViewById(conView, R.id.sendTimeView);
    TextView contentView = ViewHolder.findViewById(conView, R.id.textContent);
    ImageView imageView = ViewHolder.findViewById(conView, R.id.imageView);
    ImageView avatarView = ViewHolder.findViewById(conView, R.id.avatar);
    PlayButton playBtn = ViewHolder.findViewById(conView, R.id.playBtn);
    View contentLayout=ViewHolder.findViewById(conView,R.id.contentLayout);

    View statusSendFailed = ViewHolder.findViewById(conView, R.id.status_send_failed);
    View statusSendSucceed = ViewHolder.findViewById(conView, R.id.status_send_succeed);
    View statusSendStart = ViewHolder.findViewById(conView, R.id.status_send_start);

    // timestamp
    if (position == 0 || TimeUtils.haveTimeGap(datas.get(position - 1).getTimestamp(),
        msg.getTimestamp())) {
      sendTimeView.setVisibility(View.VISIBLE);
      sendTimeView.setText(TimeUtils.millisecs2DateString(msg.getTimestamp()));
    } else {
      sendTimeView.setVisibility(View.GONE);
    }

    String fromPeerId = msg.getFromPeerId();
    UserHelper userHelper = ChatService.getUserHelper();
    ChatService.displayAvatar(userHelper.getDisplayAvatarUrl(fromPeerId), avatarView);

    Msg.Type type = msg.getType();
    if (type == Msg.Type.Text) {
      contentView.setText(EmotionUtils.replace(ctx, msg.getContent()));
    } else if (type == Msg.Type.Image) {
      String localPath = PathUtils.getChatFileDir() + msg.getObjectId();
      String url = msg.getContent();
      displayImageByUri(imageView, localPath, url);
      setImageOnClickListener(localPath, url, imageView);
    } else if (type == Msg.Type.Audio) {
      initPlayBtn(msg, playBtn);
    }
    if (isComMsg == false) {
      hideStatusViews(statusSendStart, statusSendFailed, statusSendSucceed);
      setSendFailedBtnListener(statusSendFailed, msg);
      switch (msg.getStatus()) {
        case SendFailed:
          statusSendFailed.setVisibility(View.VISIBLE);
          break;
        case SendSucceed:
          statusSendSucceed.setVisibility(View.VISIBLE);
          break;
        case SendStart:
          statusSendStart.setVisibility(View.VISIBLE);
          break;
      }
      if(contentLayout!=null){
        contentLayout.setBackgroundResource(ChatService.getRightBubbleResId());
      }
    }
    return conView;
  }

  private void setSendFailedBtnListener(View statusSendFailed, final Msg msg) {
    statusSendFailed.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        chatActivity.resendMsg(msg);
      }
    });
  }

  private void hideStatusViews(View statusSendStart, View statusSendFailed, View statusSendSucceed) {
    statusSendFailed.setVisibility(View.GONE);
    statusSendStart.setVisibility(View.GONE);
    statusSendSucceed.setVisibility(View.GONE);
  }

  private void initPlayBtn(Msg msg, PlayButton playBtn) {
    playBtn.setPath(msg.getAudioPath());
  }

  private void setImageOnClickListener(final String path, final String url, ImageView imageView) {
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ctx, ImageBrowerActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("url", url);
        ctx.startActivity(intent);
      }
    });
  }

  public static void displayImageByUri(ImageView imageView,
                                       String localPath, String url) {
    File file = new File(localPath);
    ImageLoader imageLoader = ChatService.imageLoader;
    if (file.exists()) {
      imageLoader.displayImage("file://" + localPath, imageView, PhotoUtil.normalImageOptions);
    } else {
      imageLoader.displayImage(url, imageView, PhotoUtil.normalImageOptions);
    }
  }

  public View createViewByType(int itemViewType) {
    int[] types = new int[]{MsgViewType.COME_TEXT, MsgViewType.TO_TEXT,
        MsgViewType.COME_IMAGE, MsgViewType.TO_IMAGE, MsgViewType.COME_AUDIO,
        MsgViewType.TO_AUDIO};
    int[] layoutIds = new int[]{
        R.layout.chat_item_msg_text_left,
        R.layout.chat_item_msg_text_right,
        R.layout.chat_item_msg_image_left,
        R.layout.chat_item_msg_image_right,
        R.layout.chat_item_msg_audio_left,
        R.layout.chat_item_msg_audio_right};
    int i;
    for (i = 0; i < types.length; i++) {
      if (itemViewType == types[i]) {
        break;
      }
    }
    View view=inflater.inflate(layoutIds[i], null);
    return view;
  }
}
