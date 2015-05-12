package com.example.fengmanlou.logintest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.avobject.Healthy;

import java.util.List;

/**
 * Created by fengmanlou on 2015/4/29.
 */
public class HealthyAdapter extends BaseAdapter{
    private Context context;
    private volatile List<Healthy> healthyList;

    public HealthyAdapter() {
    }

    public HealthyAdapter(Context context) {
        this.context = context;
    }

    public HealthyAdapter(Context context, List<Healthy> healthyList) {
        this.context = context;
        this.healthyList = healthyList;
    }

    @Override
    public int getCount() {
        return healthyList == null ? 0: healthyList.size();
    }

    @Override
    public Object getItem(int position) {
        if (healthyList != null){
            return healthyList.get(position);
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
        View view;
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.healthy_item,null);
        }else {
            view = convertView;
        }

        TextView healthy_title = (TextView) view.findViewById(R.id.healthy_title);
        healthy_title.setText(healthyList.get(position).getTitle());
        return view;
    }
}
