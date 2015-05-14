package com.example.fengmanlou.logintest.service;

import android.util.Log;

import com.avos.avoscloud.AVQuery;
import com.example.fengmanlou.logintest.avobject.Case;
import com.example.fengmanlou.logintest.avobject.Device;

import java.util.Collections;
import java.util.List;

/**
 * Created by fengmanlou on 2015/5/14.
 */
public class CaseService {
    public static List<Case> findCaseList(){
        //查询当前设备的列表
        AVQuery<Case> caseAVQuery = AVQuery.getQuery(Case.class);
        caseAVQuery.orderByDescending("updateAt");
        caseAVQuery.limit(100);
        caseAVQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        try {
            return caseAVQuery.find();
        }catch (Exception e){
            Log.e("tag", "Query devices failed.", e);
            return Collections.emptyList();
        }

    }
}
