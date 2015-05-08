package com.example.fengmanlou.logintest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.avobject.Hang;

import java.util.List;

/**
 * Created by fengmanlou on 2015/5/5.
 */
public class HangAdapter extends BaseAdapter{
    private Context context;
    private List<Hang> hangList;

    public HangAdapter(Context context, List<Hang> hangList) {
        this.context = context;
        this.hangList = hangList;
    }

    @Override
    public int getCount() {
        return hangList != null ? hangList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (hangList != null){
            return hangList.get(position);
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
        MyView myView;
        if (convertView == null){
            myView = new MyView();
            convertView = LayoutInflater.from(context).inflate(R.layout.hang_items,null);
            myView.name = (TextView)convertView.findViewById(R.id.item_name);
            myView.number = (TextView)convertView.findViewById(R.id.item_number);
            myView.hospital = (TextView)convertView.findViewById(R.id.item_hospital);
            myView.depart = (TextView)convertView.findViewById(R.id.item_depart);
            myView.time = (TextView)convertView.findViewById(R.id.item_time);
            myView.status = (TextView)convertView.findViewById(R.id.item_status);
            convertView.setTag(myView);

        }else {
            myView = (MyView) convertView.getTag();
        }
        if (hangList.get(position) != null){
            myView.name.setText(hangList.get(position).getName());
            myView.number.setText(hangList.get(position).getNumber());
            myView.time.setText(hangList.get(position).getTime());
            myView.hospital.setText(hangList.get(position).getHospital());
            myView.depart.setText(hangList.get(position).getDepart());
            myView.status.setText(hangList.get(position).getStatus());
        }
        return convertView;

    }

    public class MyView{
        TextView name;
        TextView number;
        TextView time;
        TextView hospital;
        TextView depart;
        TextView status;
    }
}
