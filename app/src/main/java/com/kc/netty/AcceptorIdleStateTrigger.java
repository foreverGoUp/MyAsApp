package com.kc.netty;

import com.kc.tool.AppConstant;
import com.kc.util.Log;
import com.kc.util.MyApp;
import com.kc.util.NetStatus;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/*
* 单例类
*
* */
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private final String TAG = "AcceptorIdleStateTrigger";

    private StringBuffer mHeartBagSB = new StringBuffer();
    private boolean mReConnectOnce = false;//表示在自然掉线（有网）的情况下只触发重连一次。
    public static volatile boolean sConnectedServer = false;
    public static boolean sReConnect = true;

//    private ChannelHandlerContext mChannelHandlerContext;

//    private static AcceptorIdleStateTrigger INSTANCE;
//    private AcceptorIdleStateTrigger() {
//    }
//    public static AcceptorIdleStateTrigger getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new AcceptorIdleStateTrigger();
//        }
//        return INSTANCE;
//    }

    /*
    * 信道连接建立
    *
    * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.e(TAG, "channelActive---信道连接，全局信道变量赋值~");
        Log.e(TAG, "channelActive---Channel hashCode = " + ctx.channel().hashCode());
//        ctx.fireChannelActive();//传递信息到其他信道
        NettyUtil.sChannel = ctx.channel();
        mReConnectOnce = false;
        sConnectedServer = true;
        sReConnect = true;
    }

    /*
    * 信道断开自动重连一次，直至连接信道重新建立才恢复该机制。
    * 若一直没有建立，则需要在其他地方调用重连连接方法。
    * */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.e(TAG, "channelInactive---信道断开");
        ctx.close();
        //自动重连部分
        sConnectedServer = false;
        if (sReConnect && NetStatus.isNetworkAvailable(MyApp.getContext()) && !mReConnectOnce) {
            Log.e(TAG, "channelInactive：与服务端链路断开,每个ip将自动重连2次");
            NettyUtil.getInstance().doConnect();
            mReConnectOnce = true;
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        // TODO 自动生成的方法存根
        if (NettyUtil.sDebug) {
//        Log.e(TAG, "userEventTriggered");
            Log.e(TAG, "userEventTriggered---Channel hashCode = " + ctx.channel().hashCode());
        }
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                mHeartBagSB.setLength(0);
                mHeartBagSB.append("{\"code\":")
                        .append(AppConstant.CODE_HEART)
                        .append(",\"data\":")
                        .append(MyApp.heartBagData)
                        .append("}");
                ctx.writeAndFlush(mHeartBagSB.toString());
                Log.e("**********", "写超时，正在发送心跳包：" + mHeartBagSB.toString());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // TODO 自动生成的方法存根
        super.channelUnregistered(ctx);
        if (NettyUtil.sDebug) {
            Log.e(TAG, "channelUnregistered---信道断开");
            Log.e(TAG, "channelUnregistered---Channel hashCode = " + ctx.channel().hashCode());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // TODO 自动生成的方法存根
        ctx.close();
        super.exceptionCaught(ctx, cause);
        Log.e(TAG, "exceptionCaught");
        Log.e(TAG, "exceptionCaught---Channel hashCode = " + ctx.channel().hashCode());
    }

}
