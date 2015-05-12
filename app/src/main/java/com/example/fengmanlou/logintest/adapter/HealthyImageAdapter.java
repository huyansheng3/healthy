package com.example.fengmanlou.logintest.adapter;

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
public class HealthyImageAdapter extends BaseAdapter {
    private Context context;

    public HealthyImageAdapter(Context context) {
        this.context = context;
    }

    private Integer[] images = {
            //九宫格图片的设置
            R.drawable.healthy_icon01,
            R.drawable.healthy_icon02,
            R.drawable.healthy_icon03,
            R.drawable.healthy_icon04,
            R.drawable.healthy_icon05,
            R.drawable.healthy_icon06,
            R.drawable.healthy_icon07,
            R.drawable.healthy_icon08,
            R.drawable.healthy_icon09,

    };

    private String[] texts = {
            //九宫格图片下方文字的设置
            "健康资讯",
            "北方网健康",
            "就医地图",
            "推荐食谱",
            "中医养生",
            "健康社区",
            "健康档案",
            "提醒就医",
            "自助挂号",
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
            view.setPadding(15, 55, 15, 55);  //每格的间距
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