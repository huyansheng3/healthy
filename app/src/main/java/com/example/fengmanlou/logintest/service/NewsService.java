package com.example.fengmanlou.logintest.service;

import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.search.AVSearchQuery;
import com.example.fengmanlou.logintest.avobject.Comment;
import com.example.fengmanlou.logintest.avobject.News;
import com.example.fengmanlou.logintest.util.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by fengmanlou on 2015/4/18.
 */
public class NewsService {

    public static void fetchNewsById(String objectId,GetCallback<AVObject> getCallback){
        News news = new News();
        news.setObjectId(objectId);
        //通过Fetch获取news内容
        news.fetchInBackground(getCallback);
    }

    public static void fetchUserById(String userObjectId,GetCallback<AVObject> getCallback){
        AVUser user = new AVUser();
        user.setObjectId(userObjectId);
        user.fetchInBackground(getCallback);
    }

    public static void CreateOrUpdateNews(String objectId,String title,String content,SaveCallback saveCallback){
        final News news = new News();
        if (!TextUtils.isEmpty(objectId)){
            //如果objectId存在。保存变为更新操作
            news.setObjectId(objectId);
        }
        news.setTitle(title);
        news.setContent(content);
        news.saveInBackground(saveCallback);
    }

    public static void CreateOrUpdateComment(String objectId,String content,SaveCallback saveCallback){
        final Comment comment = new Comment();
        if (TextUtils.isEmpty(objectId)){
            LogUtil.log.d("hys","news id is null");
        }try {
            News news = AVObject.createWithoutData(News.class,objectId);
            comment.put("news",news);
            comment.setContent(content);
            comment.setCreator(AVUser.getCurrentUser());
            comment.saveInBackground(saveCallback);

        }catch (AVException e){
            LogUtil.log.d("hys",e.getMessage());
        }

    }


    public static List<News> findNewses(){
        //查询当前新闻的列表
        AVQuery<News> newsAVQuery = AVQuery.getQuery(News.class);
        newsAVQuery.orderByDescending("updatedAt");
        newsAVQuery.limit(1000);
        newsAVQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        try {
            return newsAVQuery.find();
        }catch (AVException e){
            Log.e("tag", "Query news failed.", e);
            return Collections.emptyList();
        }

    }

    public static List<Comment> findComments(String newsObjectId){
        //查询当前新闻对应的评论的列表
        try {
            News news = AVObject.createWithoutData(News.class,newsObjectId);
            AVQuery<Comment> query = AVQuery.getQuery(Comment.class);
            query.whereEqualTo("news",news);
            query.orderByDescending("updateAt");
            return  query.find();
        }catch (AVException e1){
            Log.i("hys", e1.getMessage());
            return Collections.emptyList();
        }

    }


    public static void searchQuery(String inputSearch){
        AVSearchQuery avSearchQuery = new AVSearchQuery(inputSearch);
        avSearchQuery.setLimit(50);
        avSearchQuery.search();
    }

}
