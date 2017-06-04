package com.kc.custom.PullToRefresh;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import com.kc.myasapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/15.
 */
public abstract class BaseRefreshLayout<T extends View>  extends ViewGroup{

    private Scroller mScroller;

    private int mScreenHeight, mHeaderHeight;

    //头视图
    protected View mHeaderView;
    private ProgressBar mPbRefresh;
    private ImageView mIvArrow;
    private TextView mTvRefreshText, mTvLastRefreshTime;

    protected T mContentView;

    private View mFooterView;

    protected int mInitScrollY;
    private int mLastY;
    private int mYOffset;

    private final int STATE_IDLE = 0;
    private final int STATE_REFRESHING = 1;
    private final int STATE_LOADING = 2;
    private int mCurrentState;
    private boolean mIsArrowUp = false;
    private RotateAnimation mRotateAnimation, mRotateAnimationDown;
    private OnRefreshListener mRefreshListener;
    private OnLoadingListener mLoadingListener;

    private SimpleDateFormat mSDFormat;



    public BaseRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);

        mScroller = new Scroller(context);

        //获取屏幕高度
        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        //头部高度为屏幕高度的1/4
        mHeaderHeight = mScreenHeight / 4;

        initLayout(context);

    }

    private void initLayout(Context context) {
        setupHeaderView(context);
        //设置内容视图
        setupContentView(context);
        // 设置布局参数
        setDefaultContentLayoutParams();
        // 添加mContentView
        addView(mContentView);
        //设置加载视图
        setupFooterView(context);
    }

    private void setupFooterView(Context context) {
        mFooterView = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_footer, null);
        mFooterView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mFooterView.setBackgroundColor(Color.RED);
        addView(mFooterView);
    }

    private void setDefaultContentLayoutParams() {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(lp);

    }

    protected abstract void setupContentView(Context context);

    private void setupHeaderView(Context context) {
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header, null);
        mHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, mHeaderHeight));
        mHeaderView.setPadding(0, mHeaderHeight - 200, 0, 0);
        mHeaderView.setBackgroundColor(Color.RED);
        addView(mHeaderView);
        //重新获得头部高度

        mPbRefresh = (ProgressBar) mHeaderView.findViewById(R.id.pull_to_refresh_progress);
        mIvArrow = (ImageView) mHeaderView.findViewById(R.id.pull_to_arrow_image);
        mTvRefreshText = (TextView) mHeaderView.findViewById(R.id.pull_to_refresh_text);
        mTvLastRefreshTime = (TextView) mHeaderView.findViewById(R.id.pull_to_refresh_updated_at);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int finalHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            finalHeight += child.getMeasuredHeight();
        }
        setMeasuredDimension(width, finalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = getPaddingTop();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.layout(0, top, child.getMeasuredWidth(), top + child.getMeasuredHeight());
            top += child.getMeasuredHeight();
        }

        // 计算初始化滑动的y轴距离
        mInitScrollY = mHeaderView.getMeasuredHeight() + getPaddingTop();
        // 滑动到header view高度的位置, 从而达到隐藏header view的效果
        scrollTo(0, mInitScrollY);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                mYOffset = (int) (ev.getRawY() - mLastY);
                //下拉操作
                if (isTop() && mYOffset > 0){
                    return true;
                }
                //上拉操作
                if (isBottom() && mYOffset < 0){
                    return true;
                }

                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                int curY = (int) ev.getRawY();
                mYOffset = curY - mLastY;
                handleMove(mYOffset);
                changeHeaderStyle();

                mLastY = curY;
                break;

            case MotionEvent.ACTION_UP:
                doRefresh();

                break;
        }

        return true;
    }

    private void changeHeaderStyle() {
        if (!isTop()){
            return;
        }
        if (mCurrentState != STATE_IDLE){
            return;
        }

        int curScrollY = getScrollY();
        int slop = mInitScrollY/2;
        if (curScrollY < slop && !mIsArrowUp){//达到刷新条件
            //改变箭头样式
            if (mRotateAnimation == null){
                float pivotX = mIvArrow.getWidth() / 2;
                float pivotY = mIvArrow.getHeight() / 2;
                mRotateAnimation = new RotateAnimation(0, 180, pivotX, pivotY);
                mRotateAnimation.setFillAfter(true);
                mRotateAnimation.setDuration(200);
            }

            mIvArrow.startAnimation(mRotateAnimation);
            mIsArrowUp = true;
            //改变文本
            mTvRefreshText.setText("释放即可刷新");
        }else if(curScrollY > slop && mIsArrowUp){
            //改变箭头样式
            if (mRotateAnimationDown == null){
                float pivotX = mIvArrow.getWidth() / 2;
                float pivotY = mIvArrow.getHeight() / 2;
                mRotateAnimationDown = new RotateAnimation(180, 0, pivotX, pivotY);
                mRotateAnimationDown.setFillAfter(true);
                mRotateAnimationDown.setDuration(200);
            }

            mIvArrow.startAnimation(mRotateAnimationDown);
            mIsArrowUp = false;
            //改变文本
            mTvRefreshText.setText("下拉刷新");
        }
    }

    private void doRefresh() {
        if (!isTop()){
            return;
        }

        mIvArrow.clearAnimation();
        int curScrollY = getScrollY();
        if (curScrollY < mInitScrollY /2){//满足刷新条件
            mScroller.startScroll(getScrollX(), curScrollY, 0, getPaddingTop()+ mHeaderView.getPaddingTop() - curScrollY);

            mTvRefreshText.setText("刷新中...");
            mCurrentState = STATE_REFRESHING;
            mIvArrow.setVisibility(View.GONE);
            mPbRefresh.setVisibility(View.VISIBLE);

            if (mRefreshListener != null){
                mRefreshListener.onRefresh();
            }
        }else {
            mScroller.startScroll(getScrollX(), curScrollY, 0, mInitScrollY - curScrollY);
            mCurrentState = STATE_IDLE;
        }

        invalidate();
    }

    private void handleMove(int yOffset) {
        int curScrollY = getScrollY();
        if (isTop()){
            if (yOffset > 0 && curScrollY - yOffset > getPaddingTop()){//下拉
                scrollBy(0, -yOffset);
            }else if (yOffset < 0 && curScrollY - yOffset <= mInitScrollY){//上拉
                scrollBy(0, -yOffset);
            }
        }
        if (isBottom()){
            //底部上拉最多为底部视图的高度
            if (getScrollY() - yOffset <= mInitScrollY + mFooterView.getMeasuredHeight()){
                scrollBy(0, -yOffset);
            }
            if (curScrollY - mInitScrollY > mFooterView.getMeasuredHeight()/2){
                doLoading();
            }
        }
    }

    protected abstract boolean isBottom();

    protected abstract boolean isTop();

    public interface OnRefreshListener{
        void onRefresh();
    }

    public interface OnLoadingListener{
        void onLoading();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void setRefreshListener(OnRefreshListener listener){ mRefreshListener = listener;}
    public void setLoadingListener(OnLoadingListener listener){ mLoadingListener = listener;}
    public void refreshComplete(){
        mCurrentState = STATE_IDLE;
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPbRefresh.setVisibility(View.GONE);
                mIvArrow.setVisibility(View.VISIBLE);
            }
        }, 200);
        mTvRefreshText.setText("下拉刷新");
        //改变最近刷新时间
        mTvLastRefreshTime.setVisibility(View.VISIBLE);
        if (mSDFormat == null){
            mSDFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        String time = mSDFormat.format(new Date());
        mTvLastRefreshTime.setText("上次刷新时间:"+ time);

        //滚动到原来的位置
        mScroller.startScroll(getScrollX(), getScrollY(), 0, mInitScrollY - getScrollY());
        invalidate();
    }

    public void loadComplete(){
        //隐藏footer
        smoothScroll(mInitScrollY - getScrollY());
        mCurrentState = STATE_IDLE;
    }

    private void smoothScroll(int distance){
        mScroller.startScroll(getScrollX(), getScrollY(), 0, distance);
        invalidate();
    }

    private void doLoading(){
        if (mCurrentState != STATE_IDLE){
            return;
        }

        mCurrentState = STATE_LOADING;
        if (mLoadingListener!=null){
            mLoadingListener.onLoading();
        }
    }

}
