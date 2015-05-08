package com.example.fengmanlou.logintest.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by fengmanlou on 2015/4/25.
 */
/*
* 该类用来判断是否有网络连接和2G连接、WIFI连接
* */

 public class NetWorkHelper {
    public static boolean isWifi(Context paramContext){
        return "2".equals(getNetType(paramContext)[0]);
    }

    public static boolean isMobile(Context paramContext){
        return "1".equals(getNetType(paramContext)[0]);
    }

    public static boolean isNetAvailable(Context paramContext){
        if ("1".equals(getNetType(paramContext)[0]) || "2".equals(getNetType(paramContext)[0])){
            return true;
        }
        return false;
    }





    public static String[] getNetType(Context paramContext){
        String[] arrayOfString = {"unKnown","unKnown"};
        PackageManager localPackageManager = paramContext.getPackageManager();
        if (localPackageManager.checkPermission("android.permission.ACCESS_NETWORK_STATE",paramContext.getPackageName()) != 0){
            arrayOfString[0] = "unKnown";
            return arrayOfString;
        }
        ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (localConnectivityManager == null){
            arrayOfString[0] = "unKnown";
            return arrayOfString;
        }
        NetworkInfo localNetWorkInfo = localConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (localNetWorkInfo != null && localNetWorkInfo.getState() == NetworkInfo.State.CONNECTED){
            arrayOfString[0] = "2";    //当网络连接为WIFI时，arrayOfString数组第一个取“2”
            return arrayOfString;
        }

        NetworkInfo localNetWorkInfo2 = localConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (localNetWorkInfo2 != null && localNetWorkInfo2.getState() == NetworkInfo.State.CONNECTED){
            arrayOfString[0] = "1";     //当网络连接为2G或3G时，数组第一个取“1”
            arrayOfString[1] =localNetWorkInfo2.getSubtypeName();
            return arrayOfString;
        }
        return arrayOfString;
    }
}
