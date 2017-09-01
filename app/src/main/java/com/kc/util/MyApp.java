package com.kc.util;

import android.app.Application;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;

/**
 * Created by Administrator on 2017/1/15.
 */
public class MyApp extends Application {
    public static Integer heartBagData;// 心跳包的值

    private static MyApp mMyApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mMyApp = this;
        Utils.init(this);
    }

    public static Application getContext() {
        return mMyApp;
    }

    public static void showToast(String content){
        if (content.length()<10){
            Toast.makeText(mMyApp, content, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mMyApp, content, Toast.LENGTH_LONG).show();
        }
    }

    public static void exitApp() {

    }
}
