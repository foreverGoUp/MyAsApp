package com.kc.custom.timeAxis;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/9/2.
 */

public class CsmTimeAxisLayout extends RelativeLayout implements CsmTimeAxisView.OnCsmTimeAxisListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "CsmTimeAxisLayout";
    private Runnable mLayoutAfterTask;


    public CsmTimeAxisLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(mAdapter.getTimeAxisView());
        addView(mAdapter.getIndicatorView());
        addView(mAdapter.getShowTimeTextView());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            refresh();
        }
        if (mLayoutAfterTask != null) {
            mLayoutAfterTask.run();
            mLayoutAfterTask = null;
        }
    }

    private void refresh() {
        if (getWidth() != 0) {
            removeAllViews();
            addView(mAdapter.getTimeAxisView());
            addView(mAdapter.getShowTimeTextView());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addView(mAdapter.getIndicatorView());
                }
            }, 1000);
        } else {
            mLayoutAfterTask = new Runnable() {
                @Override
                public void run() {
                    removeAllViews();
                    addView(mAdapter.getTimeAxisView());
                    addView(mAdapter.getShowTimeTextView());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addView(mAdapter.getIndicatorView());
                        }
                    }, 1000);
                }
            };
        }
    }

    private Adapter mAdapter = new Adapter() {

        private CsmTimeAxisView mCsmTimeAxisView;
        private View mIndicatorView;
        private TextView mTvShowTime;

        @Override
        public CsmTimeAxisView getTimeAxisView() {
            if (mCsmTimeAxisView == null) {
                mCsmTimeAxisView = new CsmTimeAxisView(getContext());
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 300);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                mCsmTimeAxisView.setLayoutParams(layoutParams);
                mCsmTimeAxisView.setListener(CsmTimeAxisLayout.this);
                mCsmTimeAxisView.getViewTreeObserver().addOnGlobalLayoutListener(CsmTimeAxisLayout.this);
            }
            return mCsmTimeAxisView;
        }

        @Override
        public View getIndicatorView() {
            mIndicatorView = new View(getContext());
            LayoutParams layoutParams = new LayoutParams(5, (int) mAdapter.getTimeAxisView().getMarkLineAreaHeight());
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            Log.e(TAG, "getIndicatorView: MarkLineAreaHeight=" + mAdapter.getTimeAxisView().getMarkLineAreaHeight());
            layoutParams.bottomMargin = (int) (getPaddingBottom() + mAdapter.getTimeAxisView().getOffsetBottomHeight());
            mIndicatorView.setLayoutParams(layoutParams);
            mIndicatorView.setBackgroundColor(Color.RED);
            return mIndicatorView;
        }

        @Override
        public TextView getShowTimeTextView() {
            if (mTvShowTime == null) {
                mTvShowTime = new TextView(getContext());
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                layoutParams.addRule(RelativeLayout.ABOVE, mAdapter.getTimeAxisView().getId());
                mTvShowTime.setLayoutParams(layoutParams);
            }
            return mTvShowTime;
        }
    };

    @Override
    public void onTimeAxisMove(long timeMilli) {
        TextView textView = mAdapter.getShowTimeTextView();
        if (textView != null) {
            textView.setText(TimeUtils.millis2String(timeMilli, new SimpleDateFormat("HH:mm:ss")));
        }
    }

    @Override
    public void onTimeAxisStop(long timeMilli) {
        TextView textView = mAdapter.getShowTimeTextView();
        if (textView != null) {
            textView.setText(TimeUtils.millis2String(timeMilli, new SimpleDateFormat("HH:mm:ss")));
        }
    }

    @Override
    public void onGlobalLayout() {
        Log.e(TAG, "onGlobalLayout: !!!");
        mAdapter.getTimeAxisView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
//        refresh();
    }

    private static interface Adapter {
        CsmTimeAxisView getTimeAxisView();

        View getIndicatorView();

        TextView getShowTimeTextView();
    }
}
