package com.kc.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kc.dialogfragment.ConfirmDialogFragment;
import com.kc.dialogfragment.EditNameDialogFragment;
import com.kc.dialogfragment.TipDialogFragment;
import com.kc.tool.AppConstant;
import com.kc.util.CommonUtils;


public abstract class BaseFragment extends Fragment implements TipDialogFragment.OnTipOkListener, ConfirmDialogFragment.ConfirmDialogListener {

    protected String TAG = getClass().getSimpleName();

    protected boolean mResumed = false;

    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
//        Log.e(TAG, "onCreate,hash=" + this.hashCode());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.e(TAG, "onCreateView,hash=" + this.hashCode());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLanguage();
        Log.e(TAG, "onResume,hash=" + this.hashCode());
        // 将该接口实现添加到PublicHandler中
//        PublicHandler.getInstant().setInterface(this);
    }

    protected abstract void initUI(View view);

    protected abstract void init();

    protected abstract void initData();

    public void updateLanguage() {
    }

    public void userVisible() {
    }

    private long mLastCmdSendTime = 0;

    protected boolean isOperationLimited() {
        long time = System.currentTimeMillis();
        if (time - mLastCmdSendTime < AppConstant.INTERVAL_OPERATION_LIMITED) {
            return true;
        } else {
            mLastCmdSendTime = time;
            return false;
        }
    }

    //供子类找到布局中viwe实例的方法
    protected <T extends View> T getViewById(View view, int resId) {
        return (T) view.findViewById(resId);
    }

    protected String getResString(int resId) {
        return getResources().getString(resId);
    }

    protected void setTextViewText(int tvId, int resId) {
        if (getView() != null) {
            ((TextView) getViewById(getView(), tvId)).setText(getResString(resId));
        }
    }

    protected void setButtonText(int btId, int resId) {
        ((Button) getViewById(getView(), btId)).setText(getResString(resId));
    }

    /*
      * 显示确认对话框，包含确认和取消按钮，默认可触摸取消
      *
      * @param code 某个界面调起时传入-1值，全局提醒调起时传入PublicHandle类的code值
      *
      * @param title 对话框标题
      *
      * @param msg 对话框具体信息
      */
    protected void showConfirmDialog(ConfirmDialogFragment.ConfirmDialogListener listener, int code, String title, String msg) {
        DialogFactory.getInstance().showConfirmDialog(listener, code, title, msg);
    }

    /*
     * 显示编辑名称对话框
     *
     * @param title 如：修改设备名称
     *
     * @param oldName 旧名称
     */
//    protected void showEditNameDialog(String title, String oldName) {
//        DialogFactory.getInstance().showEditNameDialog(title, oldName);
//    }
    protected void showEditNameDialog(EditNameDialogFragment.EditNameDialogListener listener, String title, String oldName, boolean oldAsHint) {
        DialogFactory.getInstance().showEditNameDialog(listener, title, oldName, oldAsHint);
    }

    @Override
    public void onPause() {
        // TODO 自动生成的方法存根
        super.onPause();
//        Log.e(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.e(TAG, "onStop");
        //清理接口
//        PublicHandler.getInstant().clearInterface(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO 自动生成的方法存根
        super.onSaveInstanceState(outState);
//	    Log.e(TAG, "onSaveInstanceState");
    }

    @Override
    public void onDestroy() {
        // TODO 自动生成的方法存根
        super.onDestroy();
//        Log.e(TAG, "onDestroy");
    }

    @Override
    public void onTipOk(int code) {
        // TODO 自动生成的方法存根
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
    public void onSelectDone(int code, boolean ok) {

    }
}
