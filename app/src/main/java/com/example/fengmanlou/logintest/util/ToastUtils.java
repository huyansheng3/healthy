package com.example.fengmanlou.logintest.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fengmanlou.logintest.R;

/**
 * Created by fengmanlou on 2015/4/14.
 */
public class ToastUtils extends Toast{

    public ToastUtils(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast toast = new Toast(context);

        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast_layout, null);
        TextView tv = (TextView)v.findViewById(R.id.toast_textView);
        tv.setText(text);

        toast.setView(v);
        //setGravity方法用于设置位置，此处为垂直居中
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(duration);
        return toast;
    }
}
