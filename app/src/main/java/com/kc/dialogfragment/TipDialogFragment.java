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

public class TipDialogFragment extends BaseDialogFragment {

    private TextView mTitleTv, mMsgTv;
    private Button mOkBt, mCancelBt;

    private OnTipOkListener mListener = null;


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
        View view = inflater.inflate(R.layout.fragment_tip_dialog, null);


        mTitleTv = (TextView) view.findViewById(R.id.tv_tip_title);
        mMsgTv = (TextView) view.findViewById(R.id.tv_tip_msg);
        mOkBt = (Button) view.findViewById(R.id.bt_ok);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitleTv.setText(bundle.getString(AppConstant.KEY_DIALOG_DESC));
            mMsgTv.setText(bundle.getString(AppConstant.KEY_DIALOG_TITLE));
            mCode = bundle.getInt(AppConstant.KEY_DIALOG_CODE);
            boolean aaa = bundle.getBoolean(AppConstant.KEY_DIALOG_CANCELABLE);
            this.setCancelable(bundle.getBoolean(AppConstant.KEY_DIALOG_CANCELABLE));
        }

        mOkBt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();
                if (mListener != null) {
                    mListener.onTipOk(mCode);
                }
            }

        });
        return view;
    }

    @Override
    public void setListener(BaseDialogListener listener) {
        mListener = (OnTipOkListener) listener;
    }

    public interface OnTipOkListener extends BaseDialogListener {
        void onTipOk(int code);
    }

}
