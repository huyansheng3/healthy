package com.example.fengmanlou.logintest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.*;
import android.os.Process;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fengmanlou.logintest.util.CustomerAnim;


/**
 * Created by fengmanlou on 2015/4/7.
 */
public class LoginActivity extends BaseActivity{
    private EditText accountEdit,passwordEdit;
    private Button login,exit;
    private CheckBox rememberPassBox;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ImageView pointView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        final CustomerAnim customerAnim = new CustomerAnim();
        customerAnim.setDuration(1000);
        pointView = (ImageView) findViewById(R.id.point_image);

        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        exit = (Button) findViewById(R.id.exit);
        rememberPassBox = (CheckBox) findViewById(R.id.remember_pass);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if (isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPassBox.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();
                if (account.equals("admin")&&password.equals("123456")){
                    editor = pref.edit();
                    if (rememberPassBox.isChecked()) {
                        editor.putString("account", account);
                        editor.putString("password", password);
                        editor.putBoolean("remember_password", true);
                    }else {
                        editor.clear();
                    }
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                }
                else {
                    pointView.setVisibility(View.VISIBLE);
                    pointView.startAnimation(customerAnim);
                    Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //android.os.Process.killProcess(Process.myPid());
                System.exit(0);
            }
        });
    }

}
