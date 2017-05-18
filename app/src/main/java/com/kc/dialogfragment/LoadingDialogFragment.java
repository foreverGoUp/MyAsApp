package com.kc.dialogfragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.kc.base.BaseDialogFragment;
import com.kc.myasapp.R;
import com.kc.tool.AppConstant;

public class LoadingDialogFragment extends BaseDialogFragment {

    private ImageView mLoadingIv;
    private TextView mDescTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO 自动生成的方法存根
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setListener(BaseDialogListener listener) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_loading_dialog, null);

        mLoadingIv = (ImageView) view.findViewById(R.id.iv_loading_img);
        mDescTv = (TextView) view.findViewById(R.id.tv_loading_desc);

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_dialog_loading);
        animation.setInterpolator(new LinearInterpolator());
        mLoadingIv.startAnimation(animation);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mDescTv.setText(bundle.getString(AppConstant.KEY_DIALOG_DESC));
            this.setCancelable(bundle.getBoolean(AppConstant.KEY_DIALOG_CANCELABLE));
        }

//		mListener = (EditNameDialogListener) getListenerInstance();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//	    Dialog dialog = getDialog();
//	    if (dialog != null) {
//	        DisplayMetrics dm = new DisplayMetrics();
//	        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//	        dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
//	    }
        Window window = getDialog().getWindow();
        //将对话框内部颜色设置成透明
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //将对话框外部颜色设置成透明
//		lp.dimAmount = 0.0f;
        window.setAttributes(lp);
    }


    @Override
    public void onStop() {
        // TODO 自动生成的方法存根
        super.onStop();
        mLoadingIv.clearAnimation();
    }
}
