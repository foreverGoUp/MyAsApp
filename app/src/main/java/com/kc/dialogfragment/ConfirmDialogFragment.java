package com.kc.dialogfragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.kc.base.BaseDialogFragment;
import com.kc.myasapp.R;
import com.kc.tool.AppConstant;

public class ConfirmDialogFragment extends BaseDialogFragment {

    private TextView mTitleTv, mMsgTv;
    private Button mOkBt, mCancelBt;

    private ConfirmDialogListener mListener;

    private int mCode = -1;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * mWidthRatio), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_confirm_dialog, null);


        mTitleTv = (TextView) view.findViewById(R.id.tv_tip_title);
        mMsgTv = (TextView) view.findViewById(R.id.tv_tip_msg);
        mOkBt = (Button) view.findViewById(R.id.bt_ok);
        mCancelBt = (Button) view.findViewById(R.id.bt_cancel);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCode = bundle.getInt(AppConstant.KEY_DIALOG_CODE, -1);
            mTitleTv.setText(bundle.getString(AppConstant.KEY_DIALOG_DESC));
            mMsgTv.setText(bundle.getString(AppConstant.KEY_DIALOG_TITLE));
        }

        mOkBt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mListener.onSelectDone(mCode, true);
                dismiss();
            }
        });
        mCancelBt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mListener.onSelectDone(mCode, false);
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void setListener(BaseDialogListener listener) {
        mListener = (ConfirmDialogListener) listener;
    }

    public interface ConfirmDialogListener extends BaseDialogListener {
        //code用途：当用户点击确定后，页面通过code来判断这个对话框是否是自己调起的，因为也有可能是app全局对话框。
        void onSelectDone(int code, boolean ok);
    }

}
