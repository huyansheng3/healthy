package com.example.fengmanlou.logintest.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.SystemImageAdapter;
import com.example.fengmanlou.logintest.ui.activity.ChatActivity;
import com.example.fengmanlou.logintest.util.Logger;

import java.util.List;

/**
 * Created by fengmanlou on 2015/4/4.
 */
public class SystemFragment extends Fragment {

    private ListView listView;
    private SystemImageAdapter adapter;
    private List<AVUser> avUserList;

    @Override
    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("SystemFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("SystemFragment");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.system_fragment, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        AVQuery<AVUser> query = AVQuery.getQuery(AVUser.class);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> avUsers, AVException e) {
                if (e == null){
                    avUserList = avUsers;
                }else {
                    Logger.d("avUsers are null");
                }
            }
        });
        adapter = new SystemImageAdapter(getActivity(),avUserList);
        listView.setAdapter(adapter);
        InitListener();
        return view;
    }

    public void InitListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :
                        goActivity(getActivity(), ChatActivity.class);
                        break;
                    case 1 :
                        Toast.makeText(getActivity(),"1",Toast.LENGTH_SHORT).show();
                        break;
                    case 2 :
                        Toast.makeText(getActivity(),"2",Toast.LENGTH_SHORT).show();
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

