package com.example.fengmanlou.logintest.util;

import android.content.Context;
import android.widget.Toast;

import com.avoscloud.leanchatlib.utils.NetAsyncTask;

/**
 * Created by lzw on 14-9-27.
 */
public abstract class SimpleNetTask extends NetAsyncTask {
  protected SimpleNetTask(Context cxt) {
    super(cxt);
  }

  protected SimpleNetTask(Context cxt, boolean openDialog) {
    super(cxt, openDialog);
  }


  @Override
  protected void onPost(Exception e) {
    if (e != null) {
      e.printStackTrace();
        Toast.makeText(ctx,"e : "+e,Toast.LENGTH_SHORT).show();
      //Utils.toast(ctx, R.string.pleaseCheckNetwork);
    } else {
      onSucceed();
    }
  }

  protected abstract void doInBack() throws Exception;

  protected abstract void onSucceed();
}
