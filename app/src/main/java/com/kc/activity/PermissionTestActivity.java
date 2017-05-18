package com.kc.activity;

import android.os.Bundle;

import com.kc.base.BaseActivity;
import com.kc.myasapp.R;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;
import com.zhy.m.permission.ShowRequestPermissionRationale;

public class PermissionTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_test);
    }

    @Override
    protected void initUI() {
        
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initData() {

    }

//    public void onBtClick(View view){
//        requestPermissions(Manifest.permission.READ_PHONE_STATE
//            , Manifest.permission.WRITE_EXTERNAL_STORAGE);
//    }

    @PermissionGrant(0)
    public void onGrant(){
        showToast("得到授权");
    }


    @PermissionDenied(0)
    public void onDenied(){
        showToast("未授权");
    }
    @ShowRequestPermissionRationale(0)
    public void onShowExplain(){
        showToast("弹出对话框|请先到设置->应用—>应用名->权限中打开***权限方可使用该功能。");
    }


}
