package com.avoscloud.chat.contrib.service;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzw on 14-9-29.
 */
public class CloudService {
  public static HashMap<String, Object> sign(String selfId, List<String> watchIds) throws AVException {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("self_id", selfId);
    map.put("watch_ids", watchIds);
    return (HashMap<String, Object>) AVCloud.callFunction("sign", map);
  }
}
