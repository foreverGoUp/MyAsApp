package com.kc.util;

import android.app.Activity;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class PoponDismissListener implements PopupWindow.OnDismissListener {
    private float bgAlpha;
    private Activity context;

    public PoponDismissListener(Activity context) {
        this.context = context;
    }

    public PoponDismissListener(Activity context, float bgAlpha) {
        this.context = context;
        this.bgAlpha = bgAlpha;
    }

    @Override
    public void onDismiss() {
        //Log.v("List_noteTypeActivity:", "我是关闭事件");
        //		        	deveiceAdapter.notifyDataSetChanged();
        backgroundAlpha(bgAlpha);
    }

    //		  * 设置添加屏幕的背景透明度
    //		     * @param bgAlpha
    //		     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

}
