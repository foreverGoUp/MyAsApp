package com.kc.custom;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by ckc on 2017/1/25.
 *
 * 测试Scroller
 */
public class CstTextView extends TextView {

    private Scroller mScroller;

    private float mLastY;

    public CstTextView(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                handleY((int) (mLastY - event.getRawY()));
                mLastY = event.getRawY();
                break;
        }
        return true;
    }

    public void handleY(int distanceY){
        mScroller.startScroll(getScrollX(),getScrollY(), 0, distanceY);
//        scrollBy(0,distanceY);
        this.invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            this.postInvalidate();
        }
    }
}
