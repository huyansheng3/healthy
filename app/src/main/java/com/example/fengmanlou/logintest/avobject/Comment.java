package com.example.fengmanlou.logintest.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import org.litepal.crud.DataSupport;

import java.sql.Date;

/**
 * Created by fengmanlou on 2015/4/17.
 */
@AVClassName("Comment")
public class Comment extends AVObject{

    public Comment(){
        super();
    }

    public String getContent(){
        return getString("content");
    }

    public void setContent(String value){
        put("content",value);
    }

    public void setCreator(AVUser user){
        put("creator",user);
    }

    public AVUser getCreator(){
        return getAVUser("creator");
    }


}
