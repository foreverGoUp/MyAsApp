package com.kc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kc.base.BaseActivity;
import com.kc.myasapp.R;

import java.util.ArrayList;
import java.util.List;

public class BRVAHActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private List<String> mList = new ArrayList<>();
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brvah);
        initUI();
        init();
    }

    @Override
    protected void initUI() {
        mRecyclerView = getViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void init() {
        for (int i = 0; i < 20; i++) {
            mList.add("item " + i);
        }
        mAdapter = new MainAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

    }

    class MainAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public MainAdapter(@Nullable List<String> data) {
            super(android.R.layout.simple_list_item_1, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(android.R.id.text1, item);
        }
    }
}
