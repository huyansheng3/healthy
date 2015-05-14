package com.example.fengmanlou.logintest.service;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.fengmanlou.logintest.avobject.Comment;
import com.example.fengmanlou.logintest.avobject.Device;
import com.example.fengmanlou.logintest.avobject.News;
import com.example.fengmanlou.logintest.util.Logger;

import java.util.Collections;
import java.util.List;

/**
 * Created by fengmanlou on 2015/5/14.
 */
public class DeviceService {

    public static List<Device> findDeviceList(){
        //查询当前设备的列表
        AVQuery<Device> deviceAVQuery = AVQuery.getQuery(Device.class);
        deviceAVQuery.orderByDescending("updateAt");
        deviceAVQuery.limit(100);
        deviceAVQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        try {
            return deviceAVQuery.find();
        }catch (Exception e){
            Log.e("tag", "Query devices failed.", e);
            return Collections.emptyList();
        }

    }

    public static Device findByObjectId(String objectId){


        try{
            Device device = AVObject.createWithoutData(Device.class,objectId);
            return device;
        }catch (Exception e){
            Logger.d("device failed");
            return null;
        }

    }

}
