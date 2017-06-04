package com.kc.dialogfragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kc.base.BaseDialogFragment;
import com.kc.myasapp.R;
import com.kc.tool.AppConstant;

public class EditNameDialogFragment extends BaseDialogFragment {

    private TextView mTitleTv;
    private EditText mEditNameEt;
    private Button mBtOk, mBtCancel;

    private EditNameDialogListener mListener;
    private String mOldName;


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
        View view = inflater.inflate(R.layout.fragment_edit_name_dialog, null);


        mTitleTv = (TextView) view.findViewById(R.id.tv_edit_name_title);
        mEditNameEt = (EditText) view.findViewById(R.id.et_edit_name);
//        mEditNameEt.setFocusable(false);
//        mEditNameEt.setFocusableInTouchMode(true);
        mBtOk = (Button) view.findViewById(R.id.bt_ok);
        mBtCancel = (Button) view.findViewById(R.id.bt_cancel);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mCode = bundle.getInt(AppConstant.KEY_DIALOG_CODE);
            mTitleTv.setText(bundle.getString(AppConstant.KEY_DIALOG_TITLE));
            mOldName = bundle.getString(AppConstant.KEY_DIALOG_OLD_NAME);
            boolean oldAsHint = bundle.getBoolean(AppConstant.KEY_DIALOG_OLD_AS_HINT);
            if (mOldName != null) {
                if (oldAsHint) {
                    mEditNameEt.setHint(mOldName);
                } else {
                    mEditNameEt.setText(mOldName);
                    mEditNameEt.setSelection(mOldName.length());
                }
            }
        }
        mBtOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String roomName = mEditNameEt.getText().toString().trim();
                if (mOldName != null && roomName.equals(mOldName)) {
//                    showToast("名字修改无效");
                    dismiss();
                    return;
                }
                if (mListener.onEditNameDone(mCode, roomName)) {
                    dismiss();
                }
            }
        });
        mBtCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    public interface EditNameDialogListener extends BaseDialogListener {
        boolean onEditNameDone(int code, String newName);
    }

    @Override
    public void setListener(BaseDialogListener listener) {
        mListener = (EditNameDialogListener) listener;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mListener = null;
        Log.e("EditNameDialogFragment", "onDestroy");
    }
}
