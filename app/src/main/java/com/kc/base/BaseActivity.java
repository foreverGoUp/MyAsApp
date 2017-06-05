package com.kc.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kc.dialogfragment.ConfirmDialogFragment;
import com.kc.dialogfragment.EditNameDialogFragment;
import com.kc.dialogfragment.TipDialogFragment;
import com.kc.interf.INotificationListener;
import com.kc.netty.NettyUtil;
import com.kc.netty.PublicHandler;
import com.kc.tool.AppConstant;
import com.kc.util.API;
import com.kc.util.ActivityCollector;
import com.kc.util.CommonUtils;

public abstract class BaseActivity extends FragmentActivity implements INotificationListener, TipDialogFragment.OnTipOkListener,
        ConfirmDialogFragment.ConfirmDialogListener {

    protected String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
//        if (this.getClass() == AirActivity.class) {
//            Log.e(TAG, "onCreate");
//        }

        // 收集活动
        ActivityCollector.getInstance().addActivity(this);
    }

    protected abstract void initUI();

    protected abstract void init();

    protected abstract void initData();

    public void updateLanguage() {
    }

    @Override
    protected void onResume() {
        // TODO 自动生成的方法存根
        super.onResume();
//        if (this.getClass() == AirActivity.class) {
//            Log.e(TAG, "onResume");
//        }
        DialogFactory.getInstance().setFragmentManager(getSupportFragmentManager());
        // 判断用户是否被迫下线，是则强制用户跳转到登陆界面
        if (AppConstant.hasUserForcedOffline) {
            notice(API.USER_REQUEST_LOGIN_ELSEWHERE);
            return;
        }
        PublicHandler.getInstant().setInterface(this);
        //更新界面文本为对应的语言
        updateLanguage();
    }

    //供子类找到布局中viwe实例的方法
    protected <T extends View> T getViewById(int viewId) {
        return (T) findViewById(viewId);
    }

    protected <T extends View> T getViewById(View view, int resId) {
        return (T) view.findViewById(resId);
    }

    protected String getResString(int resId) {
        return getResources().getString(resId);
    }

    protected void setTextViewText(int tvId, int resId) {
        ((TextView) getViewById(tvId)).setText(getResString(resId));
    }

    protected void setButtonText(int btId, int resId) {
        ((Button) getViewById(btId)).setText(getResString(resId));
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

    /*
     * 显示加载对话框
     * 
     * @param desc 如：正在加载中...
     * 
     * @param cancelable 对话框是否可触摸屏幕或返回键取消
     */
    protected void showLoadingDialog(String desc, boolean cancelable) {
        DialogFactory.getInstance().showLoadingDialog(desc, cancelable);
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
     * 显示提示对话框，只包含确认按钮
     * 
     * @param code 某个界面调起时传入-1值，全局提醒调起时传入PublicHandle类的code值
     * 
     * @param title 对话框标题
     * 
     * @param msg 对话框具体信息
     * 
     * @param cancelable 对话框是否可触摸屏幕或返回键取消
     */
    protected void showTipDialog(TipDialogFragment.OnTipOkListener listener, int code, String title, String msg, boolean cancelable) {
        DialogFactory.getInstance().showTipDialog(listener, code, title, msg, cancelable);
    }

    // 当用户点击屏幕左上角返回按钮时的通用方法
    public void onBackClick(View view) {
        this.finish();
    }

    /*
     * 这里要写的通知为app全局通知，专有通知请在子类中重写该方法
     *
     * @param code 传入PublicHandler类中的code值
     */
    @Override
    public void notice(int code) {
    }

    /*
     * 用户点击回退键调用的方法
     */
    @Override
    public void onBackPressed() {
        // TODO 自动生成的方法存根
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO 自动生成的方法存根
        super.onNewIntent(intent);
        // Log.e(TAG, "onNewIntent");
    }

    /*
     * 在此处清理每个退出前台页面的接口，如果在onStop或onDestory清理会有问题，
     * 因为切换活动时的周期顺序是：活动0：onPause-》活动1：onCreate-》活动1：onResume-》
     * 因此，注册接口的时机是onCreate、onResume均可，建议onResume
     */
    @Override
    protected void onPause() {
        // TODO 自动生成的方法存根
        super.onPause();
        // Log.e(TAG, "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO 自动生成的方法存根
        super.onSaveInstanceState(outState);
        // Log.e(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onStop() {
        // TODO 自动生成的方法存根
        super.onStop();
//        if (this.getClass() == AirActivity.class) {
//            Log.e(TAG, "onStop");
//        }
        // 清理接口
        PublicHandler.getInstant().clearInterface(this);
    }

    /*
     * 在此移除活动收集器中的已被摧毁的活动
     */
    @Override
    protected void onDestroy() {
        // TODO onDestroy
        super.onDestroy();
//        if (this.getClass() == AirActivity.class) {
//            Log.e(TAG, "onDestroy");
//        }
        ActivityCollector.getInstance().removeActivity(this);
    }

    /*
     * 提示对话框的回调方法 父类实现了提示对话框接口，表示其子类都是该接口的实现，所以子类无需再实现该接口
     * 子类中若需要得到对话框接口的回调，只要重写父类的以下方法即可。
     * 
     * @param code 子类中得到的code一般为之前传进去的-1，基类中得到的code为之前传入的PubicHandler中的值
     */
    @Override
    public void onTipOk(int code) {
        // TODO 自动生成的方法存根
    }

    // 确认对话框的回调方法
    @Override
    public void onSelectDone(int code, boolean ok) {
        // TODO onSelectDone

    }

    protected void showToast(String content) {
        CommonUtils.showToast(this, content);
//        if (content.length() < 10) {
//            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, content, Toast.LENGTH_LONG).show();
//        }
    }


    /**
     * 该方法在收到促使用户回到登录界面重登的条件下调用，防止异地登陆异常情况的发生。
     */
    protected void clearBeforeGoToLogin() {
        // 断开信道重连，服务器好处理
        AppConstant.hasUserForcedOffline = false;
        NettyUtil.getInstance().disConnected();
    }


    /**
     * 该方法用于从登陆后的界面跳回到登陆界面，子类可以直接调用。
     * 应当在确定可以立马跳转到登录页面时调用。
     */
    protected void goToReLogin() {
        ActivityCollector.getInstance().finishAll();
//        this.startActivity(new Intent(this, LoginActivity.class));
    }
}
