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
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.avobject.Device;
import com.example.fengmanlou.logintest.avobject.News;
import com.example.fengmanlou.logintest.util.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by fengmanlou on 2015/5/14.
 */
public class DeviceAdapter extends BaseAdapter{
    private Context context;
    private List<Device> deviceList;

    public DeviceAdapter(Context context, List<Device> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @Override
    public int getCount() {
        return deviceList != null ? deviceList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (deviceList != null){
            return deviceList.get(position);
        }
        else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.device_items, null);
            holder = new ViewHolder();
            holder.deviceImage = (ImageView) convertView.findViewById(R.id.device_image);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.device_title);
            holder.deviceCount = (TextView) convertView.findViewById(R.id.device_count);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Device device = deviceList.get(position);
        if (device != null) {

            holder.titleTextView.setText(deviceList.get(position).getTitle());
            holder.deviceCount.setText("数量："+deviceList.get(position).getNumber("count")+"个");
            AVFile avFile = deviceList.get(position).getAVFile("image");
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

                ImageLoader.getInstance().displayImage(url, holder.deviceImage, options);
            }
        }
        return convertView;
    }

    static class ViewHolder{
        ImageView deviceImage;
        TextView titleTextView;
        TextView deviceCount;
    }
}
