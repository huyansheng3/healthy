package com.example.fengmanlou.logintest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.avobject.News;
import com.example.fengmanlou.logintest.util.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Created by fengmanlou on 2015/4/18.
 */
public class NewsAdapter extends BaseAdapter{
    private Context context;
    private List<News> newses;

    public NewsAdapter(Context context, List<News> newses) {
        this.context = context;
        this.newses = newses;
    }

    @Override
    public int getCount() {
        return newses != null ? newses.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (newses != null){
            return newses.get(position);
        }
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.news_items, null);
            holder = new ViewHolder();
            holder.newsImage = (ImageView) convertView.findViewById(R.id.news_image);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.news_title);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        News news = newses.get(position);
        if (news != null) {

            holder.titleTextView.setText(newses.get(position).getTitle());

            AVFile avFile = newses.get(position).getAVFile("image");
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

                ImageLoader.getInstance().displayImage(url, holder.newsImage, options);
            }
        }
        return convertView;
    }

    static class ViewHolder{
        ImageView newsImage;
        TextView titleTextView;
    }

}
