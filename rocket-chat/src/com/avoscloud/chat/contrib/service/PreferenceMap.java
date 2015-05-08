package com.avoscloud.chat.contrib.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.avoscloud.chat.contrib.R;
import com.avoscloud.chat.contrib.util.Logger;

/**
 * Created by lzw on 14-6-19.
 */
public class PreferenceMap {
  public static final String NOTIFY_WHEN_NEWS = "notifyWhenNews";
  public static final String VOICE_NOTIFY = "voiceNotify";
  public static final String VIBRATE_NOTIFY = "vibrateNotify";

  Context ctx;
  SharedPreferences pref;
  SharedPreferences.Editor editor;
  public static PreferenceMap currentUserPreferenceMap;

  public PreferenceMap(Context ctx) {
    this.ctx = ctx;
    pref = PreferenceManager.getDefaultSharedPreferences(ctx);
    editor = pref.edit();
    Logger.d("PreferenceMap init no specific user");
  }

  public PreferenceMap(Context ctx, String prefName) {
    this.ctx = ctx;
    pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    editor = pref.edit();
  }

  public static PreferenceMap getCurUserPrefDao(Context ctx) {
    if (currentUserPreferenceMap == null) {
      currentUserPreferenceMap = new PreferenceMap(ctx, "preference_" + ChatService.getSelfId());
    }
    return currentUserPreferenceMap;
  }

  public boolean isNotifyWhenNews() {
    return pref.getBoolean(NOTIFY_WHEN_NEWS,
        ChatService.ctx.getResources().getBoolean(R.bool.defaultNotifyWhenNews));
  }

  public void setNotifyWhenNews(boolean notifyWhenNews) {
    editor.putBoolean(NOTIFY_WHEN_NEWS, notifyWhenNews).commit();
  }

  boolean getBooleanByResId(int resId) {
    return ChatService.ctx.getResources().getBoolean(resId);
  }

  public boolean isVoiceNotify() {
    return pref.getBoolean(VOICE_NOTIFY,
        getBooleanByResId(R.bool.defaultVoiceNotify));
  }

  public void setVoiceNotify(boolean voiceNotify) {
    editor.putBoolean(VOICE_NOTIFY, voiceNotify).commit();
  }

  public boolean isVibrateNotify() {
    return pref.getBoolean(VIBRATE_NOTIFY,
        getBooleanByResId(R.bool.defaultVibrateNotify));
  }

  public void setVibrateNotify(boolean vibrateNotify) {
    editor.putBoolean(VIBRATE_NOTIFY, vibrateNotify);
  }

}
