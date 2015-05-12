package com.example.fengmanlou.logintest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.entity.Alarm;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fengmanlou on 2015/5/4.
 */
public class AlarmAdapter extends BaseAdapter {
    private Context context;
    private List<Alarm> alarmList;

    public AlarmAdapter(Context context, List<Alarm> alarmList) {
        this.context = context;
        this.alarmList = alarmList;
    }

    @Override
    public int getCount() {
        return alarmList != null ? alarmList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (alarmList != null){
            return alarmList.get(position);
        }else {
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
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.alarm_items,null);
            holder.alarm_item_tag = (TextView) convertView.findViewById(R.id.alarm_item_tag);
            holder.alarm_item_calendar = (TextView) convertView.findViewById(R.id.alarm_item_calendar);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (alarmList.get(position) != null){
            holder.alarm_item_tag.setText(alarmList.get(position).getTag());
            String formatCalendar = Alarm.Calendar2String(alarmList.get(position).getAudreyCalendar());
            holder.alarm_item_calendar.setText(formatCalendar);
        }
        return convertView;
    }

    public class ViewHolder{
        public TextView alarm_item_tag;
        public TextView alarm_item_calendar;
    }
}
