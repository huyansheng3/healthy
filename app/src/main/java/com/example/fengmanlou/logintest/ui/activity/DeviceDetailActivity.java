package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.avobject.Device;
import com.example.fengmanlou.logintest.service.DeviceService;
import com.example.fengmanlou.logintest.util.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by fengmanlou on 2015/5/14.
 */
public class DeviceDetailActivity extends Activity{

    private String objectId;
    private ImageView detail_device_image;
    private TextView detail_device_title,detail_device_describe,detail_device_count;
    private Button device_rent;
    private Dialog dialog;
    private int device_count;
    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        detail_device_image = (ImageView) findViewById(R.id.detail_device_image);
        detail_device_title = (TextView) findViewById(R.id.detail_device_title);
        detail_device_describe = (TextView) findViewById(R.id.detail_device_describe);
        device_rent  = (Button) findViewById(R.id.device_rent);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("title");
            String describe = extras.getString("describe");
            objectId = extras.getString("objectId");
            String imageUrl = extras.getString("imageUrl");
            device_count = extras.getInt("count");
            if (imageUrl != null){
                //显示图片的配置
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnFail(R.drawable.healthy_icon01)
                        .showImageForEmptyUri(R.drawable.healthy_icon01)
                        .showImageOnLoading(R.drawable.healthy_icon01)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .displayer(new SimpleBitmapDisplayer())
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();

                ImageLoader.getInstance().displayImage(imageUrl,detail_device_image, options);
            }
            if (title != null) {
                detail_device_title.setText(title);
            }
            if (describe != null) {
                detail_device_describe.setText(describe);
            }
        }


        device_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DeviceDetailActivity.this).setTitle("确认租赁？").setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                device = DeviceService.findByObjectId(objectId);
                                if (device != null) {
//                                    if (device.getCount().intValue() > 0) {
                                        Logger.d("数量 ："+device.get("count"));
                                        // Logger.d("数量 ： "+device.getCount().intValue());
                                        device.increment("count", -1);
                                        device.saveInBackground();
/*                                    } else {
                                        new AlertDialog.Builder(DeviceDetailActivity.this).setTitle("数量不够，无法租赁")
                                                .setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false)
                                                .setPositiveButton("确定",null).show();
                                    }*/
                                }
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
