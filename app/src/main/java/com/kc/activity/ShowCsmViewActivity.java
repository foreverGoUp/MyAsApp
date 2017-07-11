package com.kc.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.kc.custom.CsmDialView;
import com.kc.custom.CsmRadarView;
import com.kc.myasapp.R;

public class ShowCsmViewActivity extends AppCompatActivity implements CsmDialView.OnCsmDialViewColorChanged
        , View.OnClickListener {

    private LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_csm_view);

        mContainer = (LinearLayout) findViewById(R.id.llayout_show_csm_view_container);
        //仿仪表盘
        CsmDialView view = (CsmDialView) findViewById(R.id.csmDialView);
        view.setListener(this);
        view.setOnClickListener(this);
        //雷达图
        CsmRadarView csmRadarView = (CsmRadarView) findViewById(R.id.csmRadarView);
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
                break;
        }
    }
}
