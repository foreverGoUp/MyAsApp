package com.kc.activity.scanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.kc.base.BaseScannerActivity;
import com.kc.myasapp.R;
import com.mylhyl.zxing.scanner.ScannerView;
import com.mylhyl.zxing.scanner.common.Scanner;
import com.mylhyl.zxing.scanner.decode.QRDecode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScannerActivity extends BaseScannerActivity implements TakePhoto.TakeResultListener, InvokeListener {
    public static final String KEY_SCAN_RESULT = "KEY_SCAN_RESULT";//

    @BindView(R.id.scannerView)
    ScannerView mScannerView;
    private TakePhoto mTakePhoto;
    private InvokeParam mInvokeParam;

    public static void actionStartForResult(FragmentActivity activity, int requestCode) {
        Intent intent = new Intent(activity, ScannerActivity.class);
//        intent.putExtra(AppConstant.KEY_ROOM_ID, curRoomId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        getTakePhoto().onCreate(savedInstanceState);
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

    @OnClick(R.id.bt_2)
    void clickBt2() {
        getTakePhoto().onPickFromGallery();
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (mTakePhoto == null) {
            mTakePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return mTakePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        Log.e(TAG, "takeSuccess: 获得照片成功：" + result.getImage().getOriginalPath());
        QRDecode.decodeQR(result.getImage().getOriginalPath(), this);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.e(TAG, "takeFail: 获得照片失败：" + msg);
    }

    @Override
    public void takeCancel() {
        Log.e(TAG, "takeCancel: 取消获得照片");
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.mInvokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, mInvokeParam, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getTakePhoto().onSaveInstanceState(outState);
    }
}
