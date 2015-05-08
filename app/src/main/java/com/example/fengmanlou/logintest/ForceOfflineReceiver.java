package com.example.fengmanlou.logintest;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

/**
 * Created by fengmanlou on 2015/4/7.
 */
public class ForceOfflineReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(final Context context, Intent intent) {
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(context);
        alertDialog.setTitle("警告");
        alertDialog.setMessage("你被强制下线，请重新登陆");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK,",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCollector.finishAll();
                Intent intent = new Intent(context,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        AlertDialog alert = alertDialog.create();
        //设置AlertDialog的类型，保证在广播接收器中可以正常弹出
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();
    }
}
