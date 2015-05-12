package com.example.fengmanlou.logintest.ui.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUtils;
import com.example.fengmanlou.logintest.R;
import com.example.fengmanlou.logintest.adapter.NewsAdapter;
import com.example.fengmanlou.logintest.avobject.News;
import com.example.fengmanlou.logintest.service.NewsService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * Created by fengmanlou on 2015/4/18.
 */
public class NewsActivity extends ListActivity {
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    public static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int SEARCH_ID = Menu.FIRST + 2;

    private volatile List<News> newses;
    private Dialog progressDialog;
    private EditText searchInput;
    private PullToRefreshListView pullToRefreshListView;



    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        @Override
        protected Void doInBackground(Void... params) {
            newses = NewsService.findNewses();
            return null;
        }

        @Override
        protected void onPreExecute() {
            NewsActivity.this.progressDialog =
                    ProgressDialog.show(NewsActivity.this, "", "Loading...", true);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // 展现ListView
            NewsAdapter adapter = new NewsAdapter(NewsActivity.this,newses);
            pullToRefreshListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            pullToRefreshListView.onRefreshComplete();
            registerForContextMenu(pullToRefreshListView);
            NewsActivity.this.progressDialog.dismiss();
            TextView empty = (TextView) findViewById(android.R.id.empty);
            if (newses != null && !newses.isEmpty()) {
                empty.setVisibility(View.INVISIBLE);
            } else {
                empty.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setTitle("健康资讯");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        TextView empty = (TextView)findViewById(android.R.id.empty);
        empty.setVisibility(View.VISIBLE);
        searchInput = new EditText(this);
        new RemoteDataTask().execute();

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(),System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
                        "更新于" + " : " + label);

                new RemoteDataTask().execute();

            }
        });

    }

    private void createNews(){
        Intent i = new Intent(this,NewsDetailActivity.class);
        startActivityForResult(i,ACTIVITY_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null) {
            return;
        }
        switch (requestCode){
            case ACTIVITY_CREATE :
                // 自定义事件统计
                AVAnalytics.onEvent(getApplicationContext(), "创建新闻");
                break;
            case ACTIVITY_EDIT :
                // 自定义事件统计
                AVAnalytics.onEvent(getApplicationContext(), "更新新闻");
                break;
        }
        // 暂时提示信息
        boolean success = intent.getBooleanExtra("success", true);
        Toast toast = null;
        if (success) {
            toast = Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT);
            // 重新查询，刷新ListView
            new RemoteDataTask().execute();
        } else {
            toast = Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT);
        }
        toast.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, "创建新闻");
        menu.add(0, SEARCH_ID, 1, "搜索新闻");
        return result;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, "删除新闻");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                // Delete the remote object
                final News news = newses.get(info.position);

                new RemoteDataTask() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            news.delete();
                        } catch (AVException e) {
                        }
                        // 自定义事件统计
                        AVAnalytics.onEvent(getApplicationContext(), "删除新闻");
                        super.doInBackground();
                        return null;
                    }
                }.execute();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createNews();
                return true;
            case SEARCH_ID:
                searchInput = new EditText(this);
                new AlertDialog.Builder(this).setTitle("请输入").setIcon(android.R.drawable.ic_dialog_info)
                        .setView(searchInput).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputSearch = searchInput.getText().toString();
                        if (!AVUtils.isBlankString(inputSearch)) {
                            NewsService.searchQuery(inputSearch);
                        }
                    }
                }).setNegativeButton("取消", null).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // 打开编辑页面，传递title、content和objectId过去
        Intent i = new Intent(this, NewsDetailActivity.class);
        i.putExtra("title",newses.get((int)id).getString("title"));
        i.putExtra("content", newses.get((int)id).getString("content"));
        i.putExtra("objectId", newses.get((int)id).getObjectId());

        if (newses.get((int)id).getAVFile("image") != null) {
            i.putExtra("imageUrl", newses.get((int) id).getAVFile("image").getUrl());
        }
        startActivityForResult(i, ACTIVITY_EDIT);
    }
}
