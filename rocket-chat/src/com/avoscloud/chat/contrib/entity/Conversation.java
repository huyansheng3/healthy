package com.avoscloud.chat.contrib.entity;


/**
 * Created by lzw on 14-9-26.
 */
public class Conversation {
  private Msg msg;
  private String toUserId;
  private int unreadCount;

  public Msg getMsg() {
    return msg;
  }

  public void setMsg(Msg msg) {
    this.msg = msg;
  }

  public String getToUserId() {
    return toUserId;
  }

  public void setToUserId(String toUserId) {
    this.toUserId = toUserId;
  }

  public int getUnreadCount() {
    return unreadCount;
  }

  public void setUnreadCount(int unreadCount) {
    this.unreadCount = unreadCount;
  }
}
