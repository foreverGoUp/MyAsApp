package com.kc.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/5/14.
 */
public class CsmDialView extends View {

    private Paint mCirclePaint, mLinePaint, mSmallCirclePaint, mTextPaint;

    private float mRadius;
    private float mSmallRadius;

    public CsmDialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CsmDialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(5);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(5);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mSmallCirclePaint = new Paint();
        mSmallCirclePaint.setColor(Color.WHITE);
        mSmallCirclePaint.setAntiAlias(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int len = width < height ? width : height;

        mRadius = len / 2;
        mSmallRadius = mRadius - 60;
        setMeasuredDimension(len, len);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawLine(canvas);
        drawText(canvas);

    }

    private float mTargetAngle = 300;
    private boolean mActionForward = false;
    private boolean mRunning = false;

    public void startAnimation(final float trueAngle) {
        if (mRunning) {
            return;
        }
        mTargetAngle = trueAngle;
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mRunning = true;
                if (!mActionForward) {//后退
                    mTargetAngle -= 3;

                    if (mTargetAngle < 0) {
                        mTargetAngle = 0;
                        mActionForward = true;
                    }

                } else {
                    mTargetAngle += 3;

                    if (mTargetAngle > trueAngle) {
                        mTargetAngle = trueAngle;
                        mActionForward = false;
                        timer.cancel();
                        mRunning = false;
                    }
                }
                postInvalidate();
            }
        }, 500, 30);
    }

    private void drawText(Canvas canvas) {
        if (mListener != null) {
            mListener.onCsmDialViewColorChanged(mRed, mGreen);
        }

        mSmallCirclePaint.setColor(Color.argb(255, mRed, mGreen, 0));
        canvas.drawCircle(mRadius, mRadius, mSmallRadius, mSmallCirclePaint);

        int score = (int) (mTargetAngle / 300 * 100);
        mTextPaint.setTextSize(60);
        canvas.drawText("" + score, mRadius, mRadius, mTextPaint);

        mTextPaint.setTextSize(30);
        canvas.drawText("分", mRadius + mSmallRadius / 2, mRadius - mSmallRadius / 2, mTextPaint);
        canvas.drawText("点击优化", mRadius, mRadius + mSmallRadius / 2, mTextPaint);
    }

    private int mRed, mGreen;

    private void drawLine(Canvas canvas) {
        canvas.save();

        canvas.translate(mRadius, mRadius);
        float rotateAngle = 300 / 100;
        canvas.rotate(30 + rotateAngle);

        float sweepedAngel = 0;
        float rate = 0;
        for (int i = 0; i < 99; i++) {
            rate = sweepedAngel / 300;
            if (sweepedAngel < mTargetAngle) {
                mRed = (int) (255 * (1 - rate));
                mGreen = (int) (255 * rate);
                mLinePaint.setColor(Color.argb(255, mRed, mGreen, 0));

            } else {
                mLinePaint.setColor(Color.WHITE);
            }

            canvas.drawLine(0, mRadius, 0, mRadius - 40, mLinePaint);
            canvas.rotate(rotateAngle);
            sweepedAngel += rotateAngle;
        }

        canvas.restore();
    }


    /**
     * drawArc方法中的0度线为画布水平向右
     */
    private void drawCircle(Canvas canvas) {
        RectF rectF = new RectF(0, 0, mRadius * 2, mRadius * 2);
        canvas.drawArc(rectF, 120, 300, false, mCirclePaint);
    }


    public interface OnCsmDialViewColorChanged {
        void onCsmDialViewColorChanged(int red, int green);
    }

    private OnCsmDialViewColorChanged mListener;

    public void setListener(OnCsmDialViewColorChanged listener) {
        this.mListener = listener;
    }
}
