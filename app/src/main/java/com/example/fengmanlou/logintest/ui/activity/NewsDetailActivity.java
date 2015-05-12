package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.CommentAdapter;
import com.example.fengmanlou.logintest.avobject.Comment;
import com.example.fengmanlou.logintest.service.NewsService;
import com.example.fengmanlou.logintest.util.ListViewUtils;
import com.example.fengmanlou.logintest.util.Logger;
import com.example.fengmanlou.logintest.util.NetWorkHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by fengmanlou on 2015/4/19.
 */
public class NewsDetailActivity extends Activity {
    private EditText comment_edit;
    private Button create_comment;
    private String objectId;
    private Dialog progressDialog;
    private TextView news_detail_title,news_detail_content;
    private ImageView news_detail_image;
    private ListView listView;
    private volatile List<Comment> comments;
    @Override
    protected void onPause() {
        super.onPause();
        //页面统计结束
        AVAnalytics.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //页面统计，开始
        AVAnalytics.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (progressDialog != null){
//            progressDialog.dismiss();
//            progressDialog = null ;
//        }
    }

    public void findView() {
        news_detail_title = (TextView) findViewById(R.id.detail_news_title);
        news_detail_content = (TextView) findViewById(R.id.detail_news_content);
        news_detail_image = (ImageView) findViewById(R.id.detail_news_image);
        comment_edit = (EditText) findViewById(R.id.comment_edit);
        create_comment = (Button) findViewById(R.id.create_comment);
        listView = (ListView) findViewById(android.R.id.list);
    }

    private class RemoteCommentTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        @Override
        protected Void doInBackground(Void... params) {
            comments = NewsService.findComments(objectId);
            Log.i("hys","comment size :"+comments.size());
            return null;
        }

        @Override
        protected void onPreExecute() {
            NewsDetailActivity.this.progressDialog =
                    ProgressDialog.show(NewsDetailActivity.this, "", "Loading...", true);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // 展现ListView
            CommentAdapter adapter = new CommentAdapter(NewsDetailActivity.this,comments);
            listView.setAdapter(adapter);
            ListViewUtils.setListViewHeightBaseOnChildren(listView);
            NewsDetailActivity.this.progressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        setTitle("创建新闻");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        findView();

        Intent intent = getIntent();

        // 通过搜索结果打开
        if (intent.getAction() == Intent.ACTION_VIEW) {
            // 如果是VIEW action，我们通过getData获取URI
            Uri uri = intent.getData();
            String path = uri.getPath();
            int index = path.lastIndexOf("/");
            if (index > 0) {
                // 获取objectId
                objectId = path.substring(index + 1);

                GetCallback<AVObject> getCallback = new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject news, AVException arg1) {
                        if (news != null) {
                            String title = news.getString("title");
                            String content = news.getString("content");
                            if (title != null) {
                                news_detail_title.setText(title);
                            }
                            if (content != null) {
                                news_detail_content.setText(content);
                            }
                            String imageUrl = news.getAVFile("image").getUrl();
                            if (imageUrl != null){
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

                                ImageLoader.getInstance().displayImage(imageUrl, news_detail_image, options);
                            }
                        }
                    }
                };
                NewsService.fetchNewsById(objectId, getCallback);
            }

            if (NetWorkHelper.isNetAvailable(NewsDetailActivity.this)) {
                new RemoteCommentTask().execute();
            }
            else {
                Toast.makeText(NewsDetailActivity.this,"网络未连接",Toast.LENGTH_SHORT).show();
            }

        } else {
            // 通过ListView点击打开
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String title = extras.getString("title");
                String content = extras.getString("content");
                objectId = extras.getString("objectId");
                String imageUrl = extras.getString("imageUrl");
                if (imageUrl != null){
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

                    ImageLoader.getInstance().displayImage(imageUrl, news_detail_image, options);
                }
                if (title != null) {
                    news_detail_title.setText(title);
                }
                if (content != null) {
                    news_detail_content.setText(content);
                }
            }
            if (NetWorkHelper.isNetAvailable(NewsDetailActivity.this)) {
                new RemoteCommentTask().execute();
            }
            else {
                Toast.makeText(NewsDetailActivity.this,"网络未连接",Toast.LENGTH_SHORT).show();
            }
            create_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SaveCallback saveCallback = new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            // done方法一定在UI线程执行
                            if (e != null){
                                Logger.d(e.getMessage());
                            }
                        }
                    };

                    String comment = comment_edit.getText().toString();
                    NewsService.CreateOrUpdateComment(objectId,comment,saveCallback); //objectId为新闻的Id
                    new RemoteCommentTask().execute();
                    comment_edit.setText("");
                }
            });
        }




        //手机应用就不提供创建新闻的功能好了，只提供阅读新闻和评论的功能
//        create_news = (Button) findViewById(R.id.create_news);
//        create_news.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                SaveCallback saveCallback = new SaveCallback() {
//                    @Override
//                    public void done(AVException e) {
//                        // done方法一定在UI线程执行
//                        if (e != null) {
//                            Log.e("hys", "Update news failed.", e);
//                        }
//                        Bundle bundle = new Bundle();
//                        bundle.putBoolean("success", e == null);
//                        Intent intent = new Intent();
//                        intent.putExtras(bundle);
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//                };
//                String title = news_title_edit.getText().toString();
//                String content = news_content_edit.getText().toString();
//
//                NewsService.CreateOrUpdateNews(objectId, title, content, saveCallback);
//            }
//        });



}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_share_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.news_share){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT,"分享");
            String title = news_detail_title.getText().toString();
            intent.putExtra(Intent.EXTRA_TEXT,title+" \n         ——来自健康服务");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent,NewsDetailActivity.this.getTitle()));
        }

        return super.onOptionsItemSelected(item);
    }
}
