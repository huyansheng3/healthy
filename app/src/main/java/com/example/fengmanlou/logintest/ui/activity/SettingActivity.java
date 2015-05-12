package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.base.BaseActivity;
import com.example.fengmanlou.logintest.ui.fragment.SettingFragment;

/**
 * Created by fengmanlou on 2015/4/22.
 */
public class SettingActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        settingFragment = new SettingFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.setting_content,settingFragment);
        fragmentTransaction.commit();


    }



    /*
    * 保存更改的设置项
    * @param sharedPreferences
    * @param key
    */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        sharedPreferences.getString(key,"");
    }
}
