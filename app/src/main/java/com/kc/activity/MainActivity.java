package com.kc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kc.activity.scanner.ScannerActivity;
import com.kc.myasapp.R;
import com.kc.tool.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_main_aty_1)
    TextView mTv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (AppConstant.RQ_1 == requestCode) {
                mTv1.setText("扫描结果：" + data.getStringExtra(AppConstant.KEY_DEVICE_ID));
            }
        }
    }

    @OnClick(R.id.bt_1)
    void clickBt1() {
        ScannerActivity.actionStartForResult(this, AppConstant.RQ_1);
    }
}
