package com.example.fengmanlou.logintest.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by fengmanlou on 2015/5/5.
 */
@AVClassName("Hang")
public class Hang extends AVObject{
    public Hang() {
    }
    public String getName(){
        return getString("name");
    }
    public void setName(String value){
        put("name",value);
    }

    public String getNumber(){
        return getString("number");
    }
    public void setNumber(String value){
        put("number",value);
    }

    public String getHospital(){
        return getString("hospital");
    }
    public void setHospital(String value){
        put("hospital",value);
    }

    public String getDepart(){
        return getString("depart");
    }
    public void setDepart(String value){
        put("depart",value);
    }

    public String getTime(){
        return getString("time");  //得到预约的时间
    }
    public void setTime(String value){
        put("time",value);          //设置预约的时间
    }

    public String getStatus(){
        return getString("status");
    }
    public void setStatus(String value){
        put("status",value);
    }
}
