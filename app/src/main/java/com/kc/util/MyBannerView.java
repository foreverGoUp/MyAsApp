package com.kc.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyBannerView extends View {

    private Paint mPaint;
    // 设定圆的半径
    public static final int CIRCLE_R = 10;
    // 设定每个圆间�?
    public static final int AVER_MARGIN = 20;
    // 设定�?要绘制的圆的个数
    public static int NUMBER = 3;
    private Paint mPaint2;
    private int position;
    private float offset;

    public MyBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 初始化画�?
        initPaint();
    }

    public MyBannerView(Context context) {
        super(context);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint2 = new Paint();
        mPaint2.setColor(Color.parseColor("#f74656"));
        mPaint.setAntiAlias(true);
        mPaint2.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * Sets the stroke color
     *
     * @param color ARGB value for the text
     */
    public void setStrokeColor(int color) {
        mPaint2.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int y = canvas.getHeight() / 2;
        int width = canvas.getWidth() / 2;
        // 第一个圆的圆心x位置
        int startX = (int) (width - (NUMBER - 1) * CIRCLE_R -
                ((NUMBER - 1) / 2.0) * AVER_MARGIN);
        for (int i = 0; i < NUMBER; i++) {
            canvas.drawCircle(startX + 2 * i * CIRCLE_R + i * AVER_MARGIN,
                    y, CIRCLE_R, mPaint);
        }
        float allOffSet = position + offset;
        canvas.drawCircle(startX
                        + 2 * allOffSet * CIRCLE_R + allOffSet * AVER_MARGIN,
                y, CIRCLE_R, mPaint2);
    }

    public void changeBanner(int position, float offset) {
        this.position = position;
        this.offset = offset;
        invalidate();
    }

    public void changeBanner(int position) {
        this.position = position;
        invalidate();
    }

}
