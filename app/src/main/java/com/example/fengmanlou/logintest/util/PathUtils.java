package com.example.fengmanlou.logintest.util;

import android.os.Environment;

import java.io.File;
import java.util.Random;

/**
 * Created by lzw on 14-9-19.
 */
public class PathUtils {
  public static String getSDcardDir() {
    return Environment.getExternalStorageDirectory().getPath() + "/";
  }

  public static String checkAndMkdirs(String dir) {
    File file = new File(dir);
    if (file.exists() == false) {
      file.mkdirs();
    }
    return dir;
  }

  public static String getAppPath() {
    String dir = getSDcardDir() + "healthy/";
    return checkAndMkdirs(dir);
  }

  public static String getAvatarDir() {
    String dir = getAppPath() + "avatar/";
    return checkAndMkdirs(dir);
  }

  public static String getAvatarTmpPath() {
    return getAvatarDir() + myUUID();
  }

  public static String getChatFileDir() {
    String dir = getAppPath() + "files/";
    return checkAndMkdirs(dir);
  }

  public static String getChatFilePath(String id) {
    String dir = getChatFileDir();
    String path = dir + id;
    return path;
  }

  public static String getRecordTmpPath() {
    return getChatFileDir() + "record_tmp";
  }

  public static String getUUIDFilePath() {
    return getChatFilePath(myUUID());
  }

    public static String myUUID() {
        StringBuilder sb = new StringBuilder();
        int start = 48, end = 58;
        appendChar(sb, start, end);
        appendChar(sb, 65, 90);
        appendChar(sb, 97, 123);
        String charSet = sb.toString();
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < 24; i++) {
            int len = charSet.length();
            int pos = new Random().nextInt(len);
            sb1.append(charSet.charAt(pos));
        }
        return sb1.toString();
    }

    public static void appendChar(StringBuilder sb, int start, int end) {
        int i;
        for (i = start; i < end; i++) {
            sb.append((char) i);
        }
    }

  public static String getTmpPath() {
    return getAppPath() + "tmp";
  }
}
