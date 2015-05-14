package com.example.fengmanlou.logintest.service;

import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.fengmanlou.logintest.avobject.News;
import com.example.fengmanlou.logintest.util.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fengmanlou on 2015/5/14.
 */
public class FamilyService {
    public static List<AVUser> findFamilies(){
        AVUser curUser = AVUser.getCurrentUser();
        AVQuery<AVUser> followeeQuery = AVUser.followeeQuery(curUser.getObjectId(),AVUser.class);
        try {
            List<String> objectIdList = new ArrayList<>();
            followeeQuery.find();

            return followeeQuery.find();
        }catch (Exception e){
            Logger.d(e.getMessage());
            return  Collections.emptyList();
        }
    }
}
