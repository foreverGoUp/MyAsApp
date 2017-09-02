package com.kc.custom.timeAxis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.kc.util.ScreenUtils;
import com.kc.util.SizeUtils;
import com.kc.util.TimeUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ckc on 2017/9/1.
 */

public class CsmTimeAxis extends View {

    private final float SECOND_WIDTH = 0.08f;//px
    private final float DEFAULT_HEIGHT = 400;//px
    private final float SCREEN_WIDTH = ScreenUtils.getScreenWidth(getContext());//px
    private final int INTERVAL_MINUTE = 10;
    private final float INTERVAL_WIDTH = INTERVAL_MINUTE * 60 * SECOND_WIDTH;
    //底部时间属性
    private int mNormalMarkTimeMarginTop = 10;//px
    private int mNormalMarkTimeTextSize = SizeUtils.sp2px(getContext(), 13);

    private Paint mNormalMarkPaint;

    private OnCsmTimeAxisListener mListener;

    private float mLastX;

    private Scroller mScroller = new Scroller(getContext());

    private Date mCurrentDate;

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
//        mNormalMarkPaint.setColor(Color.parseColor("#cdcdcd"));
        mNormalMarkPaint.setColor(Color.GRAY);
        mNormalMarkPaint.setStrokeWidth(2);
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
        int width = (int) (SCREEN_WIDTH + 24 * 3600 * SECOND_WIDTH);
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
        final int markLineAreaHeight = getHeight() - mNormalMarkTimeMarginTop - mNormalMarkTimeTextSize - getPaddingBottom() - getPaddingTop();
        final int timeMarkY = mNormalMarkTimeMarginTop + mNormalMarkTimeTextSize;
        final int hourMarkHeight = markLineAreaHeight;
        final int intervalMarkHeight = markLineAreaHeight * 1 / 3;
        final int halfHourMarkHeight = markLineAreaHeight * 2 / 3;
        canvas.save();
        canvas.translate(SCREEN_WIDTH / 2, markLineAreaHeight + getPaddingTop());
        //画00:00刻度线
        canvas.drawLine(0, 0, 0, -markLineAreaHeight * 3 / 4, mNormalMarkPaint);
        canvas.drawText(getHourString(0, false), 0, timeMarkY, mNormalMarkPaint);
//        float markMoveDis = 0;
//        final float halfScreenW = SCREEN_WIDTH/2;
        for (int i = 0; i < size; i++) {
            canvas.translate(INTERVAL_WIDTH, 0);
            if ((i + 1) % reference == 0) {//画小时刻度
                canvas.drawLine(0, 0, 0, -hourMarkHeight, mNormalMarkPaint);
                canvas.drawText(getHourString((i + 1) / reference, false), 0, timeMarkY, mNormalMarkPaint);
            } else if ((i + 1) % reference == reference / 2) {//画半小时刻度
                canvas.drawLine(0, 0, 0, -halfHourMarkHeight, mNormalMarkPaint);
                canvas.drawText(getHourString((i + 1) / reference, true), 0, timeMarkY, mNormalMarkPaint);
            } else {
                canvas.drawLine(0, 0, 0, -intervalMarkHeight, mNormalMarkPaint);
            }
        }
        canvas.restore();
    }

    /**
     * 例子：传入1，返回：01:00
     */
    private static String getHourString(int hour, boolean halfHour) {
        if (hour < 10) {
            StringBuffer sb = new StringBuffer().append("0").append(hour);
            if (halfHour) {
                sb.append(":30");
            } else {
                sb.append(":00");
            }
            return sb.toString();
        } else {
            StringBuffer sb = new StringBuffer().append(hour);
            if (halfHour) {
                sb.append(":30");
            } else {
                sb.append(":00");
            }
            return sb.toString();
        }
    }

    private void drawTopBottomLine(Canvas canvas) {
//        canvas.drawLine(0, getPaddingTop(), getWidth(), getPaddingTop(), mNormalMarkPaint);
        int y = getHeight() - mNormalMarkTimeMarginTop - mNormalMarkTimeTextSize - getPaddingBottom();
        mNormalMarkPaint.setColor(Color.RED);
        canvas.drawLine(0, y, getWidth(), y, mNormalMarkPaint);
        mNormalMarkPaint.setColor(Color.GRAY);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean consume = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getRawX();
                consume = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getRawX();
                int dx = (int) (x - mLastX);
                if (getScrollX() <= 0 && dx > 0) {//在最左边且向右滑
                    consume = false;
                    break;
                } else if (getScrollX() >= getWidth() - SCREEN_WIDTH && dx < 0) {//在最右边且向左滑
                    consume = false;
                    break;
                }
                scrollBy(-dx, 0);
                //回调时间
                callbackTime(true);
                mLastX = x;
                consume = true;
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX < 0) {
                    scrollTo(0, 0);
                    callbackTimeBeginOrEnd(true);
                } else if (scrollX >= getWidth() - SCREEN_WIDTH) {
                    scrollTo((int) (getWidth() - SCREEN_WIDTH), 0);
                    callbackTimeBeginOrEnd(false);
                } else {
                    //回调时间
                    callbackTime(false);
                }
                break;
        }
        return consume;
    }

    private void callbackTimeBeginOrEnd(boolean isBegin) {
        if (mCurrentDate == null) {
            mCurrentDate = new Date();
        }
        Date date;
        long timeMilli;
        if (isBegin) {
            date = TimeUtil.getDateBeginOrEnd(mCurrentDate, TimeUtil.FLAG_DAY_BEGIN);
        } else {
            date = TimeUtil.getDateBeginOrEnd(mCurrentDate, TimeUtil.FLAG_DAY_END);
        }
        timeMilli = date.getTime();
        if (mListener != null) {
            mListener.onTimeAxisStop(timeMilli);
        }
    }

    private void callbackTime(boolean isMoving) {
        if (mCurrentDate == null) {
            mCurrentDate = new Date();
        }

        Date dateBegin = TimeUtil.getDateBeginOrEnd(mCurrentDate, TimeUtil.FLAG_DAY_BEGIN);
        long timeBeginMilli = dateBegin.getTime();
        int scrollX = getScrollX();
        int intervalSeconds = (int) (scrollX / SECOND_WIDTH);
        long timeMilli = timeBeginMilli + intervalSeconds * 1000;
        if (mListener != null) {
            if (isMoving) {
                mListener.onTimeAxisMove(timeMilli);
            } else {
                mListener.onTimeAxisStop(timeMilli);
            }
        }
    }

    public void setListener(OnCsmTimeAxisListener listener) {
        mListener = listener;
    }

    public interface OnCsmTimeAxisListener {
        void onTimeAxisMove(long timeMilli);

        void onTimeAxisStop(long timeMilli);
    }

    public void setCurrentTime(Calendar calendar) {
        setCurrentTime(calendar.getTime());
    }

    public void setCurrentTime(Date date) {
        setCurrentTime(date.getTime());
    }

    public void setCurrentTime(long timeMilli) {
        Date date = new Date(timeMilli);
        mCurrentDate = date;

        Date dateBegin = TimeUtil.getDateBeginOrEnd(date, TimeUtil.FLAG_DAY_BEGIN);
        long timeBeginMilli = dateBegin.getTime();
        float finalScrollX = (timeMilli - timeBeginMilli) / 1000 * SECOND_WIDTH;
        int dx = (int) (finalScrollX - getScrollX());
        if (dx == 0) {
            return;
        }
        mScroller.startScroll(getScrollX(), getScrollY(), dx, 0);
    }
}
