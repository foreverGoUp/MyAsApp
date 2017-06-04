package com.kc.netty;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.ByteToMessageDecoder;

/*
* 单例类
*
* */

public class MessageDecoder extends ByteToMessageDecoder {

//    private static MessageDecoder INSTANCE;
//    private MessageDecoder() {
//    }
//    public static MessageDecoder getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new MessageDecoder();
//        }
//        return INSTANCE;
//    }

    @Override
    protected void decode(io.netty.channel.ChannelHandlerContext arg0,
                          ByteBuf buffer, List<Object> out) throws Exception {
        // 如果buffer中的可读字节大于4个（即除了长度以外还有数据，因为长度可能是为0的）
        if (buffer.readableBytes() > 4) {

            // 标记，指向当前指针位置，读取数据时使用  
            buffer.markReaderIndex();
            // 取得长度  
            int len = buffer.readInt();
            if (len > 10000000 || len < 0) {
                buffer.clear();//清除指针
                System.out.println("MessageDecoder_APP 指针异常" + len);
                return;
            }

            // 如果剩余可读字节小于长度的话，则表明发生了拆包现象，那么不对它进行处理  
            if (buffer.readableBytes() < len) {
                // 重置标记  
                buffer.resetReaderIndex();

                // 返回null，表示等待  
                return;
            }

            // 对数据进行处理  
            byte[] bytes = new byte[len];
            buffer.readBytes(bytes);
            // 将数据返回到ServerHandler中进行处理  
            out.add(new String(bytes));
        }

        return;
    }
}
