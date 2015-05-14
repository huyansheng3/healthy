package com.example.fengmanlou.logintest.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.ui.activity.AboutActivity;
import com.example.fengmanlou.logintest.ui.activity.SettingActivity;
import com.example.fengmanlou.logintest.ui.activity.UserInforActivity;
import com.example.fengmanlou.logintest.util.ToastUtils;

import org.litepal.tablemanager.Connector;

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

            adapter.add(new SampleItem("个人中心", R.drawable.ofm_person));
            adapter.add(new SampleItem("分享", R.drawable.action_share_icon_gray));
            adapter.add(new SampleItem("设置", R.drawable.ofm_setting));
            adapter.add(new SampleItem("关于", R.drawable.ofm_idea));
            adapter.add(new SampleItem("注销", R.drawable.ofm_logout));

            setListAdapter(adapter);

        }

    @Override
    public void onListItemClick(ListView l, View v,int position, long id)
    {
        switch (position) {
            case 0:
                goActivity(getActivity(), UserInforActivity.class);
                break;
            case 1:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"分享");
                intent.putExtra(Intent.EXTRA_TEXT,"小伙子们，来试试这个好玩的APP吧。");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent,getActivity().getTitle()));
                break;
            case 2:
                goActivity(getActivity(), SettingActivity.class);
                break;
            case 3:
                goActivity(getActivity(), AboutActivity.class);
                break;
            case 4:
                AVUser.logOut();             //清除缓存用户对象
                AVUser currentUser = AVUser.getCurrentUser(); // 现在的currentUser是null了
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

    public void goActivity(Context context, Class<? extends android.app.Activity> anotherclass){
        Intent intent = new Intent(context,anotherclass);
        context.startActivity(intent);
    }

}


