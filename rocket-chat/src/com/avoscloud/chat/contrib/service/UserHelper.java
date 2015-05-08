package com.avoscloud.chat.contrib.service;

/**
 * Created by lzw on 14/11/21.
 */
public interface UserHelper {
  String getDisplayName(String userId);

  String getDisplayAvatarUrl(String userId);
}
