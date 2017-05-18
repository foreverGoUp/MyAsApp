package com.kc.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class AppUtils {

    private static final String TAG = "AppUtils";
    public static final String LANGUAGE_ZH_CN = "zh_ch";
    //    public static final String LANGUAGE_ZH_TW = "zh_tw";
    public static final String LANGUAGE_EN_US = "en_us";

    /**
     * 获得包名
     * 获得版本名称
     */
    public static String[] getAppInfo(Context context) {
        String[] arr = new String[2];
        try {
            arr[0] = context.getPackageName();
            arr[1] = context.getPackageManager().getPackageInfo(
                    arr[0], 0).versionName;
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean changeLanguage(Context context, String languageTag) {
        Locale locale = null;
        switch (languageTag) {
            case LANGUAGE_ZH_CN:
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case LANGUAGE_EN_US:
                locale = Locale.US;
                break;
            default:
                Locale.getDefault();
                break;
        }
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        Log.e(TAG, "当前语言环境：" + config.locale.getCountry() + "," + config.locale.getLanguage() + "," + config.locale.getDisplayCountry() + "," + config.locale.getDisplayLanguage());
        if (config.locale == locale) {
            Log.e(TAG, "重复设置app语言");
            return false;
        } else {
            config.locale = locale;
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }

        return true;
    }

    /**
     * 只支持简体中文和美国英语
     * <p/>
     * 当默认地区为中国和美国之外的国家，默认语言为美国英语
     */
    public static String getDefaultLanguage() {
        String languageType = LANGUAGE_EN_US;
        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();
        if (lang.equals(Locale.SIMPLIFIED_CHINESE.getLanguage())) {
            languageType = LANGUAGE_ZH_CN;
        }
        return languageType;
    }

}
