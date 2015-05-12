package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.avobject.Document;
import com.example.fengmanlou.logintest.util.Logger;

import java.util.List;

/**
 * Created by fengmanlou on 2015/5/1.
 */
public class DocumentActivity extends Activity{
    private RadioGroup document_sex,document_blood;
    private EditText document_name,document_age,document_experience,document_metal,document_allergy,document_remark;
    private Button document_save_btn;
    private boolean isEdit; //是否可以编辑，第一次是可以编辑的
    private Document document;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String objectId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        setTitle("健康档案");
        findView();
        document = new Document();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        isEdit = pref.getBoolean("isEdit",true);
        if (!isEdit){  //不需要编辑了
            AVQuery<Document> query = AVQuery.getQuery(Document.class);
            query.whereEqualTo("creator",AVUser.getCurrentUser());
            query.findInBackground(new FindCallback<Document>() {
                @Override
                public void done(List<Document> documents, AVException e) {
                    if (e != null){
                        Toast.makeText(DocumentActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }else {
                        document_name.setText(documents.get(0).getName());
                        document_age.setText(documents.get(0).getAge());
                        document_experience.setText(documents.get(0).getExperience());
                        document_metal.setText(documents.get(0).getMetal());
                        document_allergy.setText(documents.get(0).getAllergy());
                        document_remark.setText(documents.get(0).getRemark());
                        document_save_btn.setClickable(false);
                        objectId = documents.get(0).getObjectId();
                    }
                }
            });

        }else {
            saveDocument();
        }
    }

    private void saveDocument() {
        document_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(document_name.getText())) {
                    Toast.makeText(DocumentActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(document_age.getText())) {
                    Toast.makeText(DocumentActivity.this, "年龄不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(document_experience.toString())) {
                    Toast.makeText(DocumentActivity.this, "病史不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(document_metal.toString())) {
                    Toast.makeText(DocumentActivity.this, "精神状态不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(document_allergy.toString())) {
                    Toast.makeText(DocumentActivity.this, "过敏不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(document_remark.toString())) {
                    Toast.makeText(DocumentActivity.this, "备注不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Logger.d("name : " + document_name.getText().toString());
                    Logger.d("sex : " + document_age.getText().toString());
                    Logger.d("experience : " + document_experience.getText().toString());
                    Logger.d("allergy : " + document_allergy.getText().toString());
                    Logger.d("metal : " + document_metal.getText().toString());

                    document.setName(document_name.getText().toString().trim());
                    document.setAge(document_age.getText().toString().trim());
                    document.setExperience(document_experience.getText().toString().trim());
                    document.setAllergy(document_allergy.getText().toString().trim());
                    document.setRemark(document_remark.getText().toString().trim());
                    document.setMetal(document_metal.getText().toString().trim());
                    document.setCreator(AVUser.getCurrentUser());
                    document.setSex("男");
                    document.setBlood("A型");
                    document.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                //保存成功
                                Toast.makeText(DocumentActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                editor = pref.edit();
                                editor.putBoolean("isEdit",false); //将IsEdit设置为不可以编辑
                                editor.commit();
                            } else {
                                Toast.makeText(DocumentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void findView() {
        document_name = (EditText) findViewById(R.id.document_name);
        document_age = (EditText) findViewById(R.id.document_age);
        document_experience = (EditText) findViewById(R.id.document_experience);
        document_metal = (EditText) findViewById(R.id.document_metal);
        document_allergy = (EditText) findViewById(R.id.document_allergy);
        document_remark = (EditText) findViewById(R.id.document_remark);
        document_sex = (RadioGroup) findViewById(R.id.document_sex);
        document_blood = (RadioGroup) findViewById(R.id.document_blood);
        document_save_btn = (Button) findViewById(R.id.document_save_btn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_document,menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_docu){
            Toast.makeText(this,"请修改内容吧！",Toast.LENGTH_SHORT).show();
            AVQuery<Document> query = AVQuery.getQuery(Document.class);
            query.whereEqualTo("creator",AVUser.getCurrentUser());
            query.findInBackground(new FindCallback<Document>() {
                @Override
                public void done(List<Document> documents, AVException e) {
                    if (e != null){
                        Toast.makeText(DocumentActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }else {
                        document_name.setText(documents.get(0).getName());
                        document_age.setText(documents.get(0).getAge());
                        document_experience.setText(documents.get(0).getExperience());
                        document_metal.setText(documents.get(0).getMetal());
                        document_allergy.setText(documents.get(0).getAllergy());
                        document_remark.setText(documents.get(0).getRemark());
                        document_save_btn.setClickable(true);
                        document = documents.get(0);
                        saveDocument();
                    }
                }
            });
        }

        return super.onMenuItemSelected(featureId, item);
    }
}
