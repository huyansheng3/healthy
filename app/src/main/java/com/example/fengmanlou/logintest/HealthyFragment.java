package com.example.fengmanlou.logintest;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fengmanlou.logintest.util.ImageAdapter;

/**
 * Created by fengmanlou on 2015/4/4.
 */
public class HealthyFragment extends Fragment {
    private GridView gridView;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.healthy_fragment,container, false);
        InitView(view);
        InitListener();
        return view;
    }

    private void InitView(View view) {
        gridView = (GridView) view.findViewById(R.id.grid_view);
        gridView.setAdapter(new ImageAdapter(getActivity()));
    }


    private void InitListener() {
       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Toast.makeText(getActivity(),"每日新闻",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(),"免疫知识",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"就医地图",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(),"健康咨询",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getActivity(),"预约体检",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(getActivity(),"提醒就医",Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(getActivity(),"自助挂号",Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(getActivity(),"评价服务",Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        Toast.makeText(getActivity(),"私人医生",Toast.LENGTH_SHORT).show();
                        break;
                }
           }
       });
    }


}