package com.daShen.recyclerView.itemDecorationIml;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * Created by ckc on 2017/7/1.
 * 当item数量不足填充整个屏幕时，该类装饰的最后一行底部还是会有分割线
 * 但是一旦item数量填充整个屏幕且最后一行隐藏在屏幕外，上拉到最后一行时
 * ，最后一行底部没有出现分割线。
 *
 * @author zhy
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;

    public DividerGridItemDecoration(Context context) {
        final TypedArray array = context.obtainStyledAttributes(ATTRS);
        mDivider = array.getDrawable(0);
        array.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
        drawHorizontal(c, parent);

    }

    private int getColumnCount(RecyclerView parent) {
        int columnCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            columnCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            columnCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return columnCount;
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        Log.d("dsd", "drawHorizontal, childCount=" + childCount);
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        Log.d("dsd", "drawVertical, childCount=" + childCount);
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


    private boolean isLastColumn(RecyclerView parent, int childCount, int columnCount, int pos) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % columnCount == 0) {
                return true;
            }

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % columnCount == 0) {
                    return true;
                }
            } else {
                final int remainder = childCount % columnCount;
                if (remainder == 0) {//当最后一列满时
                    childCount -= columnCount;
                } else {
                    childCount -= remainder;
                }
                if (pos >= childCount) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLastRow(RecyclerView parent, int childCount, int columnCount, int pos) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final int remainder = childCount % columnCount;
            if (remainder == 0) {//当最后一列满时
                childCount -= columnCount;
            } else {
                childCount -= remainder;
            }
            if (pos >= childCount) {
                return true;
            }

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                final int remainder = childCount % columnCount;
                if (remainder == 0) {//当最后一列满时
                    childCount -= columnCount;
                } else {
                    childCount -= remainder;
                }
                if (pos >= childCount) {
                    return true;
                }

            } else {
                if ((pos + 1) % columnCount == 0) {
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int columnCount = getColumnCount(parent);
        final int childCount = parent.getAdapter().getItemCount();
        final int pos = parent.getChildAdapterPosition(view);
        Log.e("dsd", "当前item，pos=" + pos);
        if (isLastColumn(parent, childCount, columnCount, pos)) {
            Log.e("dsd", "是 最后一列");
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());

        } else if (isLastRow(parent, childCount, columnCount, pos)) {
            Log.e("dsd", "是 最后一行");
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);

        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
        }
    }
}
