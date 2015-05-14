package com.example.fengmanlou.logintest.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.avos.avoscloud.AVAnalytics;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.PersonImageAdapter;
import com.example.fengmanlou.logintest.base.Constants;
import com.example.fengmanlou.logintest.ui.activity.CaseActivity;
import com.example.fengmanlou.logintest.ui.activity.DeviceActivity;
import com.example.fengmanlou.logintest.ui.activity.DocumentActivity;
import com.example.fengmanlou.logintest.ui.activity.FamilyActivity;
import com.example.fengmanlou.logintest.ui.activity.HangActivity;
import com.example.fengmanlou.logintest.ui.activity.HangListActivity;
import com.example.fengmanlou.logintest.ui.activity.HealthyActivity;
import com.example.fengmanlou.logintest.ui.activity.MapActivity;
import com.example.fengmanlou.logintest.ui.activity.NewsActivity;
import com.example.fengmanlou.logintest.ui.activity.RemindActivity;

/**
 * Created by fengmanlou on 2015/4/4.
 */
public class PersonFragment extends Fragment {
    private GridView gridView;

    @Override
    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("PersonFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("PersonFragment");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.healthy_fragment,container, false);
        InitView(view);
        InitListener();
        return view;
    }

    private void InitView(View view) {
        gridView = (GridView) view.findViewById(R.id.grid_view);
        gridView.setAdapter(new PersonImageAdapter(getActivity()));
    }

    private void InitListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        goActivity(getActivity(), RemindActivity.class);
                        break;
                    case 1:
                        goActivity(getActivity(), HangListActivity.class);
                        break;
                    case 2:
                        goActivity(getActivity(), CaseActivity.class);
                        break;
                    case 3:
                        goActivity(getActivity(), DeviceActivity.class);
                        break;
                    case 4:
                        goActivity(getActivity(), FamilyActivity.class);
                        break;
                    case 5:
                        break;
                }
            }
        });
    }

    public void goActivity(Context context, Class<? extends android.app.Activity> anotherclass){
        Intent intent = new Intent(context,anotherclass);
        context.startActivity(intent);
    }
}
