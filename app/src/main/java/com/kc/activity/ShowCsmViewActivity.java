package com.kc.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.kc.base.BaseActivity;
import com.kc.custom.CsmDialView;
import com.kc.custom.CsmRadarView;
import com.kc.myasapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShowCsmViewActivity extends BaseActivity implements CsmDialView.OnCsmDialViewColorChanged
        , View.OnClickListener, CsmRadarView.OnCsmRadarViewClickListener {

    private static final String TAG = "ShowCsmViewActivity";

    private LinearLayout mContainer;
    private CsmRadarView mCsmRadarView;
    private List<CsmRadarView.RoomInfo> mRoomInfos = new ArrayList<>(8);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_csm_view);
        initUI();
        init();
    }

    @Override
    protected void initUI() {
        mContainer = (LinearLayout) findViewById(R.id.llayout_show_csm_view_container);
        //仿仪表盘
        CsmDialView view = (CsmDialView) findViewById(R.id.csmDialView);
        view.setOnClickListener(this);
        view.setListener(this);
        //雷达图
        mCsmRadarView = (CsmRadarView) findViewById(R.id.csmRadarView);
        mCsmRadarView.setListener(this);
        mCsmRadarView.setOnClickListener(this);

        //以下格式中的2表示字符串至少占2位，若只要一位则空格补前
//        String format = "%2d";
//        for(int i = 90; i < 123; i++)
//            Log.d(TAG, String.format(format, i + 1));

//        float cos = (float) Math.cos(-Math.PI / 3);
//        Log.d(TAG, "!!!!cos=" + cos);
    }

    @Override
    protected void init() {
        //雷达图
        for (int i = 0; i < 8; i++) {
            if (i == 3) {
                mRoomInfos.add(new CsmRadarView.RoomInfo(i + 4, "Really Room Kid" + i));
            } else {
                mRoomInfos.add(new CsmRadarView.RoomInfo(i + 4, "真实房间房间" + i));
            }
        }
        mCsmRadarView.setRoomInfos(mRoomInfos);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onCsmDialViewColorChanged(int red, int green) {
        mContainer.setBackgroundColor(Color.argb(100, red, green, 0));
    }

    private int count = 0;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.csmDialView:
                ((CsmDialView) view).startAnimation(250);
                test();
                break;
            case R.id.csmButton:
                count++;
                showToast("点击了," + count);
                break;
            case R.id.csmRadarView:
                Random random = new Random();
                int nextInt = random.nextInt(8);
                int roomId = nextInt + 4;
                for (CsmRadarView.RoomInfo roomInfo : mRoomInfos) {
                    if (roomInfo.getRoomId() == roomId) {
                        mCsmRadarView.updateRoomName(roomInfo.getRoomId(), "真实真实真实" + roomId);
                        mCsmRadarView.updateRoomScore(roomInfo.getRoomId(), roomId * 10);
                    }
                }
                break;
        }
    }

    private void test() {
        for (int i = 0; i < 9; i++) {
            Log.d(TAG, "test: i=" + i);
            test(i);
        }
    }

    private void test(int i) {
        Log.d(TAG, "ano test: i=" + i);
    }

    @Override
    public void onCsmRadarViewClick(int pos, int roomId) {
        showToast("点击了pos=" + pos);
    }
}
