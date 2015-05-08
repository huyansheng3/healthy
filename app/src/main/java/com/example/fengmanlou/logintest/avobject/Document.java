package com.example.fengmanlou.logintest.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by fengmanlou on 2015/5/5.
 */
@AVClassName("Document")
public class Document extends AVObject{
    public Document() {
    }
    public String getUserName(){
        return getString("username");
    }
    public void setUserName(String value){
        put("username",value);
}
    public String getUserSex(){
        return getString("usersex");
    }
}
