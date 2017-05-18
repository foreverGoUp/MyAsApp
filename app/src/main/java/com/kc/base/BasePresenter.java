package com.kc.base;

import com.kc.tool.AppConstant;

/**
 * 子类的生命周期必须在对应的具体的活动中调用。随具体活动。
 */

public class BasePresenter {

    protected String TAG = this.getClass().getSimpleName();

    protected boolean mCreated = false;


    public BasePresenter() {
    }

    public void resume() {
        //注册监听
//        PublicHandler.getInstant().setInterface(this);
    }

    /**
     * 相当于活动的onStop方法，因此需要在活动的onsStop中调用
     */
    public void stop() {
        //解除数据监听器
//        PublicHandler.getInstant().clearInterface(this);
    }

    /**
     * 相当于活动的onDestroy方法，因此需要在具体活动的onsDestroy中调用
     * 该方法必须由具体的presenter对应的具体活动在onDestroy中调用，不能在活动的基类中调用.
     */
    public void destroy() {
    }

    private long mLastCmdSendTime = 0;

    public boolean isOperationLimited() {
        long time = System.currentTimeMillis();
        if (time - mLastCmdSendTime < AppConstant.INTERVAL_OPERATION_LIMITED) {
            return true;
        } else {
            mLastCmdSendTime = time;
            return false;
        }
    }
}
