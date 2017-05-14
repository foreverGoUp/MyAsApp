package com.kc.base;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.ShowRequestPermissionRationale;

/**
 * Created by Administrator on 2016/12/24.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected void showToast(String content){
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    protected void requestPermissions(String... permissions){
        if (!shouldShowRequestPermissionRationale(this, 0, permissions)){
            MPermissions.requestPermissions(this, 0, permissions);
        }
    }

    public boolean shouldShowRequestPermissionRationale(AppCompatActivity activity, int requestCode, String... permissions){
        for (int i = 0; i < permissions.length; i++) {
            if (MPermissions.shouldShowRequestPermissionRationale(activity
                    , permissions[i], requestCode)){
                return true;
            }
        }
        return false;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
