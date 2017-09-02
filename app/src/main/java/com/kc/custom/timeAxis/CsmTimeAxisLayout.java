package com.kc.custom.timeAxis;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/2.
 */

public class CsmTimeAxisLayout extends RelativeLayout {


    public CsmTimeAxisLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {

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
            }
            return mCsmTimeAxisView;
        }

        @Override
        public View getIndicatorView() {
            if (mIndicatorView == null) {
                mIndicatorView = new View(getContext());
                LayoutParams layoutParams = new LayoutParams(5, (int) mAdapter.getTimeAxisView().getMarkLineAreaHeight());
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.bottomMargin = (int) (getPaddingBottom() + mAdapter.getTimeAxisView().getOffsetBottomHeight());
                mIndicatorView.setLayoutParams(layoutParams);
            }
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

    private static interface Adapter {
        CsmTimeAxisView getTimeAxisView();

        View getIndicatorView();

        TextView getShowTimeTextView();
    }
}
