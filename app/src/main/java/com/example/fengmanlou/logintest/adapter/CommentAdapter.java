package com.example.fengmanlou.logintest.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.avobject.Comment;
import com.example.fengmanlou.logintest.service.NewsService;
import com.example.fengmanlou.logintest.util.DateSyncUtils;
import com.example.fengmanlou.logintest.util.Logger;
import com.example.fengmanlou.logintest.util.ToastUtils;

import java.text.ParseException;
import java.util.List;

/**
 * Created by fengmanlou on 2015/4/20.
 */
public class CommentAdapter extends BaseAdapter{
    private Context context;
    private List<Comment> comments;

    public CommentAdapter() {
        super();
    }

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments != null ? comments.size() : 0;
    }

    @Override
    public Object getItem(int position) {
       if (comments != null){
           return comments.get(position);
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
            holder = new ViewHolder();
            holder.commentTextView = (TextView) convertView.findViewById(R.id.comment);
            holder.comment_creator_nickname = (TextView) convertView.findViewById(R.id.comment_creator_nickname);
            holder.comment_createAt = (TextView) convertView.findViewById(R.id.comment_createAt);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        Comment comment = comments.get(position);
        if (comment != null) {
            holder.commentTextView.setText(comment.getContent());
            try {
                String strDate = DateSyncUtils.formatDate(comment.getCreatedAt());
                holder.comment_createAt.setText(strDate);
            }catch (ParseException e){
                Logger.i(e.getMessage());
            }
//            Log.i("hys", "creator id1: " + comment.getCreator().getObjectId());
//            //Log.i("hys","creator id2: "+comment.getCreator().get("objectId")); //这样获得的ObjectId为空
//            Log.i("hys","creator id2: "+comment.getCreator().getUsername());
            String userObjectId = comment.getCreator().getObjectId();
            GetCallback<AVObject> getCallback = new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null){
                        String nickname = avObject.getString("nickname");
                        String username = avObject.getString("username");
                        if (!TextUtils.isEmpty(nickname)) {
                            holder.comment_creator_nickname.setText(nickname);
                        }else {
                            holder.comment_creator_nickname.setText(username);
                        }
                        //  Log.i("hys","nickname : "+avObject.getString("nickname"));
                    }else {
                        e.getMessage();
                    }
                }
            };
            NewsService.fetchUserById(userObjectId,getCallback);
        }
        return convertView;
    }

    public class ViewHolder{
        TextView comment_creator_nickname;
        TextView comment_createAt;
        TextView commentTextView;
    }
}
