package com.kc.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/5/19 0019.
 */
public class RequestTimeoutUtil {

    private static final String TAG = "RequestTimeoutUtil";
    private static RequestTimeoutUtil sSington = new RequestTimeoutUtil();

    private RequestTimeoutUtil() {
    }

    public static RequestTimeoutUtil getInstance() {
        return sSington;
    }

    private OnRequestTimeoutListener mListener;
    private final int LIMIT_TIME = 3000;//3s
    private boolean mIsFinish = true;
    private Timer mTimer;
    private TimerTask mTask;
    private int count = 0;

    public void requestStart(OnRequestTimeoutListener listener) {
        mListener = listener;
        mIsFinish = false;

        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTask != null && !mTask.cancel()) {
            Log.e(TAG, "取消上次任务，监听者" + mListener.getClass());
            mTask.cancel();
            mTimer.purge();
        } else {
            mTimer.purge();
        }
        Log.e(TAG, "开始执行请求超时监控，obj=" + mListener.getClass().getSimpleName() + "，time = " + System.currentTimeMillis());
        mTask = new TimerTask() {
            @Override
            public void run() {
                if (!mIsFinish) {
                    if (mListener != null) {
                        mListener.onRequestTimeout();
                    }
                }
                Log.e(TAG, "结束执行请求超时监控，obj = " + mListener.getClass().getSimpleName() + ",time = " + System.currentTimeMillis());
                cancel();
            }
        };
        mTimer.schedule(mTask, LIMIT_TIME, LIMIT_TIME);
    }

    public void requestFinish() {
        mIsFinish = true;
    }

    public interface OnRequestTimeoutListener {
        void onRequestTimeout();
    }

}
