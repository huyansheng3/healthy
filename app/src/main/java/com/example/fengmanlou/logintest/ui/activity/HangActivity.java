package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.avobject.Hang;
import com.example.fengmanlou.logintest.util.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by fengmanlou on 2015/5/5.
 */
public class HangActivity extends Activity{
    private Button hang_submit,hang_cancel;
    private Spinner hospital_spinner,department_spinner;
    private EditText hang_name,hang_number;
    private DatePicker hang_datePicker;
    private static final String[] departData={"急诊部","皮肤科","五官科","内脏科","其他"};
    private Hang hang;
    private String name,number,dateString,hospital,depart;
    private AVUser curUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hang);
        initView();
        hang = new Hang();
        final Calendar hangCalendar = Calendar.getInstance();
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateString = simpleDateFormat.format(calendar.getTime()); //若日期为改变，不会给执行下面方法，所以给datestring一个当前日期的默认时间
        hang_datePicker.init(mYear,mMonth,mDay,new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                hangCalendar.set(year,monthOfYear,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateString = simpleDateFormat.format(hangCalendar.getTime()); //将datePicker中的所选中的日期设置为yyyy-MM-dd模式的字符串
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HangActivity.this,android.R.layout.simple_spinner_item,getData());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospital_spinner.setAdapter(adapter);
        hospital_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hospital = getData().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> departAdapter = new ArrayAdapter<String>(HangActivity.this,android.R.layout.simple_spinner_item,departData);
        departAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_spinner.setAdapter(departAdapter);
        department_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                depart = departData[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        hang_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击提交，将挂号的数据保存在远程服务器，并给挂号增加一个状态属性（申请中、审核通过）
                name = hang_name.getText().toString().trim();
                number = hang_number.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(HangActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(number)) {
                    Toast.makeText(HangActivity.this, "号码不能为空", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(dateString)) {
                    Toast.makeText(HangActivity.this, "时间不能为空", Toast.LENGTH_SHORT).show();
                }
                else if (hangCalendar.getTimeInMillis() < calendar.getTimeInMillis()) {  //预约的时间小于当前的时间
                    Toast.makeText(HangActivity.this, "预约时间不能小于当前时间", Toast.LENGTH_SHORT).show();
                }else {
                    hang.setName(name);
                    hang.setNumber(number);
                    hang.setHospital(hospital);
                    hang.setDepart(depart);
                    hang.setTime(dateString);
                    hang.setStatus("申请中");
                    curUser = AVUser.getCurrentUser();
                    hang.put("creator", curUser);
                    hang.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                // 保存成功
                                Intent intent = new Intent(HangActivity.this,HangListActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // 保存失败，输出错误信息
                                Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
                                Logger.d("saveInBackground failed");
                                Logger.d("e : " + e);
                            }
                        }
                    });
                }
            }
        });

        hang_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击取消，返回上一层的界面
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HangActivity.this,HangListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initView() {
        hang_submit = (Button) findViewById(R.id.hang_submit);
        hang_cancel = (Button) findViewById(R.id.hang_cancel);
        hospital_spinner = (Spinner) findViewById(R.id.hospital_spinner);
        department_spinner = (Spinner) findViewById(R.id.department_spinner);
        hang_name = (EditText) findViewById(R.id.hang_name);
        hang_number = (EditText) findViewById(R.id.hang_number);
        hang_datePicker = (DatePicker) findViewById(R.id.hang_datePicker);
    }

    private List<String> getData(){
        List<String> dataList = new ArrayList<>();
        dataList.add("协和医院");
        dataList.add("北京医院");
        dataList.add("西华医院");
        dataList.add("四医院");
        dataList.add("六医院");
        dataList.add("精神病院");
        return dataList;
    }
}
