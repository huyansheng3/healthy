package com.example.fengmanlou.logintest.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by fengmanlou on 2015/5/14.
 */
@AVClassName("Case")
public class Case extends AVObject{
    public Case() {
    }
    public String getTitle(){
        return getString("title");
    }
    public void setTitle(String value){
        put("title",value);
    }

    public String getName(){
        return getString("name");
    }
    public void setName(String value){
        put("name",value);
    }

    public String getAge(){
        return getString("age");
    }

    public void setAge(String value){
        put("age",value);
    }

    public String getHospital(){
        return getString("hospital");
    }

    public void setHospital(String value){
        put("hospital",value);
    }

    public String getDescribe(){
        return getString("describe");
    }

    public void setDescribe(String value){
        put("describe",value);
    }

}
