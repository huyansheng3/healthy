package com.example.fengmanlou.logintest.service;

import android.util.Log;

import com.avos.avoscloud.AVQuery;
import com.example.fengmanlou.logintest.avobject.Device;
import com.example.fengmanlou.logintest.avobject.Doctor;

import java.util.Collections;
import java.util.List;

/**
 * Created by fengmanlou on 2015/5/15.
 */
public class DoctorService {

    public static List<Doctor> findDoctorList(){
        //查询当前设备的列表
        AVQuery<Doctor> doctorAVQuery = AVQuery.getQuery(Doctor.class);
        doctorAVQuery.orderByDescending("updateAt");
        doctorAVQuery.limit(30);
        doctorAVQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        try {
            return doctorAVQuery.find();
        }catch (Exception e){
            Log.e("tag", "Query doctor failed.", e);
            return Collections.emptyList();
        }

    }

}
