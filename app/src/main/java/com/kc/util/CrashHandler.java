package com.kc.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements UncaughtExceptionHandler {

    private static String TAG = "CrashHandler";
    // 系统默认的UncaughtException处理类  
    private UncaughtExceptionHandler mDefaultHandler;

    private static CrashHandler instance = new CrashHandler();
    private Context mContext;

    // 用来存储设备信息和异常信息  
    private Map<String, String> mInfoMap = new HashMap<String, String>();

    // 用于格式化日期,作为日志文件名的一部分  
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private String mCrashFileName;
//    private String mCrashFilePath;  


    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
        //用来测试的异常
//    	 new Handler().postDelayed(new Runnable() {
// 			
// 			@Override
// 			public void run() {
// 				// TODO Auto-generated method stub
// 				throw new IllegalStateException("test");
// 			}
// 		}, 3000);
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器  
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理  
            Log.e(TAG, "!!!!!!!执行了系统默认的异常处理器");
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            //延迟等待邮件发送完毕
            Log.e(TAG, "退出应用前...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "退出应用中...");
            MyApp.exitApp();
        }
    }


    // 重启应用
//    public void restartApp() { 
//        Intent intent = new Intent(); 
//        // 参数1：包名，参数2：程序入口的activity 
//        intent.setClassName(JYApplication.getContext(), "com.jy.activity.WelcomeActivity2"); 
//        PendingIntent restartIntent = PendingIntent.getActivity( 
//                JYApplication.getContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK); 
//        AlarmManager mgr = (AlarmManager) JYApplication.getContext().getSystemService(Context.ALARM_SERVICE); 
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, 
//        restartIntent); // 1秒钟后重启应用 
////        finishProgram(); // 自定义方法，关闭当前打开的所有avtivity 
//    } 

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息; 否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null)
            return false;

        try {
            // 使用Toast来显示异常信息  
//            new Thread() {
//
//                @Override
//                public void run() {
//                    Looper.prepare();
//                    Toast.makeText(mContext, "很抱歉,程序出现异常",
//                            Toast.LENGTH_LONG).show();
//                    Looper.loop();
//                }
//            }.start();
//            JYApplication.showToast("很抱歉,程序出现异常");
            // 收集设备参数信息  
            collectDeviceInfo(mContext);
            // 保存日志文件  
            mCrashFileName = saveCrashInfoFile(ex);
            sendMail();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    private void sendMail() {
        Log.writeCacheLog();
        String filePath = FileUtil.getFilePath(FileUtil.DIR_APP_CRASH, mCrashFileName);
        if (FileUtil.existFile(FileUtil.DIR_APP_CRASH, mCrashFileName)) {
            String[] fPaths = new String[2];
            fPaths[0] = filePath;
            fPaths[1] = FileUtil.getFilePath(FileUtil.DIR_APP_LOGOUT, FileUtil.getLogoutFileName());
//            String subject = String.format(JYApplication.getContext().getResString(R.string.crash_mail_subject)
//                    , JYApplication.getContext().getResString(R.string.app_name));
//            Mail.send(true, subject, fPaths);
        } else {
            Log.e(TAG, "发送邮件失败，文件不存在...");
        }
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName + "";
                String versionCode = pi.versionCode + "";
                mInfoMap.put("versionName", versionName);
                mInfoMap.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info");
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfoMap.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info");
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     * @throws Exception
     */
    private String saveCrashInfoFile(Throwable ex) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String date = sDateFormat.format(new Date());
            sb.append("\r\n" + date + "\n");
            for (Map.Entry<String, String> entry : mInfoMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            Log.e(TAG, result);
            sb.append(result);

        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...");
            sb.append("an error occured while writing file...\r\n");
        }
        String fileName = "crash-" + mFormatter.format(new Date()) + ".log";
        FileUtil.write(FileUtil.DIR_APP_CRASH, fileName, sb.toString(), false);
        return fileName;
    }

//    private void sendMail() {
//        new Thread(new Runnable() {
//            
//            @Override
//            public void run() {
//                send();
//            }
//        }).start();
//    }

//    private void send() {
//        Log.e(TAG, "准备发送邮件");
//        Mail mail = new Mail("successfulpeter@163.com", "Kcinwyyx1");
//        mail.setTo(new String[] { "successfulpeter@163.com" });
//        mail.setSubject("shp服务端异常报告");
//        mail.setBody("请看附件~");
//        mail.setFrom("successfulpeter@163.com");
//        try {
//            mail.addAttachment(FileUtil.getFilePath(FileUtil.DIR_APP_CRASH, mCrashFileName));
//        } catch (Exception e1) {
//            // TODO Auto-generated catch block
//            Log.e(TAG, "添加附件异常...");
//            e1.printStackTrace();
//            return;
//        }
//        try {
//            if (mail.send()) {
//                //删除本地已发送邮件
//                Log.e(TAG, "邮件发送成功！");
//            } else {
//                Log.e(TAG, "邮件发送失败！");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "邮件发送异常！");
//        }
//    }

}
