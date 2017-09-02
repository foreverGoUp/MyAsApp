package com.kc.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.kc.base.BaseActivity;
import com.kc.custom.CsmDialView;
import com.kc.custom.CsmRadarView;
import com.kc.custom.CsmSpiderWebView;
import com.kc.myasapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.kc.custom.CsmSpiderWebView.INDEX_TYPE_CLEAN;
import static com.kc.custom.CsmSpiderWebView.INDEX_TYPE_LIGHT;
import static com.kc.custom.CsmSpiderWebView.INDEX_TYPE_OXYGEN;
import static com.kc.custom.CsmSpiderWebView.INDEX_TYPE_TEMP;
import static com.kc.custom.CsmSpiderWebView.INDEX_TYPE_WET;

public class ShowCsmViewActivity extends BaseActivity implements CsmDialView.OnCsmDialViewColorChanged
        , View.OnClickListener, CsmRadarView.OnCsmRadarViewClickListener {

    private static final String TAG = "ShowCsmViewActivity";

    private RelativeLayout mContainer;
    private CsmRadarView mCsmRadarView;
    private CsmSpiderWebView mCsmSpiderWebView;
    private List<CsmRadarView.RoomInfo> mRoomInfos = new ArrayList<>(8);
    private List<CsmSpiderWebView.IndexInfo> mIndexInfos2 = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_csm_view);
        initUI();
        init();
    }

    @Override
    protected void initUI() {
        mContainer = (RelativeLayout) findViewById(R.id.llayout_show_csm_view_container);
        //仿仪表盘
        CsmDialView view = (CsmDialView) findViewById(R.id.csmDialView);
        view.setOnClickListener(this);
        view.setListener(this);
        //雷达图
        mCsmRadarView = (CsmRadarView) findViewById(R.id.csmRadarView);
        mCsmRadarView.setListener(this);
        mCsmRadarView.setOnClickListener(this);
        //蜘蛛网图
        mCsmSpiderWebView = (CsmSpiderWebView) findViewById(R.id.csmSpiderWebView);
        mCsmSpiderWebView.setOnClickListener(this);

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
        for (int i = 0; i < 5; i++) {
            if (i == 3) {
                mRoomInfos.add(new CsmRadarView.RoomInfo(i + 4, "Really Room Kid" + i));
            } else {
                mRoomInfos.add(new CsmRadarView.RoomInfo(i + 4, "真实房间房间" + i));
            }
        }
        mCsmRadarView.setRoomInfos(mRoomInfos);
        //蜘蛛网图
//        CsmSpiderWebView.IndexInfo indexInfo = null;
//        for (int i = 0; i < 5; i++) {
//            switch (i) {
//                case 0:
//                    indexInfo = new CsmSpiderWebView.IndexInfo(INDEX_TYPE_OXYGEN, "氧度");
//                    break;
//                case 1:
//                    indexInfo = new CsmSpiderWebView.IndexInfo(INDEX_TYPE_CLEAN, "净度");
//                    break;
//                case 2:
//                    indexInfo = new CsmSpiderWebView.IndexInfo(INDEX_TYPE_LIGHT, "光度");
//                    break;
//                case 3:
//                    indexInfo = new CsmSpiderWebView.IndexInfo(INDEX_TYPE_WET, "湿度");
//                    break;
//                case 4:
//                    indexInfo = new CsmSpiderWebView.IndexInfo(INDEX_TYPE_TEMP, "温度");
//                    break;
//            }
//            mIndexInfos2.add(indexInfo);
//        }
//        mCsmSpiderWebView.setIndexInfos(mIndexInfos2);
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
            case R.id.csmRadarView: {
                Random random = new Random();
                int nextInt = random.nextInt(8);
                int roomId = nextInt + 4;
                int score = random.nextInt(101);
                for (CsmRadarView.RoomInfo roomInfo : mRoomInfos) {
                    if (roomInfo.getRoomId() == roomId) {
                        mCsmRadarView.updateRoomName(roomInfo.getRoomId(), "房间" + score);
                        mCsmRadarView.updateRoomScore(roomInfo.getRoomId(), score);
                        break;
                    }
                }
                break;
            }
            case R.id.csmSpiderWebView: {
                Random random = new Random();
                int i = random.nextInt(5);
                int score = random.nextInt(101);
                String indexType = null;
                String name = null;
                switch (i) {
                    case 0:
                        indexType = INDEX_TYPE_OXYGEN;
                        name = "氧度:";
                        break;
                    case 1:
                        indexType = INDEX_TYPE_CLEAN;
                        name = "净度:";
                        break;
                    case 2:
                        indexType = INDEX_TYPE_LIGHT;
                        name = "光度:";
                        break;
                    case 3:
                        indexType = INDEX_TYPE_WET;
                        name = "湿度:";
                        break;
                    case 4:
                        indexType = INDEX_TYPE_TEMP;
                        name = "温度:";
                        break;
                }
                mCsmSpiderWebView.updateIndexName(indexType, name + score);
                mCsmSpiderWebView.updateIndexScore(indexType, score);
                break;
            }
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
