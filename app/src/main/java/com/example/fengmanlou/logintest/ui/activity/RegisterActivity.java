package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.avobject.User;
import com.example.fengmanlou.logintest.base.ActivityCollector;
import com.example.fengmanlou.logintest.base.BaseActivity;

/**
 * Created by fengmanlou on 2015/4/11.
 */
public class RegisterActivity extends BaseActivity{
    private EditText usernicknameEdit,accountEdit,passwordEdit,ensurePasswordEdit,useremailEdit;
    private RadioGroup sexRadio;
    private Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        usernicknameEdit = (EditText) findViewById(R.id.usernicknameEdit);
        accountEdit = (EditText) findViewById(R.id.accountEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        ensurePasswordEdit = (EditText) findViewById(R.id.ensurePasswordEdit);
        useremailEdit = (EditText) findViewById(R.id.useremailEdit);
        sexRadio =(RadioGroup) findViewById(R.id.sexRadio);
        btn_register = (Button) findViewById(R.id.btn_register);

        final AVUser user = new AVUser();



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEdit.getText().toString().equals(ensurePasswordEdit.getText().toString())){
                    user.setUsername(accountEdit.getText().toString());
                    user.setPassword(passwordEdit.getText().toString());
                    user.setEmail(useremailEdit.getText().toString());
                    user.put("nickname",usernicknameEdit.getText().toString());
                    user.put("sex",sexRadio.getCheckedRadioButtonId());
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null){
                                //注册成功后直接登陆系统

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                ActivityCollector.finishAll();

                            }
                            else {

                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
