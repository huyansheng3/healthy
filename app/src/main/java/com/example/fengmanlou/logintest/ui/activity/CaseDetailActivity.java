package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fengmanlou.logintest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.Date;

/**
 * Created by fengmanlou on 2015/5/14.
 */
public class CaseDetailActivity extends Activity{
    private TextView case_name,case_age,case_hospital,case_time,case_describe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);

        case_name = (TextView) findViewById(R.id.case_name);
        case_age = (TextView) findViewById(R.id.case_age);
        case_hospital = (TextView) findViewById(R.id.case_hospital);
        case_time = (TextView) findViewById(R.id.case_time);
        case_describe = (TextView) findViewById(R.id.case_describe);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("title");
            String describe = extras.getString("describe");
            String name = extras.getString("name");
            String age = extras.getString("age");
            String time = extras.getString("time");
            String hospital = extras.getString("hospital");

            if (describe != null) {
                case_describe.setText(describe);
            }

            if (name != null){
                case_name.setText(name);
            }
            if (age != null){
                case_age.setText(age);
            }
            if (hospital != null){
                case_hospital.setText(hospital);
            }
            if (time != null){
                case_time.setText(time);
            }
        }


    }
}
