package com.kc.netty;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;
import com.kc.tool.SubscriberManager;

/**
 * 公用Handler
 *
 * @author clear
 * @author ckc 2016/8/14 改成单例模式
 */
public class PublicHandler {

    private static final String TAG = "PublicHandler";
    private final String DATA = "data";

    public static final int CONNECT_SERVER_SUCCESS = 900;//自补充，连接服务端成功
    public static final int CONNECT_SERVER_FAILED = 901;//自补充，连接服务端失败
    public static final int NETWORK_VALID = 902;//自补充，网络已连接
    public static final int NETWORK_INVALID = 903;//自补充，网络断开
    public static final int NETWORK_INVALID_NOTICE = 904;//自补充，网络断开
    public static final int SEARCH_IP_SUCCESS = 905;//广播成功
    public static final int SEARCH_IP_FAILED = 906;//广播失败
    public static final int JUR_CHANGED = 907;//当前登陆用户权限改变
    public static final int JUR_NEED_PARENT_GRANT = 908;//陌生人登录
    public static final int UPLOAD_LOG_SCS = 910;//上传日志成功
    public static final int UPLOAD_LOG_FAIL = 911;//上传日志失败
    //    public static final int OPERATION_LIMITED = 914;//
    public static final int ATTEMPT_CONNECT_TO_SERVER = 917;//
    public static final int HAVE_NEW_APK = 999;//
    private SubscriberManager mSubManager = new SubscriberManager();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO 自动生成的方法存根
            super.handleMessage(msg);
            int code = msg.what;
            JSONObject json = (JSONObject) msg.obj;

            switch (code) {
//                case ATTEMPT_CONNECT_TO_SERVER:
//                case NETWORK_VALID:
//                case CONNECT_SERVER_SUCCESS:
//                case CONNECT_SERVER_FAILED:
//                case NETWORK_INVALID:
//                case SEARCH_IP_SUCCESS:
//                case SEARCH_IP_FAILED:
//                case HAVE_NEW_APK:
//                case UPLOAD_LOG_SCS:
//                case UPLOAD_LOG_FAIL:
//                    showOverAppNotification(code);
//                    break;
                default:
                    showOverAppNotification(code);
                    break;
            }

        }
    };

    /**
     * 实现设备更新接口
     */
    public void setInterface(Object obj) {
        mSubManager.subscribe(obj);
    }

    public void clearInterface(Object obj) {
        mSubManager.releaseSub(obj);
    }

    private void showOverAppNotification(int code) {
//        if (mINotificationListener != null) {
//            mINotificationListener.notice(code);
//        }
//	    new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO 自动生成的方法存根
//
//
//
//            }
//        }, 5000);
    }


    // TODO 添加实现

    private static final PublicHandler instance = new PublicHandler();

    public static PublicHandler getInstant() {
        return instance;
    }

    public Handler getHandler() {
        return mHandler;
    }


}
