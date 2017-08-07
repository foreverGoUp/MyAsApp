package com.daShen.zoneView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daShen.zoneView.model.DeviceList;
import com.daShen.zoneView.model.DeviceType;
import com.daShen.zoneView.model.RoomInfo;
import com.kc.myasapp.R;
import com.kc.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yorun on 2017/7/19.
 */

public class ZoneView extends RelativeLayout {

    private final Paint mPaint;
    private final ColorDrawable mLineDrawable;
    private Adapter mAdapter;
    OnZoneClickListener onZoneClickListener;
    int mWidth;
    int mHeight;
    int boxSize;
    List<House> mHouses = new ArrayList<>();
    Runnable mLayoutAfterTask;
    private LinearLayout mTableLinearLayout;
    private int boxMargins = 10;
    private LinearLayout mYLabelLinearLayout;
    private List<Label> YLabels;
    private LinearLayout mXLabelLinearLayout;
    private List<Label> XLabelModels;
    private int mBackGroundColor = 0xff3293a9;
    private int mOpenColor = 0xFFFFE987;
    private int mLineColor = 0xFFE1F3FF;
    static final int SCROLL_MODEL_H = 1;
    static final int SCROLL_MODEL_V = 2;
    int scrollModel;
    private final ShapeDrawable mShapeDrawable;
    private final int mLineWidth;
    private ImageView xLineImageView;
    private ImageView yLineImageView;
    private int maxScrollX;


    List<RoomInfo> roomInfos;
    List<DeviceType> deviceTypes;
    List<DeviceList> deviceLists;
    int[] roomImages;
    int[] deviceTypeImages;

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    public ZoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        setClipChildren(false);
        mPaint = new Paint();
        mPaint.setColor(0XFFE1F3FF);
        mLineWidth = dip2px(context, 4);
        mPaint.setStrokeWidth(mLineWidth);

        mShapeDrawable = new ShapeDrawable(new OvalShape());
        mShapeDrawable.setColorFilter(mOpenColor, PorterDuff.Mode.SRC);

        mLineDrawable = new ColorDrawable(mLineColor);

        setBackgroundColor(mBackGroundColor);
        addTable();
        addYLabels();
        addXLabels();
        addXYLine();

        setAdapter(new Adapter() {
            @Override
            public int getRowCount() {
                return deviceTypes.size();
            }

            @Override
            public int getColumnCount() {
                return roomInfos.size();
            }

            @Override
            public View getView(final int column, final int row) {

                ViewGroup viewGroup = newTableBox();
                viewGroup.setClickable(true);

                RoomInfo roomInfo = roomInfos.get(column);
                DeviceType deviceType = deviceTypes.get(row);
                final DeviceList deviceList = getDevice(roomInfo.getRoomId(), deviceType.getName());

                View view = viewGroup.getChildAt(0);
                View selectView = viewGroup.getChildAt(1);
                if (deviceList.getState() == null) {
                    view.setBackgroundResource(R.drawable.empty);
                } else if (deviceList.getState().equals("0")) {
                    view.setBackgroundResource(R.drawable.close);
                } else {
                    view.setBackgroundResource(R.drawable.open);
                }
                selectView.setVisibility(deviceList.selected ? View.VISIBLE : View.GONE);

                viewGroup.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (deviceList.getState() == null) {
                            if (onZoneClickListener != null) {
                                onZoneClickListener.onDeviceClick(deviceList, null);
                            }
                            return;
                        }
                        deviceList.selected = !deviceList.selected;
                        if (onZoneClickListener != null) {
                            onZoneClickListener.onDeviceClick(deviceList, deviceList.selected);
                        }
                        refresh();
                    }
                });
                return viewGroup;
            }

            private ViewGroup newTableBox() {
                RelativeLayout relativeLayout = new RelativeLayout(getContext());

                View view = new View(getContext());
                LayoutParams viewLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                View selectView = new View(getContext());
                selectView.setBackgroundResource(R.drawable.selected);
                LayoutParams selectViewLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                relativeLayout.addView(view, viewLp);
                relativeLayout.addView(selectView, selectViewLp);
                return relativeLayout;
            }

            @Override
            public Label getXLabel(int column) {
                RoomInfo roomInfo = roomInfos.get(column);
                return new Label(roomInfo.getRoomName(), roomImages[column], roomInfo);
            }

            @Override
            public Label getYLabel(int row) {
                DeviceType deviceType = deviceTypes.get(row);
                return new Label(deviceType.getName(), deviceTypeImages[row], deviceType);
            }

            @Override
            public View getXLabelView(int column) {
                Label label = getXLabel(column);
                final RoomInfo roomInfo = (RoomInfo) label.tag;
                ViewGroup view = (ViewGroup) newLabelView(label);

                int openCount = 0;
                for (DeviceList deviceList : deviceLists) {
                    if (deviceList.getRoomId() == roomInfo.getRoomId() && deviceList.getState()
                            != null && !deviceList.getState().equals("0")) {
                        openCount++;
                    }
                }
                TextView hint = (TextView) view.getChildAt(2);
                hint.setVisibility(openCount == 0 ? View.GONE : View.VISIBLE);
                hint.setText("" + openCount);

                view.setClickable(true);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean allSelect = true;
                        for (DeviceList deviceList : deviceLists) {
                            if (deviceList.getRoomId() == roomInfo.getRoomId()
                                    && deviceList.getState() != null) {
                                if (!deviceList.selected) {
                                    allSelect = false;
                                    break;
                                }
                            }
                        }
                        for (DeviceList deviceList : deviceLists) {
                            if (deviceList.getRoomId() == roomInfo.getRoomId()
                                    && deviceList.getState() != null) {
                                deviceList.selected = !allSelect;
                            }
                        }
                        refresh();
                        if (onZoneClickListener != null) {
                            onZoneClickListener.onHouseClick(roomInfo, !allSelect);
                        }
                    }
                });
                return view;
            }

            @Override
            public View getYLabelView(int row) {
                Label label = getYLabel(row);
                final DeviceType deviceType = (DeviceType) label.tag;
                ViewGroup view = (ViewGroup) newLabelView(label);

                int openCount = 0;
                for (DeviceList deviceList : deviceLists) {
                    if (deviceList.getDevType().equals(deviceType.getName()) && deviceList.getState()
                            != null && !deviceList.getState().equals("0")) {
                        openCount++;
                    }
                }
                TextView hint = (TextView) view.getChildAt(2);
                hint.setVisibility(openCount == 0 ? View.GONE : View.VISIBLE);
                hint.setText("" + openCount);

                view.setClickable(true);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean allSelect = true;
                        for (DeviceList deviceList : deviceLists) {
                            if (deviceList.getDevType().equals(deviceType.getName())
                                    && deviceList.getState() != null) {
                                if (!deviceList.selected) {
                                    allSelect = false;
                                    break;
                                }
                            }
                        }
                        for (DeviceList deviceList : deviceLists) {
                            if (deviceList.getDevType().equals(deviceType.getName())
                                    && deviceList.getState() != null) {
                                deviceList.selected = !allSelect;
                            }
                        }
                        refresh();
                        if (onZoneClickListener != null) {
                            onZoneClickListener.onDeviceMulClick(deviceType, !allSelect);
                        }
                    }
                });
                return view;

            }
        });
    }

    private DeviceList getDevice(Integer roomId, String name) {
        for (DeviceList deviceList : deviceLists) {
            if (deviceList.getRoomId() == roomId && name.equals(deviceList.getDevType())) {
                return deviceList;
            }
        }
        Log.e("dsds", "getDevice null ,roomId = " + roomId + ", name=" + name);
//        return new DeviceList("dl_48", "", "0", roomId, name);
        return null;
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    private void addXYLine() {

        xLineImageView = new ImageView(getContext());
        yLineImageView = new ImageView(getContext());
        xLineImageView.setId(View.generateViewId());
        yLineImageView.setId(View.generateViewId());

        xLineImageView.setImageDrawable(mLineDrawable);
        yLineImageView.setImageDrawable(mLineDrawable);

        addView(xLineImageView);
        addView(yLineImageView);
    }


    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    private void addXLabels() {
        mXLabelLinearLayout = new LinearLayout(getContext());
        mXLabelLinearLayout.setBackgroundColor(mBackGroundColor);
        mXLabelLinearLayout.setId(View.generateViewId());
        mXLabelLinearLayout.setClipChildren(false);
        mXLabelLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(mXLabelLinearLayout);
//    RelativeLayout.LayoutParams params = new LayoutParams(boxSize);
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    private void addYLabels() {
        mYLabelLinearLayout = new LinearLayout(getContext());
        mYLabelLinearLayout.setBackgroundColor(mBackGroundColor);
        mYLabelLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mYLabelLinearLayout.setId(View.generateViewId());
        mYLabelLinearLayout.setGravity(Gravity.BOTTOM);
        mYLabelLinearLayout.setClipChildren(false);

        addView(mYLabelLinearLayout);

//    RelativeLayout.LayoutParams params = new LayoutParams();

    }

    private void addTable() {
        mTableLinearLayout = new LinearLayout(getContext()) {

            int lastX;
            int lastY;

            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;
                        lastY = y;
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        int dx = x - lastX;
                        int dy = y - lastY;
                        int absDx = Math.abs(dx);
                        int absDy = Math.abs(dy);
                        if (absDx > 20 || absDy > 20) {
                            scrollModel = absDx > absDy ? SCROLL_MODEL_H : SCROLL_MODEL_V;
                            return true;
                        }
                    case MotionEvent.ACTION_UP:
                        return false;
                }
                return true;
            }

            @Override
            public boolean onTouchEvent(MotionEvent ev) {
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = x - lastX;
                        int dy = y - lastY;

//            View childAt = mTableLinearLayout.getChildAt(0);
//            childAt.setTranslationY(dy);
                        if (scrollModel == SCROLL_MODEL_H) {
                            this.scrollBy(-dx, 0);
                        } else {
                            this.scrollBy(0, -dy);
                        }
                        scrollLabel(getScrollX(), getScrollY());
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        scrollBox();
                        break;
                }
                return true;
            }

            private void scrollBox() {

                final int scrollX = getScrollX();
                final int scrollY = getScrollY();

                int size = boxSize + boxMargins;

                if (scrollY % size != 0) {
                    final int toScrollY = getToScroll(scrollY, size, false);
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(scrollY, toScrollY).setDuration(120);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int animatedValue = (int) animation.getAnimatedValue();
                            scrollTo(scrollX, animatedValue);
                            scrollLabel(getScrollX(), getScrollY());
                        }
                    });
                    valueAnimator.start();
                }
                if (scrollX % size != 0) {
                    int toScrollX = getToScroll(scrollX, size, true);
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(scrollX, toScrollX).setDuration(120);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int animatedValue = (int) animation.getAnimatedValue();
                            scrollTo(animatedValue, scrollY);
                            scrollLabel(getScrollX(), getScrollY());
                        }
                    });
                    valueAnimator.start();
                    scrollTo(toScrollX, scrollY);
                }
            }

            private int getToScroll(int scroll, int size, boolean isX) {


                int tabWidth = getWidth() - boxSize - mLineWidth - boxMargins * 2;
                int top;
                for (int i = 0; ; i++) {
                    if (mTableLinearLayout.getHeight() + i * boxSize > mAdapter.getRowCount() * (boxSize + boxMargins)) {
                        top = i * (boxSize + boxMargins);
                        break;
                    }
                }

                int right;
                for (int i = 0; ; i++) {
                    if (tabWidth + i * boxSize > mAdapter.getColumnCount() * (boxSize + boxMargins)) {
                        right = i * (boxSize + boxMargins);
                        break;
                    }
                }

                if (isX) {
                    if (scroll < 0) {
                        return 0;
                    }
//                    if (scroll+tabWidth>mAdapter.getColumnCount() * (boxSize + boxMargins)) {
//                        return right;
//                    }
                } else {
                    if (scroll > 0) {
                        return 0;
                    }
                    if (-scroll + mTableLinearLayout.getHeight() > mAdapter.getRowCount() * (boxSize + boxMargins)) {
                        return -top;
                    }
                }

                int t;
                for (int i = 0; ; i++) {
                    if ((scroll + i) % size == 0) {
                        t = i;
                        break;
                    }
                }
                int b;
                for (int i = 0; ; i--) {
                    if ((scroll + i) % size == 0) {
                        b = i;
                        break;
                    }
                }
                if (Math.abs(t) < Math.abs(b)) {
                    return scroll + t;
                } else {
                    return scroll + b;
                }
            }
        };
        mTableLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTableLinearLayout.setClipChildren(false);
//        LayoutParams layoutParams = new LayoutParams(Integer.MAX_VALUE,
//                Integer.MAX_VALUE);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mTableLinearLayout.setGravity(Gravity.BOTTOM);
        addView(mTableLinearLayout, layoutParams);
    }


    private void scrollLabel(int x, int y) {

        mXLabelLinearLayout.scrollTo(x, 0);
        mYLabelLinearLayout.scrollTo(0, y);
    }


    public void setOnZoneClickListener(OnZoneClickListener onZoneClickListener) {
        this.onZoneClickListener = onZoneClickListener;
    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            mWidth = getWidth();
            mHeight = getHeight();
            int columnCount = mAdapter.getColumnCount();
            //（（宽度-坐标线的宽度）/（col+1+右邊留白的比例））-margins
            boxSize = (int) ((mWidth - mLineWidth) / (columnCount + 1 + 0.2f) - boxMargins);
            //计算最大滑动距离
//            Yr.d("mWidth",mWidth,"boxSize",boxSize,"mLineWidth",mLineWidth,"boxMargins");
            int tableWidth = mWidth - boxSize - mLineWidth - boxMargins * 2;
            maxScrollX = 0;
            int allBoxWidth = (boxMargins + boxSize) * mAdapter.getColumnCount();
            for (int i = 0; ; i++) {
                maxScrollX = i * (boxSize + boxMargins);
                if (maxScrollX + tableWidth > allBoxWidth) {
                    break;
                }
            }
//            Yr.d("maxScrollX",maxScrollX,"boxSize",boxSize,"tableWidth",tableWidth);
            refresh();
        }
        if (mLayoutAfterTask != null) {
            mLayoutAfterTask.run();
            mLayoutAfterTask = null;
        }
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    public void refreshXYLayout() {
        {
            mXLabelLinearLayout.removeAllViews();
            LayoutParams xLayoutParams = (LayoutParams) mXLabelLinearLayout
                    .getLayoutParams();
//    xLayoutParams.setMargins(boxSize, 0, 0, 0);
            mXLabelLinearLayout.setPadding(boxSize + boxMargins, boxMargins, 0, 0);
            xLayoutParams.width = Integer.MAX_VALUE;
            xLayoutParams.height = boxSize + boxMargins;
            xLayoutParams.addRule(ALIGN_PARENT_BOTTOM);
            mXLabelLinearLayout.setLayoutParams(xLayoutParams);

            int columnCount = mAdapter.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                View labelView = mAdapter.getXLabelView(i);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(boxSize,
                        boxSize);
                params.setMargins(boxMargins, 0, 0, 0);
                mXLabelLinearLayout.addView(labelView, params);
            }
        }
        {
            //*----y
            mYLabelLinearLayout.removeAllViews();
            int rowCount = mAdapter.getRowCount();
            LayoutParams yLayoutParams = (LayoutParams) mYLabelLinearLayout
                    .getLayoutParams();
            mYLabelLinearLayout.setPadding(0, 0, 0, boxMargins);
            yLayoutParams.width = boxSize + boxMargins;
            yLayoutParams.height = Integer.MAX_VALUE;
            yLayoutParams.setMargins(0, 0, 0, boxSize + boxMargins);
            yLayoutParams.addRule(ALIGN_PARENT_BOTTOM);
            mYLabelLinearLayout.setLayoutParams(yLayoutParams);


            for (int i = 0; i < rowCount; i++) {
                View labelView = mAdapter.getYLabelView(rowCount - i - 1);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(boxSize,
                        boxSize);
                params.setMargins(0, 0, 0, boxMargins);
                mYLabelLinearLayout.addView(labelView, params);

            }
        }
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    private View newLabelView(Label label) {

        RelativeLayout layout = new RelativeLayout(getContext());

        LayoutParams imgLp = new LayoutParams(boxSize / 2,
                boxSize / 2);
        imgLp.addRule(CENTER_IN_PARENT);
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(label.imgRes);
        imageView.setId(View.generateViewId());

        TextView textView = new TextView(getContext());
        textView.setText(label.name);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(boxSize / 12);
        textView.setId(View.generateViewId());
        LayoutParams textLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textLp.addRule(BELOW, imageView.getId());
        textLp.addRule(CENTER_HORIZONTAL);
        layout.addView(imageView, imgLp);
        layout.addView(textView, textLp);

        TextView hintTextView = new TextView(getContext());
        hintTextView.setText("99");
        hintTextView.setGravity(Gravity.CENTER);
        hintTextView.setTextColor(Color.WHITE);
        hintTextView.setBackground(mShapeDrawable);
        hintTextView.setTextSize(boxSize / 15);
        LayoutParams hintLp = new LayoutParams(boxSize / 4,
                boxSize / 4);
        hintLp.addRule(ALIGN_PARENT_TOP);
        hintLp.addRule(ALIGN_PARENT_RIGHT);
        hintLp.setMargins(0, 0, boxMargins, boxMargins);

//    hintLp.addRule();
        layout.addView(hintTextView, hintLp);

//    hintTextView.setPadding(hintTextView.getTextSize()/);


        return layout;
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    public void refresh() {
        if (mWidth == 0) {
            mLayoutAfterTask = new Runnable() {
                @Override
                public void run() {
                    refreshXYLayout();
                    refreshTable();
                    refreshXYLine();
                }
            };
        } else {
            refreshXYLayout();
            refreshTable();
            refreshXYLine();
        }
    }

    private void refreshXYLine() {

        {
            LayoutParams xLp = (LayoutParams) xLineImageView.getLayoutParams();
            xLp.width = mWidth;
            xLp.height = mLineWidth;
//            xLp.setMargins(0,0,0,boxMargins);
            xLineImageView.setPadding(boxSize / 4 * 3, 0, 0, 0);
            xLineImageView.setBackgroundColor(mBackGroundColor);
            ColorDrawable drawable = new ColorDrawable(mLineColor);
            xLp.addRule(ABOVE, mXLabelLinearLayout.getId());
            xLineImageView.setImageDrawable(drawable);
            xLineImageView.setLayoutParams(xLp);
        }
        {

            LayoutParams yLp = (LayoutParams) yLineImageView.getLayoutParams();
            ColorDrawable drawable = new ColorDrawable(mLineColor);
            yLineImageView.setImageDrawable(drawable);
            yLineImageView.setBackgroundColor(mBackGroundColor);
            yLp.width = mLineWidth;
//            yLp.setMargins(boxMargins,0,0,0);
            yLp.height = mHeight;
            yLineImageView.setPadding(0, 0, 0, boxSize / 4 * 3);
            yLp.addRule(RIGHT_OF, mYLabelLinearLayout.getId());
            yLineImageView.setLayoutParams(yLp);
        }
    }

    public void setRoomInfos(List<RoomInfo> roomInfos) {
        this.roomInfos = roomInfos;
    }

    public void setDeviceTypes(List<DeviceType> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }

    public void setDeviceLists(List<DeviceList> deviceLists) {
        this.deviceLists = deviceLists;
    }

    public void setRoomImages(int[] roomImages) {
        this.roomImages = roomImages;
    }

    public void setDeviceTypeImages(int[] deviceTypeImages) {
        this.deviceTypeImages = deviceTypeImages;
    }

    void refreshTable() {

        //*layout
        LayoutParams layoutParams = (LayoutParams) mTableLinearLayout.getLayoutParams();
        layoutParams.addRule(ABOVE, xLineImageView.getId());
        layoutParams.addRule(RIGHT_OF, yLineImageView.getId());
        mTableLinearLayout.setLayoutParams(layoutParams);
        //* data
        int rowCount = mAdapter.getRowCount();
        int columnCount = mAdapter.getColumnCount();

        for (int column = 0; column < columnCount; column++) {
            LinearLayout columnLayout = (LinearLayout) mTableLinearLayout.getChildAt(column);
            if (columnLayout == null) {
                columnLayout = new LinearLayout(getContext());
                columnLayout.setOrientation(LinearLayout.VERTICAL);
                columnLayout.setClipChildren(false);
                mTableLinearLayout.addView(columnLayout);
            }
            {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) columnLayout
                        .getLayoutParams();
                params.width = boxSize;
                params.height = (boxSize + boxMargins) * rowCount;
                params.setMargins(boxMargins, 0, 0, 0);
                columnLayout.setLayoutParams(params);
            }
            columnLayout.removeAllViews();
            for (int row = 0; row < rowCount; row++) {
//        View oldView = columnLayout.getChildAt(rowCount - row);
                View view = mAdapter.getView(column, rowCount - row - 1);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(boxSize, boxSize);
                params.setMargins(0, 0, 0, boxMargins);
                columnLayout.addView(view, row, params);
            }
        }
    }

    public void setModels(List<House> houses) {

        mHouses.clear();
        mHouses.addAll(houses);
    }

//  @Override
//  protected void onDraw(Canvas canvas) {
//    super.onDraw(canvas);
//    canvas.drawLine(boxSize+2*boxMargins, 0, boxSize+2*boxMargins, mHeight, mPaint);
//  }

    public void setYLabels(List<Label> YLabels) {
        this.YLabels = YLabels;

    }

    public static int dip2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setXLabels(List<Label> XLabelModels) {
        this.XLabelModels = XLabelModels;
    }

    public static interface Adapter {

        int getRowCount();

        int getColumnCount();

        View getView(int column, int row);

        Label getXLabel(int column);

        Label getYLabel(int row);

        View getXLabelView(int column);

        View getYLabelView(int row);
    }

    public static class House {

        String name;
        List<Device> devices;

        public House(String name) {
            this.name = name;
        }

        public List<Device> getDevices() {
            return devices;
        }

        public void setDevices(List<Device> devices) {
            this.devices = devices;
        }
    }

    public static class Device {

        public static final int STATUS_OPEN = 0;
        public static final int STATUS_CLOSE = 1;
        public static final int STATUS_EMPTY = 2;
        public int status = STATUS_EMPTY;
        boolean isSelect;
        int column;
        int row;
        String houseName;
        String deviceName;

        public int getColumn() {
            return column;
        }

        public int getRow() {
            return row;
        }

        public String getHouseName() {
            return houseName;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public Device(int status) {
            this.status = status;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "status=" + status +
                    ", column=" + column +
                    ", row=" + row +
                    ", houseName='" + houseName + '\'' +
                    ", deviceName='" + deviceName + '\'' +
                    '}';
        }
    }

    public static class Label {

        String name;
        int imgRes;
        Object tag;

        public Label(String name, int imgRes, Object tag) {
            this.name = name;
            this.imgRes = imgRes;
            this.tag = tag;
        }
    }

    public static interface OnZoneClickListener {


        void onDeviceClick(DeviceList deviceList, Boolean select);

        void onHouseClick(RoomInfo roomInfo, boolean select);

        void onDeviceMulClick(DeviceType deviceType, boolean select);
    }
}
