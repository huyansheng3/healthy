package com.example.fengmanlou.logintest.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by fengmanlou on 2015/5/5.
 */
@AVClassName("Document")
public class Document extends AVObject{
    public Document() {
    }
    public String getName(){
        return getString("name");
    }
    public void setName(String value){
        put("name",value);
    }
    public String getSex(){
        return getString("sex");
    }
    public void setSex(String value){
        put("sex",value);
    }

    public String getAge(){
        return getString("age");
    }
    public void setAge(String value){
        put("age",value);
    }

    public String getBlood(){
        return getString("blood");
    }
    public void setBlood(String value){
        put("blood",value);
    }

    public String getExperience(){
        return getString("experience");
    }
    public void setExperience(String value){
        put("experience",value);
    }

    public String getMetal(){
        return getString("metal");
    }
    public void setMetal(String value){
        put("metal",value);
    }

    public String getAllergy(){
        return getString("allergy");
    }
    public void setAllergy(String value){
        put("allergy",value);
    }

    public String getRemark(){
        return getString("remark");
    }

    public void setRemark(String value){
        put("remark",value);
    }

    public void setCreator(AVUser avUser){
        put("creator",avUser);
    }

    public AVUser getCreator(){
        return getAVUser("creator");
    }
}
