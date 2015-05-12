package com.example.fengmanlou.logintest.service;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;

import java.io.IOException;

/**
 * Created by fengmanlou on 2015/5/9.
 */
public class UserService {


    public static void saveAvatar(String path) throws IOException,AVException{
        AVUser user = AVUser.getCurrentUser();
        final AVFile file = AVFile.withAbsoluteLocalPath(user.getUsername(),path);
        file.save();
        user.put("avatar",file);
        user.save();
        user.fetch();
    }
}
