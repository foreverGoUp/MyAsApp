/**
 * ClientMulticastSocketTest.java
 * 版权所有(C) 2014
 * 创建者:cuiran 2014-1-9 下午3:24:25
 */
package com.kc.util;

import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientBroadcastSocket {

    private static final String TAG = "ClientBroadcastSocket";
    private Handler mHandler;

    private DatagramSocket mSendSocket;
    private DatagramSocket mReceiveSocket;
    private DatagramPacket mSendDPacket;
    private DatagramPacket mReceiveDPacket;
    private final String KEY = "jy9191";
    private final int SIZE_RECV = 50;
    private final int RECV_TIME_OUT_TIME = 200;//ms 

    private volatile boolean mBlSearching = false;

    //单例模式
    private static ClientBroadcastSocket mInstance;

    private ClientBroadcastSocket() {
//        mHandler = PublicHandler.getInstant().getHandler();
    }

    public static ClientBroadcastSocket getInstance() {
        if (mInstance == null) {
            mInstance = new ClientBroadcastSocket();
        }
        return mInstance;
    }

//    public void setHandler(Handler handler) {
//        mHandler = handler;
//    }

    public void search() {
        //如果正在搜索则结束再搜索
        if (mBlSearching) {
            Log.e(TAG, "正在广播中...");
            return;
        }
        //如果mHandler为空抛出异常
        if (mHandler == null) {
            throw new IllegalStateException("mHandler不能为空...");
        }

        mBlSearching = true;
        //启动线程来执行网络操作
        new Thread(new Runnable() {

            @Override
            public void run() {
                doSearch();
            }
        }).start();
    }

    private void doSearch() {
        try {
            if (mSendDPacket == null) {
                byte[] keyArr = KEY.getBytes();
                InetAddress inetAddress = InetAddress.getByName("255.255.255.255");
                mSendDPacket = new DatagramPacket(keyArr, keyArr.length, inetAddress, 10100);
            }

            //发送套接字
            mSendSocket = new DatagramSocket();
            //接收套接字
            mReceiveSocket = new DatagramSocket(10102);
            mReceiveSocket.setSoTimeout(RECV_TIME_OUT_TIME);
            //发送数据包
            mSendSocket.send(mSendDPacket);
        } catch (IOException e) {
            e.printStackTrace();
//            mHandler.sendEmptyMessage(PublicHandler.SEARCH_IP_FAILED);
            mBlSearching = false;
            return;
        }

        int i = 0;
        String hostAddress = null;
        while (i < 10) {
            i++;
            try {
                //接收数据包
                mReceiveDPacket = new DatagramPacket(new byte[SIZE_RECV], SIZE_RECV);
                mReceiveSocket.receive(mReceiveDPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String data = new String(mReceiveDPacket.getData()).trim();
            if (data.length() > 0 && data.contains(KEY)) {
//                    Log.e(TAG, "数据长度："+data.length() +"接收到的数据："+data);
                JSONObject jsonObject = JSONObject.parseObject(data);
                String localHomeId = jsonObject.getString(KEY);
//                if (localHomeId.equals(JYApplication.sHomeID)) {
//                    Log.e(TAG, "homeid一致");
//                    hostAddress = mReceiveDPacket.getAddress().getHostAddress();// 服务器局域网IP
//                    NettyUtil.getInstance().setLanIp(hostAddress);
//                    Log.e(TAG, "得到ip：" + hostAddress);
//                } else {
//                    Log.e(TAG, "homeid不同");
//                }
                break;
            }
        }

        if (hostAddress != null) {
            Log.e(TAG, "搜索ip成功:" + hostAddress);
//            Message msg = Message.obtain(mHandler, PublicHandler.SEARCH_IP_SUCCESS, hostAddress);
//            mHandler.sendMessage(msg);
        } else {
            Log.e(TAG, "搜索ip失败...");
//            mHandler.sendEmptyMessage(PublicHandler.SEARCH_IP_FAILED);
        }

        if (mSendSocket != null) {
            mSendSocket.close();
        }
        if (mReceiveSocket != null) {
            mReceiveSocket.close();
        }
        mBlSearching = false;
    }

}