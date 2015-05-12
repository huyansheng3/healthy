package com.example.fengmanlou.logintest.service;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.example.fengmanlou.logintest.avobject.Hang;

import java.util.Collections;
import java.util.List;

/**
 * Created by fengmanlou on 2015/5/5.
 */
public class HangService {
    public static List<Hang> findHangList(String userId){
        try {
            AVUser avUser = AVObject.createWithoutData(AVUser.class,userId);
            AVQuery<Hang> query = AVQuery.getQuery(Hang.class);
            query.whereEqualTo("creator",avUser);
            query.orderByAscending("time");
            return  query.find();
        }catch (AVException e1){
            Log.i("hys", e1.getMessage());
            return Collections.emptyList();
        }
    }
}
