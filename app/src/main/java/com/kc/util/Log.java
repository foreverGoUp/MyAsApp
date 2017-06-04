package com.kc.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class Log {

    private static final String TAG = "Log";

    private static final boolean ENABLE_V = false;
    private static final boolean ENABLE_D = false;
    private static final boolean ENABLE_I = false;
    private static final boolean ENABLE_W = false;
    private static final boolean ENABLE_E = true;

    private static final int MAX_CACHE_COUNT = 30;//收集到最大数量的日志数后统一输出到文件中

    private static Vector<String> sLogToFileVector = new Vector();
    private static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static void v(String tag, String content) {
        if (ENABLE_V || ENABLE_D) {
            addLog(tag, content);
        }
        android.util.Log.v(tag, content);
    }

    public static void d(String tag, String content) {
        if (ENABLE_V || ENABLE_D) {
            addLog(tag, content);
        }
        android.util.Log.d(tag, content);
    }

    public static void i(String tag, String content) {
        if (ENABLE_V || ENABLE_D || ENABLE_I) {
            addLog(tag, content);
        }
        android.util.Log.i(tag, content);
    }

    public static void e(String tag, String content) {
        if (ENABLE_V || ENABLE_D || ENABLE_I || ENABLE_W || ENABLE_E) {
            addLog(tag, content);
        }
        android.util.Log.e(tag, content);
    }

    private static void addLog(String tag, String content) {
        sLogToFileVector.add("\n" + getCurrentTime() + " " + tag + " " + content);
        if (sLogToFileVector.size() > MAX_CACHE_COUNT) {
            FileUtil.writeToLogoutFile(sLogToFileVector.toString());
            sLogToFileVector.clear();
            //检查日志文件大小
            String filePath = FileUtil.getFilePath(FileUtil.DIR_APP_LOGOUT, FileUtil.getLogoutFileName());
            double logSize = FileSizeUtil.getFileOrFilesSize(filePath
                    , FileSizeUtil.SIZETYPE_MB);
            Log.e(TAG, "日志文件大小：" + logSize + "MB");
            if (logSize > 1) {
//				Mail.send(true, "大系统VR版日志", filePath);
                FileUtil.deleteFile(filePath);
                Log.e(TAG, "日志超过1MB，删除文件。");
            }
        }
    }

    public static void writeCacheLog() {
        FileUtil.writeToLogoutFile(sLogToFileVector.toString());
        sLogToFileVector.clear();
    }

    /*
     * 
     * 得到当前时间
     * */
    private static String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        return DEFAULT_SDF.format(date);
    }
}
