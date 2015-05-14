package com.example.fengmanlou.logintest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.util.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by fengmanlou on 2015/4/9.
 */
public class UserImageAdapter extends BaseAdapter {
    private Context context;
    private List<AVUser> avUserList;

    public UserImageAdapter(Context context, List<AVUser> avUserList) {
        this.context = context;
        this.avUserList = avUserList;
    }


    //get the number
    @Override
    public int getCount() {
        return avUserList != null ? avUserList.size():0;
    }

    @Override
    public Object getItem(int position) {
        if (avUserList != null){
            return avUserList.get(position);
        }
        else {
            return position;
        }
    }

    //get the current selector's id number
    @Override
    public long getItemId(int position) {
        return position;
    }

    //create view method
    @Override
    public View getView(int position, View view, ViewGroup viewgroup) {
        ImgTextWrapper wrapper;
        if (view == null) {
            wrapper = new ImgTextWrapper();
            view = LayoutInflater.from(context).inflate(R.layout.user_items,null);
            wrapper.user_items_avatar = (ImageView) view.findViewById(R.id.user_items_avatar);
            wrapper.user_items_nickname = (TextView) view.findViewById(R.id.user_items_nickname);
            view.setTag(wrapper);
        } else {
            wrapper = (ImgTextWrapper) view.getTag();
        }

        if (avUserList.get(position) != null){
            AVFile avFile = avUserList.get(position).getAVFile("avatar");
            if (avFile == null) {
                Logger.d("该条新闻的图片没有");

            }
            if (avFile != null) {
                String url = avFile.getUrl();
                //显示图片的配置
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnFail(R.drawable.healthy_icon01)
                        .showImageForEmptyUri(R.drawable.healthy_icon01)
                        .showImageOnLoading(R.drawable.healthy_icon01)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .displayer(new SimpleBitmapDisplayer())
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();

                ImageLoader.getInstance().displayImage(url, wrapper.user_items_avatar, options);
            }
            if (avUserList.get(position).getString("nickname") != null) {
                wrapper.user_items_nickname.setText(avUserList.get(position).getString("nickname"));
            }
        }

        return view;
    }


    class ImgTextWrapper {
        ImageView user_items_avatar;
        TextView user_items_nickname;
    }

}