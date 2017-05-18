package com.kc.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class MaxTextLengthFilter implements InputFilter {

    private Toast toast;
    private int mMaxLength;

    public MaxTextLengthFilter(int max) {
        mMaxLength = max - 1;
        JYApplication context = JYApplication.getContext();
        toast = Toast.makeText(context, "字符不能超过32个", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 235);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = mMaxLength - (dest.length() - (dend - dstart));
        if (keep < (end - start)) {
            toast.show();
        }
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null;
        } else {
            return source.subSequence(start, start + keep);
        }
    }
}
