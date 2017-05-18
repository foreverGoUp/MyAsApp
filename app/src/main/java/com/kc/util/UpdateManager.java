package com.kc.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.jy.R;
import com.jy.bean.ApkUpdateInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManager {

    private static final String TAG = "UpdateManager";
    private Context mContext;
    HttpURLConnection httpConnection;
    //提示语

    private String updateMsg = "有最新的软件包哦，亲快下载吧~";

    //返回的安装包url
    private String apkUrl = "http://139.196.52.159/downloadApk.php?act=downloadApk&app=SmartMiniPhone";

    private URL url;
    private Dialog noticeDialog;

    private Dialog downloadDialog;
    /* 下载包安装路径 */

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;


    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private Thread mDownLoadThread;

    private boolean mIntercepted = false;

    private String[] mAppInfoArr;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    //外部接口让主Activity调用
    public void checkUpdateInfo() {
        showNoticeDialog();
    }


    /**
     * 获得版本号
     */
    public int getVerCode() {
        int verCode = -1;
        try {
            verCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e("版本号获取异常", e.getMessage());
        }
        return verCode;
    }

    /**
     * 获得版本名称
     */
    public String getVerName() {
        String verName = "";
        try {
            verName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("版本名称获取异常", e.getMessage());
        }
        return verName;
    }

    public static ApkUpdateInfo sApkUpdateInfo;

    /**
     * 从服务器端获得版本号与版本名称
     *
     * @return
     */
    public boolean getServerVer(OnApkUpdateListener l) {
        mListener = l;
        try {
            url = new URL("http://139.196.52.159/downloadApk.php?act=version&app=SmartMiniPhone");
            new Thread() {
                @Override
                public void run() {
                    try {
                        httpConnection = (HttpURLConnection) url.openConnection();
                        httpConnection.setDoInput(true);
                        httpConnection.setDoOutput(true);
                        httpConnection.setRequestMethod("GET");
                        httpConnection.setConnectTimeout(500);
                        httpConnection.setReadTimeout(500);
                        httpConnection.connect();
                        InputStreamReader reader = new InputStreamReader(httpConnection.getInputStream());
                        BufferedReader bReader = new BufferedReader(reader);
                        String json = bReader.readLine();

                        ApkUpdateInfo info = JSON.parseObject(json, ApkUpdateInfo.class);
                        String verName = info.getVersionName();
                        Log.e(TAG, "从服务端获取版本json数据:" + json);
                        Log.e(TAG, "从服务端获取版本号:" + info.getVersionName());
                        //本地版本号
                        mAppInfoArr = AppUtils.getAppInfo(mContext);
                        if (mAppInfoArr == null) {
                            Log.e(TAG, "获取本地版本为空!!!通知取消更新");
                            cancelUpdate();
                            return;
                        }

                        if (!verName.equals(mAppInfoArr[1])) {
                            sApkUpdateInfo = info;
//                            PublicHandler.getInstant().getHandler().sendEmptyMessage(PublicHandler.HAVE_NEW_APK);
                            mSaveApkName = new StringBuffer().append(mContext.getResources().getString(R.string.app_name))
                                    .append("-").append(verName).append(".apk").toString();
                            Log.e(TAG, "新apk名称:" + mSaveApkName);

                            if (mListener != null) {
                                mListener.onApkUpdate(info);
                            }
                        } else {
                            sApkUpdateInfo = null;
                            Log.e(TAG, "版本一致,通知取消更新");
                            if (mListener != null) {
                                mListener.onApkUpdate(null);
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "1从服务端获取版本号异常!!!,通知取消更新");
                        e.printStackTrace();
                        cancelUpdate();
                    }
                }
            }.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "2从服务端获取版本号异常!!!,通知取消更新");
            e.printStackTrace();
            cancelUpdate();
            return false;
        }
        return true;
    }

    private void cancelUpdate() {
        if (mListener != null) {
            mListener.onCancleUpdateApk();
        }
    }

    public void showNoticeDialog() {
        Builder builder = new Builder(mContext);
        String apkUpdate = JYApplication.getResString(R.string.apk_update);
        builder.setTitle(apkUpdate);
        String resString = JYApplication.getResString(R.string.swpakcet_quick_downl);
        builder.setMessage(resString);
        String download = JYApplication.getResString(R.string.download);
        builder.setPositiveButton(download, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        String afterTalk = JYApplication.getResString(R.string.after_talk);
        builder.setNegativeButton(afterTalk, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelUpdate();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.setCanceledOnTouchOutside(false);
        noticeDialog.show();
    }

    private void showDownloadDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle(JYApplication.getResString(R.string.apk_update));
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        builder.setView(v);
        builder.setNegativeButton(JYApplication.getResString(R.string.common_tv_no), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelUpdate();
                mIntercepted = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.show();

        downloadApk();
    }


    private String mSaveApkPath;
    private String mSaveApkName;
    private Runnable mDownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mIntercepted = false;
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                com.jy.util.Log.e(TAG, "开始下载,新apk大小（字节）：" + length);
                InputStream is = conn.getInputStream();

                mSaveApkPath = FileUtil.getFilePath(FileUtil.DIR_APP_APK, mSaveApkName);
                FileUtil.createFilePath(mSaveApkPath);
                FileOutputStream fos = new FileOutputStream(new File(mSaveApkPath), false);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!mIntercepted);//点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                Log.e(TAG, "1下载出现异常!!!");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, "2下载出现异常!!!");
                e.printStackTrace();
            }
        }
    };

    /**
     * 下载apk
     *
     * @param
     */

    private void downloadApk() {
        mDownLoadThread = new Thread(mDownApkRunnable);
        mDownLoadThread.start();
    }

    /**
     * 安装apk
     *
     * @param
     */
    private void installApk() {
        File f = new File(mSaveApkPath);
        if (!f.exists()) {
            Log.e(TAG, "new apk not exist!!!");
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + f.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }

    public interface OnApkUpdateListener {
        void onApkUpdate(ApkUpdateInfo info);

        void onCancleUpdateApk();
    }

    private OnApkUpdateListener mListener;

    public void clear() {
        mContext = null;
        mListener = null;
    }
}
