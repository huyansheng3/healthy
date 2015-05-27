package com.example.fengmanlou.logintest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.service.NewsService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by fengmanlou on 2015/5/17.
 */
public class FamilyAdapter extends BaseAdapter{
    private Context context;
    private List<String> objectIdList;

    public FamilyAdapter(Context context, List<String> objectIdList) {
        this.context = context;
        this.objectIdList = objectIdList;
    }

    @Override
    public int getCount() {
        return objectIdList != null ? objectIdList.size():0;
    }

    @Override
    public Object getItem(int position) {
        if(objectIdList != null){
            return objectIdList.get(position);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.user_items,null);
            holder.avatar_image = (ImageView) convertView.findViewById(R.id.user_items_avatar);
            holder.name_textView = (TextView) convertView.findViewById(R.id.user_items_nickname);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String objectId = objectIdList.get(position);
        if (objectId != null){
            GetCallback<AVObject> getCallback = new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null){
                        String nickname = avObject.getString("nickname");
                        String imageUrl = avObject.getAVFile("avatar").getUrl();
                        if (!TextUtils.isEmpty(nickname)) {
                            holder.name_textView.setText(nickname);
                        }
                        if (imageUrl != null) {
                            DisplayImageOptions options = new DisplayImageOptions.Builder()
                                    .showImageOnFail(R.drawable.healthy_icon01)
                                    .showImageForEmptyUri(R.drawable.healthy_icon01)
                                    .showImageOnLoading(R.drawable.healthy_icon01)
                                    .cacheInMemory(true)
                                    .cacheOnDisk(true)
                                    .displayer(new SimpleBitmapDisplayer())
                                    .bitmapConfig(Bitmap.Config.RGB_565)
                                    .build();

                            ImageLoader.getInstance().displayImage(imageUrl, holder.avatar_image, options);
                        }

                        //  Log.i("hys","nickname : "+avObject.getString("nickname"));
                    }else {
                        e.getMessage();
                    }
                }
            };
            NewsService.fetchUserById(objectId, getCallback);
        }

        return convertView;
    }

    class ViewHolder{
        ImageView avatar_image;
        TextView name_textView;
    }
}
