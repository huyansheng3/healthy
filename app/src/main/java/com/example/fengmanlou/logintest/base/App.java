package com.example.fengmanlou.logintest.base;

import android.app.Notification;
import android.content.Context;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.UserInfoFactory;
import com.avoscloud.leanchatlib.model.UserInfo;
import com.example.fengmanlou.logintest.avobject.Case;
import com.example.fengmanlou.logintest.avobject.Comment;
import com.example.fengmanlou.logintest.avobject.Device;
import com.example.fengmanlou.logintest.avobject.Document;
import com.example.fengmanlou.logintest.avobject.Hang;
import com.example.fengmanlou.logintest.avobject.News;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.litepal.LitePalApplication;

import java.util.List;

/**
 * Created by fengmanlou on 2015/4/16.
 */
public class App extends LitePalApplication{

    @Override
    public void onCreate() {
        super.onCreate();

        AVObject.registerSubclass(Case.class);
        AVObject.registerSubclass(Device.class);
        AVObject.registerSubclass(Hang.class);
        AVObject.registerSubclass(News.class);
        AVObject.registerSubclass(Comment.class);
        AVObject.registerSubclass(Document.class);
        AVOSCloud.initialize(this, "0f24mab128qingz2afo1qcsbic46tie77qcss9guuz8kmc4s",
                "wfe7497s5dhry96f9w2778dudzmc2u2lfcxcxubnuwa6yzgd");


        final ChatManager chatManager = ChatManager.getInstance();
        chatManager.init(this);
        chatManager.setUserInfoFactory(new UserInfoFactory() {
            @Override
            public UserInfo getUserInfoById(String userId) {
                AVUser user = AVUser.getCurrentUser();
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(userId);
                if (user.getAVFile("avatar") != null) {
                    userInfo.setAvatarUrl(user.getAVFile("avatar").getUrl());
                }
                return userInfo;
            }

            @Override
            public void cacheUserInfoByIdsInBackground(List<String> userIds) throws Exception {

            }

            @Override
            public boolean showNotificationWhenNewMessageCome(String selfId) {
                return true;
            }

            @Override
            public void configureNotification(Notification notification) {
                notification.defaults |= Notification.DEFAULT_ALL;
            }
        });


            //启用错误崩溃报告
            AVOSCloud.setDebugLogEnabled(true);
            AVAnalytics.enableCrashReport(this,true);

            InitImageLoader(this);


        }

    public static void InitImageLoader(Context context){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2) //加载图片的线程数
                .denyCacheImageMultipleSizesInMemory() //解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //设置磁盘缓存文件名称
                .tasksProcessingOrder(QueueProcessingType.FIFO) //设置加载显示图片队列进程，为先进先出
                .writeDebugLogs().build(); // Remove for release app

        ImageLoader.getInstance().init(config);
    }


}
