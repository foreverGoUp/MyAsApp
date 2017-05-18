package com.kc.netty;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kc.util.Log;
import com.kc.util.MyApp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientUserHandler extends SimpleChannelInboundHandler<String> {
    private String TAG = "ClientUserHandler";
    private Handler handler = null;
    private JSONObject mJsonObject;
    private final String DATA = "data";

    public ClientUserHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg)
            throws Exception {
        if (msg != null && !msg.equals("")) {
            Log.e(TAG, msg);

            try {
                mJsonObject = JSONObject.parseObject(msg.toString());
            } catch (JSONException e) {
                Log.e(TAG, "从服务端来的数据解析成json异常！");
                return;
            }
            Integer code = mJsonObject.getIntValue("code");

            if (code == null) {
                return;
            }

            if (code == 0) {
                //过滤心跳包
                int value = mJsonObject.getIntValue("data");
                if (value != 0) {
                    Log.d(TAG, "存储心跳包：" + value);
                    MyApp.heartBagData = value;
                }

                Log.e(TAG, "msg = " + msg);
                return;
            }

            //调试时因设备信息太长而省略了部分数据，通过字符串拆成数组来获得完整设备信息
            if (code == 2) {
                String[] arr = msg.split("客厅墙边2");
                String[] bbb = arr;
                String[] ccc = bbb;
            }

            Message message = handler.obtainMessage(code);
            message.obj = mJsonObject;
            handler.sendMessage(message);

        }
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        // TODO 自动生成的方法存根
        Log.v(TAG, "acceptInboundMessage接收上游信息" + msg);
        return super.acceptInboundMessage(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext arg0, Object arg1)
            throws Exception {
        // TODO 自动生成的方法存根
        Log.v(TAG, "channelRead读取信息" + arg1.toString());
        super.channelRead(arg0, arg1);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // TODO 自动生成的方法存根
        Log.v(TAG, "channelActive信道活动");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO 自动生成的方法存根
        Log.v(TAG, "channelInactive不活动了");
        super.channelInactive(ctx);
        //重新连接服务器
//		ctx.close();

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // TODO 自动生成的方法存根
        Log.v(TAG, "channelReadComplete读取结束");
        // ctx.channel().close();
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // TODO 自动生成的方法存根
        Log.v(TAG, "channelRegistered信道注册");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // TODO 自动生成的方法存根
        Log.v(TAG, "channelUnregistered注销信道");
        super.channelUnregistered(ctx);
//		handler.sendMessage(handler.obtainMessage(4));
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx)
            throws Exception {
        // TODO 自动生成的方法存根
        Log.v(TAG, "channelWritabilityChanged");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // TODO 自动生成的方法存根
        Log.v(TAG, "处理异常");
        super.exceptionCaught(ctx, cause);
//		ctx.close();
//		NettyUtil.doConnect(handler);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        // TODO 自动生成的方法存根
        super.userEventTriggered(ctx, evt);

//		  if (evt instanceof IdleStateEvent) {  
//	            IdleStateEvent event = (IdleStateEvent) evt;  
//	            if (event.state().equals(IdleState.READER_IDLE)) {  
//	                //未进行读操作  
//	                Log.v(TAG,"READER_IDLE");  
//	                // 超时关闭channel  
//	                ctx.close();  
//	            } else if (event.state().equals(IdleState.WRITER_IDLE)) {  
//	            	Log.v(TAG,"WRITER_IDLE");
//	            	ctx.close(); 
//	            } else if (event.state().equals(IdleState.ALL_IDLE)) {  
//	                //未进行读写  
//	                Log.v(TAG,"ALL_IDLE");  
//	                // 发送心跳消息  
////	                MsgHandleService.getInstance().sendMsgUtil.sendHeartMessage(ctx);
//	                ByteBuf buf=Unpooled.copiedBuffer("{}".getBytes());
////	                ctx.writeAndFlush(buf);
//	                NettyUtil.future.channel().writeAndFlush(buf);
//	                Log.v("客户端心跳发送", buf.toString());
//	            }  
//	        }  
    }
}
