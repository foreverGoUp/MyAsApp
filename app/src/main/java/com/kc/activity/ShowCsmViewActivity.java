package com.kc.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.kc.custom.CsmDialView;
import com.kc.custom.CsmRadarView;
import com.kc.myasapp.R;

public class ShowCsmViewActivity extends AppCompatActivity implements CsmDialView.OnCsmDialViewColorChanged
        , View.OnClickListener {

    private static final String TAG = "ShowCsmViewActivity";

    private LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_csm_view);
        Log.d(TAG, "onCreate: dsd");

        mContainer = (LinearLayout) findViewById(R.id.llayout_show_csm_view_container);
        //仿仪表盘
        CsmDialView view = (CsmDialView) findViewById(R.id.csmDialView);
        view.setOnClickListener(this);
        view.setListener(this);
        //雷达图
        CsmRadarView csmRadarView = (CsmRadarView) findViewById(R.id.csmRadarView);

        //以下格式中的2表示字符串至少占2位，若只要一位则空格补前
//        String format = "%2d";
//        for(int i = 90; i < 123; i++)
//            Log.d(TAG, String.format(format, i + 1));

        float cos = (float) Math.cos(-Math.PI / 3);
        Log.d(TAG, "!!!!cos=" + cos);
    }

    @Override
    public void onCsmDialViewColorChanged(int red, int green) {
        mContainer.setBackgroundColor(Color.argb(100, red, green, 0));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.csmDialView:
                ((CsmDialView) view).startAnimation(250);
                test();
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

}
