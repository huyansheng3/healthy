package com.example.fengmanlou.logintest.ui.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.avos.avoscloud.feedback.FeedbackAgent;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.util.ToastUtils;

/**
 * Created by fengmanlou on 2015/4/26.
 */
public class SettingFragment extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        String clearCache = getResources().getString(R.string.setting_key_clear_cache);
        String postBack = getResources().getString(R.string.setting_key_post_back);
        String checkUpdate = getResources().getString(R.string.setting_key_check_update);

        if (clearCache.equals(key)){
            Toast.makeText(getActivity(),"清除缓存",Toast.LENGTH_SHORT).show();
            return true;
        }else if (checkUpdate.equals(key)){
            Toast.makeText(getActivity(),"检查更新",Toast.LENGTH_SHORT).show();
            return true;
        }else if (postBack.equals(key)){
            FeedbackAgent agent = new FeedbackAgent(getActivity());
            agent.startDefaultThreadActivity();
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Registers a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(
                (android.content.SharedPreferences.OnSharedPreferenceChangeListener) getActivity());

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
                (android.content.SharedPreferences.OnSharedPreferenceChangeListener) getActivity());
    }
}
