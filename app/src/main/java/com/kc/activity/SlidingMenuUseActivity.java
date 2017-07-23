package com.kc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.daShen.SlidingMenu.BinarySlidingMenu;
import com.kc.base.BaseActivity;
import com.kc.myasapp.R;

import java.util.ArrayList;
import java.util.List;

public class SlidingMenuUseActivity extends BaseActivity implements BinarySlidingMenu.OnMenuOpenListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private BinarySlidingMenu mSlidingMenu;
    private ListView mLvLeftMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_menu_use);
        initUI();
        init();
    }

    @Override
    protected void initUI() {
        mSlidingMenu = (BinarySlidingMenu) findViewById(R.id.id_menu);
        mSlidingMenu.setOnMenuOpenListener(this);
        mLvLeftMenu = getViewById(R.id.lv_sliding_menu_left_menu);
    }

    @Override
    protected void init() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            list.add("" + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, list);
        mLvLeftMenu.setAdapter(adapter);
//        ListView lvContent = getViewById(R.id.list);
//        lvContent.setAdapter(adapter);
//        lvContent.setOnItemClickListener(this);
        mLvLeftMenu.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMenuOpen(boolean isOpen, int flag) {
        if (isOpen) {
            showToast((flag == 0 ? "left menu" : "right menu") + " open");
        } else {
            showToast((flag == 0 ? "left menu" : "right menu") + " close");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        showToast("点击了位置" + i);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        showToast("long点击了位置" + i);
        return false;
    }
}
