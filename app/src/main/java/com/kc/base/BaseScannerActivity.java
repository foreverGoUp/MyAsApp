package com.kc.base;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;

/**
 * Created by Administrator on 2017/9/4.
 */

public abstract class BaseScannerActivity extends AppCompatActivity implements OnScannerCompletionListener {

    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void OnScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {

    }
}
