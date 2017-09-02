package com.kc.custom.timeAxis;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.kc.myasapp.R;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/9/2.
 */
//TODO 总结本次自定义经验
public class CsmTimeAxisLayout extends RelativeLayout implements CsmTimeAxisView.OnCsmTimeAxisListener {

    private static final String TAG = "CsmTimeAxisLayout";
    private Runnable mLayoutAfterTask;
    //
    private Drawable mTimeTextViewBackground = getResources().getDrawable(android.R.drawable.toast_frame);
    private int mTimeTextViewTextColor = Color.WHITE;
    private int mTimeAxisViewPadding = 10;
    private int mTimeAxisViewHeight = 300;
    private int mIndicatorViewWidth = 5;


    public CsmTimeAxisLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        addView(mAdapter.getShowTimeTextView());
    }

    private void init(Context context, AttributeSet attrs) {
        Log.e(TAG, "init>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CsmTimeAxisLayout);
        mTimeTextViewBackground = typedArray.getDrawable(R.styleable.CsmTimeAxisLayout_timeTextViewBackground);
        mTimeTextViewTextColor = typedArray.getColor(R.styleable.CsmTimeAxisLayout_timeTextViewTextColor, mTimeTextViewTextColor);
        mTimeAxisViewPadding = typedArray.getDimensionPixelSize(R.styleable.CsmTimeAxisLayout_timeAxisViewPadding, mTimeAxisViewPadding);
//        mTimeAxisViewHeight = typedArray.getDimensionPixelSize(R.styleable.CsmTimeAxisLayout_timeAxisViewHeight, mTimeAxisViewHeight);
        mIndicatorViewWidth = typedArray.getDimensionPixelSize(R.styleable.CsmTimeAxisLayout_indicatorViewWidth, mIndicatorViewWidth);
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onLayout: ");
        if (changed) {
            Log.e(TAG, "onLayout: changed getHeight=" + getHeight() + ",getWidth=" + getWidth());
            Log.e(TAG, "onLayout: mAdapter.getShowTimeTextView().getHeight=" + mAdapter.getShowTimeTextView().getMeasuredHeight());
            mTimeAxisViewHeight = getHeight() - getPaddingBottom() - getPaddingTop() - mAdapter.getShowTimeTextView().getMeasuredHeight();
            refresh();
        }
//        if (mLayoutAfterTask != null) {
//            mLayoutAfterTask.run();
//            mLayoutAfterTask = null;
//        }
    }

    private void refresh() {
        Log.e(TAG, "refresh: ");
        removeAllViews();
        addView(mAdapter.getShowTimeTextView());
        addView(mAdapter.getTimeAxisView());
        if (mAdapter.getTimeAxisView().getMarkLineAreaHeight() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addView(mAdapter.getIndicatorView());
                }
            }, 100);
        } else {
            addView(mAdapter.getIndicatorView());
        }
//        if (getWidth() != 0) {
//        } else {
//            mLayoutAfterTask = new Runnable() {
//                @Override
//                public void run() {
//                    removeAllViews();
//                    addView(mAdapter.getTimeAxisView());
//                    addView(mAdapter.getShowTimeTextView());
//                    addView(mAdapter.getIndicatorView());
//                }
//            };
//        }
    }

    private Adapter mAdapter = new Adapter() {
        private final int mCsmTimeAxisViewId = View.generateViewId();
        private final int mShowTimeTextViewId = View.generateViewId();

        private CsmTimeAxisView mCsmTimeAxisView;
        private View mIndicatorView;
        private TextView mTvShowTime;

        @Override
        public int getTimeAxisViewId() {
            return mCsmTimeAxisViewId;
        }

        @Override
        public int getShowTimeTextViewId() {
            return mShowTimeTextViewId;
        }

        @Override
        public CsmTimeAxisView getTimeAxisView() {
            Log.e(TAG, "getTimeAxisView,,,,,,");
            //
//            LayoutParams layoutParams1 = (LayoutParams) mAdapter.getShowTimeTextView().getLayoutParams();
//            layoutParams1.addRule(RelativeLayout.ABOVE, mCsmTimeAxisView.getId());
//            mAdapter.getShowTimeTextView().setLayoutParams(layoutParams1);
            if (mCsmTimeAxisView == null) {
                mCsmTimeAxisView = new CsmTimeAxisView(getContext());
                mCsmTimeAxisView.setId(getTimeAxisViewId());
                Log.e(TAG, "getTimeAxisView: mTimeAxisViewHeight=" + mTimeAxisViewHeight);
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mTimeAxisViewHeight);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//                layoutParams.addRule(RelativeLayout.BELOW, getShowTimeTextViewId());
                mCsmTimeAxisView.setLayoutParams(layoutParams);
                //setPadding
                mCsmTimeAxisView.setPadding(0, mTimeAxisViewPadding, 0, mTimeAxisViewPadding);
                mCsmTimeAxisView.setListener(CsmTimeAxisLayout.this);
            }
            return mCsmTimeAxisView;
        }

        @Override
        public View getIndicatorView() {
            Log.e(TAG, "getIndicatorView,,,,,,");
            if (mIndicatorView == null) {
            }
            mIndicatorView = new View(getContext());
            LayoutParams layoutParams = new LayoutParams(mIndicatorViewWidth, (int) mAdapter.getTimeAxisView().getMarkLineAreaHeight());
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
            Log.e(TAG, "getShowTimeTextView,,,,,,");
            if (mTvShowTime == null) {
                mTvShowTime = new TextView(getContext());
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                layoutParams.addRule(RelativeLayout.ABOVE, mAdapter.getTimeAxisViewId());
                mTvShowTime.setLayoutParams(layoutParams);
                mTvShowTime.setGravity(Gravity.CENTER);
                mTvShowTime.setPadding(5, 0, 5, 0);
                mTvShowTime.setBackground(mTimeTextViewBackground);
                mTvShowTime.setTextColor(mTimeTextViewTextColor);
                mTvShowTime.setText("00:00:00");
                Log.e(TAG, "getShowTimeTextView: init height=" + mTvShowTime.getMeasuredHeight());
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
        //
        if (mListener != null) {
            mListener.onTimeAxisMove(timeMilli);
        }
    }

    @Override
    public void onTimeAxisStop(long timeMilli) {
        TextView textView = mAdapter.getShowTimeTextView();
        if (textView != null) {
            textView.setText(TimeUtils.millis2String(timeMilli, new SimpleDateFormat("HH:mm:ss")));
        }
        //
        if (mListener != null) {
            mListener.onTimeAxisStop(timeMilli);
        }
    }

    private interface Adapter {
        int getTimeAxisViewId();

        int getShowTimeTextViewId();

        CsmTimeAxisView getTimeAxisView();

        View getIndicatorView();

        TextView getShowTimeTextView();
    }

    private CsmTimeAxisView.OnCsmTimeAxisListener mListener;

    public void setListener(CsmTimeAxisView.OnCsmTimeAxisListener listener) {
        mListener = listener;
    }

    public CsmTimeAxisView getTimeAxisView() {
        return mAdapter.getTimeAxisView();
    }
}
