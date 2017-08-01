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
 * 定义：第0个房间总是位于view的垂直中分线上方。
 * <p>
 * <p>
 * 功能：
 * 1、可处理xml中定义的宽高、padding、paddingLeft
 * 2、可根据文本长度动态变化雷达半径
 * 3、中文文本长度（length）至少支持5.
 * 4、点击某个房间名称可回调该房间在列表的位置和id。
 * 5、按压文本变色
 * 6、动态改变分数数值显示。
 * 7、房间信息以RoomInfo实体类表示。
 * 8、增加xml定义属性和公共接口（修改房间名称，修改房间分数，设置房间信息列表）
 * 9、增加房间名称超出宽度部分省略
 * <p>
 * <p>
 * 待增加功能
 * 1、
 * 2、趣味性：雷达跟随手指旋转，放开回旋至默认位置。
 */
public class CsmRadarView extends View implements GestureDetector.OnGestureListener {


    private static final String TAG = "CsmRadarView";
    private static final int MAX_ROOM_NUM = 8;
    private final int mCircleNum = 4;
    //文字最多显示N个文本大小的宽度
    private final int MAX_SHOW_TEXT_WIDTH_NUM = 5;
    private GestureDetector mGestureDetector = new GestureDetector(getContext(), this);
    private OnCsmRadarViewClickListener mListener;
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

    private float[] mLocations = new float[MAX_ROOM_NUM * 2];//最大支持显示房间数量
    //    private String[] mRoomNames = new String[]{"二楼大卧室", "二楼一楼厅", "二楼厨房二", "二二楼卧室", "二楼大卧室", "楼大卧室厨房", "二楼大卧室", "二楼大厨房"};
    private Map<Integer, Integer> mRoomScoreMap = new HashMap<>(MAX_ROOM_NUM);//最大支持显示房间数量


    //房间相关变量
    private List<RoomInfo> mRoomInfos = null;
    //如果房间信息为空，使用例子房间信息列表绘图
    private List<RoomInfo> mExampleRoomInfos = new ArrayList<>(MAX_ROOM_NUM);
    private List<RoomInfo> mDrawRoomInfos = null;
    private int mRoomNum = 7;
    private float mRotateAngle;
    private float mRadius = 0;
    private float mOriginCircleRadius;//原点圆半径


    public CsmRadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initExampleRoomInfos();
        initExampleRoomScoreMap();
        init(context, attrs);
    }

    private void initExampleRoomInfos() {
        for (int i = 0; i < MAX_ROOM_NUM; i++) {
            RoomInfo roomInfo = new RoomInfo(i, "示例房间" + (i + 1));
            mExampleRoomInfos.add(roomInfo);
        }
    }

    private void initExampleRoomScoreMap() {
//        Random random = new Random();
        for (int i = 0; i < MAX_ROOM_NUM; i++) {
//            mRoomScoreMap.put(i, random.nextInt(101));
            mRoomScoreMap.put(i, mDefaultScore);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        //测试获取xml定义的属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CsmRadarView);
        mTextColor = typedArray.getInt(R.styleable.CsmRadarView_textColor, mTextColor);
        mPressTextColor = typedArray.getInt(R.styleable.CsmRadarView_pressTextColor, mPressTextColor);
        mRadarLineColor = typedArray.getInt(R.styleable.CsmRadarView_radarLineColor, mRadarLineColor);
        mScoreLineColor = typedArray.getInt(R.styleable.CsmRadarView_scoreLineColor, mScoreLineColor);
        mTextSize = typedArray.getDimension(R.styleable.CsmRadarView_textSize, mTextSize);
        //获得默认分数
        int defaultScore = typedArray.getInt(R.styleable.CsmRadarView_defaultScore, mDefaultScore);
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
//        mCanvas = canvas;
        confirmRoomInfos();
        calculateRadius();
        drawCircle(canvas);
        drawLine(canvas);
        drawText(canvas);
        drawScoreLines(canvas);
    }

    private void confirmRoomInfos() {
        mDrawRoomInfos = mRoomInfos;
        if (mDrawRoomInfos == null || mDrawRoomInfos.size() == 0) {
            mDrawRoomInfos = mExampleRoomInfos;
        }
        mRoomNum = mDrawRoomInfos.size();
    }

    private void drawScoreLines(Canvas canvas) {
        Path path = new Path();

        double step = 2 * Math.PI / mRoomNum;
        double angle = -Math.PI / 2;
        for (int i = 0; i < mRoomNum; i++) {
            Integer score = mRoomScoreMap.get(mDrawRoomInfos.get(i).getRoomId());
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
        mCenterPoint.x = mCenterPoint.y = mWidth / 2;
//        Log.e(TAG, "calculateRadius: before,center x=" + mCenterPoint.x + ",center y=" + mCenterPoint.y + ",r=" + mRadius);

        Rect rect = new Rect();
        double step = 2 * Math.PI / mRoomNum;
        double angle = -Math.PI / 2;
        for (int i = 0; i < mRoomNum; i++) {
            float x = mCenterPoint.x + mRadius * (float) Math.cos(angle);
            float y = mCenterPoint.y + mRadius * (float) Math.sin(angle);
//            Log.e(TAG, "text location i:" + i + ",x=" + x + ",y=" + y);
            String roomName = mDrawRoomInfos.get(i).getRoomName();
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
        String room0Name = mDrawRoomInfos.get(0).getRoomName();
        mTextPaint.getTextBounds(room0Name, 0, room0Name.length(), rect2);
        float minX = mLocations[0] - rect2.width() / 2;
        float maxX = mLocations[0] + rect2.width() / 2;
        for (int i = 1; i < mRoomNum; i++) {
            String roomName = mDrawRoomInfos.get(i).getRoomName();
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
        String roomName = mDrawRoomInfos.get(selPos).getRoomName();
        mTextPaint.getTextBounds(roomName, 0, roomName.length(), rect2);
        mRadius = mWidth / 2 - getPaddingLeft() - rect2.width();
        mOriginCircleRadius = mRadius / 10;
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
            String roomName = mDrawRoomInfos.get(i).getRoomName();
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
            String roomName = mDrawRoomInfos.get(i).getRoomName();
            canvas.drawText(roomName, mLocations[i * 2], mLocations[i * 2 + 1], mTextPaint);
//            canvas.drawCircle(mLocations[i * 2], mLocations[i * 2 + 1], mOriginCircleRadius, mOriginCirclePaint);
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
            String roomName = mDrawRoomInfos.get(i).getRoomName();
            float halfNameWidth = mTextSize * roomName.length() / 2;
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
        String roomName = mDrawRoomInfos.get(pos).getRoomName();
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
        String roomName = mDrawRoomInfos.get(pos).getRoomName();
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
        if (pos != -1 && mListener != null && mDrawRoomInfos != mExampleRoomInfos) {
            mListener.onCsmRadarViewClick(pos, mDrawRoomInfos.get(pos).getRoomId());
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

    public void setListener(OnCsmRadarViewClickListener listener) {
        mListener = listener;
    }

    public interface OnCsmRadarViewClickListener {
        void onCsmRadarViewClick(int pos, int roomId);
    }

    public void updateRoomName(Integer roomId, String name) {
        if (roomId == null) {
            Log.e(TAG, "更新房间名称失败：参数roomId为空");
        }
        if (name == null) {
            Log.e(TAG, "更新房间名称失败：参数name为空");
        }
        if (mRoomInfos == null) {
            Log.e(TAG, "更新房间名称失败：mRoomInfos为空");
            return;
        }
        boolean find = false;
        for (RoomInfo roomInfo : mRoomInfos) {
            if (roomInfo.getRoomId() == roomId) {
                find = true;
                roomInfo.setRoomName(getEllipsizeEndName(name));
                break;
            }
        }
        if (find) {
            invalidate();
        }
    }

    public void updateRoomScore(Integer roomId, Integer score) {
        if (roomId == null) {
            Log.e(TAG, "更新房间分数失败：参数roomId为空");
        }
        if (score == null) {
            Log.e(TAG, "更新房间分数失败：参数score为空");
        }
        if (score < 0 || score > 100) {
            Log.e(TAG, "更新房间分数失败：参数score不在0-100范围内");
            return;
        }
        if (mRoomInfos == null) {
            Log.e(TAG, "更新房间分数失败：mRoomInfos为空");
            return;
        }
        boolean find = false;
        for (RoomInfo roomInfo : mRoomInfos) {
            if (roomInfo.getRoomId() == roomId) {
                find = true;
                break;
            }
        }
        if (find) {
            mRoomScoreMap.put(roomId, score);
            invalidate();
        }
    }

    public void setRoomInfos(List<RoomInfo> list) {
        if (list == null) {
            Log.e(TAG, "设置房间信息失败：参数list为空");
            return;
        }
        for (RoomInfo roomInfo : list) {
            if (roomInfo.getRoomName() != null) {
                roomInfo.setRoomName(getEllipsizeEndName(roomInfo.getRoomName()));
            } else {
                roomInfo.setRoomName("未命名房间");
            }
        }
        mRoomInfos = list;
        invalidate();
    }

    /**
     * 测试查看不同房间的view效果
     * */
//    public void refreshScore() {
//        mRoomNum++;
//        if (mRoomNum == 9) {
//            mRoomNum = 1;
//        }
//        initExampleRoomScoreMap();
//        invalidate();
//    }

    /**
     * 房间信息bean
     */
    public static class RoomInfo {
        private int roomId;
        private String roomName;

        public RoomInfo() {
        }

        public RoomInfo(int roomId, String roomName) {
            this.roomId = roomId;
            this.roomName = roomName;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }
    }
}
