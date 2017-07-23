package com.kc.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ckc on 2017/7/11.
 */
public class CsmRadarView extends View {

    private static final String TAG = "CsmRadarView";
    private final int mCircleNum = 4;
    //    private int mWidth = 400;
//    private int mHeight = 400;

    private float mRadius = 0;
    private float mOriginCircleRadius;//原点圆半径
    private float mPadding = 60;
    private PointF mCenterPoint;

    private Paint mCirclePaint, mOriginCirclePaint, mTextPaint;

    private int mRoomNum = 6;
    private float mRotateAngle;
    private float[] mLocations = new float[16];
    private String[] mRoomNames = new String[]{"卧室", "客厅", "厨房", "卧室q", "客厅s", "厨房s"};

    public CsmRadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //测试获取xml定义的属性
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CsmRadarView);
//        int testInt = typedArray.getInt(R.styleable.CsmRadarView_testInt, 404);
//        String testString = typedArray.getString(R.styleable.CsmRadarView_testString);
//        boolean testBoolean = typedArray.getBoolean(R.styleable.CsmRadarView_tesBoolean, false);
//        int testDimenPixelOffset = typedArray.getDimensionPixelOffset(R.styleable.CsmRadarView_testDimen, 404);
//        float testDimen = typedArray.getDimension(R.styleable.CsmRadarView_testDimen, 404);
//        int testDimenPixelSize = typedArray.getDimensionPixelSize(R.styleable.CsmRadarView_testDimen, 404);
//        Log.e("test", "!!!!!\ntestInt="+testInt+",testString="+testString+",testBoolean="+testBoolean
//                +",testDimenPixelOffset="+testDimenPixelOffset+",testDimen="+testDimen
//                +",testDimenPixelSize="+testDimenPixelSize+"\n!!!!!");
//        typedArray.recycle();

        mRotateAngle = 360 / mRoomNum;

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setARGB(255, 0, 0, 0);

        mOriginCirclePaint = new Paint();
        mOriginCirclePaint.setAntiAlias(true);
        mOriginCirclePaint.setStyle(Paint.Style.FILL);
        mOriginCirclePaint.setARGB(100, 0, 0, 0);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setARGB(255, 0, 0, 255);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(40f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        mRadius = (width - mPadding * 2) / 2;
        mCenterPoint = new PointF(mPadding + mRadius, mPadding + mRadius);
        mOriginCircleRadius = mRadius / 10;
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawLine(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        Rect rect = new Rect();
        double step = 2 * Math.PI / mRoomNum;
        double angle = -Math.PI / 2;
        for (int i = 0; i < mRoomNum; i++) {
            float x = mCenterPoint.x + mRadius * (float) Math.cos(angle);
            float y = mCenterPoint.y + mRadius * (float) Math.sin(angle);
            Log.e(TAG, "text location i:" + i + ",x=" + x + ",y=" + y);
            mTextPaint.getTextBounds(mRoomNames[i], 0, mRoomNames[i].length(), rect);
            if (x == mCenterPoint.x) {
                if (y > mCenterPoint.y) {
                    y += rect.height();
                } else {
                    y -= rect.height() / 2;
                }
            } else if (x > mCenterPoint.x) {
                x += rect.width() / 2;
                y += rect.height() / 2;
            } else {
                x -= rect.width() / 2;
                y += rect.height() / 2;
            }
            mLocations[i * 2] = x;
            mLocations[i * 2 + 1] = y;
            angle += step;
        }
        for (int i = 0; i < mRoomNum; i++) {
            canvas.drawText(mRoomNames[i], mLocations[i * 2], mLocations[i * 2 + 1], mTextPaint);
        }
    }

    private void drawLine(Canvas canvas) {
        canvas.save();

        canvas.translate(mCenterPoint.x, mCenterPoint.y);
        for (int i = 0; i < mRoomNum; i++) {
            canvas.drawLine(0, 0, 0, -mRadius, mCirclePaint);
//            canvas.drawText("卧室", 0, -mRadius, mCirclePaint);
            canvas.rotate(mRotateAngle);
        }

        canvas.restore();
    }

    private void drawCircle(Canvas canvas) {
        for (int i = 0; i < mCircleNum; i++) {
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius * (i + 1) / mCircleNum, mCirclePaint);
        }
        //画原点圆
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mOriginCircleRadius, mOriginCirclePaint);
    }

}
