package com.kc.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.kc.util.SizeUtils;

/**
 * Created by ckc on 2017/7/11.
 * 定义：第0个房间总是位于view的垂直中分线上方。
 *
 *
 * 功能：
 * 1、可处理xml中定义的宽高、padding、paddingLeft
 * 2、可根据文本长度动态变化雷达半径
 * 3、中文文本长度（length）至少支持5.
 * 4、点击某个房间名称可回调该房间在列表的位置和id。
 *
 * 待增加功能
 * 1、按压文本变色
 * 2、趣味性：雷达跟随手指旋转，放开回旋至默认位置。
 *
 */
public class CsmRadarView extends View implements GestureDetector.OnGestureListener {


    private static final String TAG = "CsmRadarView";
    private final int mCircleNum = 4;
    private GestureDetector mGestureDetector = new GestureDetector(getContext(), this);
    private OnCsmRadarViewClickListener mListener;
    private Canvas mCanvas;
    private float mWidth;

    private float mRadius = 0;
    private float mOriginCircleRadius;//原点圆半径
    //    private float mPadding = 60;
    private PointF mCenterPoint;

    private Paint mCirclePaint, mOriginCirclePaint, mTextPaint;
    private float mTextSize = SizeUtils.dp2px(getContext(), 15);

    private int mRoomNum = 1;
    private float mRotateAngle;
    private float[] mLocations = new float[16];
    private String[] mRoomNames = new String[]{"二楼大卧室", "二楼一楼厅", "二楼厨房二", "二二楼卧室", "二楼大卧室", "楼大卧室厨房", "二楼大卧室", "二楼大厨房"};

    //样式
    private int mPressedTextColor = Color.RED;
    private int mNormalTextColor = Color.WHITE;

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

        mCenterPoint = new PointF();

        mRotateAngle = 360 / mRoomNum;

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.WHITE);

        mOriginCirclePaint = new Paint();
        mOriginCirclePaint.setAntiAlias(true);
        mOriginCirclePaint.setStyle(Paint.Style.FILL);
        mOriginCirclePaint.setColor(Color.WHITE);
        mOriginCirclePaint.setAlpha(125);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(mNormalTextColor);
//        mTextPaint.setARGB(255, 0, 0, 255);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            width = SizeUtils.dp2px(getContext(), 200);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = height;
        }
        mWidth = width;
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateRadius();
//        mCanvas = canvas;
        drawCircle(canvas);
        drawLine(canvas);
        drawText(canvas);
    }

    /**
     * 计算半径
     * <p>
     * 思路：确定任意一个半径，比如设置画布宽度的1/4。
     * 先以此半径计算每个文本的坐标。
     * 利用Rect获得每个文本的宽高。
     * 获得x坐标最小的文本位置和x坐标最大的文本位置
     * 比较minX到圆心x的绝对值和maxX到圆心x的绝对值的大小
     * 获得绝对值较大的相应的文本位置对应的文本为最压缩水平半径的文本。
     * 计算得到半径=画布宽度/2-paddingLeft-文本宽度。
     */
    private void calculateRadius() {
        mRadius = mWidth / 4;//假设为1/4宽度
        mCenterPoint.x = mCenterPoint.y = mWidth / 2;
        Log.e(TAG, "calculateRadius: before,center x=" + mCenterPoint.x + ",center y=" + mCenterPoint.y + ",r=" + mRadius);

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
                if (y > mCenterPoint.y) {
                    y += rect.height() / 2;
                } else if (y < mCenterPoint.y) {
                    y -= rect.height() / 2;
                }
            } else {
                x -= rect.width() / 2;
                if (y > mCenterPoint.y) {
                    y += rect.height() / 2;
                } else if (y < mCenterPoint.y) {
                    y -= rect.height() / 2;
                }
            }

            mLocations[i * 2] = x;
            mLocations[i * 2 + 1] = y;
            angle += step;
        }


        int minXPos = 0;
        int maxXPos = 0;
        Rect rect2 = new Rect();
        mTextPaint.getTextBounds(mRoomNames[0], 0, mRoomNames[0].length(), rect2);
        float minX = mLocations[0] - rect2.width() / 2;
        float maxX = mLocations[0] + rect2.width() / 2;
        for (int i = 1; i < mRoomNum; i++) {
            mTextPaint.getTextBounds(mRoomNames[i], 0, mRoomNames[i].length(), rect2);
            //选出最小
            float x = mLocations[i * 2] - rect2.width() / 2;
            if (x < minX) {
                minX = x;
                minXPos = i;
            }
            //选出最大
            x = mLocations[i * 2] + rect2.width() / 2;
            if (x > maxX) {
                maxX = x;
                maxXPos = i;
            }
        }
        int selPos = 0;
        if ((mCenterPoint.x - minX) >= (maxX - mCenterPoint.x)) {
            selPos = minXPos;
        } else {
            selPos = maxXPos;
        }
        //开始计算半径
        mTextPaint.getTextBounds(mRoomNames[selPos], 0, mRoomNames[selPos].length(), rect2);
        mRadius = mWidth / 2 - getPaddingLeft() - rect2.width();
        mOriginCircleRadius = mRadius / 10;
        Log.e(TAG, "calculateRadius: after,r=" + mRadius);
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
                    y -= rect.height();
                }
            } else if (x > mCenterPoint.x) {
                x += rect.width() / 2;
                if (y > mCenterPoint.y) {
                    y += rect.height() / 2;
                } else if (y < mCenterPoint.y) {
                    y -= rect.height() / 2;
                }
            } else {
                x -= rect.width() / 2;
                if (y > mCenterPoint.y) {
                    y += rect.height() / 2;
                } else if (y < mCenterPoint.y) {
                    y -= rect.height() / 2;
                }
            }
            mLocations[i * 2] = x;
            mLocations[i * 2 + 1] = y + rect.height() / 2;//所有文本下移半个文本高度
            angle += step;
        }
        for (int i = 0; i < mRoomNum; i++) {
//            if (mLastPressPos == i) {
//                mTextPaint.setColor(mPressedTextColor);
//            } else {
//                mTextPaint.setColor(mNormalTextColor);
//            }
            canvas.drawText(mRoomNames[i], mLocations[i * 2], mLocations[i * 2 + 1], mTextPaint);
//            canvas.drawCircle(mLocations[i * 2], mLocations[i * 2 + 1], mOriginCircleRadius, mOriginCirclePaint);
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

    private int isClickedOne(MotionEvent motionEvent) {
        int pos = -1;

        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        for (int i = 0; i < mRoomNum; i++) {
            float halfNameWidth = mTextSize * mRoomNames[i].length() / 2;
            if (halfNameWidth < mTextSize) {//说明房间名称只有一个字
                halfNameWidth = mTextSize;
            }
            float x1 = mLocations[i * 2] - halfNameWidth;
            float x2 = mLocations[i * 2] + halfNameWidth;
            float y1 = mLocations[i * 2 + 1] - mTextSize;
            if (x > x1 && x < x2 && y > y1 && y < mLocations[i * 2 + 1]) {
                pos = i;
                break;
            }
        }

        return pos;
    }

    private void fingerPressOne(int pos) {
        if (pos == -1 || pos > mRoomNum - 1) {
            return;
        }
        mLastPressPos = pos;

        float x = mLocations[pos * 2];
        float y = mLocations[pos * 2 + 1];
        mTextPaint.setColor(mPressedTextColor);
        mCanvas.save();
        mCanvas.drawText(mRoomNames[pos], x, y, mTextPaint);
        mCanvas.restore();
    }

    private void fingerFarFromOne() {
        if (mLastPressPos == -1 || mLastPressPos > mRoomNum - 1) {
            return;
        }
        int pos = mLastPressPos;
        mLastPressPos = -1;
        float x = mLocations[pos * 2];
        float y = mLocations[pos * 2 + 1];
        mTextPaint.setColor(mNormalTextColor);
        mCanvas.save();
        mCanvas.drawText(mRoomNames[pos], x, y, mTextPaint);
        mCanvas.restore();
    }

    private Path mLinePath = new Path();
    private float mLastX;
    private float mLastY;

    private void drawLineFollowFinger(MotionEvent event) {
        Log.e(TAG, "drawLineFollowFinger: action=" + CustomViewTool.getAction(event.getAction()));
        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mLinePath.reset();
                mLinePath.moveTo(x, y);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
//                if (mLinePath == null){
//                    mLinePath = new Path();
//                }
                mLinePath.lineTo(x, y);
                mCanvas.drawPath(mLinePath, mCirclePaint);
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
        }
        mLastX = x;
        mLastY = y;
    }

    private int mLastPressPos = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        drawLineFollowFinger(event);
        //按压文字变色
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                final int pos = isClickedOne(event);
//                if (pos != -1 && pos != mLastPressPos) {
//                    Log.e(TAG, "onTouchEvent: ACTION_DOWN手指按压名称区域:" + pos);
//                    fingerPressOne(pos);
////                    invalidate();
//                }
//                break;
//            }
//            case MotionEvent.ACTION_MOVE: {
//                final int pos = isClickedOne(event);
//                if (mLastPressPos != -1 && (pos == -1 || pos != mLastPressPos)) {
//                    Log.e(TAG, "onTouchEvent: ACTION_MOVE手指离开名称区域");
//                    fingerFarFromOne();
////                    invalidate();
//                }
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                if (mLastPressPos != -1) {
//                    Log.e(TAG, "onTouchEvent: ACTION_UP手指离开名称区域");
//                    fingerFarFromOne();
////                    invalidate();
//                }
//                break;
//            }
//        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.e(TAG, "onShowPress: ");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        int pos = isClickedOne(e);
        if (pos != -1 && mListener != null) {
            mListener.onCsmRadarViewClick(pos, 5);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void setListener(OnCsmRadarViewClickListener listener) {
        mListener = listener;
    }

    public interface OnCsmRadarViewClickListener {
        void onCsmRadarViewClick(int pos, int roomId);
    }
}
