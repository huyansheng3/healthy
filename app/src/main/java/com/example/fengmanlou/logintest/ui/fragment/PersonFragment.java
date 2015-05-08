package com.example.fengmanlou.logintest.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.avos.avoscloud.AVAnalytics;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.PersonImageAdapter;

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
        return view;
    }

    private void InitView(View view) {
        gridView = (GridView) view.findViewById(R.id.grid_view);
        gridView.setAdapter(new PersonImageAdapter(getActivity()));
    }
}
