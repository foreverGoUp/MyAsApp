package com.kc.base;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.kc.dialogfragment.ConfirmDialogFragment;
import com.kc.dialogfragment.EditNameDialogFragment;
import com.kc.dialogfragment.LoadingDialogFragment;
import com.kc.dialogfragment.TipDialogFragment;
import com.kc.tool.AppConstant;

import java.util.ArrayList;
import java.util.List;

public class DialogFactory {
    private final String TAG_LOADING_DIALOG = "LoadingDialogFragment";

    private static DialogFactory dialogFactory;

    private FragmentManager mFragmentManager;

    private List<BaseDialogFragment> mShowingDialogList = new ArrayList<>();

    //单例
    private DialogFactory() {
    }

    public static DialogFactory getInstance() {
        if (dialogFactory == null) {
            dialogFactory = new DialogFactory();
            return dialogFactory;
        } else {
            return dialogFactory;
        }
    }

    //每个活动对应一个片段管理器，在此得到该对象以显示对话框片段
    public void setFragmentManager(FragmentManager manager) {
        this.mFragmentManager = manager;
    }

    //显示编辑名称对话框
    public void showEditNameDialog(EditNameDialogFragment.EditNameDialogListener listener, String title, String oldName, boolean oldAsHint) {
        EditNameDialogFragment fragment = new EditNameDialogFragment();
        fragment.setListener(listener);
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_DIALOG_TITLE, title);
        bundle.putString(AppConstant.KEY_DIALOG_OLD_NAME, oldName);
        bundle.putBoolean(AppConstant.KEY_DIALOG_OLD_AS_HINT, oldAsHint);
        fragment.setArguments(bundle);

        String tag = fragment.getClass().getSimpleName();
        fragment.show(mFragmentManager.beginTransaction(), tag);
//		mFragmentManager.beginTransaction().add(containerId, fragment).commit();
    }

    //显示加载对话框
    public void showLoadingDialog(String desc, boolean cancelable) {
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_DIALOG_DESC, desc);
        bundle.putBoolean(AppConstant.KEY_DIALOG_CANCELABLE, cancelable);
        fragment.setArguments(bundle);

        String tag = fragment.getClass().getSimpleName();
        fragment.show(mFragmentManager.beginTransaction(), tag);
//		mFragmentManager.beginTransaction().add(containerId, fragment).commit();
    }

    //显示确认对话框
    public void showConfirmDialog(ConfirmDialogFragment.ConfirmDialogListener listener, int code, String title, String msg) {
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        fragment.setListener(listener);
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstant.KEY_DIALOG_CODE, code);
        bundle.putString(AppConstant.KEY_DIALOG_DESC, title);
        bundle.putString(AppConstant.KEY_DIALOG_TITLE, msg);
        fragment.setArguments(bundle);

        String tag = fragment.getClass().getSimpleName();
        fragment.show(mFragmentManager.beginTransaction(), tag);
//        mFragmentManager.beginTransaction().add(containerId, fragment).commit();
    }

    //显示提示对话框
    public void showTipDialog(TipDialogFragment.OnTipOkListener listener, int code, String title, String msg, boolean cancelable) {
        TipDialogFragment fragment = new TipDialogFragment();
        fragment.setListener(listener);
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstant.KEY_DIALOG_CODE, code);
        bundle.putString(AppConstant.KEY_DIALOG_DESC, title);
        bundle.putString(AppConstant.KEY_DIALOG_TITLE, msg);
        bundle.putBoolean(AppConstant.KEY_DIALOG_CANCELABLE, cancelable);
        fragment.setArguments(bundle);

        String tag = fragment.getClass().getSimpleName();

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        fragment.show(ft, tag);
//		mFragmentManager.beginTransaction().add(containerId, fragment).commit();
    }

    //该方法在baseDialogfragment中被调用，用来保存正在显示的对话框的实例。
    public void addShowingDialog(BaseDialogFragment fragment) {
        if (!mShowingDialogList.contains(fragment)) {
            mShowingDialogList.add(fragment);
        }
    }

    public void removeShowingDialog(BaseDialogFragment fragment) {
        if (mShowingDialogList.contains(fragment)) {
            mShowingDialogList.remove(fragment);
        }
    }

    //加载对话框特别需要使用到该方法，与其他对话框相比较特殊。当从服务器返回数据后才调用该方法隐藏加载对话框。
    public void removeShowingDialog(int code) {
        for (int i = 0; i < mShowingDialogList.size(); i++) {
            if (code == mShowingDialogList.get(i).getDialogFlag()) {
                mShowingDialogList.get(i).dismiss();
            }
        }
    }

    public void removeAll() {
        for (int i = 0; i < mShowingDialogList.size(); i++) {
            mShowingDialogList.get(i).dismiss();
        }
    }
}
