package com.example.fengmanlou.logintest.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by fengmanlou on 2015/5/15.
 */
@AVClassName("Doctor")
public class Doctor extends AVObject{
    public Doctor() {
    }

    public String  getName(){
        return getString("name");
    }

    public String getHospital(){
        return getString("hospital");
    }

    public String getPrice(){
        return getString("price");
    }

    public String getDepart(){
        return getString("depart");
    }

}
