package com.kc.custom.timeAxis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.kc.util.ScreenUtils;
import com.kc.util.SizeUtils;

/**
 * Created by ckc on 2017/9/1.
 */

public class CsmTimeAxis extends View {

    private final float SECOND_WIDTH = 0.04f;//px
    private final float DEFAULT_HEIGHT = 400;//px
    private final float SCREEN_WIDTH = ScreenUtils.getScreenWidth(getContext());//px
    private final int INTERVAL_MINUTE = 15;
    private final float INTERVAL_WIDTH = INTERVAL_MINUTE * 60 * SECOND_WIDTH;
    //底部时间属性
    private int mNormalMarkTimeMarginTop = 10;//px
    private int mNormalMarkTimeTextSize = SizeUtils.sp2px(getContext(), 10);

    private Paint mNormalMarkPaint;

    public CsmTimeAxis(Context context) {
        super(context);
        init(context, null);
    }

    public CsmTimeAxis(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mNormalMarkPaint = new Paint();
        mNormalMarkPaint.setAntiAlias(true);
        mNormalMarkPaint.setStyle(Paint.Style.STROKE);
        mNormalMarkPaint.setColor(Color.parseColor("#000000"));
        mNormalMarkPaint.setStrokeWidth(3);
        mNormalMarkPaint.setTextSize(mNormalMarkTimeTextSize);
        mNormalMarkPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            height = (int) DEFAULT_HEIGHT;
        }
        int width = (int) (ScreenUtils.getScreenWidth(getContext()) + 24 * 3600 * SECOND_WIDTH);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTopBottomLine(canvas);
        drawNormalMarks(canvas);
    }

    private void drawNormalMarks(Canvas canvas) {
        int size = 24 * 60 / INTERVAL_MINUTE;//
        final int reference = 60 / INTERVAL_MINUTE;
        final int markLineAreaHeight = getHeight() - mNormalMarkTimeMarginTop - mNormalMarkTimeTextSize;
        final int timeMarkY = mNormalMarkTimeMarginTop + mNormalMarkTimeTextSize;
        final int hourMarkHeight = markLineAreaHeight * 3 / 4;
        final int intervalMarkHeight = markLineAreaHeight * 1 / 4;
        canvas.save();
        canvas.translate(SCREEN_WIDTH / 2, markLineAreaHeight);
        //画00:00刻度线
        canvas.drawLine(0, 0, 0, -markLineAreaHeight * 3 / 4, mNormalMarkPaint);
        canvas.drawText(getHourString(0), 0, timeMarkY, mNormalMarkPaint);
//        float markMoveDis = 0;
//        final float halfScreenW = SCREEN_WIDTH/2;
        for (int i = 0; i < size; i++) {
            canvas.translate(INTERVAL_WIDTH, 0);
            if ((i + 1) % reference == 0) {//画小时刻度
                canvas.drawLine(0, 0, 0, -hourMarkHeight, mNormalMarkPaint);
                canvas.drawText(getHourString((i + 1) / reference), 0, timeMarkY, mNormalMarkPaint);
            } else {
                canvas.drawLine(0, 0, 0, -intervalMarkHeight, mNormalMarkPaint);
            }
        }
        canvas.restore();
    }

    /**
     * 例子：传入1，返回：01:00
     */
    private static String getHourString(int hour) {
        if (hour < 10) {
            return new StringBuffer().append("0").append(hour).append(":00").toString();
        } else {
            return new StringBuffer().append(hour).append(":00").toString();
        }
    }

    private void drawTopBottomLine(Canvas canvas) {
        canvas.drawLine(0, 0, getWidth(), 0, mNormalMarkPaint);
        int y = getHeight() - mNormalMarkTimeMarginTop - mNormalMarkTimeTextSize;
        canvas.drawLine(0, y, getWidth(), y, mNormalMarkPaint);
    }
}
