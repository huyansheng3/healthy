package com.example.fengmanlou.logintest.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by fengmanlou on 2015/4/17.
 */
@AVClassName("News")
public class News extends AVObject{
    public News(){
        super();
    }
    public String getTitle(){
        return getString("title");
    }

    public void setTitle(String value){
        put("title",value);
    }

    public String getContent(){
        return getString("content");
    }

    public void setContent(String value){
        put("content",value);
    }

    @SuppressWarnings("unchecked")
    public List getComments(){
        return (List)getList("comments");
    }

    public void addComment(Comment comment){
        addUnique("comments",comment);
    }


}
