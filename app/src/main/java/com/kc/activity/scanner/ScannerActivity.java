package com.kc.activity.scanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.kc.base.BaseScannerActivity;
import com.kc.myasapp.R;
import com.mylhyl.zxing.scanner.ScannerView;
import com.mylhyl.zxing.scanner.common.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScannerActivity extends BaseScannerActivity {
    public static final String KEY_SCAN_RESULT = "KEY_SCAN_RESULT";//

    @BindView(R.id.scannerView)
    ScannerView mScannerView;

    public static void actionStartForResult(FragmentActivity activity, int requestCode) {
        Intent intent = new Intent(activity, ScannerActivity.class);
//        intent.putExtra(AppConstant.KEY_ROOM_ID, curRoomId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        initUI();
        init();
    }

    protected void initUI() {
        ButterKnife.bind(this);
        mScannerView.setOnScannerCompletionListener(this);
    }

    protected void init() {
//        mScannerView.setMediaResId(R.raw.beep);//设置扫描成功的声音
        mScannerView.setDrawText("将二维码放入框内", true);
        mScannerView.setDrawTextColor(Color.RED);
        //二维码
        mScannerView.setScanMode(Scanner.ScanMode.QR_CODE_MODE);
        //显示扫描成功后的缩略图
        mScannerView.isShowResThumbnail(false);
        //线图
        mScannerView.setLaserLineResId(R.drawable.wx_scan_line);
        //网格图
//        mScannerView.setLaserGridLineResId(R.drawable.zfb_grid_scan_line);
//        mScannerView.setLaserFrameBoundColor(0xFF26CEFF);//支付宝颜色
        //
        mScannerView.setLaserColor(Color.RED);
    }

    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.onPause();
    }

    @Override
    public void OnScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
        super.OnScannerCompletion(rawResult, parsedResult, barcode);
        Log.e(TAG, "OnScannerCompletion: rawResult" + rawResult);
        String text = rawResult.getText();
        Log.e(TAG, "OnScannerCompletion: text=" + text);
        if (rawResult == null) {
            setResult(Activity.RESULT_FIRST_USER);
        } else {
            Intent intent = new Intent();
            intent.putExtra(KEY_SCAN_RESULT, text);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }
}
