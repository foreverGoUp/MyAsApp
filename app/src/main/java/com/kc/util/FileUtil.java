package com.kc.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
 * 从系统api获得的sd卡目录的尾巴无"/"。
 * 
 * 约定：
 * 目录的形式如：abc/
 * 路径的形式如：abc/a.txt
 * 
 * */

public class FileUtil {

    private static final String TAG = "FileUtil";
    //固定目录值
    public static final String DIR_APP = "SmartMiniPhone/";
    public static final String DIR_APP_APK = DIR_APP + "apk/";
    public static final String DIR_APP_LOGOUT = DIR_APP + "logout/";
    public static final String DIR_APP_CRASH = DIR_APP + "crash/";
    public static final String DIR_APP_SCREENSHOT = DIR_APP + "ScreenShot/";
    public static final String DIR_APP_IMAGE_HEAD = DIR_APP + "Image/Head/";
    //固定目录标识
    public static final int DIR_FLAG_DOWNLOADS = 0;
    //固定常用文件名称
    public static final String FILE_NAME_CONFIG = "config.log";
    public static final String FILE_NAME_README = null;
    public static final String FILE_NAME_LOGOUT = "logout.log";
    //常用后缀
    private static final String SUFFIX_TXT = ".log";
    //
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    /*
     * 保存截图
     * */
    public static boolean saveScreenshot(String fileName, Bitmap bmp) {
        String filePath = getFilePath(DIR_APP_SCREENSHOT, fileName);
        if (filePath == null) {
            Log.e(TAG, "保存截图失败，存储不可用...");
            return false;
        }

        File file = new File(filePath);
//        if (!file.exists()) {
//            if (!createFilePath(filePath)) {
//                return false;
//            }
//        }
        try {
            FileOutputStream os = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e(TAG, "保存截图失败，发生异常...");
            return false;
        }
        return true;
    }

    public static void createDefaultConfigFile(String dir, String fileName, String content, boolean cover) {
        String filePath = getFilePath(dir, fileName);
        if (new File(filePath).exists()) {
            return;
        }
        write(filePath, content, false);
    }

    /*
     * 写文件
     * */
    public static void writeToLogoutFile(String content) {
        write(DIR_APP_LOGOUT, getLogoutFileName(), content, true);
    }

    /*
     * 写文件
     * */
    public static void write(String dir, String fileName, String content, boolean append) {
        String filePath = getFilePath(dir, fileName);
        write(filePath, content, append);
    }

    /*
     * 写文件
     * */
    public static void write(int dirFlag, String dir, String fileName, String content, boolean append) {
        String filePath = getFilePath(dirFlag, dir, fileName);
        write(filePath, content, append);
    }

    /*
     * 内部写文件
     * */
    private static void write(String filePath, String content, boolean append) {
        if (filePath == null) {
            Log.e(TAG, "write-获得文件路径失败...");
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            if (!createFilePath(filePath)) {
                return;
            }
        }

        FileOutputStream fout = null;
        try {
            // true表示追加写入
            fout = new FileOutputStream(file, append);
            byte[] bytes = content.getBytes();
            fout.write(bytes);
        } catch (Exception e) {
            Log.e(TAG, "write-写入文件异常...");
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "write-关闭输出流异常...");
                e.printStackTrace();
            }
        }
    }

    public static String read(String dir, String fileName) {
        String filePath = getFilePath(dir, fileName);
        if (filePath == null) {
            Log.e(TAG, "read-获得文件路径失败...");
            return null;
        }
        if (!new File(filePath).exists()) {
            Log.e(TAG, "read-文件不存在...");
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer = stringBuffer.append(line);
            }

            reader.close();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, "read-文件读取异常...");
            e.printStackTrace();
            return null;
        }

        return stringBuffer.toString();
    }

    /*
     * 获取 手机文件路径
     * @param direc 目录，如果为null表示app根目录，否则为app根目录中的其他目录。
     * @param fileName 可能为null
     * 
     * */
    public static String getFilePath(String dir, String fileName) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        StringBuffer path = new StringBuffer();
        path.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        path.append(File.separator);
        if (dir != null) {
            path.append(dir);
        } else {
            path.append(DIR_APP);
        }
        if (fileName != null) {
            path.append(fileName);
        }

        return path.toString();
    }

    /*
     * 用来获得sd卡本身具有的存储特定文件的文件路径，如下载、图片、音乐目录中的文件路径
     * 
     * */
    public static String getFilePath(int dirFlag, String dir, String fileName) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        StringBuffer path = new StringBuffer();
        switch (dirFlag) {
            case DIR_FLAG_DOWNLOADS:
                path.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                break;

            default:
                //默认为sd卡的下载目录
                path.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                break;
        }

        path.append(File.separator);
        //dir有值如：abc/，表示在sd卡下载目录中还有一层文件夹
        if (dir != null) {
            path.append(dir).append(fileName);
        } else {
            path.append(fileName);
        }

        return path.toString();
    }

    /**
     * 获取SDCard的目录路径功能
     *
     * @return
     */
    private static String getSDCardPath() {
        String sdcardDir = null;
        // 判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return sdcardDir;
    }

    public static void printFileByLine(String filePath) {
        if (!new File(filePath).exists()) {
            return;
        }
        try {
            FileInputStream inputStream = new FileInputStream("text.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d(TAG, "printFileByLine-\n" + line);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    // 一定时间内清理日志
    // long intervalHour =
    // TimeUtils.getIntervalTime(AppConstants.lastClearLogMillSecTime,
    // System.currentTimeMillis(), TimeUtils.UNIT_HOUR);
    // if (intervalHour >= AppConstants.TIME_CLEAR_LOG_HOUR) {
    // AppConstants.lastClearLogMillSecTime = System.currentTimeMillis();
    // FileUtil.writeFileToDirDownload(FileUtil.DIR_OTHER_LOG, "", false);
    // }

    private static String[] getDate() {
        String[] ret = new String[3];
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
        dateFormat.applyPattern("yyyy");
        ret[0] = dateFormat.format(date);
        dateFormat.applyPattern("MM");
        ret[1] = dateFormat.format(date);
        dateFormat.applyPattern("dd");
        ret[2] = dateFormat.format(date);
        return ret;
    }

    /**
     * 根据文件路径 递归创建文件
     *
     * @param
     */
    public static boolean createFilePath(String filePath) {
        String fileDir = filePath.substring(0, filePath.lastIndexOf("/"));
        File file = new File(filePath);
        File parentFile = new File(fileDir);
        if (!file.exists()) {
            parentFile.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "createFilePath-创建文件路径失败...：" + filePath);
                return false;
            }
        }
        return true;
    }

    /*
     * 删除文件
     * */
    public static boolean deleteFile(String dir, String fileName) {
        String filePath = getFilePath(dir, fileName);
        if (filePath == null) {
            Log.e(TAG, "deleteFile-删除文件失败,文件路径不存在...");
            return false;
        }
        return deleteFile(filePath);
    }

    /*
     * 删除文件
     * */
    public static boolean deleteFile(int dirFlag, String dir, String fileName) {
        String filePath = getFilePath(dirFlag, dir, fileName);
        if (filePath == null) {
            Log.e(TAG, "deleteFile-删除文件失败,文件路径不存在...");
            return false;
        }

        return deleteFile(filePath);
    }

    /*
     * 内部删除文件
     * */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return true;
    }

    /*
     * 判断文件存在
     * */
    public static boolean existFile(String dir, String fileName) {
        String filePath = getFilePath(dir, fileName);
        return existFile(filePath);
    }

    /*
     * 判断文件存在
     * */
    public static boolean existFile(int dirFlag, String dir, String fileName) {
        String filePath = getFilePath(dirFlag, dir, fileName);
        return existFile(filePath);
    }

    /*
     * 内部判断文件存在
     * */
    private static boolean existFile(String filePath) {
        if (filePath == null) {
            return false;
        }
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 获得以日期命名的日志文件名称
     * 如:logout-20170412.log
     * */
    public static String getLogoutFileName() {
        StringBuffer sb = new StringBuffer(25);
        String dateArr[] = getDate();
        return sb.append("logout-").append(dateArr[0])
                .append(dateArr[1]).append(dateArr[2]).append(".log").toString();
    }


    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        // 如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        // 遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                // 删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                // 删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前空目录
        return dirFile.delete();
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param filePath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }
}
