package com.example.fengmanlou.logintest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.avobject.Doctor;
import com.example.fengmanlou.logintest.util.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by fengmanlou on 2015/4/9.
 */
public class DoctorAdapter extends BaseAdapter {
    private Context context;
    private List<Doctor> doctorList;

    public DoctorAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }


    //get the number
    @Override
    public int getCount() {
        return doctorList != null ? doctorList.size():0;
    }

    @Override
    public Object getItem(int position) {
        if (doctorList != null){
            return doctorList.get(position);
        }
        else {
            return position;
        }
    }

    //get the current selector's id number
    @Override
    public long getItemId(int position) {
        return position;
    }

    //create view method
    @Override
    public View getView(int position, View view, ViewGroup viewgroup) {
        ImgTextWrapper wrapper;
        if (view == null) {
            wrapper = new ImgTextWrapper();
            view = LayoutInflater.from(context).inflate(R.layout.doctor_item,null);
            wrapper.doctor_image = (ImageView) view.findViewById(R.id.doctor_image);
            wrapper.doctor_name = (TextView) view.findViewById(R.id.doctor_name);
            wrapper.doctor_hospital = (TextView) view.findViewById(R.id.doctor_hospital);
            wrapper.doctor_depart = (TextView) view.findViewById(R.id.doctor_depart);
            view.setTag(wrapper);
        } else {
            wrapper = (ImgTextWrapper) view.getTag();
        }

        if (doctorList.get(position) != null){
            AVFile avFile = doctorList.get(position).getAVFile("image");
            if (avFile == null) {
                Logger.d("该医生的图片没有");

            }
            if (avFile != null) {
                String url = avFile.getUrl();
                //显示图片的配置
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnFail(R.drawable.healthy_icon01)
                        .showImageForEmptyUri(R.drawable.healthy_icon01)
                        .showImageOnLoading(R.drawable.healthy_icon01)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .displayer(new SimpleBitmapDisplayer())
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();

                ImageLoader.getInstance().displayImage(url, wrapper.doctor_image, options);
            }
            if (doctorList.get(position).getName() != null) {
                wrapper.doctor_name.setText(doctorList.get(position).getName());
            }
            if (doctorList.get(position).getHospital() != null) {
                wrapper.doctor_hospital.setText(doctorList.get(position).getHospital());
            }
            if (doctorList.get(position).getDepart() != null) {
                wrapper.doctor_depart.setText(doctorList.get(position).getDepart());
            }
        }
        return view;
    }


    class ImgTextWrapper {
        ImageView doctor_image;
        TextView doctor_name;
        TextView doctor_hospital;
        TextView doctor_depart;
    }

}