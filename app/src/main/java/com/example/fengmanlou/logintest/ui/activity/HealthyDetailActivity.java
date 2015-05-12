package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Watson;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.util.Logger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fengmanlou on 2015/4/27.
 */
public class HealthyDetailActivity extends Activity{
    private TextView healthy_content_view,healthy_message_view,healthy_title_view;
    private NetworkImageGetter mImageGetter;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healty_detail);
        healthy_content_view = (TextView) findViewById(R.id.healthy_content_view);
        healthy_message_view =(TextView) findViewById(R.id.healthy_message_view);
        healthy_title_view =(TextView) findViewById(R.id.healthy_title_view);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        AsyncHttpClient asyncHttpClient= new AsyncHttpClient();

        /*"http://health.enorth.com.cn/system/2015/04/27/030188986.shtml"*/
        asyncHttpClient.get(url,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String str = new String(bytes,"gb2312");
                    Document document = Jsoup.parse(str);
                    Element body = document.body();
                    Element title = body.getElementById("Title");
                    Element source = body.getElementById("source_baidu");
                    Element content = body.getElementById("Content");
                    Element author = body.getElementById("author_baidu");
                    Element pubtime = body.getElementById("pubtime_baidu");
                    Element editor = body.getElementById("editor_baidu");
                    healthy_title_view.setText(title.text());

                    if (source != null && author != null && pubtime != null && editor != null) {
                        healthy_message_view.setText("    " + source.text() + "\n" + author.text() + " " + pubtime.text() + " " + editor.text());
                    }
                    //去你麻痹的各种网络教程博客，4.0以上全他妈不支持了！
                    mImageGetter = new NetworkImageGetter();
                    Spanned sp = Html.fromHtml(content.html(),mImageGetter,null);
                    healthy_content_view.setText(sp);

                }catch (IOException e){
                    Log.d("file read exception : ", e.getMessage());
                }


            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(HealthyDetailActivity.this,"请检查网络连接",Toast.LENGTH_SHORT).show();
            }
        });
    }

        public class NetworkImageGetter implements Html.ImageGetter{
            @Override
            public Drawable getDrawable(String source) {
                LevelListDrawable d = new LevelListDrawable();
                Drawable empty = getResources().getDrawable(R.drawable.healthy_icon01);
                d.addLevel(0, 0, empty);
                d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                new LoadImage().execute(source, d);
                return d;
            }
        }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];

            try {
                Logger.d("try到底执行了么？");
                Logger.d("source : "+source);
                Logger.d("mDrawable  : "+mDrawable);
                InputStream is = new URL(source).openStream();
                Logger.d("inputStream 不执行");  //当我使用龙五的手机时可以正常的读写输入流，而我的手机不可以，我猜测是由于我的手机被对方屏蔽了图片的请求。
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Logger.d("bitmap 是否为空 ？ ： "+bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth()*2, bitmap.getHeight()*2);
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                Logger.d("healthy_content_view.setText(t); 为什么图片不刷新呢？");
                CharSequence t = healthy_content_view.getText();
                healthy_content_view.setText(t);

            }
        }
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
            String title = healthy_title_view.getText().toString();
            intent.putExtra(Intent.EXTRA_TEXT,title+"\n"+url+" \n         ——来自健康服务");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent,HealthyDetailActivity.this.getTitle()));
        }

        return super.onOptionsItemSelected(item);
    }
}
