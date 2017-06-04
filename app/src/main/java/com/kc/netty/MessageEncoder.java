package com.kc.netty;

import android.util.Log;

import java.net.SocketAddress;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * 发送的时候编码器
 *
 * @author clear
 */
public class MessageEncoder extends MessageToMessageEncoder<String> {

    private static final String TAG = "MessageEncoder";

//    private static MessageEncoder INSTANCE;
//    private MessageEncoder() {
//    }
//    public static MessageEncoder getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new MessageEncoder();
//        }
//        return INSTANCE;
//    }

    @Override
    protected void encode(ChannelHandlerContext arg0,
                          String msg, List<Object> out) throws Exception {
        //将数据转成buf
        ByteBuf arg2 = Unpooled.buffer();
        arg2.writeInt(msg.getBytes().length);
        arg2.writeBytes(msg.getBytes());
        out.add(arg2);
        Log.e(TAG, "发送至服务端msg=" + msg);
//		System.err.println("Server发送信息");
    }

    @Override
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        Log.v(TAG, "Server   acceptOutboundMessage");
        return super.acceptOutboundMessage(msg);
    }

    @Override
    public void write(ChannelHandlerContext arg0, Object arg1,
                      ChannelPromise arg2) throws Exception {
        super.write(arg0, arg1, arg2);
        Log.v(TAG, "Server  write");
    }

    @Override
    public void connect(ChannelHandlerContext ctx,
                        SocketAddress remoteAddress, SocketAddress localAddress,
                        ChannelPromise promise) throws Exception {
        super.connect(ctx, remoteAddress, localAddress, promise);
        Log.v(TAG, "Server  connect");
    }

    @Override
    public void read(ChannelHandlerContext ctx)
            throws Exception {
        super.read(ctx);
        Log.v(TAG, "Server  read");
    }
}
