package com.example.fengmanlou.logintest.ui.menu;

import android.content.Context;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.example.fengmanlou.logintest.R;

/**
 * Created by fengmanlou on 2015/4/4.
 */
public class PlusActionProvider extends ActionProvider {

    private Context context;

    public PlusActionProvider(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
        subMenu.add("添加好友")
                .setIcon(R.drawable.actionbar_add_icon)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                });

        subMenu.add("分享")
                .setIcon(R.drawable.actionbar_share_icon)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                });
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }

}