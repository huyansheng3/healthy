package com.example.fengmanlou.logintest.ui.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.HealthyAdapter;
import com.example.fengmanlou.logintest.avobject.Healthy;
import com.example.fengmanlou.logintest.base.Constants;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengmanlou on 2015/4/27.
 */
public class HealthyActivity extends Activity{
    private  List<Healthy> healthyList;
    private Healthy healthy;
    private PullToRefreshListView pullToRefreshListView;
    private String nextPageUrl;
    private String news_source,news_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_titleList);
        healthyList = new ArrayList<>();
        nextPageUrl = new String();
        Intent intent =getIntent();
        news_source = intent.getStringExtra("news_source");
        news_title = intent.getStringExtra("news_title");
        setTitle(news_title);
        /*asyncHttpClient网络请求可以在主线程中进行，其实是httpClient和new runnable进行了封装*/
        final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(news_source, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {   //发送http请求返回字节流成功
                try {
                    String str = new String(bytes, "gb2312");
                    Document document = Jsoup.parse(str);
                    Element body = document.body();
                    Elements tdWidth = body.select("td[width=500]");
                    Elements tdHeight = tdWidth.select("td[height=40]");
                    Elements align = tdWidth.select("td[align=left]");
                    Elements links = tdHeight.select("a");
                    for (Element link : links) {
                        String title = link.text();
                        String url = link.attr("href");
                        healthy = new Healthy();
                        healthy.setTitle(title);
                        healthy.setUrl(url);
                        healthyList.add(healthy);
                    }
                    Elements link_more = body.select("a[href^=http://health.enorth.com.cn/system/more/]");
                    nextPageUrl = link_more.attr("href");
                } catch (IOException e) {
                    Log.d("file read exception : ", e.getMessage());
                }

                HealthyAdapter healthyAdapter = new HealthyAdapter(HealthyActivity.this, healthyList);
                pullToRefreshListView.setAdapter(healthyAdapter);
                pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(HealthyActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                Log.d("asyncHttpClient fail : ", throwable.getMessage());
            }
        });

        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = healthyList.get((int) id).getTitle();
                String url = healthyList.get((int) id).getUrl();
                Intent intent = new Intent(HealthyActivity.this, HealthyDetailActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {   //下滑刷新
                String label = DateUtils.formatDateTime(getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("更新于" + " : " + label);
                //Toast.makeText(HealthyActivity.this,"下拉刷新",Toast.LENGTH_SHORT).show();

                new FinishRefresh().execute();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {  //上滑刷新
                //Toast.makeText(HealthyActivity.this,"上拉刷新",Toast.LENGTH_SHORT).show();
                String label = DateUtils.formatDateTime(getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
                        "更新于" + " : " + label);

                asyncHttpClient.get(nextPageUrl,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        String str = new String(bytes,"gb2312");
                        Document document = Jsoup.parse(str);
                        Element body = document.body();
                        Elements tdWidth = body.select("td[width=500]");
                        Elements tdHeight = tdWidth.select("td[height=40]");
                        Elements links = tdHeight.select("a");
                        for (Element link : links){
                            String title = link.text();
                            String url = link.attr("href");
                            healthy = new Healthy();
                            healthy.setTitle(title);
                            healthy.setUrl(url);
                            healthyList.add(healthy);
                        }

//
//
//                        Iterator<Healthy> iterator = healthyList.iterator();
//                        while (iterator.hasNext()){
//                            Healthy healthy = iterator.next();
//                            Log.d("hys","healthy1.getTitle() : "+healthy.getTitle());
//                        }

                        HealthyAdapter healthyAdapter = new HealthyAdapter(HealthyActivity.this,healthyList);

                        healthyAdapter.notifyDataSetChanged();
                        pullToRefreshListView.onRefreshComplete();

                        Elements link_more = body.select("a[href^=http://health.enorth.com.cn/system/more/]");
                        if (link_more.size() == 2){
                            nextPageUrl = link_more.get(1).attr("href");
                        }
                        else if (link_more.size() == 1){
                            nextPageUrl = link_more.get(0).attr("href");
                        }
                        else if (link_more.size() == 0){
                            Toast.makeText(HealthyActivity.this, "没有内容啦！", Toast.LENGTH_SHORT).show();
                        }

                    }catch (IOException e){
                        Log.d("file read exception : ", e.getMessage());
                    }

                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Toast.makeText(HealthyActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                    Log.d("asyncHttpClient fail : ",throwable.getMessage());
                }
            });
        }


              //  new GetDataTask(pullToRefreshListView,healthyAdapter).execute();
      });

    }

    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            pullToRefreshListView.onRefreshComplete();
        }
    }

}
