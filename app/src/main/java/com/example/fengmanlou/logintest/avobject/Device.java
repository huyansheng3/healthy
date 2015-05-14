package com.example.fengmanlou.logintest.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by fengmanlou on 2015/5/14.
 */
@AVClassName("Device")
public class Device extends AVObject{
    public Device() {
    }
    public String getTitle(){
        return getString("title");
    }
    public void setTitle(String value){
        put("title",value);
    }

    public String getDescribe(){
        return getString("describe");
    }
    public void setDescribe(String value){
        put("describe",value);
    }

    public boolean getIsRented(){  //是否已经被租赁
        return getBoolean("isRented");
    }

    public void setIsRented(boolean value){
        put("isRented",value);
    }

    public Number getCount(){
        return getNumber("count");
    }
    public void setCount(int value){
        put("count",value);
    }
}
