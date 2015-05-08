package com.example.fengmanlou.logintest;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SampleListFragment extends ListFragment {



    /**
     *
     *	功能描述：列表Fragment，用来显示列表视图
     */

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.list, null);
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            SampleAdapter adapter = new SampleAdapter(getActivity());

            adapter.add(new SampleItem("个人中心", R.drawable.logo));
            adapter.add(new SampleItem("病历夹", R.drawable.ofm_feedback_icon));
            adapter.add(new SampleItem("消息", R.drawable.ofm_add_icon));
            adapter.add(new SampleItem("设置", R.drawable.ofm_setting_icon));
            adapter.add(new SampleItem("理念", R.drawable.ofm_setting_icon));
            adapter.add(new SampleItem("退出", R.drawable.ofm_setting_icon));
            setListAdapter(adapter);

        }

    @Override
    public void onListItemClick(ListView l, View v,int position, long id)
    {
        switch (position) {
            case 0:
                Toast.makeText(getActivity(),"个人中心 ",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getActivity(),"病历夹 ",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getActivity(),"消息 ",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(getActivity(),"设置 ",Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(getActivity(),"理念 ",Toast.LENGTH_SHORT).show();
                break;
            case 5:
                System.exit(0);
                break;
            default:
                break;
        }
    }


    private class SampleItem {
            public String tag;
            public int iconRes;
            public SampleItem(String tag, int iconRes) {
                this.tag = tag;
                this.iconRes = iconRes;
            }
        }


        public class SampleAdapter extends ArrayAdapter<SampleItem> {

            public SampleAdapter(Context context) {
                super(context, 0);
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView == null) {
                    view = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
                }
                else {
                    view = convertView;
                }
                ImageView icon = (ImageView) view.findViewById(R.id.row_icon);
                icon.setImageResource(getItem(position).iconRes);
                TextView title = (TextView) view.findViewById(R.id.row_title);
                title.setText(getItem(position).tag);

                return view;
            }

        }


}


