package com.kc.base;

import android.app.Application;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/1/15.
 */
public class MyApp extends Application {


    private static MyApp mMyApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mMyApp = this;
    }

    public static Application getApplication(){
        return mMyApp;
    }

    public static void showToast(String content){
        if (content.length()<10){
            Toast.makeText(mMyApp, content, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mMyApp, content, Toast.LENGTH_LONG).show();
        }
    }

}
