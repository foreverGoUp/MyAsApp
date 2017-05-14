package com.kc.custom.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.kc.custom.BaseRefreshLayout;

/**
 * Created by Administrator on 2017/2/5.
 */
public class MyRefreshListView extends BaseRefreshLayout<ListView> {

    public MyRefreshListView(Context context) {
        super(context);
    }

    public MyRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setupContentView(Context context) {
        mContentView = new ListView(context);
    }

    @Override
    protected boolean isBottom() {
        if (getScrollY() >= mInitScrollY
                && mContentView.getLastVisiblePosition() == getAdapter().getCount()-1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected boolean isTop() {
        if (getScrollY() <= mInitScrollY
                && mContentView.getFirstVisiblePosition() == 0){
            return true;
        }else {
            return false;
        }
    }

    public void setAdapter(ListAdapter adapter){
        mContentView.setAdapter(adapter);
    }

    public ListAdapter getAdapter(){
        return mContentView.getAdapter();
    }


}
