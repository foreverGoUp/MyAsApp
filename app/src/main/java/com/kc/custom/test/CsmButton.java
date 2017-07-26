package com.kc.custom.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import static com.kc.custom.CustomViewTool.getAction;

/**
 * Created by ckc on 2017/7/25.
 */

public class CsmButton extends android.support.v7.widget.AppCompatButton implements GestureDetector.OnGestureListener {

    private static final String TAG = "CsmButton";

    private GestureDetector mGestureDetector = new GestureDetector(getContext(), this);

    public CsmButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: action=" + getAction(event.getAction()));
//        boolean handled = super.onTouchEvent(event);
        boolean handled = mGestureDetector.onTouchEvent(event);
        Log.e(TAG, "onTouchEvent: handled=" + handled);
        return handled;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.e(TAG, "onDown: " + getAction(e.getAction()));
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.e(TAG, "onShowPress: ");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.e(TAG, "onSingleTapUp: 点击了按钮," + getAction(e.getAction()));
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
