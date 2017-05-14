package com.kc.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.kc.base.MyApp;
import com.kc.custom.BaseRefreshLayout;
import com.kc.custom.impl.MyRefreshListView;
import com.kc.custom.impl.RefreshListView;
import com.kc.listener.OnLoadListener;
import com.kc.listener.OnRefreshListener;
import com.kc.myasapp.R;

public class PullToRefreshTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MyRefreshListView listView = new MyRefreshListView(this);
        setContentView(listView);
        String[] dataArr = new String[20];
        for (int i = 0; i < dataArr.length; i++) {
            dataArr[i] = "item - " + i;
        }

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataArr));
        listView.setRefreshListener(new BaseRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyApp.showToast("正在刷新...");

                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.refreshComplete();
                        MyApp.showToast("刷新完成！");
                    }
                }, 3000);
            }
        });

        listView.setLoadingListener(new BaseRefreshLayout.OnLoadingListener() {
            @Override
            public void onLoading() {
                MyApp.showToast("正在加载...");

                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.loadComplete();
                        MyApp.showToast("加载完成！");
                    }
                }, 3000);
            }
        });
    }
}
