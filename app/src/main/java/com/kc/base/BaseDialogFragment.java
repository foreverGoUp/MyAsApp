package com.kc.base;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.kc.util.CommonUtils;

public abstract class BaseDialogFragment extends DialogFragment {

    public String TAG = this.getClass().getSimpleName();
    protected final double mWidthRatio = 0.75;
    protected int mCode = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        DialogFactory.getInstance().addShowingDialog(this);
    }

    @Override
    public void onResume() {
        // TODO onResume
        Log.e(TAG, "onResume");
        super.onResume();
//		PublicHandler.getInstant().setInterface(this);
//		CommonDeviceStatusPresenter.getInstance().setInterface(this);
    }

    public int getDialogFlag() {
        return mCode;
    }

    public abstract void setListener(BaseDialogListener listener);

    //该接口可以用来统一各个对话框中接口的差异性，用来做引用变量很方便
    public interface BaseDialogListener {
    }

    //供子类找到布局中viwe实例的方法
    protected <T extends View> T getViewById(View view, int resId) {
        return (T) view.findViewById(resId);
    }

    protected String getResString(int resId) {
        return getResources().getString(resId);
    }

    protected void showToast(String content) {
        CommonUtils.showToast(getActivity(), content);

//        if (content.length() < 10) {
//            Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getActivity(), content, Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void onPause() {
        // TODO 自动生成的方法存根
        Log.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        // TODO onStop
        Log.e(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle arg0) {
        // TODO 自动生成的方法存根
        Log.e(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(arg0);
    }

    @Override
    public void onDestroy() {
        // TODO 自动生成的方法存根
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        DialogFactory.getInstance().removeShowingDialog(this);
    }

}
