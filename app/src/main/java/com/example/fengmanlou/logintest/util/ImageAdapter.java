package com.example.fengmanlou.logintest.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fengmanlou.logintest.R;

/**
 * Created by fengmanlou on 2015/4/9.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;

    public ImageAdapter(Context context) {
        this.context = context;
    }

    private Integer[] images = {
            //九宫格图片的设置
            R.drawable.imagebutton01,
            R.drawable.imagebutton02,
            R.drawable.imagebutton03,
            R.drawable.imagebutton04,
            R.drawable.imagebutton05,
            R.drawable.imagebutton06,
            R.drawable.imagebutton07,
            R.drawable.imagebutton08,
            R.drawable.imagebutton09,

    };

    private String[] texts = {
            //九宫格图片下方文字的设置
            "每日新闻",
            "免疫知识",
            "就医地图",
            "健康咨询",
            "预约体检",
            "提醒就医",
            "自助挂号",
            "评价服务",
            "私人医生",
    };

    //get the number
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
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
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.grid_items, null);
            view.setTag(wrapper);
            view.setPadding(15, 30, 15, 30);  //每格的间距
        } else {
            wrapper = (ImgTextWrapper) view.getTag();
        }

        wrapper.imageView = (ImageView) view.findViewById(R.id.grid_imageViews);
        wrapper.imageView.setBackgroundResource(images[position]);
        wrapper.textView = (TextView) view.findViewById(R.id.grid_textViews);
        wrapper.textView.setText(texts[position]);

        return view;
    }


    class ImgTextWrapper {
        ImageView imageView;
        TextView textView;
    }

}