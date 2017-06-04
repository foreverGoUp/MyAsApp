package com.kc.util;

import android.util.Log;

import com.kc.base.BaseActivity;

import java.util.Stack;


public class ActivityCollector {

    private Stack<BaseActivity> mActivityStack = new Stack<BaseActivity>();
    private static ActivityCollector INSTANCE;

    private ActivityCollector() {
    }

    public static ActivityCollector getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ActivityCollector();
        }
        return INSTANCE;
    }

    //写在activity的onDestory中
    public void removeActivity(BaseActivity activity) {
        if (activity != null) {
            if (mActivityStack.contains(activity)) {
                mActivityStack.remove(activity);
            }
        }
    }

    //写在activity的onCreate中
    public void addActivity(BaseActivity activity) {
        if (activity != null) {
            mActivityStack.add(activity);
        }
    }

    public void finishActivity(BaseActivity activity) {
        if (activity != null) {
            activity.finish();
            mActivityStack.remove(activity);
        }
    }

    public BaseActivity currentActivity() {
        BaseActivity activity = null;
        if (!mActivityStack.empty()) {
            activity = mActivityStack.lastElement();
        }

        return activity;
    }

    public void finishAll() {
        while (!mActivityStack.empty()) {
            BaseActivity activity = currentActivity();
            finishActivity(activity);
        }
    }

    public void finishAllExcept(Class class1) {
        while (!mActivityStack.empty()) {
            BaseActivity activity = mActivityStack.firstElement();
            if (activity.getClass() == class1) {
                Log.e("ActivityCollector", "finishAllExcept done:size=" + mActivityStack.size());
                break;
            }
            finishActivity(activity);
        }
    }

}
