package com.kc.custom;

import android.content.Context;
import android.content.res.TypedArray;
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

import com.kc.myasapp.R;
import com.kc.util.SizeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ckc on 2017/7/11.
 * 定义：第0个指数总是位于view的垂直中分线上方。
 * <p>
 * <p>
 * 功能：
 * 1、可处理xml中定义的宽高、padding、paddingLeft
 * 2、可根据文本长度动态变化雷达半径
 * 3、中文文本长度（length）至少支持5.
 * 4、点击某个指数名称可回调该指数在列表的位置和id。
 * 5、按压文本变色
 * 6、动态改变分数数值显示。
 * 7、指数信息以RoomInfo实体类表示。
 * 8、增加xml定义属性和公共接口（修改指数名称，修改指数分数，设置指数信息列表）
 * 9、增加指数名称超出宽度部分省略
 * <p>
 * <p>
 * 待增加功能
 * 1、
 * 2、趣味性：雷达跟随手指旋转，放开回旋至默认位置。
 */
public class CsmSpiderWebView extends View implements GestureDetector.OnGestureListener {


    private static final String TAG = "CsmRadarView";
    private static final int MAX_INDEX_NUM = 5;
    private final int mCircleNum = 4;
    //文字最多显示N个文本大小的宽度
    private final int MAX_SHOW_TEXT_WIDTH_NUM = 5;
    private GestureDetector mGestureDetector = new GestureDetector(getContext(), this);
    private OnCsmSpiderWebViewClickListener mListener;
    private Canvas mCanvas;
    private float mWidth;

    //xml初始化的属性
    private int mRadarLineColor = Color.WHITE;
    private int mScoreLineColor = Color.YELLOW;
    private int mPressTextColor = Color.RED;
    private int mTextColor = Color.WHITE;
    private float mTextSize = SizeUtils.dp2px(getContext(), 15);
    private int mDefaultScore = 80;

    private PointF mCenterPoint;

    private Paint mCirclePaint, mOriginCirclePaint, mTextPaint, mScoreLinesPaint;

    private float[] mLocations = new float[MAX_INDEX_NUM * 2];//最大支持显示指数数量
    //    private String[] mRoomNames = new String[]{"二楼大卧室", "二楼一楼厅", "二楼厨房二", "二二楼卧室", "二楼大卧室", "楼大卧室厨房", "二楼大卧室", "二楼大厨房"};
    private Map<String, Integer> mIndexScoreMap = new HashMap<>(MAX_INDEX_NUM);//最大支持显示指数数量


    //指数相关变量
    private List<IndexInfo> mIndexInfos = null;
    //如果指数信息为空，使用例子指数信息列表绘图
    private List<IndexInfo> mExampleIndexInfos = new ArrayList<>(MAX_INDEX_NUM);
    private List<IndexInfo> mDrawIndexInfos = null;
    private int mRoomNum = 7;
    private float mRotateAngle;
    private float mRadius = 0;
    private float mOriginCircleRadius;//原点圆半径


    public CsmSpiderWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initExampleIndexInfos();
        initExampleIndexScoreMap();
        init(context, attrs);
    }

    private void initExampleIndexInfos() {
        IndexInfo indexInfo = null;
        for (int i = 0; i < MAX_INDEX_NUM; i++) {
//            mIndexScoreMap.put(i, random.nextInt(101));
            switch (i) {
                case 0:
                    indexInfo = new IndexInfo(INDEX_TYPE_OXYGEN, "氧度");
                    break;
                case 1:
                    indexInfo = new IndexInfo(INDEX_TYPE_CLEAN, "净度");
                    break;
                case 2:
                    indexInfo = new IndexInfo(INDEX_TYPE_LIGHT, "光度");
                    break;
                case 3:
                    indexInfo = new IndexInfo(INDEX_TYPE_WET, "湿度");
                    break;
                case 4:
                    indexInfo = new IndexInfo(INDEX_TYPE_TEMP, "温度");
                    break;
            }
            mExampleIndexInfos.add(indexInfo);
        }
    }

    private void initExampleIndexScoreMap() {
//        Random random = new Random();
        for (int i = 0; i < MAX_INDEX_NUM; i++) {
//            mIndexScoreMap.put(i, random.nextInt(101));
            switch (i) {
                case 0:
                    mIndexScoreMap.put(INDEX_TYPE_OXYGEN, mDefaultScore);
                    break;
                case 1:
                    mIndexScoreMap.put(INDEX_TYPE_CLEAN, mDefaultScore);
                    break;
                case 2:
                    mIndexScoreMap.put(INDEX_TYPE_LIGHT, mDefaultScore);
                    break;
                case 3:
                    mIndexScoreMap.put(INDEX_TYPE_WET, mDefaultScore);
                    break;
                case 4:
                    mIndexScoreMap.put(INDEX_TYPE_TEMP, mDefaultScore);
                    break;
            }

        }
    }

    private void init(Context context, AttributeSet attrs) {
        //测试获取xml定义的属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CsmRadarView);
        mTextColor = typedArray.getInt(R.styleable.CsmSpiderWebView_textColor1, mTextColor);
        mPressTextColor = typedArray.getInt(R.styleable.CsmSpiderWebView_pressTextColor1, mPressTextColor);
        mRadarLineColor = typedArray.getInt(R.styleable.CsmSpiderWebView_radarLineColor1, mRadarLineColor);
        mScoreLineColor = typedArray.getInt(R.styleable.CsmSpiderWebView_scoreLineColor1, mScoreLineColor);
        mTextSize = typedArray.getDimension(R.styleable.CsmSpiderWebView_textSize1, mTextSize);
        //获得默认分数
        int defaultScore = typedArray.getInt(R.styleable.CsmSpiderWebView_defaultScore1, mDefaultScore);
        if (defaultScore > -1 && defaultScore < 101) {
            mDefaultScore = defaultScore;
        }
//        int testInt = typedArray.getInt(R.styleable.CsmRadarView_testInt, 404);
//        String testString = typedArray.getString(R.styleable.CsmRadarView_testString);
//        boolean testBoolean = typedArray.getBoolean(R.styleable.CsmRadarView_tesBoolean, false);
//        int testDimenPixelOffset = typedArray.getDimensionPixelOffset(R.styleable.CsmRadarView_testDimen, 404);
//        float testDimen = typedArray.getDimension(R.styleable.CsmRadarView_testDimen, 404);
//        int testDimenPixelSize = typedArray.getDimensionPixelSize(R.styleable.CsmRadarView_testDimen, 404);
//        Log.e("test", "!!!!!\ntestInt="+testInt+",testString="+testString+",testBoolean="+testBoolean
//                +",testDimenPixelOffset="+testDimenPixelOffset+",testDimen="+testDimen
//                +",testDimenPixelSize="+testDimenPixelSize+"\n!!!!!");
        typedArray.recycle();

        mCenterPoint = new PointF();

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(mRadarLineColor);

        mScoreLinesPaint = new Paint();
        mScoreLinesPaint.setAntiAlias(true);
        mScoreLinesPaint.setStyle(Paint.Style.STROKE);
        mScoreLinesPaint.setStrokeWidth(SizeUtils.dp2px(getContext(), 3));
        mScoreLinesPaint.setColor(mScoreLineColor);

        mOriginCirclePaint = new Paint();
        mOriginCirclePaint.setAntiAlias(true);
        mOriginCirclePaint.setStyle(Paint.Style.FILL);
        mOriginCirclePaint.setColor(mRadarLineColor);
        mOriginCirclePaint.setAlpha(125);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(mTextColor);
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
        if (widthMode == MeasureSpec.AT_MOST) {
            Log.e(TAG, "AT_MOST时 width=" + width);
            width = SizeUtils.dp2px(getContext(), 200);
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            Log.e(TAG, "AT_MOST时 height=" + height);
            height = width;
        }
        setMeasuredDimension(width, height);
        mWidth = width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mCanvas = canvas;
        confirmRoomInfos();
        calculateRadius();
        drawSpiderWeb(canvas);
        drawLine(canvas);
        drawText(canvas);
        drawScoreLines(canvas);
    }

    private void confirmRoomInfos() {
        mDrawIndexInfos = mIndexInfos;
        if (mDrawIndexInfos == null || mDrawIndexInfos.size() == 0) {
            mDrawIndexInfos = mExampleIndexInfos;
        }
        mRoomNum = mDrawIndexInfos.size();
    }

    private void drawScoreLines(Canvas canvas) {
        Path path = new Path();

        double step = 2 * Math.PI / mRoomNum;
        double angle = -Math.PI / 2;
        for (int i = 0; i < mRoomNum; i++) {
            Integer score = mIndexScoreMap.get(mDrawIndexInfos.get(i).getType());
            if (score == null) {
                score = mDefaultScore;
            }
            float scoreR = mRadius * score / 100;
            float x = mCenterPoint.x + scoreR * (float) Math.cos(angle);
            float y = mCenterPoint.y + scoreR * (float) Math.sin(angle);
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            angle += step;
        }
        if (mRoomNum == 1) {
            path.lineTo(mCenterPoint.x, mCenterPoint.y);
        }
        path.close();
        mScoreLinesPaint.setStyle(Paint.Style.STROKE);
        mScoreLinesPaint.setColor(mScoreLineColor);
        canvas.drawPath(path, mScoreLinesPaint);
        mScoreLinesPaint.setStyle(Paint.Style.FILL);
        mScoreLinesPaint.setAlpha(100);
        canvas.drawPath(path, mScoreLinesPaint);
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
//        mCenterPoint.x = mCenterPoint.y = mWidth / 2;
        mCenterPoint.x = mWidth / 2;
        mCenterPoint.y = getMeasuredHeight() / 2;
//        Log.e(TAG, "calculateRadius: before,center x=" + mCenterPoint.x + ",center y=" + mCenterPoint.y + ",r=" + mRadius);

        Rect rect = new Rect();
        double step = 2 * Math.PI / mRoomNum;
        double angle = -Math.PI / 2;
        for (int i = 0; i < mRoomNum; i++) {
            float x = mCenterPoint.x + mRadius * (float) Math.cos(angle);
            float y = mCenterPoint.y + mRadius * (float) Math.sin(angle);
//            Log.e(TAG, "text location i:" + i + ",x=" + x + ",y=" + y);
            String roomName = mDrawIndexInfos.get(i).getName();
            mTextPaint.getTextBounds(roomName, 0, roomName.length(), rect);
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
        String room0Name = mDrawIndexInfos.get(0).getName();
        mTextPaint.getTextBounds(room0Name, 0, room0Name.length(), rect2);
        float minX = mLocations[0] - rect2.width() / 2;
        float maxX = mLocations[0] + rect2.width() / 2;
        for (int i = 1; i < mRoomNum; i++) {
            String roomName = mDrawIndexInfos.get(i).getName();
            mTextPaint.getTextBounds(roomName, 0, roomName.length(), rect2);
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
        String roomName = mDrawIndexInfos.get(selPos).getName();
        mTextPaint.getTextBounds(roomName, 0, roomName.length(), rect2);
        mRadius = mWidth / 2 - getPaddingLeft() - rect2.width();
        mOriginCircleRadius = mRadius / 10;

        //判断此时在垂直方向上是否会超出画布范围
        if (mCenterPoint.y - mRadius - mTextSize * 3 / 2 < 0) {
            mRadius = mCenterPoint.y - mTextSize * 3 / 2;
            mOriginCircleRadius = mRadius / 10;
        }
//        Log.e(TAG, "calculateRadius: after,r=" + mRadius);
    }

    private void drawText(Canvas canvas) {
        Rect rect = new Rect();
        double step = 2 * Math.PI / mRoomNum;
        double angle = -Math.PI / 2;
        for (int i = 0; i < mRoomNum; i++) {
            float x = mCenterPoint.x + mRadius * (float) Math.cos(angle);
            float y = mCenterPoint.y + mRadius * (float) Math.sin(angle);
//            Log.e(TAG, "text location i:" + i + ",x=" + x + ",y=" + y);
            String roomName = mDrawIndexInfos.get(i).getName();
            mTextPaint.getTextBounds(roomName, 0, roomName.length(), rect);
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
            if (mLastPressPos == i) {
                mTextPaint.setColor(mPressTextColor);
            } else {
                mTextPaint.setColor(mTextColor);
            }
            String roomName = mDrawIndexInfos.get(i).getName();
            canvas.drawText(roomName, mLocations[i * 2], mLocations[i * 2 + 1], mTextPaint);
//            canvas.drawSpiderWeb(mLocations[i * 2], mLocations[i * 2 + 1], mOriginCircleRadius, mOriginCirclePaint);
        }
    }

    private void drawLine(Canvas canvas) {
        mRotateAngle = 360 / mRoomNum;
        canvas.save();

        canvas.translate(mCenterPoint.x, mCenterPoint.y);
        for (int i = 0; i < mRoomNum; i++) {
            canvas.drawLine(0, 0, 0, -mRadius, mCirclePaint);
//            canvas.drawText("卧室", 0, -mRadius, mCirclePaint);
            canvas.rotate(mRotateAngle);
        }

        canvas.restore();
    }

    private void drawSpiderWeb(Canvas canvas) {
        Path path = new Path();
        double step = 2 * Math.PI / mRoomNum;
        double angle = -Math.PI / 2;

        for (int i = 0; i < mCircleNum; i++) {
            if (i != 0) {
                path.reset();
            }

            float webR = mRadius * (i + 1) / mCircleNum;
            for (int j = 0; j < mRoomNum; j++) {
                float x = mCenterPoint.x + webR * (float) Math.cos(angle);
                float y = mCenterPoint.y + webR * (float) Math.sin(angle);
                if (j == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
                if (mRoomNum == 1) {
                    path.lineTo(mCenterPoint.x, mCenterPoint.y);
                }
                if (j == mRoomNum - 1) {
                    path.close();
                }
                canvas.drawPath(path, mCirclePaint);
                angle += step;
            }
        }
    }

    private int isClickedOne(MotionEvent motionEvent) {
        int pos = -1;

        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        for (int i = 0; i < mRoomNum; i++) {
            String roomName = mDrawIndexInfos.get(i).getName();
            float halfNameWidth = mTextSize * roomName.length() / 2;
            if (halfNameWidth < mTextSize) {//说明指数名称只有一个字
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

    /**
     * 手指按压某个文本时调用
     * 停用：效果不稳定
     */
    private void fingerPressOne(int pos) {
        if (pos == -1 || pos > mRoomNum - 1) {
            return;
        }
        mLastPressPos = pos;

        float x = mLocations[pos * 2];
        float y = mLocations[pos * 2 + 1];
        mTextPaint.setColor(mPressTextColor);
        String roomName = mDrawIndexInfos.get(pos).getName();
        mCanvas.drawText(roomName, x, y, mTextPaint);
    }

    /**
     * 手指不在之前按压的文本范围内移动或抬起动作时调用
     * 停用：效果不稳定
     */
    private void fingerFarFromOne() {
        if (mLastPressPos == -1 || mLastPressPos > mRoomNum - 1) {
            return;
        }
        int pos = mLastPressPos;
        mLastPressPos = -1;
        float x = mLocations[pos * 2];
        float y = mLocations[pos * 2 + 1];
        mTextPaint.setColor(mTextColor);
        String roomName = mDrawIndexInfos.get(pos).getName();
        mCanvas.drawText(roomName, x, y, mTextPaint);
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
        //使onClick回调生效
        super.onTouchEvent(event);
//        drawLineFollowFinger(event);
        //按压文字变色
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                final int pos = isClickedOne(event);
                if (pos != -1 && pos != mLastPressPos) {
                    Log.e(TAG, "onTouchEvent: ACTION_DOWN手指按压名称区域:" + pos);
//                    fingerPressOne(pos);
                    mLastPressPos = pos;
                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int pos = isClickedOne(event);
                Log.e(TAG, "onTouchEvent: ACTION_MOVE，pos=" + pos + ",mLastPressPos=" + mLastPressPos);
                if (mLastPressPos != -1) {//手指在文字上移动直接恢复原色
                    // 手指在文字上移动保持按压色，移到文字外恢复原色，有50%概率不正常。
//                if (mLastPressPos != -1 && (pos == -1 || pos != mLastPressPos)) {
                    Log.e(TAG, "onTouchEvent: ACTION_MOVE手指离开名称区域");
//                    fingerFarFromOne();
                    mLastPressPos = -1;
                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (mLastPressPos != -1) {
                    Log.e(TAG, "onTouchEvent: ACTION_UP手指离开名称区域");
//                    fingerFarFromOne();
                    mLastPressPos = -1;
                    invalidate();
                }
                break;
            }
        }
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
        if (pos != -1 && mListener != null && mDrawIndexInfos != mExampleIndexInfos) {
            mListener.onCsmSpiderWebViewClick(pos, mDrawIndexInfos.get(pos).getType());
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

    /**
     * 将超过宽度限制的文本末尾用...表示
     * <p>
     * 若要修改宽度显示数量，请修改MAX_SHOW_TEXT_WIDTH_NUM变量
     */
    private String getEllipsizeEndName(String name) {
        final float textMaxWidth = mTextSize * MAX_SHOW_TEXT_WIDTH_NUM;

        String ret = null;
        Rect rect = new Rect();
        mTextPaint.getTextBounds(name, 0, name.length(), rect);
        if (rect.width() > textMaxWidth) {
            int lastLessWidthEnd = 0;
            for (int i = 0; i < name.length(); i++) {
                mTextPaint.getTextBounds(name, 0, i + 1, rect);
//                Log.e(TAG, "name="+name+",rect.width()="+rect.width()+",textMaxWidth="+textMaxWidth);
                if (rect.width() <= textMaxWidth) {
                    lastLessWidthEnd = i + 1;
                } else {
                    break;
                }
            }
            ret = name.substring(0, lastLessWidthEnd) + "...";
        } else {
            ret = name;
        }
        return ret;
    }

    public void setListener(OnCsmSpiderWebViewClickListener listener) {
        mListener = listener;
    }

    public interface OnCsmSpiderWebViewClickListener {
        void onCsmSpiderWebViewClick(int pos, String indexType);
    }

    public void updateIndexName(String indexType, String name) {
        if (indexType == null) {
            Log.e(TAG, "更新指数名称失败：参数indexType为空");
        }
        if (!mIndexScoreMap.containsKey(indexType)) {
            Log.e(TAG, "更新指数分数失败：参数indexType不存在");
            return;
        }
        if (name == null) {
            Log.e(TAG, "更新指数名称失败：参数name为空");
        }
//        if (mIndexInfos == null) {
//            Log.e(TAG, "更新指数名称失败：mIndexInfos为空");
//            return;
//        }
        for (IndexInfo indexInfo : mDrawIndexInfos) {
            if (indexInfo.getType().equals(indexType)) {
                indexInfo.setName(getEllipsizeEndName(name));
                break;
            }
        }
        invalidate();
    }

    public void updateIndexScore(String indexType, Integer score) {
        if (indexType == null) {
            Log.e(TAG, "更新指数分数失败：参数indexType为空");
        }
        if (!mIndexScoreMap.containsKey(indexType)) {
            Log.e(TAG, "更新指数分数失败：参数indexType不存在");
            return;
        }
        if (score == null) {
            Log.e(TAG, "更新指数分数失败：参数score为空");
        }
        if (score < 0 || score > 100) {
            Log.e(TAG, "更新指数分数失败：参数score不在0-100范围内");
            return;
        }
//        if (mIndexInfos == null) {
//            Log.e(TAG, "更新指数分数失败：mIndexInfos为空");
//            return;
//        }
        mIndexScoreMap.put(indexType, score);
        invalidate();
    }

    public void setIndexInfos(List<IndexInfo> list) {
        if (list == null) {
            Log.e(TAG, "设置指数信息失败：参数list为空");
            return;
        }
        for (IndexInfo indexInfo : list) {
            if (indexInfo.getName() != null) {
                indexInfo.setName(getEllipsizeEndName(indexInfo.getName()));
            } else {
                indexInfo.setName("未命名指数");
            }
        }
        mIndexInfos = list;
        invalidate();
    }

    /**
     * 测试查看不同指数的view效果
     * */
//    public void refreshScore() {
//        mRoomNum++;
//        if (mRoomNum == 9) {
//            mRoomNum = 1;
//        }
//        initExampleIndexScoreMap();
//        invalidate();
//    }

    public static final String INDEX_TYPE_OXYGEN = "oxygen";
    public static final String INDEX_TYPE_CLEAN = "clean";
    public static final String INDEX_TYPE_LIGHT = "light";
    public static final String INDEX_TYPE_WET = "wet";
    public static final String INDEX_TYPE_TEMP = "temperature";

    /**
     * 指数信息bean
     */
    public static class IndexInfo {
        private int roomId;
        private String type;
        private String name;

        public IndexInfo() {
        }

        public IndexInfo(String type, String name) {
            this.type = type;
            this.name = name;
        }

//        public int getRoomId() {
//            return roomId;
//        }
//
//        public void setRoomId(int roomId) {
//            this.roomId = roomId;
//        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
