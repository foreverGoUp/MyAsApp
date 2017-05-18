package com.kc.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ckc on 2016/11/30.
 */
public class SPreferencesHelper {

    //不同命名则可以建立多个SharedPreferences表
    private static final String mPreferencesName = "SHPreferences";

    public static void saveString(String key, String value) {
        SharedPreferences sP = JYApplication.getContext().getSharedPreferences(mPreferencesName, Context.MODE_PRIVATE);
        if (sP != null) {
            SharedPreferences.Editor editor = sP.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static String getString(String key) {
        SharedPreferences sP = JYApplication.getContext().getSharedPreferences(mPreferencesName, Context.MODE_PRIVATE);
        if (sP != null) {
            return sP.getString(key, null);
        } else {
            return null;
        }
    }

    public static void saveBool(String key, boolean value) {
        SharedPreferences sP = JYApplication.getContext().getSharedPreferences(mPreferencesName, Context.MODE_PRIVATE);
        if (sP != null) {
            SharedPreferences.Editor editor = sP.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public static boolean getBool(String key) {
        SharedPreferences sP = JYApplication.getContext().getSharedPreferences(mPreferencesName, Context.MODE_PRIVATE);
        if (sP != null) {
            return sP.getBoolean(key, false);
        } else {
            return false;
        }
    }

    public static void saveInt(String key, int value) {
        SharedPreferences sP = JYApplication.getContext().getSharedPreferences(mPreferencesName, Context.MODE_PRIVATE);
        if (sP != null) {
            SharedPreferences.Editor editor = sP.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public static int getInt(String key) {
        SharedPreferences sP = JYApplication.getContext().getSharedPreferences(mPreferencesName, Context.MODE_PRIVATE);
        if (sP != null) {
            return sP.getInt(key, -404);
        } else {
            return -404;
        }
    }

    public static void clear() {
//        SharedPreferences mSp = JYApplication.getContext().getSharedPreferences(mPreferencesName, Context.MODE_PRIVATE);
//        if (mSp != null) {
//            SharedPreferences.Editor mEditor = mSp.edit();
//            mEditor = mSp.edit();
//            mEditor.clear();
//            mEditor.commit();
//            mEditor = null;
//        }
    }
}
