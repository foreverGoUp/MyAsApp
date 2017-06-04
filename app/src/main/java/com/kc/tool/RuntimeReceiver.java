package com.kc.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public class RuntimeReceiver extends BroadcastReceiver {

    public static boolean sNetWorkValid = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();

//        if (Intent.ACTION_SCREEN_ON.equals(action)) {
//            // startService(new
//            // Intent(context,SendCommandService.class));
//        } 
//        else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
//            // stopService(new Intent(context,
//            // SendCommandService.class));
//        }
//      else if (Intent.ACTION_USER_PRESENT.equals(action)) {
//          Log.e(TAG, "screen unlock");
//      }
//      else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent
//              .getAction())) {
//          Log.e(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
//      }
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            State wifiState = null;
            State mobileState = null;
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
                mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            }
            if (wifiState != null && State.CONNECTED == wifiState
                    || mobileState != null && State.CONNECTED == mobileState) {
                // 网络连接成功
                if (!sNetWorkValid) {
                    sNetWorkValid = true;
//                    PublicHandler.getInstant().getHandler().sendEmptyMessage(PublicHandler.NETWORK_VALID);
                }
            } else {
                // 无网络连接
                if (sNetWorkValid) {
                    sNetWorkValid = false;
//                    PublicHandler.getInstant().getHandler().sendEmptyMessage(PublicHandler.NETWORK_INVALID);
                }
            }
        }
    }

}
