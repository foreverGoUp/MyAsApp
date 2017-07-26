package com.kc.custom;

import android.view.MotionEvent;

/**
 * Created by ckc on 2017/7/26.
 */

public class CustomViewTool {

    public static String getAction(int action) {
        String ret = null;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                ret = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                ret = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                ret = "ACTION_UP";
                break;
        }
        return ret;
    }
}
