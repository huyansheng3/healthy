package com.example.fengmanlou.logintest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.avobject.Case;

import java.util.List;

/**
 * Created by fengmanlou on 2015/5/14.
 */
public class CaseAdapter extends BaseAdapter{
    private Context context;
    private List<Case> caseList;

    public CaseAdapter(Context context, List<Case> caseList) {
        this.context = context;
        this.caseList = caseList;
    }

    @Override
    public int getCount() {
        return caseList != null ? caseList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (caseList != null){
            return caseList.get(position);
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
        View view;
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.case_item,null);

        }else {
            view = convertView;
        }

        TextView case_title = (TextView) view.findViewById(R.id.case_title);
        if (caseList.get(position) != null) {
            case_title.setText(caseList.get(position).getTitle());
        }
        return view;
    }
}
