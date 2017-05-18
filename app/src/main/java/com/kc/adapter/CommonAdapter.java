package com.kc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kc.util.ViewHolder;

import java.util.List;

//@author zhy
/*
 * 
 * 说明：该通用适配器适合显示各种布局的列表条目，而不适合对项目里的事件进行监听回调的使用。
 * 
 * */

public abstract class CommonAdapter<T> extends BaseAdapter {

    protected LayoutInflater mInflater;

    protected Context mContext;

    protected List<T> mDataList;

    protected final int mLayoutId;

    protected int showItemNum = 0;

    public CommonAdapter(Context context, List<T> list, int layoutId) {
        mInflater = LayoutInflater.from(context);
        this.mDataList = list;
        this.mContext = context;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        // TODO 自动生成的方法存根
        if (showItemNum != 0) {
            if (mDataList.size() < showItemNum) {
                return mDataList.size();
            } else if (mDataList.size() >= showItemNum) {
                return showItemNum;
            }

        }
        return mDataList.size();
    }

    @Override
    public T getItem(int position) {
        // TODO 自动生成的方法存根
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO 自动生成的方法存根
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO 自动生成的方法存根
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder helper, T item, int position);


    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mLayoutId, position);
    }

    public void setShowItemNum(int num) {
        this.showItemNum = num;
    }

}
