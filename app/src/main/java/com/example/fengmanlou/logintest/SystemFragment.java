package com.example.fengmanlou.logintest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.fengmanlou.logintest.util.ImageAdapter;

/**
 * Created by fengmanlou on 2015/4/4.
 */
public class SystemFragment extends Fragment {

    private GridView gridView;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.healthy_fragment, container, false);
//            InitView(view);
//            return view;
//        }

    private void InitView(View view) {
        gridView = (GridView) view.findViewById(R.id.grid_view);
        gridView.setAdapter(new ImageAdapter(getActivity()));
    }
    }
