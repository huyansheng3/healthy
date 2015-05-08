package com.example.fengmanlou.logintest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.sns.SNSType;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.base.BaseActivity;
import com.example.fengmanlou.logintest.util.CustomerAnim;



/**
 * Created by fengmanlou on 2015/4/7.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText accountEdit, passwordEdit;
    private Button login, exit;
    private ImageView pointView;
    private TextView registerBtn;
    private ImageButton qq_login, weibo_login;
    private SNSType type = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);





        final CustomerAnim customerAnim = new CustomerAnim();
        customerAnim.setDuration(1000);
        pointView = (ImageView) findViewById(R.id.point_image);

        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        exit = (Button) findViewById(R.id.exit);
        qq_login = (ImageButton) findViewById(R.id.qq_login);
        weibo_login = (ImageButton) findViewById(R.id.weibo_login);
        registerBtn = (TextView) findViewById(R.id.btn_register);

        registerBtn.setOnClickListener(this);
        qq_login.setOnClickListener(this);
        weibo_login.setOnClickListener(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //点击登陆，进行登陆验证
                String account = accountEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();


                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                    pointView.setVisibility(View.VISIBLE);
                    pointView.startAnimation(customerAnim);
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    AVUser.logInInBackground(account, password, new LogInCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            if (avUser != null) {
                                //登陆成功
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //登陆失败
                                pointView.setVisibility(View.VISIBLE);
                                pointView.startAnimation(customerAnim);
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });

        exit.setOnClickListener(new View.OnClickListener() { //点击退出直接退出程序
            @Override
            public void onClick(View v) {
                //android.os.Process.killProcess(Process.myPid());
                System.exit(0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.qq_login :
                Toast.makeText(LoginActivity.this,"QQ登陆",Toast.LENGTH_SHORT).show();
                break;

            case R.id.weibo_login :
                Toast.makeText(LoginActivity.this,"微博登陆",Toast.LENGTH_SHORT).show();
                break;
        }
    }





}