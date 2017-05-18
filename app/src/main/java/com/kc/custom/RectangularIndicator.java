package com.kc.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.kc.util.SizeUtils;

/**
 * Created by Administrator on 2017/3/2 0002.
 */

public class RectangularIndicator extends View {
    private static final String TAG = "RectangularIndicator";
    private Paint mPaint;
    private Paint mPaint4CurItem;
    private int mCurItemPos;
    // 设定�?要绘制的长方形的个数
    private int mItemNum = 3;
    private int mBlankWidth = SizeUtils.dp2px(getContext(), 10);
    private int mItemWidth = SizeUtils.dp2px(getContext(), 30);
    private int mItemHeight = SizeUtils.dp2px(getContext(), 5);

    public RectangularIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public RectangularIndicator(Context context) {
        super(context);
    }

    public void setItemNumber(int number) {
        mItemNum = number;
        if (number > 5) {
            mBlankWidth = SizeUtils.dp2px(getContext(), 5);
            mItemWidth = SizeUtils.dp2px(getContext(), 20);
            mItemHeight = SizeUtils.dp2px(getContext(), 3);
        }
        destroyDrawingCache();
        requestLayout();
    }

    public void setCurItem(int curItemPos) {
        mCurItemPos = curItemPos;
        invalidate();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint4CurItem = new Paint();
        mPaint4CurItem.setColor(Color.parseColor("#ff6d77"));
        mPaint4CurItem.setAntiAlias(true);
        mPaint4CurItem.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = mItemNum * mItemWidth + (mItemNum - 1) * mBlankWidth;
        setMeasuredDimension(width, mItemHeight);
//        Log.e(TAG, "onMeasure, width=" + width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.e(TAG, "onDraw, mItemNum=" + mItemNum);

        int startX = 0;
        for (int i = 0; i < mItemNum; i++) {
            startX = i * (mBlankWidth + mItemWidth);
            if (i == mCurItemPos) {
                canvas.drawRect(startX, 0, startX + mItemWidth, mItemHeight, mPaint4CurItem);
            } else {
                canvas.drawRect(startX, 0, startX + mItemWidth, mItemHeight, mPaint);
            }
        }
    }
}
