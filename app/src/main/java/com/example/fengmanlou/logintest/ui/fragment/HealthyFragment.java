package com.example.fengmanlou.logintest.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AnalyticsUtils;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.HealthyImageAdapter;
import com.example.fengmanlou.logintest.base.Constants;
import com.example.fengmanlou.logintest.ui.activity.DocumentActivity;
import com.example.fengmanlou.logintest.ui.activity.HangActivity;
import com.example.fengmanlou.logintest.ui.activity.HangListActivity;
import com.example.fengmanlou.logintest.ui.activity.HealthyActivity;
import com.example.fengmanlou.logintest.ui.activity.HealthyDetailActivity;
import com.example.fengmanlou.logintest.ui.activity.MapActivity;
import com.example.fengmanlou.logintest.ui.activity.NewsActivity;
import com.example.fengmanlou.logintest.ui.activity.RemindActivity;

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
    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("HealthyFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("HealthyFragment");
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
        gridView.setAdapter(new HealthyImageAdapter(getActivity()));
    }


    private void InitListener() {
       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        goActivity(getActivity(), NewsActivity.class);
                        break;
                    case 1:
                        Intent intent = new Intent(getActivity(),HealthyActivity.class);
                        intent.putExtra("news_source", Constants.HEALTHY_NEWS_LIST);
                        intent.putExtra("news_title", "北方网健康");
                        startActivity(intent);
                        break;
                    case 2:
                        goActivity(getActivity(),MapActivity.class);
                        break;
                    case 3:
                        Intent intent1 = new Intent(getActivity(),HealthyActivity.class);
                        intent1.putExtra("news_source", Constants.RECOMMEND_DIETS);
                        intent1.putExtra("news_title", "推荐食谱");
                        startActivity(intent1);
                        break;
                    case 4:
                        Intent intent2 = new Intent(getActivity(),HealthyActivity.class);
                        intent2.putExtra("news_source", Constants.CHINA_HEALTHY);
                        intent2.putExtra("news_title", "中医养生");
                        startActivity(intent2);
                        break;
                    case 5:
                        goActivity(getActivity(), DocumentActivity.class);
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